package de.sample.java.xml.dom;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lombok.Getter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GameLibraryParser {

	@Getter
	private final List<Game> games = new ArrayList<>();
	
	public GameLibraryParser(InputStream in) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(in);
		NodeList nList = document.getElementsByTagNameNS("*", "game");
		addGames(nList);
	}
	
	private void addGames(NodeList nlist){
		for(int i = 0; i < nlist.getLength(); i++){
			Node node = nlist.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element) node;
				String namespaceURI = "*";
				String title = element.getElementsByTagNameNS(namespaceURI, "title").item(0).getTextContent();
				NodeList genresElement = element.getElementsByTagNameNS(namespaceURI, "genre");
				int genresCount = genresElement.getLength();
				String[] genres = new String[genresCount];
				for(int j = 0; j < genresCount; j++){
					genres[j] = genresElement.item(j).getTextContent();
				}
				int rating = Integer.parseInt(element.getElementsByTagNameNS(namespaceURI, "rating").item(0).getTextContent());
				Duration timePlayed = Duration.parse(element.getElementsByTagNameNS(namespaceURI, "timePlayed").item(0).getTextContent());
				double price = Double.parseDouble(element.getElementsByTagNameNS(namespaceURI, "price").item(0).getTextContent());
				games.add(new Game(title, genres, rating, timePlayed, price));
			}
		}
	}

}
