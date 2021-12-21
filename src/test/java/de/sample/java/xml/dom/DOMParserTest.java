package de.sample.java.xml.dom;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DOMParserTest {

	@Test
	void testParseGameTitle() throws IOException, ParserConfigurationException, SAXException{
		try(InputStream in = DOMParserTest.class.getResourceAsStream("/gameLibrary.xml")){
			String title = new GameLibraryParser(in).getGames().get(0).getTitle();
			assertEquals("Elderscrolles IV: Skyrim", title);
		}
	}
	
	@Test
	 void testParseGameGenre() throws IOException, ParserConfigurationException, SAXException{
		try(InputStream in = DOMParserTest.class.getResourceAsStream("/gameLibrary2.xml")){
			String[] genre = new GameLibraryParser(in).getGames().get(0).getGenres();
			assertEquals("Survival", genre[1]);
		}
	}
	
	@Test
	 void testParseSingleGame() throws IOException, ParserConfigurationException, SAXException{
		try(InputStream in = DOMParserTest.class.getResourceAsStream("/gameLibrary3.xml")){
			String title = new GameLibraryParser(in).getGames().get(0).getTitle();
			assertEquals("Elderscrolles IV: Skyrim", title);
		}
	}

	@Test
	 void testMultipleNSGame() throws IOException, ParserConfigurationException, SAXException{
		try(InputStream in = DOMParserTest.class.getResourceAsStream("/gameLibrary4.xml")){
			String title = new GameLibraryParser(in).getGames().get(0).getTitle();
			assertEquals("Elderscrolles IV: Skyrim", title);
		}
	}
}
