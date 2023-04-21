package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.io.FahrzeugeTransformer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class FahrzeugeTransformerTests {

    FahrzeugeTransformer transformer = new FahrzeugeTransformer();

    @Test
    void shouldPrintHerstellerName() throws IOException {
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
            transformer.toPlainText(
              XmlTestResources.string("""
                <?xml version="1.0" encoding="UTF-8"?>
                <f:fahrzeuge xmlns:f="http://www.samples.de/xml/fahrzeuge">
                    <f:hersteller id="OPEL">
                        <f:name>OPEL Automobile GmbH</f:name>
                        <f:sitz>Rüsselsheim (Deutschland)</f:sitz>
                        <f:geschäftsführer>Florian Huettl</f:geschäftsführer>
                        <f:geschäftsführer>Ralf Wangemann</f:geschäftsführer>
                        <f:gründungsdatum>1862-01-21</f:gründungsdatum>
                    </f:hersteller>
                </f:fahrzeuge>"""),
              () -> bout
            );
            final var result = bout.toString(StandardCharsets.UTF_8);
            assertThat(result)
              .contains("- Name: OPEL Automobile GmbH");
            System.out.println(result);
        }
    }

}
