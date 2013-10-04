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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorException;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.Template;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.IntegrationTest;

/**
 * Testuje dopředné parsování šablony.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AIMLTemplateParser
 */
@Category(IntegrationTest.class)
public final class AIMLTemplateParserForwardProcessingTest {
    
    /**
     * URI prostoru jmen AIML.
     */
    private static final String AIML_NAMESPACE_URI = AIML.NAMESPACE_URI.getValue();
    
    /**
     * Jméno predikátu.
     */
    private static final String USERNAME_PREDICATE_NAME = "username";

    /**
     * Hodnota predikátu.
     */
    private static final String USERNAME_PREDICATE_VALUE = "John";


    /**
     * Testovaný parser.
     */
    private TemplateParser parser = null;

    /**
     * Stub výsledku.
     */
    private MatchResult resultStub = null;

    /**
     * Stub konverzace.
     */
    private Conversation conversationStub = null;

    /**
     * Stub robota.
     */
    private Bot botStub = null;

    /**
     * Stub šablony.
     */
    private Template templateStub = null;

    /**
     * Sestaví testovanou soustavu.
     * 
     * @throws IOException I/O chyba
     */
    @Before
    public void setUp() throws IOException {
        botStub = EasyMock.createMock(Bot.class);
        replay(botStub);

        conversationStub = EasyMock.createMock(Conversation.class);
        expect(conversationStub.getPredicateValue(USERNAME_PREDICATE_NAME)).andStubReturn(
                USERNAME_PREDICATE_VALUE);
        expect(conversationStub.getBot()).andStubReturn(botStub);
        replay(conversationStub);

        templateStub = EasyMock.createMock(Template.class);
        replay(templateStub);

        resultStub = EasyMock.createMock(MatchResult.class);
        expect(resultStub.getTemplate()).andStubReturn(templateStub);
        replay(resultStub);

        parser = new AIMLTemplateParserFactory().getParser(conversationStub, resultStub, true);
    }

    /**
     * Otestuje tiché zpracování neznámých AIML elementů, pokud je specifikována
     * verze vyšší než 1.0.
     * 
     * @throws ProcessorException
     *             chyba při zpracování
     */
    @Test
    public void testForwardParsing() throws ProcessorException {
        final String result = parser.process("<template xmlns=\"" + AIML_NAMESPACE_URI + "\">This is just an example, <get name=\"" + USERNAME_PREDICATE_NAME + "\"/>. <exciting-new-1.1-feature xmlns=\"" + AIML_NAMESPACE_URI + "\"/></template>");
        
        assertEquals("This is just an example, " + USERNAME_PREDICATE_VALUE + ". ", result);
    }

    /**
     * Uklidí testovanou soustavu.
     */
    @After
    public void tearDown() {
        parser = null;

        conversationStub = null;

        resultStub = null;
        
        botStub = null;
        
        templateStub = null;
    }
}
