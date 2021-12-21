package de.sample.java.xml.xslt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XsltToHtml {


	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		try (InputStream in = XsltToHtml.class.getResourceAsStream("/pets.xml");
		FileOutputStream output = new FileOutputStream("target/output.html")) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(in);
			transform(document, output);
		}
	}

	private static void transform(Document doc, OutputStream output) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
		transformerFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		Transformer transformer = transformerFactory.newTransformer(new StreamSource(XsltToHtml.class.getResourceAsStream("/pets2html.xslt")));
		transformer.transform(new DOMSource(doc), new StreamResult(output));

	}
}
