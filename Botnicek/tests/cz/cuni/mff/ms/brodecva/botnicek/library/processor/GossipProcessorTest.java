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

import java.io.FileWriter;
import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje procesor pro ukládání promluv.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see GossipProcessor
 */
@Category(UnitTest.class)
public final class GossipProcessorTest {

    /**
     * Zapisovaná promluva.
     */
    private static final String GOSSIP = "Gossip written down.";

    /**
     * Oddělovač řádek.
     */
    public static final String LINE_SEPARATOR = System.getProperty(
            "line.separator", "\n");

    /**
     * Mock bota.
     */
    private Bot botStub = null;

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
     * Mock zapisovače.
     */
    private FileWriter writerMock;

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
        final NodeList nodeListStub = EasyMock.createNiceMock(NodeList.class);
        replay(nodeListStub);

        elementMock = EasyMock.createMock(Element.class);
        expect(elementMock.getChildNodes()).andReturn(nodeListStub);
        replay(elementMock);

        botStub = EasyMock.createNiceMock(Bot.class);
        replay(botStub);

        parserMock = EasyMock.createMock(TemplateParser.class);
        expect(parserMock.evaluate(isA(NodeList.class))).andReturn(GOSSIP);
        expect(parserMock.getBot()).andReturn(botStub);
        expect(parserMock.getConversation()).andReturn(null);
        replay(parserMock);

        writerMock = EasyMock.createStrictMock(FileWriter.class);
        expect(writerMock.append(GOSSIP + LINE_SEPARATOR))
                .andReturn(writerMock);
        writerMock.flush();
        writerMock.close();
        replay(writerMock);

        processor = new GossipProcessor(writerMock);
    }

    /**
     * Uklidí po testování.
     */
    @After
    public void tearDown() {
        processor = null;

        botStub = null;

        parserMock = null;

        writerMock = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.processor.GossipProcessor#process(org.w3c.dom.Element, cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)}
     * .
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testProcessWritesGossip() throws ProcessorException {
        final String result = processor.process(elementMock, parserMock);

        verify(elementMock);
        verify(parserMock);
        verify(botStub);
        verify(writerMock);

        assertEquals(Processor.EMPTY_RESPONSE, result);
    }

}
