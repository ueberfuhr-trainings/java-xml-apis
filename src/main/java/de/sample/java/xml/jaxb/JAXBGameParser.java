package de.sample.java.xml.jaxb;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import lombok.Getter;

import java.io.InputStream;

public class JAXBGameParser {

    @Getter
    private final Library library;

    public JAXBGameParser(InputStream in) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Library.class);
        library = (Library) jaxbContext.createUnmarshaller().unmarshal(in);
    }

}
