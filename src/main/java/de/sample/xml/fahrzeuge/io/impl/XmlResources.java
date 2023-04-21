package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.io.InputStreamSupplier;
import lombok.experimental.UtilityClass;

@UtilityClass
public class XmlResources {

    public final InputStreamSupplier xsd = resource("/fahrzeuge.xsd");
    public final InputStreamSupplier xsl2Text = resource("/fahrzeuge2text.xsl");

    public InputStreamSupplier resource(String path) {
        return resource(path, XmlResources.class);
    }

    public InputStreamSupplier resource(String path, Class<?> relativeClass) {
        return () -> relativeClass.getResourceAsStream(path);
    }

}
