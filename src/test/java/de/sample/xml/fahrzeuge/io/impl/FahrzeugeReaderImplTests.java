package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.domain.Hersteller;
import de.sample.xml.fahrzeuge.io.FahrzeugeReader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Single test class that provides tests for all implementations.
 * The test cases are always the same, only the implementation is different, so
 * we can do this as parameterized test here.
 */
class FahrzeugeReaderImplTests {

    @ParameterizedTest
    @ArgumentsSource(FahrzeugeReaderImplementationsProvider.class)
    void shouldReadAllHersteller(FahrzeugeReader impl) throws IOException {
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

    @ParameterizedTest
    @ArgumentsSource(FahrzeugeReaderImplementationsProvider.class)
    void shouldReadSingleHersteller(FahrzeugeReader impl) throws IOException {
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

    @ParameterizedTest
    @ArgumentsSource(FahrzeugeReaderImplementationsProvider.class)
    void shouldReadSingleHerstellerNotFound(FahrzeugeReader impl) throws IOException {
        Optional<Hersteller> result = impl.getHerstellerById("ABC");
        assertThat(result)
          .isEmpty();
    }

    @ParameterizedTest
    @ArgumentsSource(FahrzeugeReaderImplementationsProvider.class)
    @FahrzeugeReaderImplementationsProvider.Xml("""
      <?xml version="1.0" encoding="UTF-8"?>
      <f:fahrzeuge xmlns:f="http://www.samples.de/xml/fahrzeuge">
      	<f:hersteller id="OPEL">
      		<f:name>OPEL Automobile GmbH</f:name>
      		<!-- <sitz> is missing -->
      	</f:hersteller>
      </f:fahrzeuge>
      """)
    void shouldOccurInValidationError(FahrzeugeReader impl) {
        assertThatThrownBy(impl::getHersteller)
          // missing <sitz> would otherwise lead to NPE in HerstellerBuilder, if validation is not done
          .isInstanceOf(IOException.class);
    }

}
