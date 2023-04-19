package de.sample.xml.fahrzeuge.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Singular;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Hersteller {

    @NonNull
    @EqualsAndHashCode.Include
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String sitz;
    @Singular("geschaeftsfuehrer")
    private final List<String> geschaeftsfuehrer;
    private LocalDate gruendungsdatum;

}
