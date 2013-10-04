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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.api.AIMLBotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.AIMLConversationConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.AIMLLanguageConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.AIMLSession;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.ConfigurationException;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.ConversationConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.Session;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.SessionException;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.ConversationException;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property;

/**
 * Provede sérii testů procesorů a souvisejících tříd za pomoci samotného stroje
 * pro vyhodnocování AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see cz.cuni.mff.ms.brodecva.botnicek.library.processor.Processor
 * @see Conversation
 * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot
 * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language
 */
@Category(IntegrationTest.class)
public final class ProcessorTest {

    /**
     * Testovací relace.
     */
    private Session session = null;

    /**
     * Vytvoří testovací relaci.
     * 
     * @throws IOException
     *             chyba v načítání konfiguračních souborů
     * @throws ConfigurationException
     *             chyba ve formátu konfigurace
     * @throws SessionException
     *             chyba při vytváření relace
     */
    @Before
    public void setUp() throws ConfigurationException, IOException,
            SessionException {
        final Class<?> currentClass = getClass();

        final BotConfiguration botConfig =
                AIMLBotConfiguration.create(Property.load(currentClass,
                        "bot/bot.properties"), Property.load(currentClass,
                        "bot/botpredicates.properties"));

        final LanguageConfiguration languageConfiguration =
                AIMLLanguageConfiguration.create(Property.load(currentClass,
                        "bot/language.properties"), Property.load(currentClass,
                        "bot/gender.properties"), Property.load(currentClass,
                        "bot/person.properties"), Property.load(currentClass,
                        "bot/person2.properties"), Property.load(currentClass,
                        "bot/abbreviations.properties"), Property.load(
                        currentClass, "bot/spelling.properties"), Property
                        .load(currentClass, "bot/emoticons.properties"),
                        Property.load(currentClass,
                                "bot/punctuation.properties"));

        final ConversationConfiguration conversationConfig =
                AIMLConversationConfiguration.create(Property.load(
                        currentClass, "bot/defaultpredicates.properties"),
                        Property.load(currentClass,
                                "bot/setbehavior.properties"));

        session =
                AIMLSession.start(botConfig, languageConfiguration,
                        conversationConfig);
    }

    /**
     * Uklidí testovací relaci.
     */
    @After
    public void tearDown() {
        session = null;
    }

    /**
     * Provede testy.
     * 
     * @throws IOException
     *             chyba při načítání
     * @throws ConversationException
     *             chyba při vyhodnocování vstupu
     */
    @Test
    public void test() throws IOException, ConversationException {
        final Conversation conversation = session.getConversation();
        conversation.talk("test");
        final String response = conversation.listen();
        System.out.println("ProcessorTest result:" + response);
        assertTrue(isReportSuccesful(response));
    }

    /**
     * Vrátí true, pokud je výsledek tvořen skupinami číslo-ok oddělenými bílými
     * znaky.
     * 
     * @param aimlTestResult
     *            odpověď testovacího robota
     * @return true, pokud výsledek odpovídá formě úspěšného testu
     */
    private static boolean isReportSuccesful(final String aimlTestResult) {
        final String cutOutParentheses =
                aimlTestResult.replaceAll("\\([^\\(\\)]*\\)", "");

        final boolean isInNumberOkForm =
                cutOutParentheses.matches("(?:\\s*[1-9]*[0-9]-+ok\\s*)*");

        return isInNumberOkForm;
    }

}
