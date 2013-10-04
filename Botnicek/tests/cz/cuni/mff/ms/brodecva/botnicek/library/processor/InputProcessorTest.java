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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.IndexFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje procesor získávání minulého uživatelského vstupu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see InputProcessor
 */
@Category(UnitTest.class)
public final class InputProcessorTest {

    /**
     * Očekávaný minulý vstup.
     */
    private static final String EXPECTED_INPUT = "expected input";

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
     * Stub prvku.
     */
    private Element elementStub = null;

    /**
     * Mock továrny na indexy.
     */
    private IndexFactory factoryMock;

    /**
     * Stub 2D indexu.
     */
    private TwoDimensionalIndex twoDimensionalIndexStub;

    /**
     * Nastaví objekty k testování.
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     * @throws IOException
     *             chyba při zápisu
     */
    @Before
    public void setUp() throws ProcessorException, IOException {
        elementStub = EasyMock.createNiceMock(Element.class);
        replay(elementStub);

        conversationMock = EasyMock.createMock(Conversation.class);
        expect(conversationMock.getUserInput(isA(TwoDimensionalIndex.class)))
                .andReturn(EXPECTED_INPUT);
        replay(conversationMock);

        parserMock = EasyMock.createMock(TemplateParser.class);
        expect(parserMock.getConversation()).andReturn(conversationMock);
        replay(parserMock);

        twoDimensionalIndexStub =
                EasyMock.createNiceMock(TwoDimensionalIndex.class);
        replay(twoDimensionalIndexStub);

        factoryMock = EasyMock.createMock(IndexFactory.class);
        expect(factoryMock.create2DIndex(isA(Element.class))).andReturn(
                twoDimensionalIndexStub);
        replay(factoryMock);

        processor = new InputProcessor(factoryMock);
    }

    /**
     * Uklidí po testování.
     */
    @After
    public void tearDown() {
        processor = null;

        conversationMock = null;

        parserMock = null;

        factoryMock = null;

        twoDimensionalIndexStub = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.InputProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessReturnsInput() throws ProcessorException {
        final String result = processor.process(elementStub, parserMock);

        verify(parserMock);
        verify(conversationMock);
        verify(factoryMock);

        assertEquals(EXPECTED_INPUT, result);
    }

}
