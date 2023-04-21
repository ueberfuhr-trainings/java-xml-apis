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
import jakarta.xml.bind.Unmarshaller;
import org.mapstruct.factory.Mappers;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FahrzeugeReaderJaxbImpl extends ValidatingReaderImpl implements FahrzeugeReader {

    private final HerstellerJaxb2DomainMapper mapper;

    public FahrzeugeReaderJaxbImpl(
      InputStreamSupplier inputStreamSupplier,
      InputStreamSupplier schemaInputStreamSupplier
    ) {
        this(
          inputStreamSupplier,
          schemaInputStreamSupplier,
          Mappers.getMapper(HerstellerJaxb2DomainMapper.class)
        );
    }

    public FahrzeugeReaderJaxbImpl(
      InputStreamSupplier inputStreamSupplier,
      InputStreamSupplier schemaInputStreamSupplier,
      HerstellerJaxb2DomainMapper mapper
    ) {
        super(inputStreamSupplier, schemaInputStreamSupplier);
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
        return (in, schemaIn) -> {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                var sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                var schema = sf.newSchema(new StreamSource(schemaIn));
                jaxbUnmarshaller.setSchema(schema);

                return jaxbUnmarshaller
                  .unmarshal(new StreamSource(in), Fahrzeuge.class)
                  .getValue();
            } catch (SAXException | JAXBException e) {
                throw new IOException(e);
            }

        };
    }

}
