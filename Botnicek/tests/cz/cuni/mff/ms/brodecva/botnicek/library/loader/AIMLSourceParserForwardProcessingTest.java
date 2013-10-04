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

import static org.easymock.EasyMock.anyChar;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLPartMarker;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWildcard;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWord;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.Template;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.IntegrationTest;

/**
 * Testuje dopředné zpracování při načítání souboru.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AIMLSourceParser
 * @see AIMLHandler
 */
@Category(IntegrationTest.class)
public final class AIMLSourceParserForwardProcessingTest {
    
    /**
     * URI prostoru jmen AIML.
     */
    private static final String AIML_NAMESPACE_URI = AIML.NAMESPACE_URI.getValue();
    
    /**
     * Mock plněné struktury.
     */
    private MatchingStructure structureStub;

    /**
     * Stub robota.
     */
    private Bot botStub = null;

    /**
     * Testovaný parser, používající výchozí nastavení.
     */
    private AIMLSourceParser parser = null;

    /**
     * Zachycená vstupní cesta při plnění struktury.
     */
    private Capture<InputPath> pathCapture = null;

    /**
     * Zachycená šablona při plnění struktury.
     */
    private Capture<Template> templateCapture = null;

    /**
     * Stub normalizéru pro slova cesty.
     */
    private Normalizer normalizerStub = null;

    /**
     * Nastaví testovanou soustavu.
     */
    @Before
    public void setUp() {
        botStub = EasyMock.createMock(Bot.class);
        replay(botStub);
        
        pathCapture = new Capture<InputPath>();
        templateCapture = new Capture<Template>();
        
        structureStub = EasyMock.createMock(MatchingStructure.class);
        structureStub.add(EasyMock.capture(pathCapture),
                EasyMock.capture(templateCapture));
        structureStub.setForwardCompatible(true);
        expect(structureStub.isForwardCompatible()).andStubReturn(true);
        replay(structureStub);
        
        normalizerStub = EasyMock.createMock(Normalizer.class);
        expect(normalizerStub.isNormal(anyChar())).andStubReturn(true);
        replay(normalizerStub);

        parser = AIMLSourceParser.create();
    }

    /**
     * Uklidí testovanou soustavu.
     */
    @After
    public void tearDown() {
        botStub = null;

        parser = null;

        structureStub = null;

        pathCapture = null;
        
        normalizerStub = null;

        templateCapture = null;
    }

    /**
     * Testuje příklad ze specifikace.
     * 
     * @throws SourceParserException
     *             chyba při parsování
     */
    @Test
    public void testParse() throws SourceParserException {
        final String code = "<aiml:aiml version=\"1.1\"\r\n"

        + "                xmlns:aiml=\"http://alicebot.org/2001/AIML-1.0.1\">\r\n"

        + "    <aiml:topic name=\"DEMONSTRATION *\">\r\n"

        + "        <aiml:category>\r\n"

        + "            <aiml:pattern>* EXAMPLE</aiml:pattern>\r\n"

        + "            <aiml:template>\r\n"

        + "                This is just an example, <aiml:get name=\"username\"/>.\r\n"

        + "                <aiml:exciting-new-1.1-feature/>\r\n"

        + "            </aiml:template>\r\n"

        + "        </aiml:category>\r\n"

        + "    </aiml:topic>\r\n"

        + "</aiml:aiml>";

        final InputStream stringStream =
                new ByteArrayInputStream(
                        code.getBytes(Charset.defaultCharset()));

        parser.parse(stringStream, "dummyId", structureStub, botStub);

        verify(botStub);
        verify(structureStub);

        final Word[] expectedPathWords =
                new Word[] { AIMLWildcard.ASTERISK, new AIMLWord("EXAMPLE", normalizerStub),
                        AIMLPartMarker.THAT, AIMLWildcard.ASTERISK,
                        AIMLPartMarker.TOPIC, new AIMLWord("DEMONSTRATION", normalizerStub), AIMLWildcard.ASTERISK };
        assertEquals(new AIMLInputPath(Arrays.asList(expectedPathWords)),
                pathCapture.getValue());

        assertEquals(
                "<template xmlns=\"" + AIML_NAMESPACE_URI + "\"> This is just an example, <get xmlns=\"" + AIML_NAMESPACE_URI + "\" name=\"username\"/>. <exciting-new-1.1-feature xmlns=\"" + AIML_NAMESPACE_URI + "\"/> </template>",
                templateCapture.getValue().getValue());
    }

}
