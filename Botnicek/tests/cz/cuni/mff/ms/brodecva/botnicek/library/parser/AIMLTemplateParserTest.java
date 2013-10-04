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

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorException;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorRegistry;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassManagment;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje parser šablony AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AIMLTemplateParser
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ XML.class, ClassManagment.class, AIMLTemplateParser.class })
@Category(UnitTest.class)
public final class AIMLTemplateParserTest {
    
    /**
     * Testovací implementace tisknoucí prvek AIML.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    public static final class PrintsAimlElementProcessor implements Processor {
        
        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.processor.Processor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)
         */
        @Override
        public String process(final Element element, final TemplateParser parser)
                throws ProcessorException {
            return PRINTED_AIML_ELEMENT;
        }
        
    }
    
    /**
     * Testovací implementace tisknoucí známý prvek.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    public static final class PrintsKnownElementProcessor implements Processor {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.processor.Processor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)
         */
        @Override
        public String process(final Element element, final TemplateParser parser)
                throws ProcessorException {
            return PRINTED_KNOWN_ELEMENT;
        }
        
    }
    
    /**
     * Výsledek zpracování neznámého prvku.
     */
    private static final String UNKNOW_ELEMENT_HANDLING_RESULT = "unknownHandlingResult";
    
    /**
     * Vytištěný (zpracovaný) prázdný prvek nepatřící do prostoru jmen AIML.
     */
    private static final String PRINTED_NOT_AIML_EMPTY_ELEMENT = "printedNotAimlEmptyElement";
    
    /**
     * Vytištěný prvek nepatřící do prostoru jmen AIML.
     */
    private static final String PRINTED_NOT_AIML_ELEMENT = "printedNotAimlElement";
    
    /**
     * Vytištěný AIML prvek.
     */
    private static final String PRINTED_AIML_ELEMENT = "printedAimlElement";
    
    /**
     * Jméno nového prvku - náhrady za zkrácený.
     */
    private static final String NEW_ELEMENT_NAME = "newElementName";
    
    /**
     * Jméno synovského prvku přidaného k novému prvku - náhradě za zkrácený.
     */
    private static final String CHILD_NAME = "childContent";
    
    /**
     * Vytištěný nahrazený prvek.
     */
    private static final String FULL_ELEMENT_PRINTED = "fullElementPrinted";
    
    /**
     * Jméno stubu AIML prvku.
     */
    private static final String AIML_ELEMENT_STUB_NAME = "aimlElementStubName";
    
    /**
     * Řetězec popisující URI prostoru jmen AIML.
     */
    private static final String AIML_NAMESPACE_URI = AIML.NAMESPACE_URI.getValue();
    
    /**
     * URI prostoru jmen AIML.
     */
    private static final URI AIML_NAMESPACE = URI.create(AIML_NAMESPACE_URI);
    
    /**
     * Jméno neznámého prvku v prostoru jmen AIML.
     */
    private static final String UNKNOWN_AIML_ELEMENT_STUB_NAME = "unknownAimlElementStubName";
    
    /**
     * Jméno známého prvku bez označení prostoru jmen.
     */
    private static final String KNOWN_ELEMENT_STUB_NAME = "knownElementStubName";
    
    /**
     * Řetězec popisující URI, které se liší od URI prostoru jmen AIML.
     */
    private static final String NOT_AIML_NAMESPACE_URI = "http://not.aiml.namespace.uri.com";
    
    /**
     * Jméno neznámého prvku mimo prostor jmen AIML.
     */
    private static final String UNKNOWN_ELEMENT_STUB_NAME = "unknownElementStubName";
    
    /**
     * Vytištěný známý prvek mimo prostor jmen AIML.
     */
    private static final String PRINTED_KNOWN_ELEMENT = "printedKnownElement";
    
    /**
     * Vytištěný začátek tagu prvku mimo prostor jmen AIML.
     */
    private static final String PRINTED_NOT_AIML_ELEMENT_START = "printedNotAimlElementStart";
    
    /**
     * Vytištěný konec tagu prvku mimo prostor jmen AIML.
     */
    private static final String PRINTED_NOT_AIML_ELEMENT_END = "printedNotAimlElementEnd";
    
    /**
     * Stub konverzace, ze pro kterou se parsování vykonává.
     */
    private Conversation conversationStub = null;
    
    /**
     * Výsledek hledání vstupní cesty.
     */
    private MatchResult matchResultStub = null;
    
    /**
     * Částečný mock parseru pro testování metody {@link AIMLTemplateParser#process(Element)}.
     */
    private TemplateParser parserMockForProcess = null;
    
    /**
     * Mock registru procesorů.
     */
    private ProcessorRegistry registryMock = null;
    
    /**
     * Stub prvku mimo prostor jmen AIML.
     */
    private Element notAimlElementStub = null;
    
    /**
     * Stub prázdného prvku mimo prostor jmen AIML.
     */
    private Element emptyNotAimlElementStub = null;
    
    /**
     * Stub AIML prvku.
     */
    private Element aimlElementStub = null;
    
    /**
     * Stub neznámého prvku v prostoru jmen AIML.
     */
    private Element unknownAimlElementStub = null;
    
    /**
     * Stub známého prvku mimo prostor jmen AIML.
     */
    private Element knownElementStub = null;
    
    /**
     * Stub neznámého prvku mimo prostor jmen AIML.
     */
    private Element unknownElementStub = null;
    
    /**
     * Stub zkráceného prvku.
     */
    private Element shortElementStub = null;
    
    /**
     * Mock {@link org.w3c.dom.Document} pro testování {@link AIMLTemplateParser#processShortenedTag(Element, String, String)}.
     */
    private Document documentMock = null;
    
    /**
     * Stub nového synovského prvku při zpracování zkráceného prvku.
     */
    private Element childElementStub = null;
    
    /**
     * Stub nového prvku, vytvořeného ze zkráceného.
     */
    private Element newElementStub = null;
    
    /**
     * Mock prázdného seznamu uzlů.
     */
    private NodeList emptyNodeListMock = null;
    
    /**
     * Stub neprázdného (a tudíž neplatného) zkráceného prvku.
     */
    private Element notEmptyShortElementStub = null;
    
    /**
     * Mock neprázdného seznamu uzlů.
     */
    private NodeList notEmptyNodeListMock = null;
    
    /**
     * Částečný mock pro testování {@link AIMLTemplateParser#processShortenedTag(Element, String, String)}.
     */
    private AIMLTemplateParser parserMockForProcessShortenedTag = null;

    /**
     * Částečný mock pro testování dopřeného chování {@link AIMLTemplateParser#processShortenedTag(Element, String, String)}.
     */
    private AIMLTemplateParser forwardParserMockForProcess;
    
    /**
     * Nastaví mocky, stuby a volání statických metod.
     * 
     * @throws Exception chyba
     */
    @SuppressWarnings("rawtypes")
    @Before
    public void setUp() throws Exception {
        conversationStub = EasyMock.createMock(Conversation.class);
        expect(conversationStub.getBot()).andStubReturn(null);
        replay(conversationStub);
        
        matchResultStub = EasyMock.createMock(MatchResult.class);
        expect(matchResultStub.getTemplate()).andStubReturn(null);
        replay(matchResultStub);
        
        childElementStub = EasyMock.createMock(Element.class);
        replay(childElementStub);
        
        newElementStub = EasyMock.createMock(Element.class);
        expect(newElementStub.appendChild(childElementStub)).andStubReturn(childElementStub);
        replay(newElementStub);
        
        documentMock = EasyMock.createMock(Document.class);
        expect(documentMock.createElementNS(AIML_NAMESPACE_URI, NEW_ELEMENT_NAME)).andStubReturn(newElementStub);
        expect(documentMock.createElementNS(AIML_NAMESPACE_URI, CHILD_NAME)).andStubReturn(childElementStub);
        replay(documentMock);
        
        aimlElementStub = EasyMock.createMock(Element.class);
        expect(aimlElementStub.getNamespaceURI()).andStubReturn(AIML_NAMESPACE_URI);
        expect(aimlElementStub.getLocalName()).andStubReturn(AIML_ELEMENT_STUB_NAME);
        replay(aimlElementStub);
        
        unknownAimlElementStub = EasyMock.createMock(Element.class);
        expect(unknownAimlElementStub.getNamespaceURI()).andStubReturn(AIML_NAMESPACE_URI);
        expect(unknownAimlElementStub.getLocalName()).andStubReturn(UNKNOWN_AIML_ELEMENT_STUB_NAME);
        replay(unknownAimlElementStub);
        
        knownElementStub = EasyMock.createMock(Element.class);
        expect(knownElementStub.getNamespaceURI()).andStubReturn(null);
        expect(knownElementStub.getLocalName()).andStubReturn(KNOWN_ELEMENT_STUB_NAME);
        replay(knownElementStub);
        
        unknownElementStub = EasyMock.createMock(Element.class);
        expect(unknownElementStub.getNamespaceURI()).andStubReturn(null);
        expect(unknownElementStub.getLocalName()).andStubReturn(UNKNOWN_ELEMENT_STUB_NAME);
        replay(unknownElementStub);
        
        emptyNodeListMock = EasyMock.createMock(NodeList.class);
        expect(emptyNodeListMock.getLength()).andStubReturn(0);
        replay(emptyNodeListMock);
        
        emptyNotAimlElementStub = EasyMock.createMock(Element.class);
        expect(emptyNotAimlElementStub.getChildNodes()).andStubReturn(emptyNodeListMock);
        expect(emptyNotAimlElementStub.getNamespaceURI()).andStubReturn(NOT_AIML_NAMESPACE_URI);
        replay(emptyNotAimlElementStub);
        
        shortElementStub = EasyMock.createMock(Element.class);
        expect(shortElementStub.getNamespaceURI()).andStubReturn(AIML_NAMESPACE_URI);
        expect(shortElementStub.getOwnerDocument()).andStubReturn(documentMock);
        expect(shortElementStub.getChildNodes()).andStubReturn(emptyNodeListMock);
        replay(shortElementStub);
        
        notEmptyNodeListMock = EasyMock.createMock(NodeList.class);
        expect(notEmptyNodeListMock.getLength()).andStubReturn(1);
        replay(notEmptyNodeListMock);
        
        notAimlElementStub = EasyMock.createMock(Element.class);
        expect(notAimlElementStub.getChildNodes()).andStubReturn(notEmptyNodeListMock);
        expect(notAimlElementStub.getNamespaceURI()).andStubReturn(NOT_AIML_NAMESPACE_URI);
        replay(notAimlElementStub);
        
        notEmptyShortElementStub = EasyMock.createMock(Element.class);
        expect(notEmptyShortElementStub.getChildNodes()).andStubReturn(notEmptyNodeListMock);
        replay(notEmptyShortElementStub);
        
        registryMock = EasyMock.createMock(ProcessorRegistry.class);
        expect((Class) registryMock.get(AIML_ELEMENT_STUB_NAME, AIML_NAMESPACE)).andStubReturn(PrintsAimlElementProcessor.class);
        expect((Class) registryMock.get(KNOWN_ELEMENT_STUB_NAME, null)).andStubReturn(PrintsKnownElementProcessor.class);
        expect((Class) registryMock.get(UNKNOWN_ELEMENT_STUB_NAME, null)).andStubThrow(new ClassNotFoundException());
        expect((Class) registryMock.get(UNKNOWN_AIML_ELEMENT_STUB_NAME, AIML_NAMESPACE)).andStubThrow(new ClassNotFoundException());
        expect(registryMock.getNamespace()).andStubReturn(AIML_NAMESPACE);
        replay(registryMock);
        
        PowerMock.mockStatic(XML.class);        
        expect(XML.createEmptyElement(emptyNotAimlElementStub)).andStubReturn(PRINTED_NOT_AIML_EMPTY_ELEMENT);
        expect(XML.createElementStart(notAimlElementStub)).andStubReturn(PRINTED_NOT_AIML_ELEMENT_START);
        expect(XML.createElementEnd(notAimlElementStub)).andStubReturn(PRINTED_NOT_AIML_ELEMENT_END);
        
        PowerMock.mockStatic(ClassManagment.class);
        expect(ClassManagment.getNewInstance(PrintsAimlElementProcessor.class)).andStubReturn(new PrintsAimlElementProcessor());
        expect(ClassManagment.getNewInstance(PrintsKnownElementProcessor.class)).andStubReturn(new PrintsKnownElementProcessor());

        PowerMock.replayAll();        
        
        parserMockForProcessShortenedTag = PowerMock.createPartialMock(AIMLTemplateParser.class, "evaluate", new Class<?>[] { Node.class }, conversationStub, matchResultStub, registryMock, false);
        expect(parserMockForProcessShortenedTag.evaluate(newElementStub)).andStubReturn(FULL_ELEMENT_PRINTED);
        replay(parserMockForProcessShortenedTag);
        
        parserMockForProcess = PowerMock.createPartialMock(AIMLTemplateParser.class, new String[] {"evaluate", "handleUnknownElement"}, conversationStub, matchResultStub, registryMock, false);
        expect(parserMockForProcess.evaluate(notEmptyNodeListMock)).andStubReturn(PRINTED_NOT_AIML_ELEMENT);
        PowerMock.expectPrivate(parserMockForProcess, "handleUnknownElement", eq(unknownAimlElementStub), isA(ClassNotFoundException.class)).andStubReturn(UNKNOW_ELEMENT_HANDLING_RESULT);
        PowerMock.expectPrivate(parserMockForProcess, "handleUnknownElement", eq(unknownElementStub), isA(ClassNotFoundException.class)).andStubReturn(UNKNOW_ELEMENT_HANDLING_RESULT);
        replay(parserMockForProcess);
        
        forwardParserMockForProcess = new AIMLTemplateParser(conversationStub, matchResultStub, registryMock, true);
    }

    /**
     * Uklidí stuby, mocky a ověří volání statických metod.
     */
    @After
    public void tearDown() {
        aimlElementStub = null;
        
        childElementStub = null;
        
        conversationStub = null;
        
        documentMock = null;
        
        emptyNodeListMock = null;
        
        emptyNotAimlElementStub = null;
        
        knownElementStub = null;
        
        matchResultStub = null;
        
        newElementStub = null;
        
        notAimlElementStub = null;
        
        notEmptyNodeListMock = null;
        
        notEmptyShortElementStub = null;
        
        parserMockForProcess = null;
        
        parserMockForProcessShortenedTag = null;
        
        registryMock = null;
        
        shortElementStub = null;
        
        unknownAimlElementStub = null;
        
        unknownElementStub = null;
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#AIMLTemplateParser(Conversation, MatchResult, ProcessorRegistry, boolean)}.
     */
    @Test
    public void testAIMLTemplateParser() {
        new AIMLTemplateParser(conversationStub, matchResultStub, registryMock, false);
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#process(org.w3c.dom.Element)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test
    public void testProcessElementWhenNullReturnsEmpty() throws ProcessorException {
        assertEquals(Processor.EMPTY_RESPONSE, parserMockForProcess.process((Element) null));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#process(org.w3c.dom.Element)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test
    public void testProcessElementWhenEmptyAndNotAimlReturnsPrintedEmptyElement() throws ProcessorException {
        assertEquals(PRINTED_NOT_AIML_EMPTY_ELEMENT, parserMockForProcess.process(emptyNotAimlElementStub));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#process(org.w3c.dom.Element)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test
    public void testProcessElementWhenNotEmptyAndNotAimlReturnsPrintedElement() throws ProcessorException {
        assertEquals(PRINTED_NOT_AIML_ELEMENT_START + PRINTED_NOT_AIML_ELEMENT + PRINTED_NOT_AIML_ELEMENT_END, parserMockForProcess.process(notAimlElementStub));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#process(org.w3c.dom.Element)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test
    public void testProcessElementWhenKnownAndInAimlNamespaceReturnsProcessedElement() throws ProcessorException {
        assertEquals(PRINTED_AIML_ELEMENT, parserMockForProcess.process(aimlElementStub));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#process(org.w3c.dom.Element)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test
    public void testProcessElementWhenUnknownAndInAimlNamespaceReturnsUnknownHandlingResult() throws ProcessorException {
        assertEquals(UNKNOW_ELEMENT_HANDLING_RESULT, parserMockForProcess.process(unknownAimlElementStub));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#process(org.w3c.dom.Element)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test
    public void testProcessElementWhenUnknownAndInAimlNamespaceAndForwardEnabledReturnsEmptyResponse() throws ProcessorException {
        assertEquals(Processor.EMPTY_RESPONSE, forwardParserMockForProcess.process(unknownAimlElementStub));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#process(org.w3c.dom.Element)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test
    public void testProcessElementWhenKnownAndWithoutNamespaceReturnsProcessedElement() throws ProcessorException {
        assertEquals(PRINTED_KNOWN_ELEMENT, parserMockForProcess.process(knownElementStub));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#process(org.w3c.dom.Element)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test
    public void testProcessElementWhenUnknownAndWithoutNamespaceReturnsUnknownHandlingResult() throws ProcessorException {
        assertEquals(UNKNOW_ELEMENT_HANDLING_RESULT, parserMockForProcess.process(unknownElementStub));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#processShortenedTag(Element, String, String)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test(expected = NullPointerException.class)
    public void testProcessShortenedTagWhenElementNull() throws ProcessorException {
        parserMockForProcessShortenedTag.processShortenedTag(null, NEW_ELEMENT_NAME, CHILD_NAME);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#processShortenedTag(Element, String, String)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test(expected = NullPointerException.class)
    public void testProcessShortenedTagWhenNewNameNull() throws ProcessorException {
        parserMockForProcessShortenedTag.processShortenedTag(shortElementStub, null, CHILD_NAME);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#processShortenedTag(Element, String, String)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test(expected = NullPointerException.class)
    public void testProcessShortenedTagWhenChildNameNull() throws ProcessorException {
        parserMockForProcessShortenedTag.processShortenedTag(null, NEW_ELEMENT_NAME, null);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#processShortenedTag(Element, String, String)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test(expected = IllegalArgumentException.class)
    public void testProcessShortenedTagWhenElementNotEmpty() throws ProcessorException {
        parserMockForProcessShortenedTag.processShortenedTag(notEmptyShortElementStub , NEW_ELEMENT_NAME, CHILD_NAME);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParser#processShortenedTag(Element, String, String)}.
     * @throws ProcessorException chyba při zpracování
     */
    @Test
    public void testProcessShortenedTagWhenAllValidReturnsProcessingResult() throws ProcessorException {
        assertEquals(FULL_ELEMENT_PRINTED, parserMockForProcessShortenedTag.processShortenedTag(shortElementStub, NEW_ELEMENT_NAME, CHILD_NAME));
    }
}
