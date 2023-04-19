package de.sample.xml.fahrzeuge.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Leistung {

    private int wert;
    @NonNull
    private Leistungseinheit einheit;

    public enum Leistungseinheit {

        PS, KW

    }

}
