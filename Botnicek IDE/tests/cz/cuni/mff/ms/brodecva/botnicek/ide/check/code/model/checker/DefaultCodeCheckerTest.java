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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.RuntimeSettings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.IntegrationTest;

/**
 * Testuje výchozí implementaci validátoru kódu šablony jazyka AIML, jež užívá
 * části interpretu, které mají na starosti načtení kódu. S výchozím nastavením.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultCodeChecker
 */
@Category(IntegrationTest.class)
public class DefaultCodeCheckerTest {

    private static final String AIML_PREFIX = "aiml";
    private static final String SCHEMA_PREFIX = "customschemans";

    private DefaultCodeChecker defaultPrefixedTested = Intended.nullReference();
    private DefaultCodeChecker customPrefixedTested = Intended.nullReference();

    /**
     * Sestaví testovaný objekt.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        final RuntimeSettings runtimeSettings = RuntimeSettings.getDefault();
        this.defaultPrefixedTested =
                DefaultCodeChecker.create(
                        runtimeSettings.getBotConfiguration(), runtimeSettings
                                .getLanguageConfiguration(), Settings
                                .getDefault().getNamespacesToPrefixes());

        final Map<URI, String> customNamespacePrefixes =
                ImmutableMap.of(URI.create(AIML.NAMESPACE_URI.getValue()),
                        AIML_PREFIX,
                        URI.create(XML.SCHEMA_NAMESPACE_URI.getValue()),
                        SCHEMA_PREFIX);
        this.customPrefixedTested =
                DefaultCodeChecker.create(
                        runtimeSettings.getBotConfiguration(),
                        runtimeSettings.getLanguageConfiguration(),
                        customNamespacePrefixes);
    }

    /**
     * Uklidí testovaný objekt.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.defaultPrefixedTested = Intended.nullReference();
        this.customPrefixedTested = Intended.nullReference();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker#check(java.lang.String)}
     * .
     */
    @Test
    public
            void
            testCheckWhenCustomPrefixCorrectLocalNameButDifferentPrefixExpectInvalid() {
        assertFalse(this.customPrefixedTested.check(
                "<otherns:get name=\"BLA\"/>").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenCustomPrefixedEmptyInputExpectValid() {
        assertTrue(this.customPrefixedTested.check("").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenCustomPrefixedOpenTagExpectInvalid() {
        assertFalse(this.customPrefixedTested.check("<").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenCustomPrefixedUnknownElementExpectInvalid() {
        assertFalse(this.customPrefixedTested.check("<aiml:unknown/>")
                .isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenCustomPrefixedValidAimlCodeExpectValid() {
        assertTrue(this.customPrefixedTested
                .check("<aiml:random><aiml:li><aiml:get name=\"TOPIC\"/> is the topic</aiml:li><aiml:li><aiml:javascript><aiml:get name=\"USER\"></aiml:get></aiml:javascript></aiml:li></aiml:random>")
                .isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenEmptyInputExpectValid() {
        assertTrue(this.defaultPrefixedTested.check("").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenOpenTagExpectColumnPositionTwo() {
        final CheckResult result = this.defaultPrefixedTested.check("<");

        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER,
                result.getErrorLineNumber());
        assertEquals(2, result.getErrorColumnNumber());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenOpenTagExpectInvalid() {
        assertFalse(this.defaultPrefixedTested.check("<").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker#check(java.lang.String)}
     * .
     */
    @Test
    public
            void
            testCheckWhenUnknownElementExpectColumnPositionTagCodeLengthPlusOne() {
        final String tagCode = "<unknown/>";

        final CheckResult result = this.defaultPrefixedTested.check(tagCode);

        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER,
                result.getErrorLineNumber());
        assertEquals(tagCode.length() + 1, result.getErrorColumnNumber());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenUnknownElementExpectInvalid() {
        assertFalse(this.defaultPrefixedTested.check("<unknown/>").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenValidAimlCodeExpectValid() {
        assertTrue(this.defaultPrefixedTested
                .check("<random><li><get name=\"TOPIC\"/> is the topic</li><li><javascript><get name=\"USER\"></get></javascript></li></random>")
                .isValid());
    }
}
