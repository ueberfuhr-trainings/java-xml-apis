package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.domain.Antrieb;
import de.sample.xml.fahrzeuge.domain.Fahrzeugtyp;
import de.sample.xml.fahrzeuge.domain.Hersteller;
import de.sample.xml.fahrzeuge.io.FahrzeugeReader;
import de.sample.xml.fahrzeuge.io.InputStreamSupplier;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

public class FahrzeugeReaderDomImpl extends ValidatingReaderImpl implements FahrzeugeReader {

    public FahrzeugeReaderDomImpl(
      InputStreamSupplier inputStreamSupplier,
      InputStreamSupplier schemaInputStreamSupplier
    ) {
        super(inputStreamSupplier, schemaInputStreamSupplier);
    }

    @Override
    public List<Hersteller> getHersteller() throws IOException {
        return super.read(domParse(HerstellerDomHandler.all()::getHersteller));
    }

    @Override
    public Optional<Hersteller> getHerstellerById(String id) throws IOException {
        return getHersteller()
          .stream()
          .filter(h -> id.equals(h.getId()))
          .findFirst();
    }

    @Override
    public List<Fahrzeugtyp> getFahrzeugTypen() throws IOException {
        return null;
    }

    @Override
    public Optional<Fahrzeugtyp> getFahrzeugTypById(String id) throws IOException {
        return Optional.empty();
    }

    @Override
    public List<Fahrzeugtyp> getFahrzeugTypenByAntrieb(Antrieb antrieb) throws IOException {
        return null;
    }

    private static <T> InputStreamReadingFunction<T> domParse(Function<Document, T> fetchData) {
        return (in, schemaIn) -> {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {

                var sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                var schema = sf.newSchema(new StreamSource(schemaIn));
                factory.setSchema(schema);
                factory.setNamespaceAware(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                builder.setErrorHandler(new DefaultErrorHandler() {
                });
                Document document = builder.parse(in);
                return fetchData.apply(document); // read parsed data from handler
            } catch (ParserConfigurationException | SAXException e) {
                throw new IOException(e);
            }

        };
    }

    // liest nur die Hersteller aus
    private static class HerstellerDomHandler {

        public List<Hersteller> getHersteller(Document document) {
            List<Hersteller> result = new ArrayList<>();
            NodeList herstellerNodes = document.getElementsByTagName("hersteller");
            for (int i = 0; i < herstellerNodes.getLength(); i++) {
                // Java 17: Pattern Matching for instanceof
                if (herstellerNodes.item(i) instanceof Element element) {
                    var hersteller = Hersteller.builder();
                    hersteller.id(element.getAttribute("id"));
                    Optional.ofNullable(element.getElementsByTagName("name").item(0))
                      .map(Node::getTextContent)
                      .ifPresent(hersteller::name);
                    Optional.ofNullable(element.getElementsByTagName("sitz").item(0))
                      .map(Node::getTextContent)
                      .ifPresent(hersteller::sitz);
                    var geschaeftsfuehrerList = element.getElementsByTagName("geschäftsführer");
                    IntStream.range(0, geschaeftsfuehrerList.getLength())
                      .mapToObj(geschaeftsfuehrerList::item) // map NodeList to Java Stream
                      .map(Node::getTextContent)
                      .forEach(hersteller::geschaeftsfuehrer);
                    Optional.ofNullable(element.getElementsByTagName("gründungsdatum").item(0))
                      .map(Node::getTextContent)
                      .map(LocalDate::parse)
                      .ifPresent(hersteller::gruendungsdatum);
                    result.add(hersteller.build());
                }
            }
            return result;
        }

        public static HerstellerDomHandler all() {
            return new HerstellerDomHandler();
        }

    }

}
