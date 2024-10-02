package jaxb.converter.api;

import component.sheet.api.Sheet;
import jakarta.xml.bind.JAXBException;

import java.io.FileNotFoundException;

public interface XMLToSheetConverter {
    Sheet convert(String xml) throws FileNotFoundException, JAXBException;
}
