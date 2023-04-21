package de.sample.xml.fahrzeuge.io;

import de.sample.xml.fahrzeuge.io.impl.XmlResources;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class FahrzeugeTransformer {

    public void toPlainText(
      InputStreamSupplier inputStreamSupplier,
      OutputStreamSupplier outputStreamSupplier
    ) throws IOException {
        try (
          InputStream in = inputStreamSupplier.get();
          OutputStream out = outputStreamSupplier.get();
          InputStream xsl = XmlResources.xsl2Text.get()
        ) {
            Objects.requireNonNull(in, "InputStream must not be null");
            Objects.requireNonNull(out, "OutputStream must not be null");
            Objects.requireNonNull(xsl, "XSL could not be found!");
            final var transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(xsl));
            transformer.transform(new StreamSource(in), new StreamResult(out));
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }

}
