package de.sample.xml.fahrzeuge.io;

import de.sample.xml.fahrzeuge.domain.Antrieb;
import de.sample.xml.fahrzeuge.domain.Fahrzeugtyp;
import de.sample.xml.fahrzeuge.domain.Hersteller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FahrzeugeReader {

    List<Hersteller> getHersteller() throws IOException;

    Optional<Hersteller> getHerstellerById(String id) throws IOException;

    List<Fahrzeugtyp> getFahrzeugTypen() throws IOException;

    Optional<Fahrzeugtyp> getFahrzeugTypById(String id) throws IOException;

    List<Fahrzeugtyp> getFahrzeugTypenByAntrieb(Antrieb antrieb) throws IOException;

}
