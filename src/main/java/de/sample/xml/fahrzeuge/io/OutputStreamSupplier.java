package de.sample.xml.fahrzeuge.io;

import java.io.IOException;
import java.io.OutputStream;

public interface OutputStreamSupplier {

    OutputStream get() throws IOException;

}
