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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLHandlerTest;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParserTest;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XMLTest;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.RandomProcessorTest;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversationTest;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNodeTest;

/**
 * Provede všechny jednotkové testy spouštěné pomocí
 * {@link org.powermock.modules.junit4.PowerMockRunner}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({ AIMLHandlerTest.class, AIMLTemplateParserTest.class,
        WordNodeTest.class, XMLTest.class, RandomProcessorTest.class, AIMLConversationTest.class })
public final class AllUnitPowerMockTests {
}
