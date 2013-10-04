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
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje procesor pro nastavení hodnoty uživatelského predikátu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see SetProcessor
 */
@Category(UnitTest.class)
public final class SetProcessorTest {

    /**
     * Definovaný klíč hodnoty predikátu.
     */
    private static final String DEFINED_KEY = "definedKey";

    /**
     * Definovaná hodnota predikátu bota.
     */
    private static final String DEFINED_VALUE = "definedValue";

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
     * Mock prvku.
     */
    private Element elementMock = null;

    /**
     * Nastaví objekty k testování.
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Before
    public void setUp() throws ProcessorException {
        processor = new SetProcessor();

        final NodeList nodeListStub = EasyMock.createMock(NodeList.class);
        replay(nodeListStub);

        elementMock = EasyMock.createMock(Element.class);
        expect(elementMock.getAttribute(eq(AIML.ATT_NAME.getValue())))
                .andReturn(DEFINED_KEY);
        expect(elementMock.getChildNodes()).andReturn(nodeListStub);
        replay(elementMock);

        conversationMock = EasyMock.createMock(Conversation.class);
        expect(
                conversationMock.setPredicateValue(eq(DEFINED_KEY),
                        eq(DEFINED_VALUE))).andReturn(DEFINED_VALUE);
        replay(conversationMock);

        parserMock = EasyMock.createMock(TemplateParser.class);
        expect(parserMock.evaluate(isA(NodeList.class))).andReturn(
                DEFINED_VALUE);
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

        elementMock = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.SetProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessReturnsDefinedValue() throws ProcessorException {
        final String result = processor.process(elementMock, parserMock);

        verify(elementMock);
        verify(parserMock);
        verify(conversationMock);

        assertEquals(DEFINED_VALUE, result);
    }
}
