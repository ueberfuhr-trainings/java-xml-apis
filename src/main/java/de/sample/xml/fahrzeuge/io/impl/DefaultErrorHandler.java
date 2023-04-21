package de.sample.xml.fahrzeuge.io.impl;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public interface DefaultErrorHandler extends ErrorHandler {
    @Override
    default void warning(SAXParseException exception) throws SAXException {
        // don't do anything
    }

    @Override
    default void error(SAXParseException exception) throws SAXException {
        // throw to get error on parsing
        throw exception;
    }

    @Override
    default void fatalError(SAXParseException exception) throws SAXException {
        // throw to get error on parsing
        throw exception;
    }
}
