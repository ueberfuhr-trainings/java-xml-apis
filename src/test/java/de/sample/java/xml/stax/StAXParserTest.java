package de.sample.java.xml.stax;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StAXParserTest {

    @Test
    void testParserAutoMarkenname() throws IOException, XMLStreamException {
        // XML-Datei finden und lesen
        try (InputStream in = StAXParserTest.class.getResourceAsStream("/auto.xml")) {
            String marke = new AutoParser(in).getAutoMarke();// .getAuto().getMarke()
            assertEquals("VW", marke);
        }
    }

    @Test
    void testGetPetName() throws XMLStreamException, IOException {
        try (InputStream in = StAXParserTest.class.getResourceAsStream("/pets.xml")) {
            String name = new PetParserStAX(in).getPetName();
            assertEquals("Sparky", name);
        }
    }
}
