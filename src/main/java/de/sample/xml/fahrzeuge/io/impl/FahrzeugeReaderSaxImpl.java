package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.domain.Antrieb;
import de.sample.xml.fahrzeuge.domain.Fahrzeugtyp;
import de.sample.xml.fahrzeuge.domain.Hersteller;
import de.sample.xml.fahrzeuge.io.FahrzeugeReader;
import de.sample.xml.fahrzeuge.io.InputStreamSupplier;
import lombok.Getter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class FahrzeugeReaderSaxImpl extends ReaderImpl implements FahrzeugeReader {

    public FahrzeugeReaderSaxImpl(InputStreamSupplier inputStreamSupplier) {
        super(inputStreamSupplier);
    }

    @Override
    public List<Hersteller> getHersteller() throws IOException {
        return super.read(saxParse(HerstellerSaxHandler.all(), HerstellerSaxHandler::getHersteller));
    }

    @Override
    public Optional<Hersteller> getHerstellerById(String id) throws IOException {
        // we always read all hersteller
        // for big data, we could extend the handler by a predicate to only read out Hersteller with the given id
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

    private static <T, H extends DefaultHandler> InputStreamReadingFunction<T> saxParse(H handler, Function<H, T> fetchData) {
        return in -> {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            try {
                factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                SAXParser saxParser = factory.newSAXParser();
                saxParser.parse(in, handler); // parse XML
                return fetchData.apply(handler); // read parsed data from handler
            } catch (ParserConfigurationException | SAXException e) {
                throw new IOException(e);
            }

        };
    }

    // liest nur die Hersteller aus
    private static class HerstellerSaxHandler extends DefaultHandler2 {

        @Getter
        private final List<Hersteller> hersteller = new ArrayList<>();

        // (1) aktuell zu lesender Hersteller (Builder Pattern)
        private Hersteller.HerstellerBuilder aktuellerHersteller;
        // (2) Anweisung, wie die folgenden Characters zu verarbeiten sind
        private Consumer<String> applyCharacters;

        public static HerstellerSaxHandler all() {
            return new HerstellerSaxHandler();
        }

        /*
         * Ansatz:
         *  - Namespaces werden hier ignoriert (nur localName ausgelesen)
         *  - hersteller ist eindeutig, es gibt kein zweites Hersteller-Element
         *    -> (1) es gibt den aktuellen Hersteller als internen Zustand, der nach und nach gefüllt wird
         *  - abhängig davon, ob der Hersteller belegt ist, werden die Attribute gelesen
         *  - für jedes Unterelemement muss beim Hersteller ein anderes Attribut besetzt werden
         *    -> (2) wird über einen Consumer abgebildet
         */

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (null != this.aktuellerHersteller) {
                final Hersteller.HerstellerBuilder h = aktuellerHersteller;
                // wir lesen gerade die Unterelemente des Herstellers
                // == wir geben die Anweisung, wie nachfolgende Characters verarbeitet werden sollen (2)
                // Switch Pattern Matching (seit Java 17)
                this.applyCharacters = switch (qName) {
                    case "name" -> h::name;
                    case "sitz" -> h::sitz;
                    case "geschäftsführer" -> h::geschaeftsfuehrer;
                    case "gründungsdatum" -> text -> h.gruendungsdatum(LocalDate.parse(text));
                    default -> null;
                };
            } else if ("hersteller".equals(qName)) {
                // wir beginnen mit dem Lesen eines Herstellers
                this.aktuellerHersteller = Hersteller.builder();
                this.aktuellerHersteller.id(attributes.getValue("id"));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (null != this.applyCharacters) {
                String text = String.valueOf(ch, start, length);
                this.applyCharacters.accept(text);
                // nur einmal lesen
                this.applyCharacters = null;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("hersteller".equals(qName) && null != this.aktuellerHersteller) {
                // fertig mit dem Lesen - wir speichern den gelesenen Hersteller
                this.hersteller.add(this.aktuellerHersteller.build());
                this.aktuellerHersteller = null;
            }
            this.applyCharacters = null; // bei leeren Properties notwendig
        }

    }

}
