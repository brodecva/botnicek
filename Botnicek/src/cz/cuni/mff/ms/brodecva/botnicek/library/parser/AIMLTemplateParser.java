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
package cz.cuni.mff.ms.brodecva.botnicek.library.parser;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorException;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorRegistry;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassManagment;

/**
 * Parser pro obsah template AIML tagu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLTemplateParser implements TemplateParser, Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 1450773977861301041L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLTemplateParser.class);

    /**
     * Číslo verze.
     */
    public static final String VERSION = "1.0";

    /**
     * Document builder.
     */
    private static final DocumentBuilder DOCUMENT_BUILDER;

    /**
     * Kontext konverzace.
     */
    private final Conversation conversation;

    /**
     * Kontext výsledku hledání v prohledávací struktuře.
     */
    private final MatchResult matchResult;

    /**
     * Registr procesorů.
     */
    private final ProcessorRegistry processorRegistry;

    /**
     * Indikuje povolené dopředné zpracování prvků AIML.
     */
    private boolean forwardProcessingEnabled;

    static {
        final DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        try {
            DOCUMENT_BUILDER = factory.newDocumentBuilder();
        } catch (final ParserConfigurationException e) {
            throw new ParseError(e);
        }
    }

    /**
     * Vytvoří parser vnitřku Template tagu.
     * 
     * @param conversation
     *            kontext konverzace
     * @param matchResult
     *            kontext výsledku hledání
     * @param processorRegistry
     *            registr procesorů
     * @param forwardProcessingEnabled
     *            indikuje povolené dopředné zpracování
     */
    public AIMLTemplateParser(final Conversation conversation,
            final MatchResult matchResult,
            final ProcessorRegistry processorRegistry,
            final boolean forwardProcessingEnabled) {
        this.conversation = conversation;
        this.matchResult = matchResult;
        this.processorRegistry = processorRegistry;
        this.forwardProcessingEnabled = forwardProcessingEnabled;
        
        LOGGER.log(
                Level.INFO,
                "parser.AIMLTemplateParserCreated",
                new Object[] { conversation.getBot(), conversation,
                        matchResult.getTemplate() });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser#getBot()
     */
    @Override
    public Bot getBot() {
        return conversation.getBot();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser#
     * getConversation()
     */
    @Override
    public Conversation getConversation() {
        return conversation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser#
     * getMatchResult ()
     */
    @Override
    public MatchResult getMatchResult() {
        return matchResult;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser#process
     * (java.lang.String)
     */
    @Override
    public String process(final String input) throws ProcessorException {
        try {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "parser.AIMLTemplateParserProcessInput", input);
            }
            
            final Document template =
                    DOCUMENT_BUILDER.parse(new InputSource(new StringReader(
                            input)));

            return evaluate(template);
        } catch (final SAXException e) {
            throw new ProcessorException(e);
        } catch (final IOException e) {
            throw new ProcessorException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser#process
     * (org.w3c.dom.Element)
     */
    @Override
    public String process(final Element element) throws ProcessorException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "parser.AIMLTemplateParserProcessElement", element);
        }
        
        if (element == null) {
            return EMPTY_RESPONSE;
        }

        final String elementNamespaceDescription = element.getNamespaceURI();

        final URI elementNamespace =
                elementNamespaceDescription == null ? null : URI
                        .create(elementNamespaceDescription);

        if (elementNamespace == null
                || processorRegistry.getNamespace().equals(elementNamespace)) {
            final Class<? extends Processor> processorClass;
            try {
                processorClass =
                        processorRegistry.get(element.getLocalName(),
                                elementNamespace);
            } catch (final ClassNotFoundException e) {
                return handleUnknownElement(element, e);
            }

            return ClassManagment.getNewInstance(processorClass).process(
                    element, this);
        }
        
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "parser.AIMLTemplateParserProcessForeign", element);
        }
        
        if (element.getChildNodes().getLength() == 0) {
            return XML.createEmptyElement(element);
        }

        return XML.createElementStart(element)
                + evaluate(element.getChildNodes())
                + XML.createElementEnd(element);
    }

    /**
     * Zpracuje neznámý prvek.
     * 
     * @param element
     *            prvek
     * @param e
     *            vyvolávající chyba či výjimka
     * @return výstup zpracování
     */
    private String
            handleUnknownElement(final Element element, final Throwable e) {
        if (forwardProcessingEnabled) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.log(Level.FINER, "parser.AIMLTemplateHandleForward", element);
            }
            
            return EMPTY_RESPONSE;
        }

        throw new ParseError(MESSAGE_LOCALIZER.getMessage("parser.AIMLUnknownElement",
                element.getLocalName()), e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser#evaluate
     * (org.w3c.dom.Document)
     */
    @Override
    public String evaluate(final Document document) throws ProcessorException {
        return evaluate(document.getDocumentElement());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser#evaluate
     * (org.w3c.dom.NodeList)
     */
    @Override
    public String evaluate(final NodeList list) throws ProcessorException {
        final StringBuilder result = new StringBuilder();

        for (int index = 0; index < list.getLength(); index++) {
            result.append(evaluate(list.item(index)));
        }

        return result.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser#evaluate
     * (org.w3c.dom.Node)
     */
    @Override
    public String evaluate(final Node node) throws ProcessorException {
        String response = EMPTY_RESPONSE;

        if (node == null) {
            return response;
        }

        final short nodeType = node.getNodeType();
        switch (nodeType) {
        case Node.ELEMENT_NODE:
            response += process((Element) node);
            break;
        case Node.TEXT_NODE:
            response += node.getNodeValue();
            break;
        case Node.CDATA_SECTION_NODE:
            response += XML.CDATA_START + node.getNodeValue() + XML.CDATA_END;
            break;
        case Node.COMMENT_NODE:
            response +=
                    XML.COMMENT_START + node.getTextContent() + XML.COMMENT_END;
            break;
        default:
            break;
        }
        
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "parser.AIMLTemplateHandleNode", new Object[] { node, response });
        }
        
        return response;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser#
     * processShortenedTag(org.w3c.dom.Element, java.lang.String,
     * java.lang.String, short)
     */
    @Override
    public String processShortenedTag(final Element element,
            final String newElementName, final String childName)
            throws ProcessorException {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "parser.AIMLTemplateHandleShortened", new Object[] { element, newElementName, childName });
        }
        
        if (element == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("parser.ShortenedElementNull"));
        }

        if (newElementName == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("parser.NewElementNameNull"));
        }

        if (childName == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("parser.ChildNameNull"));
        }

        if (element.getChildNodes().getLength() != 0) {
            throw new IllegalArgumentException(
                    MESSAGE_LOCALIZER.getMessage("parser.ElementNotEmpty"));
        }
        
        final Document ownerDoc = element.getOwnerDocument();
        final Element newElement =
                ownerDoc.createElementNS(element.getNamespaceURI(),
                        newElementName);
        newElement.appendChild(ownerDoc.createElementNS(
                element.getNamespaceURI(), childName));

        return evaluate(newElement);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser#getVersion
     * ()
     */
    @Override
    public String getVersion() {
        return VERSION;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("AIMLTemplateParser [conversation=");
        builder.append(conversation);
        builder.append(", matchResult=");
        builder.append(matchResult);
        builder.append(", processorRegistry=");
        builder.append(processorRegistry);
        builder.append("]");
        return builder.toString();
    }
}
