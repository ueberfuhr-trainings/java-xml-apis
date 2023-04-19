package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.domain.Antrieb;
import de.sample.xml.fahrzeuge.domain.Fahrzeugtyp;
import de.sample.xml.fahrzeuge.domain.Hersteller;
import de.sample.xml.fahrzeuge.io.FahrzeugeReader;
import de.sample.xml.fahrzeuge.io.InputStreamSupplier;
import de.sample.xml.fahrzeuge.io.impl.jaxb.Fahrzeuge;
import de.sample.xml.fahrzeuge.io.impl.jaxb.HerstellerJaxb2DomainMapper;
import de.sample.xml.fahrzeuge.io.impl.jaxb.ObjectFactory;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.mapstruct.factory.Mappers;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FahrzeugeReaderJaxbImpl extends ReaderImpl implements FahrzeugeReader {

    private final HerstellerJaxb2DomainMapper mapper;

    public FahrzeugeReaderJaxbImpl(InputStreamSupplier inputStreamSupplier) {
        this(inputStreamSupplier, Mappers.getMapper(HerstellerJaxb2DomainMapper.class));
    }

    public FahrzeugeReaderJaxbImpl(InputStreamSupplier inputStreamSupplier, HerstellerJaxb2DomainMapper mapper) {
        super(inputStreamSupplier);
        this.mapper = mapper;
    }

    @Override
    public List<Hersteller> getHersteller() throws IOException {
        return super.read(jaxbParse()).getHersteller()
          // we need to map
          .stream()
          .map(mapper::map)
          .collect(Collectors.toList());
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

    private static InputStreamReadingFunction<Fahrzeuge> jaxbParse() {
        return in -> {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
                return jaxbContext
                  .createUnmarshaller()
                  .unmarshal(new StreamSource(in), Fahrzeuge.class)
                  .getValue();
            } catch (JAXBException e) {
                throw new IOException(e);
            }

        };
    }

}
