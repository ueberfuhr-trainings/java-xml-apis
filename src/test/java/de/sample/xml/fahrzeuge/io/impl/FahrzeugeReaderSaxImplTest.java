package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.domain.Hersteller;
import de.sample.xml.fahrzeuge.io.InputStreamSupplier;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FahrzeugeReaderSaxImplTest {

    static final InputStreamSupplier IN = () -> FahrzeugeReaderSaxImplTest.class.getResourceAsStream("/fahrzeuge.xml");
    FahrzeugeReaderSaxImpl impl;

    @Test
    void getHersteller() throws IOException {
        impl = new FahrzeugeReaderSaxImpl(IN);
        List<Hersteller> result = impl.getHersteller();
        assertThat(result)
          .usingRecursiveFieldByFieldElementComparator()
          .containsExactly(
            Hersteller.builder()
              .id("OPEL")
              .name("OPEL Automobile GmbH")
              .sitz("Rüsselsheim (Deutschland)")
              .geschaeftsfuehrer("Florian Huettl")
              .geschaeftsfuehrer("Ralf Wangemann")
              .gruendungsdatum(LocalDate.of(1862, Month.JANUARY, 21))
              .build(),
            Hersteller.builder()
              .id("FORD")
              .name("Ford Motor Company")
              .sitz("Dearborn (USA)")
              .geschaeftsfuehrer("Jim Farley")
              .gruendungsdatum(LocalDate.of(1903, Month.JUNE, 16))
              .build()
          );
    }
}
