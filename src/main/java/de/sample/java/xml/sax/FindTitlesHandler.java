package de.sample.java.xml.sax;

import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.ext.DefaultHandler2;

import java.util.LinkedList;
import java.util.List;

public class FindTitlesHandler extends DefaultHandler2 {

	@Getter
	private final List<String> titles = new LinkedList<>();
	private boolean inTitle;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {

		if ("title".equals(localName) || localName.isEmpty() && "title".equals(qName)) {
			inTitle = true;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) {
		if (inTitle) {
			String title = String.valueOf(ch, start, length);
			getTitles().add(title);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) {
		inTitle = false;
	}

}
