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
package cz.cuni.mff.ms.brodecva.botnicek.library.processor;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje procesor pro odečítání hodnoty nastaveného uživatelského predikátu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see GetProcessor
 */
@Category(UnitTest.class)
public final class GetProcessorTest {

    /**
     * Definovaný klíč hodnoty predikátu.
     */
    private static final String DEFINED_KEY = "definedKey";

    /**
     * Definovaná hodnota predikátu bota.
     */
    private static final String DEFINED_VALUE = "definedValue";

    /**
     * Prázdný řetězec. Signalizuje nenalezení hodnoty.
     */
    private static final String EMPTY_STRING = "";

    /**
     * Nedefinovaný klíč hodnoty predikátu.
     */
    private static final String UNDEFINED_KEY = "undefinedKey";

    /**
     * Mock konverzace.
     */
    private Conversation conversationMock = null;

    /**
     * Mock parseru.
     */
    private TemplateParser parserMock = null;

    /**
     * Testovaný procesor.
     */
    private Processor processor = null;

    /**
     * Nastaví objekty k testování.
     */
    @Before
    public void setUp() {
        processor = new GetProcessor();

        conversationMock = EasyMock.createMock(Conversation.class);
        expect(conversationMock.getPredicateValue(eq(DEFINED_KEY)))
                .andStubReturn(DEFINED_VALUE);
        expect(conversationMock.getPredicateValue(eq(UNDEFINED_KEY)))
                .andStubReturn(EMPTY_STRING);
        replay(conversationMock);

        parserMock = EasyMock.createMock(TemplateParser.class);
        expect(parserMock.getConversation()).andStubReturn(conversationMock);
        replay(parserMock);
    }

    /**
     * Uklidí po testování.
     */
    @After
    public void tearDown() {
        processor = null;

        conversationMock = null;

        parserMock = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.GetProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessWhenBotPredicateDefined() throws ProcessorException {
        final Element elementMock = EasyMock.createMock(Element.class);
        expect(elementMock.getAttribute(eq(AIML.ATT_NAME.getValue())))
                .andStubReturn(DEFINED_KEY);
        replay(elementMock);

        final String result = processor.process(elementMock, parserMock);

        verify(elementMock);
        verify(parserMock);
        verify(conversationMock);

        assertEquals(DEFINED_VALUE, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.GetProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessWhenBotPredicateUndefined()
            throws ProcessorException {
        final Element elementMock = EasyMock.createMock(Element.class);
        expect(elementMock.getAttribute(eq(AIML.ATT_NAME.getValue())))
                .andStubReturn(UNDEFINED_KEY);
        replay(elementMock);

        final String result = processor.process(elementMock, parserMock);

        verify(elementMock);
        verify(parserMock);
        verify(conversationMock);

        assertEquals(EMPTY_STRING, result);
    }

}
