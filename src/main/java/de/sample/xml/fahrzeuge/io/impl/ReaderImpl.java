package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.io.InputStreamSupplier;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
class ReaderImpl {

    public interface InputStreamReadingFunction<R> {

        R apply(InputStream in) throws IOException;

    }

    private final InputStreamSupplier inputStreamSupplier;

    protected InputStream getInputStream() throws IOException {
        return inputStreamSupplier.get();
    }

    protected <T> T read(InputStreamReadingFunction<T> func) throws IOException {
        try (InputStream in = getInputStream()) {
            return func.apply(in);
        }
    }

}
