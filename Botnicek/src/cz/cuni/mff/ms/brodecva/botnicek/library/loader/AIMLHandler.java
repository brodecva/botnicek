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

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLPartMarker;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLTemplate;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Handler pro parsování AIML souboru pomocí SAX.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLHandler extends DefaultHandler2 implements Handler,
        Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -740583819565890651L;

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLHandler.class);

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * URI prostoru jmen AIML.
     */
    private static final String AIML_NAMESPACE_URI = AIML.NAMESPACE_URI
            .getValue();

    /**
     * Uložiště, do kterého je obsah transformován.
     */
    private final MatchingStructure brain;

    /**
     * Bot.
     */
    private final Bot bot;

    /**
     * Téma zpracovávané části.
     */
    private String topic;

    /**
     * Obsah that tagu zpracovávané části.
     */
    private String that;

    /**
     * Obsah pattern tagu zpracovávané části.
     */
    private String pattern;

    /**
     * Obsah template tagu zpracovávané části.
     */
    private String template;

    /**
     * Buffer s textovým obsahem zpracovávaného tagu.
     */
    private final StringBuilder chars = new StringBuilder();

    /**
     * Výchozí strategie pro bílé znaky.
     */
    private final SpaceStrategy defaultStrategy;

    /**
     * Zachovávající strategie pro bílé znaky.
     */
    private final SpaceStrategy preserveStrategy;

    /**
     * Zásobník, který udržuje aktuální stav chování vůči bílým znakům ve větvi.
     */
    private final Deque<ElementSpacing> spacePolicyStack =
            new ArrayDeque<ElementSpacing>();

    /**
     * Obsluha chyb pro striktní režim.
     */
    private final ErrorHandler strictErrorHandler;

    /**
     * Indikátor právě zpracovávané části kategorie (pattern, that či template).
     */
    private AIMLPartMarker phase;

    /**
     * Vrátí nový objekt v daném stavu.
     * 
     * @param prototype
     *            prototyp pro vytvoření nového objektu.
     * @param topic
     *            téma
     * @param that
     *            odkazovaná promluva
     * @param pattern
     *            zpracovávaný vzor
     * @param template
     *            zpracovávaná šablona
     * @param chars
     *            načtené znaky
     * @param phase
     *            aktuální fáze
     * @param spacingStackContent
     *            zásobník pro uchovávání strategie pro zpracovávaný podstrom
     * @return obslužný objekt v zadaném stavu
     */
    public static AIMLHandler createInState(final AIMLHandler prototype,
            final String topic, final String that, final String pattern,
            final String template, final String chars,
            final AIMLPartMarker phase,
            final Deque<ElementSpacing> spacingStackContent) {
        LOGGER.log(Level.INFO, "loader.AIMLHandlerInState", new Object[] { prototype,
                topic, that, pattern, template, chars, phase,
                spacingStackContent });

        final AIMLHandler newInstance = new AIMLHandler(prototype);

        newInstance.topic = topic;
        newInstance.that = that;
        newInstance.pattern = pattern;
        newInstance.template = template;
        newInstance.phase = phase;
        newInstance.chars.setLength(0);
        newInstance.chars.append(chars);
        newInstance.spacePolicyStack.addAll(spacingStackContent);

        return newInstance;
    }

    /**
     * Vytvoří obslužný objekt ve výchozím stavu.
     * 
     * @param brain
     *            uložiště, do kterého je obsah transformován
     * @param bot
     *            nastavení robota
     */
    public AIMLHandler(final MatchingStructure brain, final Bot bot) {
        this(brain, bot, DefaultSpaceStrategy.create(), PreserveSpaceStrategy
                .create(), new AIMLErrorHandler());
        this.phase = AIMLPartMarker.UNDEFINED;
    }

    /**
     * Vytvoří nový obslužný objekt.
     * 
     * @param brain
     *            uložiště, do kterého je obsah transformován
     * @param bot
     *            nastavení robota
     * @param defaultStrategy
     *            výchozí strategie pro bílé znaky
     * @param preserveStrategy
     *            zachovávající strategie pro bílé znaky
     * @param strictErrorHandler
     *            obslužný objekt pro obsluhu chyb ve striktním režimu
     */
    public AIMLHandler(final MatchingStructure brain, final Bot bot,
            final SpaceStrategy defaultStrategy,
            final SpaceStrategy preserveStrategy,
            final ErrorHandler strictErrorHandler) {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "loader.AIMLHandlerConstruction", new Object[] { brain, bot, defaultStrategy, preserveStrategy, strictErrorHandler });
        }
        
        if (brain == null || bot == null || defaultStrategy == null
                || preserveStrategy == null || strictErrorHandler == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("loader.NullArgument"));
        }

        this.brain = brain;
        this.bot = bot;
        this.defaultStrategy = defaultStrategy;
        this.preserveStrategy = preserveStrategy;
        this.strictErrorHandler = strictErrorHandler;
    }

    /**
     * Vytvoří mělkou kopii objektu.
     * 
     * @param original
     *            původní objekt
     */
    public AIMLHandler(final AIMLHandler original) {
        LOGGER.log(Level.INFO, "loader.AIMLHandlerCopy", original);
        
        this.brain = original.brain;
        this.bot = original.bot;
        this.defaultStrategy = original.defaultStrategy;
        this.preserveStrategy = original.preserveStrategy;
        this.strictErrorHandler = original.strictErrorHandler;

        this.topic = original.topic;
        this.that = original.that;
        this.pattern = original.pattern;
        this.template = original.template;
        this.phase = original.phase;
        this.chars.append(original.chars.toString());
        this.spacePolicyStack.addAll(spacePolicyStack);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(final char[] ch, final int start, final int length) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "loader.AIMLHandlerCharacters", new Object[] { Arrays.toString(Arrays.copyOfRange(ch, start, start + length)) });
        }
        
        getCurrentSpacePolicy().transformChars(chars, ch, start, length);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#ignorableWhitespace(char[], int,
     * int)
     */
    @Override
    public void ignorableWhitespace(final char[] ch, final int start,
            final int length) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "loader.AIMLHandlerIgnorableSpaces", new Object[] { length });
        }
        
        getCurrentSpacePolicy().transformChars(chars, ch, start, length);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
     * java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(final String uri, final String localName,
            final String qName, final Attributes atts) throws SAXException {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "loader.AIMLHandlerStartElementBeforeState",
                        new Object[] { pattern, that, topic, template, chars });
            LOGGER.log(Level.FINER, "loader.AIMLHandlerStartElement", new Object[] { uri,
                    localName, qName, atts });
        }

        pushPolicy(uri, localName, atts);

        if (uri.equals(AIML_NAMESPACE_URI)) {
            if (localName.equals(AIML.AIML.getValue())) {
                final String documentVersion =
                        atts.getValue(AIML.ATT_VERSION.getValue());

                determineProcessingMode(documentVersion);
            } else if (localName.equals(AIML.CATEGORY.getValue())) {
                phase = AIMLPartMarker.UNDEFINED;
            } else if (localName.equals(AIML.PATTERN.getValue())) {
                resetCharBuffer();
                phase = AIMLPartMarker.PATTERN;
            } else if (localName.equals(AIML.BOT.getValue())
                    && phase != AIMLPartMarker.TEMPLATE) {
                chars.append(bot.getPredicateValue(atts.getValue(AIML.NAME
                        .getValue())));
            } else if (localName.equals(AIML.THAT.getValue())
                    && phase != AIMLPartMarker.TEMPLATE) {
                resetCharBuffer();
                phase = AIMLPartMarker.THAT;
            } else if (localName.equals(AIML.TEMPLATE.getValue())) {
                resetCharBuffer();

                openTag(uri, localName, atts);

                phase = AIMLPartMarker.TEMPLATE;
            } else if (phase == AIMLPartMarker.TEMPLATE) {
                openTag(uri, localName, atts);
            } else if (localName.equals(AIML.TOPIC.getValue())
                    && phase != AIMLPartMarker.TEMPLATE) {
                topic = atts.getValue(AIML.NAME.getValue());
            } else if (isForwardProcessingEnabled()) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "loader.AIMLHandlerForwardProcessing",
                            new Object[] { localName, uri });
                }
            } else {
                throw new SAXException("NotValidSource");
            }
        } else {
            if (phase == AIMLPartMarker.TEMPLATE) {
                openTag(uri, localName, atts);
            } else {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "loader.AIMLHandlerForeign", new Object[] {
                            localName, uri });
                }
            }
        }
        
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "loader.AIMLHandlerStartElementAfterState",
                    new Object[] { pattern, that, topic, template, chars });
        }
    }

    /**
     * Vloží otevřený tag do bufferu znaků.
     * 
     * @param uri
     *            URI
     * @param localName
     *            místní jméno
     * @param atts
     *            atributy (bez definic prostorů jmen)
     */
    private void openTag(final String uri, final String localName,
            final Attributes atts) {
        getCurrentSpacePolicy().transformOpenTag(chars);
        chars.append(XML.createElementStart(uri, localName, atts));
    }

    /**
     * Vrátí aktuální opatření ohledně bílých znaků v podstromu dokumentu.
     * 
     * @return aktuální opatření ohledně bílých znaků v podstromu dokumentu
     */
    private SpaceStrategy getCurrentSpacePolicy() {
        if (spacePolicyStack.isEmpty()) {
            return defaultStrategy;
        }

        return spacePolicyStack.peek().getSpaceStrategy();
    }

    /**
     * Detekuje, zda-li prvek definuje opatření ohledně chování k bílým znakům a
     * případně jej přidá na zásobník.
     * 
     * @param elementUri
     *            URI prostoru jmen prvku
     * @param elementName
     *            jméno prvku
     * @param atts
     *            atributy prvku
     */
    private void pushPolicy(final String elementUri, final String elementName,
            final Attributes atts) {
        final String whitespacePolicy = atts.getValue(XML.SPACE_ATT.getValue());
        if (whitespacePolicy == null) {
            final boolean isAimlRoot =
                    elementName.equals(AIML.AIML.getValue())
                            && (elementUri == null || elementUri
                                    .equals(AIML.NAMESPACE_URI.getValue()));

            if (isAimlRoot) {
                spacePolicyStack.push(new ElementSpacing(elementUri,
                        elementName, defaultStrategy));
            }

            return;
        }

        final SpaceStrategy subtreeStrategy;
        if (whitespacePolicy.equals(XML.SPACE_PRESERVE.getValue())) {
            subtreeStrategy = preserveStrategy;
        } else if (whitespacePolicy.equals(XML.SPACE_DEFAULT)) {
            subtreeStrategy = defaultStrategy;
        } else {
            subtreeStrategy = null;
            assert false;
        }

        if (getCurrentSpacePolicy().equals(subtreeStrategy)) {
            return;
        }
        
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "loader.AIMLHandlerCustomPolicyPushed", new Object[] { elementUri, elementName, atts, subtreeStrategy });
        }
        spacePolicyStack.push(new ElementSpacing(elementUri, elementName,
                subtreeStrategy));
    }

    /**
     * Zapne dopředné zpracování, pokud se setká s jinou než implementovanou
     * verzí.
     * 
     * @param documentVersion
     *            verze AIML v dokumentu
     */
    private void determineProcessingMode(final String documentVersion) {
        final String implementedVersion = AIML.IMPLEMENTED_VERSION.getValue();

        if (!documentVersion.equals(implementedVersion)) {
            LOGGER.log(Level.INFO, "loader.AIMLHandlerForwardSet", documentVersion);
            
            brain.setForwardCompatible(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(final String uri, final String localName,
            final String qName) throws SAXException {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "loader.AIMLHandlerEndElement", new Object[] { uri,
                    localName, qName });
            LOGGER.log(Level.FINER, "loader.AIMLHandlerEndElementBeforeState",
                    new Object[] { pattern, that, topic, template, chars });
        }
        
        if (uri.equals(AIML_NAMESPACE_URI)) {
            if (phase == AIMLPartMarker.PATTERN) {
                pattern = getCharacters();
            } else if (phase == AIMLPartMarker.THAT) {
                that = getCharacters();
            } else if (phase == AIMLPartMarker.TEMPLATE) {
                closeTag(localName);

                if (localName.equals(AIML.TEMPLATE.getValue())) {
                    template = getCharacters();

                    brain.add(new AIMLInputPath(pattern, that, topic),
                            new AIMLTemplate(template));

                    pattern = null;
                    that = null;
                    template = null;
                    phase = AIMLPartMarker.UNDEFINED;
                }
            } else if (localName.equals(AIML.TOPIC.getValue())) {
                topic = null;
            } else if (isForwardProcessingEnabled()) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "loader.AIMLHandlerForwardProcessingEnd",
                            new Object[] { localName, uri });
                }
            }
        } else {
            if (phase == AIMLPartMarker.TEMPLATE) {
                closeTag(localName);
            } else {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "loader.AIMLHandlerForeignEnd", new Object[] {
                            localName, uri });
                }
            }
        }

        popPolicy(uri, localName);
        
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "loader.AIMLHandlerEndElementAfterState",
                    new Object[] { pattern, that, topic, template, chars });
        }
    }

    /**
     * Korektně uzavře tag v šabloně na bufferu znaků, ať už je zkrácený či
     * párový.
     * 
     * @param localName
     *            místní jméno prvku
     */
    private void closeTag(final String localName) {
        final int templateLength = chars.length();

        assert templateLength >= 2; // Měla by obsahovat začátek.

        final String templateTwoLastChars = chars.substring(templateLength - 2);
        final String templateLastChar =
                templateTwoLastChars
                        .substring(templateTwoLastChars.length() - 1);

        if (templateLastChar.equals(XML.TAG_END.getValue())
                && !templateTwoLastChars.equals(XML.EMPTY_TAG_END.getValue())) {
            final int lastOpenIndex =
                    chars.lastIndexOf(XML.TAG_START.getValue());
            if (chars.indexOf(localName, lastOpenIndex) == lastOpenIndex + 1) {
                chars.insert(templateLength - 1, XML.SLASH.getValue());
                return;
            }
        }

        getCurrentSpacePolicy().transformCloseTag(chars);

        chars.append(XML.createElementEnd(localName));
    }

    /**
     * Odstraní z vrcholu zásobníku strategii pro nakládání s bílými znaky,
     * pokud se asociovaný prvek shoduje.
     * 
     * @param uri
     *            URI prostoru jmen prvku
     * @param name
     *            jméno prvku
     */
    private void popPolicy(final String uri, final String name) {
        final ElementSpacing currentSpacing = spacePolicyStack.peek();

        if (currentSpacing.equals(new ElementSpacing(uri, name, currentSpacing
                .getSpaceStrategy()))) {
            spacePolicyStack.pop();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ext.DefaultHandler2#startCDATA()
     */
    @Override
    public void startCDATA() {
        chars.append(XML.CDATA_START);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ext.DefaultHandler2#endCDATA()
     */
    @Override
    public void endCDATA() {
        chars.append(XML.CDATA_END);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xml.sax.helpers.DefaultHandler#error(org.xml.sax.SAXParseException)
     */
    @Override
    public void error(final SAXParseException spe) throws SAXException {
        if (isForwardProcessingEnabled()) {
            warning(spe);
            return;
        }

        strictErrorHandler.error(spe);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.xml.sax.helpers.DefaultHandler#warning(org.xml.sax.SAXParseException)
     */
    @Override
    public void warning(final SAXParseException spe) throws SAXException {
        strictErrorHandler.warning(spe);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.helpers.DefaultHandler#
     * fatalError(org.xml.sax.SAXParseException )
     */
    @Override
    public void fatalError(final SAXParseException spe) throws SAXException {
        strictErrorHandler.fatalError(spe);
    }

    /**
     * Vrátí textový obsah zpracovávaného elementu a resetuje buffer.
     * 
     * @return textový obsah elementu
     */
    public String getCharacters() {
        return chars.toString();
    }

    /**
     * Smaže zásobník znaků.
     */
    private void resetCharBuffer() {
        chars.setLength(0);
    }

    /**
     * Vrátí otevřené téma.
     * 
     * @return otevřené téma
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Vrátí otevřený odkaz na předchozí promluvu.
     * 
     * @return otevřený odkaz na předchozí promluvu
     */
    public String getThat() {
        return that;
    }

    /**
     * Vrátí zpracovávaný vzor.
     * 
     * @return zpracovávaný vzor
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Vrátí zpracovávanou šablonu.
     * 
     * @return zpracovávaná šablona
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Vrátí aktuální fázi.
     * 
     * @return aktuální fáze
     */
    public AIMLPartMarker getPhase() {
        return phase;
    }

    /**
     * Indikuje povolené dopředné zpracování.
     * 
     * @return true, pokud je povoleno dopředné zpracování
     */
    public boolean isForwardProcessingEnabled() {
        return brain.isForwardCompatible();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("AIMLHandler [brain=");
        builder.append(brain);
        builder.append(", bot=");
        builder.append(bot);
        builder.append(", topic=");
        builder.append(topic);
        builder.append(", that=");
        builder.append(that);
        builder.append(", pattern=");
        builder.append(pattern);
        builder.append(", template=");
        builder.append(template);
        builder.append(", chars=");
        builder.append(chars);
        builder.append(", phase=");
        builder.append(phase);
        builder.append(", strictErrorHandler=");
        builder.append(strictErrorHandler);
        builder.append(", forwardProcessingEnabled=");
        builder.append(isForwardProcessingEnabled());
        builder.append(", preserveStrategy=");
        builder.append(preserveStrategy);
        builder.append(", defaultStrategy=");
        builder.append(defaultStrategy);
        builder.append(", spacePolicyStack=");
        builder.append(spacePolicyStack);
        builder.append("]");
        return builder.toString();
    }
}
