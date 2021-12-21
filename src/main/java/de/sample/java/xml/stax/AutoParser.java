package de.sample.java.xml.stax;

import lombok.Getter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Iterator;

// StAX Iterator API
public class AutoParser {

    @Getter
    private String autoMarke;

    public AutoParser(InputStream in) throws XMLStreamException {

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

        // XML-lesen
        while (eventReader.hasNext() && null == autoMarke) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement() && "marke".equals(event.asStartElement().getName().getLocalPart())) {
                @SuppressWarnings("rawtypes")
                Iterator attributes = event.asStartElement().getAttributes();
                while (attributes.hasNext()) {
                    Attribute next = (Attribute) attributes.next();
                    if ("name".equals(next.getName().getLocalPart())) {
                        autoMarke = next.getValue();
                        break;
                    }
                }
            }
        }
    }

}
