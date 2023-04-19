package de.sample.xml.fahrzeuge.io;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamSupplier {

    InputStream get() throws IOException;

}
