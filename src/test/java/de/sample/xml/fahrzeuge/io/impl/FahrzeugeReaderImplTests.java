package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.domain.Hersteller;
import de.sample.xml.fahrzeuge.io.FahrzeugeReader;
import de.sample.xml.fahrzeuge.io.InputStreamSupplier;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Single test class that provides tests for all implementations.
 * The test cases are always the same, only the implementation is different, so
 * we can do this as parameterized test here.
 */
class FahrzeugeReaderImplTests {

    static final InputStreamSupplier IN = () -> FahrzeugeReaderImplTests.class.getResourceAsStream("/fahrzeuge.xml");

    static class FahrzeugeReaderImplementationsProvider implements ArgumentsProvider {

        private Stream<Function<InputStreamSupplier, FahrzeugeReader>> getImplementations() {
            return Stream.of(
              FahrzeugeReaderSaxImpl::new,
              FahrzeugeReaderDomImpl::new
            );
        }

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return getImplementations()
              // create instances of the reader implementations
              .map(f -> f.apply(IN))
              // make it compatible to JUnit 5
              .map(Arguments::of);
        }
    }

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

}
