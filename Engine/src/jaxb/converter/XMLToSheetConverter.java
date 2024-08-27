package jaxb.converter;

import component.sheet.api.Sheet;
import jakarta.xml.bind.JAXBException;

import java.io.FileNotFoundException;

public interface XMLToSheetConverter {
    public Sheet convert(String xml) throws FileNotFoundException, JAXBException;
}
