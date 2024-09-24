package jaxb.converter;

import component.cell.api.Cell;
import component.cell.impl.CellImpl;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jaxb.generated.STLCell;
import jaxb.generated.STLRange;
import jaxb.generated.STLSheet;
import logic.parser.OriginalValueParser;

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

    private Sheet STLSheetToSheet(STLSheet stlSheet) {
        SheetImpl sheet = new SheetImpl(stlSheet);
        stlSheet.getSTLRanges().getSTLRange().forEach(stlRange -> this.createNewRange(stlRange, sheet));
        stlSheet.getSTLCells().getSTLCell().forEach(stlCell -> this.createNewCell(stlCell, sheet));
        sheet.getRanges().forEach((rangeName, range) -> {
            range.populateRange(sheet);
        });

        return sheet.updateSheet(sheet, true);
    }

    private void createNewCell(STLCell stlCell, Sheet sheet){
        String cellID = createCellID(stlCell.getRow(), stlCell.getColumn());

        if (!sheet.cellInLayout(cellID)){
            String format = String.format("""
                    File contains Cell outside of Sheet layout.
                    Sheet layout: %d rows, %d columns
                    Cell ID: %s""", sheet.getLayout().getNumberOfRows(), sheet.getLayout().getNumberOfColumns(), cellID);
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
        sheet.getSheetCells().put(cell.getCellId(), cell);
    }

    private String createCellID(int row, String col) {
        return col + row;
    }

    private void createNewRange(STLRange stlRange, Sheet sheet) {
        String from = stlRange.getSTLBoundaries().getFrom();
        String to = stlRange.getSTLBoundaries().getTo();

        if (!sheet.cellInLayout(from) || !sheet.cellInLayout(to)) {
            String format = String.format("""
                    File contains Range exceeding Sheet layout.
                    Sheet layout: %d rows, %d columns
                    Range: %s""", sheet.getLayout().getNumberOfRows(), sheet.getLayout().getNumberOfColumns(), stlRange.getName());
            throw new IllegalArgumentException(format);
        }

        sheet.createRange(stlRange.getName(), from + ".." + to);
    }


    private STLSheet deserializeFrom(InputStream in) throws JAXBException {
        String JAXB_XML_PACKAGE_NAME = "jaxb.generated";
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (STLSheet) u.unmarshal(in);
    }

}