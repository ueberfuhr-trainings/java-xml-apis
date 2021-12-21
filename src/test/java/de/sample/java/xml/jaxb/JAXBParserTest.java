package de.sample.java.xml.jaxb;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

class JAXBParserTest {

    @Test
    void testeParseGameTitle() throws IOException, JAXBException {
        try (InputStream in = JAXBParserTest.class.getResourceAsStream("/gameLibrary.xml")) {
            JAXBGameParser jaxbAutoParser = new JAXBGameParser(in);
            Library library = jaxbAutoParser.getLibrary();
            Optional<String> found = library.getGames().stream()
              .map(Game::getTitle)
              .filter("Elderscrolles IV: Skyrim"::equals)
              .findFirst();
            Assertions.assertTrue(found.isPresent());
        }
    }
}
