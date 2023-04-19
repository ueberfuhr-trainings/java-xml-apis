package de.sample.xml.fahrzeuge.io;

import de.sample.xml.fahrzeuge.domain.Fahrzeugtyp;

import java.io.IOException;
import java.util.List;

public interface FahrzeugeWriter {

    void writeFahrzeuge(List<Fahrzeugtyp> fahrzeuge) throws IOException;

}
