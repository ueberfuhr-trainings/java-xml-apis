package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.domain.Antrieb;
import de.sample.xml.fahrzeuge.domain.Fahrzeugtyp;
import de.sample.xml.fahrzeuge.domain.Hersteller;
import de.sample.xml.fahrzeuge.io.FahrzeugeReader;
import de.sample.xml.fahrzeuge.io.InputStreamSupplier;
import lombok.SneakyThrows;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FahrzeugeReaderStaxImpl extends ReaderImpl implements FahrzeugeReader {

    public FahrzeugeReaderStaxImpl(InputStreamSupplier inputStreamSupplier) {
        super(inputStreamSupplier);
    }

    @Override
    public List<Hersteller> getHersteller() throws IOException {
        return super.read(staxParse(HerstellerStaxHandler.all()::getHersteller));
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

    private static <T> InputStreamReadingFunction<T> staxParse(Function<XMLStreamReader, T> fetchData) {
        return in -> {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            try {
                xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
                XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(in);
                try {
                    return fetchData.apply(reader); // read parsed data from handler
                } finally {
                    reader.close();
                }
            } catch (XMLStreamException e) {
                throw new IOException(e);
            }

        };
    }

    // liest nur die Hersteller aus
    private static class HerstellerStaxHandler {

        @SneakyThrows
        public List<Hersteller> getHersteller(XMLStreamReader reader) {
            List<Hersteller> hersteller = new ArrayList<>();
            // aktuell zu lesender Hersteller (Builder Pattern)
            Hersteller.HerstellerBuilder aktuellerHersteller = null;
            while (reader.hasNext()) {
                // Switch Pattern Matching (seit Java 17)
                switch (reader.next()) {
                case XMLEvent.START_ELEMENT -> {
                    if (null != aktuellerHersteller) {
                        // Switch Pattern Matching (seit Java 17)
                        switch (reader.getName().getLocalPart()) {
                        case "name" -> aktuellerHersteller.name(reader.getElementText());
                        case "sitz" -> aktuellerHersteller.sitz(reader.getElementText());
                        case "geschäftsführer" -> aktuellerHersteller.geschaeftsfuehrer(reader.getElementText());
                        case "gründungsdatum" -> aktuellerHersteller.gruendungsdatum(LocalDate.parse(reader.getElementText()));
                        }
                    } else if ("hersteller".equals(reader.getName().getLocalPart())) {
                        // wir beginnen mit dem Lesen eines Herstellers
                        aktuellerHersteller = Hersteller.builder();
                        aktuellerHersteller.id(reader.getAttributeValue(null, "id"));
                    }
                }
                case XMLEvent.END_ELEMENT -> {
                    if ("hersteller".equals(reader.getName().getLocalPart()) && null != aktuellerHersteller) {
                        // fertig mit dem Lesen - wir speichern den gelesenen Hersteller
                        hersteller.add(aktuellerHersteller.build());
                        aktuellerHersteller = null;
                    }
                }
                }
            }
            return hersteller;
        }

        public static HerstellerStaxHandler all() {
            return new HerstellerStaxHandler();
        }

    }

}
