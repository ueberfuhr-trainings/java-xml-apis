package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.io.FahrzeugeReader;
import de.sample.xml.fahrzeuge.io.InputStreamSupplier;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class FahrzeugeReaderImplementationsProvider implements ArgumentsProvider {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Xml {
        String value();
    }

    private Stream<BiFunction<InputStreamSupplier, InputStreamSupplier, FahrzeugeReader>> getImplementations() {
        return Stream.of(
          FahrzeugeReaderSaxImpl::new,
          FahrzeugeReaderDomImpl::new,
          FahrzeugeReaderStaxImpl::new,
          FahrzeugeReaderJaxbImpl::new
        );
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        //context.getTestMethod().get().getAnnotation(Xml.class)
        var xml = context.getTestMethod()
          .map(m -> m.getAnnotation(Xml.class))
          .map(Xml::value)
          .map(XmlTestResources::string)
          .orElse(XmlTestResources.defaultXml);
        return getImplementations()
          // create instances of the reader implementations
          .map(f -> f.apply(xml, XmlTestResources.xsd))
          // make it compatible to JUnit 5
          .map(Arguments::of);
    }

}
