package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.io.InputStreamSupplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
abstract class ValidatingReaderImpl {

    public interface InputStreamReadingFunction<R> {

        R apply(InputStream in, InputStream schema) throws IOException;

    }

    @Getter(AccessLevel.PROTECTED)
    private final InputStreamSupplier inputStreamSupplier;
    @Getter(AccessLevel.PROTECTED)
    private final InputStreamSupplier schemaInputStreamSupplier;

    protected <T> T read(InputStreamReadingFunction<T> func) throws IOException {
        try (InputStream in = getInputStreamSupplier().get();
          InputStream schema = getSchemaInputStreamSupplier().get()) {
            return func.apply(in, schema);
        }
    }

}
