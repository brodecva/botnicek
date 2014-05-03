/**
 * Copyright Václav Brodec 2013.
 * 
 * This file is part of Botníček.
 * 
 * Botníček is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Botníček is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Botníček.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ms.brodecva.botnicek.library.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;

/**
 * Parser zdrojového kódu AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLSourceParser implements SourceParser, Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 2349731914071945068L;

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLSourceParser.class);

    /**
     * Cesta k schématu.
     */
    public static final String AIML_SCHEMA_SOURCE =
            "/cz/cuni/mff/ms/brodecva/botnicek/library/platform/AIML.xsd";

    /**
     * {@link XMLReader} pro parsování.
     */
    private final transient XMLReader reader;

    /**
     * Továrna na {@link Handler}.
     */
    private final SourceHandlerFactory handlerFactory;

    /**
     * Vytvoří výchozí parser zdrojového kódu AIML.
     * 
     * @return výchozí parser
     */
    public static AIMLSourceParser create() {
        XMLReader newReader;
        try {
            newReader = createAIMLReader();
        } catch (SAXException | ParserConfigurationException e) {
            throw new SourceParserError(e);
        }

        return new AIMLSourceParser(newReader, new AIMLHandlerFactory());
    }

    /**
     * Vrátí XML reader pro zpracování zdrojových AIML souborů.
     * 
     * @return XML reader pro zpracování zdrojových AIML souborů
     * @throws SAXException
     *             pokud dojde k chybě při parsování schématu
     * @throws ParserConfigurationException
     *             pokud nemůže být vytvořen parser odpovídající konfiguraci
     */
    private static XMLReader createAIMLReader() throws SAXException,
            ParserConfigurationException {
        final SchemaFactory schemaFactory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        final Schema aimlSchema =
                schemaFactory.newSchema(new StreamSource(
                        AIMLCategoryLoader.class
                                .getResourceAsStream(AIML_SCHEMA_SOURCE)));

        final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        parserFactory
                .setFeature("http://xml.org/sax/features/namespaces", true);
        parserFactory.setFeature(
                "http://xml.org/sax/features/namespace-prefixes", false);
        parserFactory.setSchema(aimlSchema);

        final SAXParser saxParser = parserFactory.newSAXParser();

        return saxParser.getXMLReader();
    }

    /**
     * Vytvoří parser zdrojového kódu.
     * 
     * @param reader
     *            čtecí objekt SAXu
     * @param handlerFactory
     *            továrna na {@link ContentHandler} a {@link AIMLErrorHandler}.
     */
    private AIMLSourceParser(final XMLReader reader,
            final SourceHandlerFactory handlerFactory) {
        this.reader = reader;
        this.handlerFactory = handlerFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.loader.SourceParser#parse(java
     * .io.InputStream, java.lang.String,
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure,
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot)
     */
    @Override
    public void parse(final InputStream inputStream, final String systemId,
            final MatchingStructure filledStructure, final Bot bot)
            throws SourceParserException {
        final InputSource input = new InputSource(inputStream);
        input.setSystemId(systemId);

        LOGGER.log(Level.INFO, "loader.AIMLParseAttempt", new Object[] { input,
                filledStructure, bot });

        final Handler handler =
                handlerFactory.createHandler(filledStructure, bot);

        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);
        try {
            reader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
        } catch (final SAXNotRecognizedException | SAXNotSupportedException e) {
            throw new SourceParserError(e);
        }

        try {
            reader.parse(input);
        } catch (final IOException e) {
            throw new SourceParserException(e);
        } catch (final SAXException e) {
            final Throwable cause = e.getCause();
            
            if (cause instanceof SAXParseException) {
                final SAXParseException parseCause = (SAXParseException) cause;
                throw new SourceParserException(parseCause, parseCause.getSystemId(), parseCause.getPublicId(), parseCause.getLineNumber(), parseCause.getColumnNumber());
            }
            
            throw new SourceParserException(e);
        }
    }

    /**
     * Serializuje za použití proxy.
     * 
     * @return serializovaná verze objektu
     * @throws ObjectStreamException
     *             chyba při serializaci
     */
    private Object writeReplace() throws ObjectStreamException {
        return new SerializationProxy(this);
    }

    /**
     * Serializační proxy.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    private static final class SerializationProxy implements Serializable {

        /**
         * UID serializované verze.
         */
        private static final long serialVersionUID = -919965534118787744L;

        /**
         * Továrna na obslužné objekty.
         */
        private final SourceHandlerFactory handlerFactory;

        /**
         * Vytvoří serializační proxy.
         * 
         * @param original
         *            objekt k serializaci
         */
        public SerializationProxy(final AIMLSourceParser original) {
            handlerFactory = original.handlerFactory;
        }

        /**
         * Vrátí deserializovaný objekt.
         * 
         * @return deserializovaný objekt
         * @throws ObjectStreamException
         *             chyba při deserializaci
         */
        Object readResolve() throws ObjectStreamException {
            try {
                return new AIMLSourceParser(createAIMLReader(), handlerFactory);
            } catch (SAXException | ParserConfigurationException e) {
                throw new SourceParserError(e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AIMLSourceParser [reader=" + reader + ", handlerFactory="
                + handlerFactory + "]";
    }
}
