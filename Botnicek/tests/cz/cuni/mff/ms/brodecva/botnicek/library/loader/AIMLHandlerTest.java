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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayDeque;
import java.util.Deque;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLPartMarker;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.Template;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje obslužný objekt pro zpracování AIML pomocí SAX.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AIMLHandler
 */
@Category(UnitTest.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest(XML.class)
public final class AIMLHandlerTest {

    /**
     * Řetězec URI prostoru jmen jazyka AIML.
     */
    private static final String AIML_URI = AIML.NAMESPACE_URI.getValue();

    /**
     * Cizí řetězec URI.
     */
    private static final String FOREIGN_URI = "http://foreign.uri.com";
    
    /**
     * Prázdný řetězec.
     */
    private static final Object EMPTY_STRING = "";

    /**
     * Hodnota predikátu.
     */
    private static final String PREDICATE_VALUE = "predicateValue";

    /**
     * Název tématu.
     */
    private static final String TOPIC_NAME = "topicName";

    /**
     * Jméno predikátu.
     */
    private static final String PREDICATE_NAME = "predicateName";

    /**
     * Znaky vzoru po inicializaci.
     */
    private static final String PATTERN_CHARS = "CHARS IN PATTERN";

    /**
     * Znaky reference na předchozí promluvu po inicializaci.
     */
    private static final String THAT_CHARS = "CHARS IN THAT";

    /**
     * Znaky šablony po inicializaci.
     */
    private static final String TEMPLATE_CHARS = "<template xmlns=\"" + AIML_URI + "\">CHARS IN TEMPLATE";

    /**
     * Znaky párové značky v šabloně po inicializaci.
     */
    private static final String FULL_TAG_IN_TEMPLATE_CHARS =
            "content around <full-tag>full tag content";

    /**
     * Začátek fáze zpracování.
     */
    private AIMLHandler startPhaseHandler = null;

    /**
     * Prázdný seznam atributů.
     */
    private Attributes emptyAttributesStub = null;

    /**
     * Seznam obsahující jediný atribut - název robota.
     */
    private Attributes botNameAttribute = null;

    /**
     * Stub atributů prvku v šabloně.
     */
    private Attributes inTemplateElementAttsStub = null;

    /**
     * Zpracování šablony.
     */
    private AIMLHandler templateHandler = null;

    /**
     * Seznam s jedním atributem - popisem tématu.
     */
    private Attributes topicNameAttribute = null;

    /**
     * Zpracování konce vzoru.
     */
    private AIMLHandler endPatternHandler = null;

    /**
     * Zpracování konce reference.
     */
    private AIMLHandler endThatHandler = null;

    /**
     * Zpracování konce šablony.
     */
    private AIMLHandler endTemplateHandler = null;

    /**
     * Mock plněné struktury.
     */
    private MatchingStructure structureMock = null;

    /**
     * Zpracování párového prvku v šabloně.
     */
    private AIMLHandler inTemplateFullTagHandler = null;

    /**
     * Zpracování prázdného prvku v šabloně (bez atributů).
     */
    private AIMLHandler inTemplateEmptyTagHandler = null;

    /**
     * Zpracování konce tématu.
     */
    private AIMLHandler endTopicHandler = null;

    /**
     * Stub robota.
     */
    private Bot botStub = null;

    /**
     * Zpracování prázdného prvku v šabloně.
     */
    private AIMLHandler inTemplateEmptyTagWithAttsHandler = null;

    /**
     * Zachycená šablona.
     */
    private Capture<Template> capturedTemplate = null;

    /**
     * Zpracování konce cizího prvku.
     */
    private AIMLHandler foreignEndHandler = null;

    /**
     * Objekt pro obsluhu chyb, který žádnou nečeká.
     */
    private ErrorHandler noErrorHandlerMock = null;

    /**
     * Nastaví testované objekty.
     */
    @Before
    public void setUp() {
        structureMock = EasyMock.createMock(MatchingStructure.class);
        capturedTemplate = new Capture<Template>();
        structureMock.add(isA(AIMLInputPath.class),
                EasyMock.capture(capturedTemplate));
        expect(structureMock.isForwardCompatible()).andStubReturn(false);
        replay(structureMock);

        botNameAttribute = EasyMock.createMock(Attributes.class);
        expect(botNameAttribute.getValue(AIML.ATT_NAME.getValue())).andReturn(
                PREDICATE_NAME);
        expect(botNameAttribute.getValue(eq("xml:space"))).andStubReturn(null);
        replay(botNameAttribute);

        botStub = EasyMock.createMock(Bot.class);
        expect(botStub.getPredicateValue(PREDICATE_NAME)).andStubReturn(
                PREDICATE_VALUE);
        replay(botStub);

        emptyAttributesStub = EasyMock.createMock(Attributes.class);
        expect(emptyAttributesStub.getValue(eq("xml:space"))).andStubReturn(null);
        expect(emptyAttributesStub.getLength()).andStubReturn(0);
        replay(emptyAttributesStub);

        inTemplateElementAttsStub = EasyMock.createMock(Attributes.class);
        expect(inTemplateElementAttsStub.getValue(eq("xml:space"))).andStubReturn(null);
        replay(inTemplateElementAttsStub);

        topicNameAttribute = EasyMock.createMock(Attributes.class);
        expect(topicNameAttribute.getValue(AIML.ATT_NAME.getValue()))
                .andReturn(TOPIC_NAME);
        expect(topicNameAttribute.getValue(eq("xml:space"))).andStubReturn(null);
        replay(topicNameAttribute);

        noErrorHandlerMock = EasyMock.createMock(ErrorHandler.class);
        replay(noErrorHandlerMock);

        final SpaceStrategy defaultStrategyStub = new SpaceStrategy() {
            
            @Override
            public void transformOpenTag(final StringBuilder chars) {
            }
            
            @Override
            public void transformCloseTag(final StringBuilder chars) {
            }
            
            @Override
            public void transformChars(final StringBuilder chars, final char[] ch, final int start,
                    final int length) {
                chars.append(ch, start, length);
            }
        };

        final SpaceStrategy preserveStrategyStub = defaultStrategyStub;

        final AIMLHandler prototype =
                new AIMLHandler(structureMock, botStub, defaultStrategyStub,
                        preserveStrategyStub, noErrorHandlerMock);

        final ElementSpacing elementSpacingStub =
                new ElementSpacing("http://dummy.com", "dummy",
                        defaultStrategyStub);

        final Deque<ElementSpacing> spacingStackStub = new ArrayDeque<ElementSpacing>();
        spacingStackStub.add(elementSpacingStub);
        
        startPhaseHandler =
                AIMLHandler.createInState(prototype, null, null, null, null,
                        "", AIMLPartMarker.UNDEFINED, spacingStackStub);

        templateHandler =
                AIMLHandler.createInState(prototype, null, null, null, null,
                        "", AIMLPartMarker.TEMPLATE, spacingStackStub);

        endPatternHandler =
                AIMLHandler.createInState(prototype, null, null, null, null,
                        PATTERN_CHARS, AIMLPartMarker.PATTERN,
                        spacingStackStub);

        endThatHandler =
                AIMLHandler.createInState(prototype, null, null, null, null,
                        THAT_CHARS, AIMLPartMarker.THAT,
                        spacingStackStub);

        endTemplateHandler =
                AIMLHandler.createInState(prototype, null, null, null, null,
                        TEMPLATE_CHARS, AIMLPartMarker.TEMPLATE,
                        spacingStackStub);

        inTemplateFullTagHandler =
                AIMLHandler.createInState(prototype, null, null, null, null,
                        FULL_TAG_IN_TEMPLATE_CHARS, AIMLPartMarker.TEMPLATE,
                        spacingStackStub);

        inTemplateEmptyTagHandler =
                AIMLHandler.createInState(prototype, null, null, null, null,
                        "content around <empty-tag>", AIMLPartMarker.TEMPLATE,
                        spacingStackStub);

        inTemplateEmptyTagWithAttsHandler =
                AIMLHandler
                        .createInState(
                                prototype,
                                null,
                                null,
                                null,
                                null,
                                "content around <empty-tag-with-atts first-att=\"value\" second-att=\"another value\">",
                                AIMLPartMarker.TEMPLATE,
                                spacingStackStub);

        endTopicHandler =
                AIMLHandler.createInState(prototype, "current topic", null,
                        null, null, "", AIMLPartMarker.UNDEFINED,
                        spacingStackStub);

        foreignEndHandler =
                AIMLHandler.createInState(prototype, null, null, null, null,
                        "", AIMLPartMarker.UNDEFINED, spacingStackStub);
    }

    /**
     * Uklidí testované objekty.
     */
    @After
    public void tearDown() {
        botNameAttribute = null;

        botStub = null;

        capturedTemplate = null;

        emptyAttributesStub = null;

        endPatternHandler = null;

        endTemplateHandler = null;

        endThatHandler = null;

        endTopicHandler = null;

        inTemplateElementAttsStub = null;

        inTemplateEmptyTagHandler = null;

        inTemplateEmptyTagWithAttsHandler = null;

        inTemplateFullTagHandler = null;

        startPhaseHandler = null;

        structureMock = null;

        templateHandler = null;

        noErrorHandlerMock = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#characters(char[], int, int)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testCharactersAddsToChars() throws SAXException {
        startPhaseHandler.characters(new char[] { 'n', 'e', 'w', 'c', 'h', 'a',
                'r', 's' }, 2, 2);
        assertEquals("wc", startPhaseHandler.getCharacters());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testStartElementWhenPatternExpectPhasePatternAndEmptyChars()
            throws SAXException {
        startPhaseHandler.startElement(AIML_URI, "pattern", "",
                emptyAttributesStub);

        verify(emptyAttributesStub);

        assertEquals(AIMLPartMarker.PATTERN, startPhaseHandler.getPhase());
        assertEquals(EMPTY_STRING, startPhaseHandler.getCharacters());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testStartElementWhenBotExpectCharsEqualsPredicateValue()
            throws SAXException {
        startPhaseHandler
                .startElement(AIML_URI, "bot", "", botNameAttribute);

        verify(botNameAttribute);

        assertEquals(PREDICATE_VALUE, startPhaseHandler.getCharacters());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testStartElementWhenThatExpectPhaseThatAndEmptyChars()
            throws SAXException {
        startPhaseHandler.startElement(AIML_URI, "that", "",
                emptyAttributesStub);

        verify(emptyAttributesStub);

        assertEquals(AIMLPartMarker.THAT, startPhaseHandler.getPhase());
        assertEquals(EMPTY_STRING, startPhaseHandler.getCharacters());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testStartElementWhenTemplateExpectPhaseTemplateAndEmptyChars()
            throws SAXException {
        PowerMock.mockStatic(XML.class);
        expect(
                XML.createElementStart(AIML_URI, "template",
                        emptyAttributesStub))
                .andReturn(
                        "<template xmlns=\"" + AIML_URI + "\">");
        PowerMock.replayAll();

        
        startPhaseHandler.startElement(AIML_URI, "template", "",
                emptyAttributesStub);

        verify(emptyAttributesStub);
        PowerMock.verifyAll();

        assertEquals(AIMLPartMarker.TEMPLATE, startPhaseHandler.getPhase());
        assertEquals("<template xmlns=\"" + AIML_URI + "\">", startPhaseHandler.getCharacters());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testStartElementWhenInTemplate() throws SAXException {
        PowerMock.mockStatic(XML.class);
        expect(
                XML.createElementStart(AIML_URI, "in-template-element",
                        inTemplateElementAttsStub))
                .andReturn(
                        "<in-template-element xmlns=\"" + AIML_URI + "\" first-att=\"value\" prefix:second-att=\"another value\">");
        PowerMock.replayAll();

        templateHandler.startElement(AIML_URI, "in-template-element",
                "", inTemplateElementAttsStub);

        PowerMock.verifyAll();

        assertEquals(
                "<in-template-element xmlns=\"" + AIML_URI + "\" first-att=\"value\" prefix:second-att=\"another value\">",
                templateHandler.getCharacters());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testStartElementWhenTopic() throws SAXException {
        startPhaseHandler.startElement(AIML_URI, "topic", "",
                topicNameAttribute);

        verify(topicNameAttribute);

        assertEquals(TOPIC_NAME, startPhaseHandler.getTopic());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testStartElementWhenElementForeign() throws SAXException {
        startPhaseHandler.startElement(FOREIGN_URI, "foreign-element",
                "", emptyAttributesStub);

        verify(emptyAttributesStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testEndElementWhenPatternExpectPatternEqualsChars()
            throws SAXException {
        endPatternHandler.endElement(AIML_URI, "pattern", "");

        assertEquals(PATTERN_CHARS, endPatternHandler.getPattern());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testEndElementWhenThatExpectThatEqualsChars()
            throws SAXException {
        endThatHandler.endElement(AIML_URI, "that", "");

        assertEquals(THAT_CHARS, endThatHandler.getThat());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testEndElementWhenTemplateExpectTemplateWrapsChars()
            throws SAXException {
        endTemplateHandler.endElement(AIML_URI, "template", "");

        assertEquals(TEMPLATE_CHARS + "</template>",
                capturedTemplate.getValue().getValue());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testEndElementWhenTemplateExpectBrainAddsPathAndTemplate()
            throws SAXException {
        endTemplateHandler.endElement(AIML_URI, "template", "");

        verify(structureMock);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void
            testEndElementWhenInTemplateFullTagExpectCharsWithFullTagClosed()
                    throws SAXException {
        PowerMock.mockStatic(XML.class);
        expect(
                XML.createElementEnd("full-tag"))
                .andReturn(
                        "</full-tag>");
        PowerMock.replayAll();

        inTemplateFullTagHandler.endElement(AIML_URI, "full-tag", "");
        
        PowerMock.verifyAll();

        assertEquals(FULL_TAG_IN_TEMPLATE_CHARS + "</full-tag>",
                inTemplateFullTagHandler.getCharacters());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void
            testEndElementWhenInTemplateEmptyTagExpectCharsWithEmptyTagClosed()
                    throws SAXException {
        inTemplateEmptyTagHandler
                .endElement(AIML_URI, "empty-tag", "");

        assertEquals("content around <empty-tag/>",
                inTemplateEmptyTagHandler.getCharacters());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public
            void
            testEndElementWhenInTemplateEmptyTagWithAttsExpectCharsWithEmptyTagClosed()
                    throws SAXException {
        inTemplateEmptyTagWithAttsHandler.endElement(AIML_URI,
                "empty-tag-with-atts", "");

        assertEquals(
                "content around <empty-tag-with-atts first-att=\"value\" second-att=\"another value\"/>",
                inTemplateEmptyTagWithAttsHandler.getCharacters());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testEndElementWhenTopicExpectTopicNull() throws SAXException {
        endTopicHandler.endElement(AIML_URI, "topic", "");

        assertNull(endTopicHandler.getTopic());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)}
     * .
     * 
     * @throws SAXException
     *             chyba při zpracování
     */
    @Test
    public void testEndElementWhenElementForeignExpectCharsUnchanged() throws SAXException {
        foreignEndHandler.endElement(FOREIGN_URI, "foreign-element",
                "");
        
        assertEquals(EMPTY_STRING, foreignEndHandler.getCharacters());
    }
}
