package de.sample.java.xml.stax;

import lombok.Getter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Iterator;

public class PetParserStAX {

    @Getter
    private String petName;

    public PetParserStAX(InputStream in) throws XMLStreamException {

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLEventReader eventReader = factory.createXMLEventReader(in);

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            if (event.isStartElement() && "pet".equals(event.asStartElement().getName().getLocalPart())) {
                @SuppressWarnings("rawtypes")
                Iterator attributes = event.asStartElement().getAttributes();
                while (attributes.hasNext()) {
                    Attribute next = (Attribute) attributes.next();
                    if ("name".equals(next.getName().getLocalPart())) {
                        petName = next.getValue();
                        return;
                    }
                }
            }
        }
    }

}
