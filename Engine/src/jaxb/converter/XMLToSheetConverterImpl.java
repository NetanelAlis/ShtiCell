package jaxb.converter;

import component.cell.impl.CellImpl;
import component.sheet.api.Sheet;
import component.sheet.impl.SheetImpl;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jaxb.generated.STLCell;
import jaxb.generated.STLSheet;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class XMLToSheetConverterImpl implements XMLToSheetConverter {

    @Override
    public Sheet convert(String xml) throws FileNotFoundException, JAXBException {
        InputStream inputStream = new FileInputStream(xml);
        return this.STLSheetToSheet(this.deserializeFrom(inputStream));
    }

    private Sheet STLSheetToSheet(STLSheet stlSheet) {
        SheetImpl sheet = new SheetImpl(stlSheet);
        stlSheet.getSTLCells().getSTLCell().forEach(stlCell -> this.createNewCell(stlCell, sheet));
        return sheet.updateSheet(sheet);
    }

    private void createNewCell(STLCell STLcell, Sheet sheet){
        String cellID = this.createCellID(STLcell.getRow(), STLcell.getColumn());

        cellID = Character.toUpperCase(cellID.charAt(0)) + cellID.substring(1);
        if (!sheet.cellInLayout(cellID)){
            String format = String.format("""
                    File contains Cell outside of Sheet layout.
                    Sheet layout: %d rows, %d columns
                    Cell ID: %s""", sheet.getLayout().getNumberOfRows(), sheet.getLayout().getNumberOfColumns(), cellID);
            throw new IllegalArgumentException(format);
        }

        CellImpl cell = new CellImpl(cellID, STLcell.getSTLOriginalValue(),
                1, sheet);

        sheet.getSheetCells().put(cell.getCellId(), cell);
    }

    private String createCellID(int row, String col){
        return col + row;
    }


    private STLSheet deserializeFrom(InputStream in) throws JAXBException {
        String JAXB_XML_PACKAGE_NAME = "jaxb.generated";
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (STLSheet) u.unmarshal(in);
    }
}