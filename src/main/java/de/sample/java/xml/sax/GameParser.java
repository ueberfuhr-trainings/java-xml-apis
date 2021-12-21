package de.sample.java.xml.sax;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import lombok.Getter;
import org.xml.sax.SAXException;

public class GameParser {

	@Getter
	private final List<String> titles;

	public GameParser(InputStream in) throws IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			SAXParser saxParser = factory.newSAXParser();
			FindTitlesHandler handler = new FindTitlesHandler();
			saxParser.parse(in, handler);
			this.titles = handler.getTitles();
		} catch (ParserConfigurationException | SAXException e) {
			throw new IOException(e);
		}

	}

}
