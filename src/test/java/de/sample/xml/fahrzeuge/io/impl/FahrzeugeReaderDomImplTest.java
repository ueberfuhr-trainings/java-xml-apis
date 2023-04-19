package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.domain.Hersteller;
import de.sample.xml.fahrzeuge.io.InputStreamSupplier;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class FahrzeugeReaderDomImplTest {

    static final InputStreamSupplier IN = () -> FahrzeugeReaderDomImplTest.class.getResourceAsStream("/fahrzeuge.xml");
    FahrzeugeReaderDomImpl impl;

    @Test
    void shouldReadAllHersteller() throws IOException {
        impl = new FahrzeugeReaderDomImpl(IN);
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

    @Test
    void shouldReadSingleHersteller() throws IOException {
        impl = new FahrzeugeReaderDomImpl(IN);
        Optional<Hersteller> result = impl.getHerstellerById("FORD");
        assertThat(result)
          .isNotEmpty()
          .get()
          .usingRecursiveComparison()
          .isEqualTo(
            Hersteller.builder()
              .id("FORD")
              .name("Ford Motor Company")
              .sitz("Dearborn (USA)")
              .geschaeftsfuehrer("Jim Farley")
              .gruendungsdatum(LocalDate.of(1903, Month.JUNE, 16))
              .build()
          );
    }

    @Test
    void shouldReadSingleHerstellerNotFound() throws IOException {
        impl = new FahrzeugeReaderDomImpl(IN);
        Optional<Hersteller> result = impl.getHerstellerById("ABC");
        assertThat(result)
          .isEmpty();
    }

}