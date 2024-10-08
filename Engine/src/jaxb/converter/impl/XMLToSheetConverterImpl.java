package jaxb.converter.impl;

import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jaxb.converter.api.XMLToSheetConverter;
import jaxb.generated.STLCell;
import jaxb.generated.STLRange;
import jaxb.generated.STLSheet;
import logic.function.parser.OriginalValueParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Set;

public class XMLToSheetConverterImpl implements XMLToSheetConverter {

    @Override
    public Sheet convert(String xml) throws FileNotFoundException, JAXBException {
        InputStream inputStream = new FileInputStream(xml);
        return this.STLSheetToSheet(this.deserializeFrom(inputStream));
    }

    @Override
    public Sheet convertFromStream(InputStream inputStream) throws FileNotFoundException, JAXBException {
        return this.STLSheetToSheet(this.deserializeFrom(inputStream));
    }

    private Sheet STLSheetToSheet(STLSheet stlSheet) {
        SheetImpl sheet = new SheetImpl(stlSheet);
        stlSheet.getSTLRanges().getSTLRange().forEach(stlRange -> this.createNewRange(stlRange, sheet));
        stlSheet.getSTLCells().getSTLCell().forEach(stlCell -> this.createNewCell(stlCell, sheet));
        sheet.getRanges().forEach((rangeName, range) -> range.populateRange(sheet));
        return sheet.updateSheet(sheet, true);
    }
    
    private void createNewRange(STLRange stlRange, SheetImpl sheet) {
        String from = stlRange.getSTLBoundaries().getFrom();
        String to = stlRange.getSTLBoundaries().getTo();
        String rangeName = stlRange.getName();
        String boundaries = from + ".." + to;
        
        if (!sheet.cellInLayout(from) || !sheet.cellInLayout(to)){
            String format = String.format("""
                    File contains Range exceeding Sheet layout.
                    Sheet layout: %d rows, %d columns
                    Range: %s %s""",
                    sheet.getLayout().getRow(), sheet.getLayout().getColumn(), rangeName, boundaries);
            throw new IllegalArgumentException(format);
        }
        
        sheet.createRange(rangeName, boundaries);
    }
    
    private void createNewCell(STLCell stlCell, Sheet sheet){
        String cellID = createCellID(stlCell.getRow(), stlCell.getColumn());

        if (!sheet.cellInLayout(cellID)){
            String format = String.format("""
                    File contains Cell outside of Sheet layout.
                    Sheet layout: %d rows, %d columns
                    Cell ID: %s""", sheet.getLayout().getRow(), sheet.getLayout().getColumn(), cellID);
            throw new IllegalArgumentException(format);
        }

        Set<String> rangeNames = OriginalValueParser.SUM.extract(stlCell.getSTLOriginalValue());
        rangeNames.addAll(OriginalValueParser.AVERAGE.extract(stlCell.getSTLOriginalValue()));

        rangeNames.forEach(rangeName -> {
            if (!sheet.getRanges().containsKey(rangeName)) {
                String format = String.format("""
                    File contains a Range Function for a non-existing range.
                    Range Name: %s
                    Cell ID: %s""", rangeName, cellID);
                throw new IllegalArgumentException(format);
            }
        });

        Cell cell = new CellImpl(cellID, stlCell.getSTLOriginalValue(), 1, sheet);
        sheet.getCells().put(cell.getCellId(), cell);
    }

    private String createCellID(int row, String col){
        return col + row;
    }

    private STLSheet deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("jaxb.generated");
        Unmarshaller u = jc.createUnmarshaller();
        return (STLSheet) u.unmarshal(in);
    }
}
