package de.sample.xml.fahrzeuge.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Fahrzeugtyp {

    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private Antrieb antrieb;
    private Leistung leistung;

}
