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

import java.nio.file.Path;
import java.nio.file.Paths;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cz.cuni.mff.ms.brodecva.botnicek.library.loader.LoaderException;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje procesor učení ze souboru.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see LearnProcessor
 */
@Category(UnitTest.class)
public final class LearnProcessorTest {

    /**
     * Výchozí umístění souborů.
     */
    private static final Path DEFAULT_PATH = Paths.get("/root", "subroot");

    /**
     * Relativní cesta k souboru k naučení.
     */
    private static final String RELATIVE_LEARN_PATH = "foo/bar/file.aiml";

    /**
     * Očekávaný výstup z relativní cesty.
     */
    private static final Path EXPECTED_FROM_RELATIVE_PATH = Paths.get("/root",
            "subroot", "foo", "bar", "file.aiml");

    /**
     * Absolutní cesta k souboru k naučení.
     */
    private static final String ABSOLUTE_LEARN_PATH = "/foo/bar/file.aiml";

    /**
     * Očekávaný výstup z absolutní cesty.
     */
    private static final Path EXPECTED_FROM_ABSOLUTE_PATH = Paths.get("/foo",
            "bar", "file.aiml");

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
     * Mock bota.
     */
    private Bot botMock;

    /**
     * Nastaví objekty k testování.
     */
    @Before
    public void setUp() {
        final NodeList nodeListStub = EasyMock.createNiceMock(NodeList.class);
        replay(nodeListStub);

        elementMock = EasyMock.createMock(Element.class);
        expect(elementMock.getChildNodes()).andReturn(nodeListStub);
        replay(elementMock);

        botMock = EasyMock.createMock(Bot.class);
        expect(botMock.getFilesPath()).andReturn(DEFAULT_PATH);
        replay(botMock);

        conversationMock = EasyMock.createMock(Conversation.class);

        parserMock = EasyMock.createMock(TemplateParser.class);
        expect(parserMock.getBot()).andStubReturn(botMock);
        expect(parserMock.getConversation()).andStubReturn(conversationMock);

        processor = new LearnProcessor();
    }

    /**
     * Uklidí po testování.
     */
    @After
    public void tearDown() {
        processor = null;

        conversationMock = null;

        parserMock = null;

        botMock = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.LearnProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     * @throws LoaderException
     *             chyba při načítání
     */
    @Test
    public void testProcessLearnsFromRelativePath() throws ProcessorException,
            LoaderException {
        conversationMock.learn(EXPECTED_FROM_RELATIVE_PATH);
        replay(conversationMock);

        expect(parserMock.evaluate(isA(NodeList.class))).andReturn(
                RELATIVE_LEARN_PATH);
        replay(parserMock);

        final String result = processor.process(elementMock, parserMock);

        verify(parserMock);
        verify(conversationMock);
        verify(botMock);

        assertEquals(Processor.EMPTY_RESPONSE, result);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.LearnProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     * @throws LoaderException
     *             chyb při načítání
     */
    @Test
    public void testProcessLearnsFromAbsolutePath() throws ProcessorException,
            LoaderException {
        conversationMock.learn(EXPECTED_FROM_ABSOLUTE_PATH);
        replay(conversationMock);

        expect(parserMock.evaluate(isA(NodeList.class))).andReturn(
                ABSOLUTE_LEARN_PATH);
        replay(parserMock);

        final String result = processor.process(elementMock, parserMock);

        verify(parserMock);
        verify(conversationMock);
        verify(botMock);

        assertEquals(Processor.EMPTY_RESPONSE, result);
    }
}
