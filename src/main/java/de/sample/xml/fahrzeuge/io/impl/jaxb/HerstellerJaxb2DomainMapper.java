package de.sample.xml.fahrzeuge.io.impl.jaxb;

import de.sample.xml.fahrzeuge.domain.Hersteller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper // MapStruct generiert die Implementierung beim Kompilieren
public interface HerstellerJaxb2DomainMapper {

    @Mapping(source = "gründungsdatum", target = "gruendungsdatum")
    @Mapping(source = "geschäftsführer", target = "geschaeftsfuehrer")
    Hersteller map(de.sample.xml.fahrzeuge.io.impl.jaxb.Hersteller hersteller);

}
