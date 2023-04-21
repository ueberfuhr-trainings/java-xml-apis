package de.sample.xml.fahrzeuge.io.impl;

import de.sample.xml.fahrzeuge.io.InputStreamSupplier;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayInputStream;

@UtilityClass
public class XmlTestResources {

    public final InputStreamSupplier defaultXml = XmlResources.resource("/fahrzeuge.xml", XmlTestResources.class);
    public final InputStreamSupplier xsd = XmlResources.xsd;

    public InputStreamSupplier string(String xml) {
        return () -> new ByteArrayInputStream(xml.getBytes());
    }

}
