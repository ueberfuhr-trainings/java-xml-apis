package de.sample.java.xml.sax;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SAXParserTest {

	@Test
	void testParseGamePrice() throws IOException {

		try (InputStream in = SAXParserTest.class.getResourceAsStream("/gameLibrary.xml")) {
			List<String> titles = new GameParser(in).getTitles();
			assertEquals(2, titles.size());
			assertEquals("Elderscrolles IV: Skyrim", titles.get(0));
		}

	}

}
