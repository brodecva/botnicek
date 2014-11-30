/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.RuntimeSettings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.IntegrationTest;

/**
 * Testuje výchozí implementaci validátoru složeného vzoru jazyka AIML v
 * případě, že využívá výchozí validátor kódu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultMixedPatternChecker
 * @see DefaultCodeChecker
 */
@Category(IntegrationTest.class)
public class DefaultMixedPatternCheckerTest {

    private static final String AIML_PREFIX = "aiml";
    private static final String SCHEMA_PREFIX = "customschemans";

    private DefaultMixedPatternChecker defaultPrefixedTested = Intended
            .nullReference();
    private DefaultMixedPatternChecker customPrefixedTested = Intended
            .nullReference();

    /**
     * Vytvoří testované varianty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        final RuntimeSettings runtimeSettings = RuntimeSettings.getDefault();
        final CodeChecker unprefixedAimlCodeChecker =
                DefaultCodeChecker.create(
                        runtimeSettings.getBotConfiguration(), runtimeSettings
                                .getLanguageConfiguration(), Settings
                                .getDefault().getNamespacesToPrefixes());

        this.defaultPrefixedTested =
                DefaultMixedPatternChecker.create(unprefixedAimlCodeChecker);

        final Map<URI, String> customNamespacePrefixes =
                ImmutableMap.of(URI.create(AIML.NAMESPACE_URI.getValue()),
                        AIML_PREFIX,
                        URI.create(XML.SCHEMA_NAMESPACE_URI.getValue()),
                        SCHEMA_PREFIX);
        final CodeChecker prefixedAimlCodeChecker =
                DefaultCodeChecker.create(
                        runtimeSettings.getBotConfiguration(),
                        runtimeSettings.getLanguageConfiguration(),
                        customNamespacePrefixes);
        this.customPrefixedTested =
                DefaultMixedPatternChecker.create(prefixedAimlCodeChecker);
    }

    /**
     * Uklidí testované varianty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.defaultPrefixedTested = Intended.nullReference();
        this.customPrefixedTested = Intended.nullReference();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenAdditionalOtherThanNameAttributesExpectInvalid() {
        assertFalse(this.customPrefixedTested.check(
                "ONE <aiml:bot name=\"DFD\" other=\"blabla\"/> THREE *")
                .isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void
            testCheckWhenAttributeNameInAnotherNamespaceThanBotExpectInvalid() {
        assertFalse(this.customPrefixedTested.check(
                "ONE <aiml:bot somenamespace:name=\"DFD\"/> THREE").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void
            testCheckWhenBadTagSyntaxExpectColumnPositionInvalidSequenceStart() {
        final String input = "ONE <bot \"DFD\"> THREE";

        final CheckResult result = this.defaultPrefixedTested.check(input);

        final int tagStartIndex = CharMatcher.is('<').indexIn(input);

        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER,
                result.getErrorLineNumber());
        assertEquals(tagStartIndex + 1, result.getErrorColumnNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenBadTagSyntaxExpectInvalid() {
        assertFalse(this.defaultPrefixedTested.check("ONE <bot \"DFD\"> THREE")
                .isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenEmptyPatternExpectColumnPositionZero() {
        final CheckResult result = this.defaultPrefixedTested.check("");

        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER,
                result.getErrorLineNumber());
        assertEquals(0, result.getErrorColumnNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenEmptyPatternExpectInvalid() {
        assertFalse(this.defaultPrefixedTested.check("").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenLiteralMixedPatternOneBotTagExpectValid() {
        assertTrue(this.defaultPrefixedTested.check(
                "ONE <bot name=\"KAREL\"/> TWO").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public
            void
            testCheckWhenLiteralMixedPatternOneBotTagWithAdditionalSpacingExpectValid() {
        assertTrue(this.defaultPrefixedTested.check(
                "ONE <bot   name =  \"KAREL\" /> TWO").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenLiteralMixedPatternWithPrefixedBotTagExpectValid() {
        assertTrue(this.customPrefixedTested.check(
                "ONE <aiml:bot name=\"KAREL\"/> TWO").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenLiteralSimplePatternExpectValid() {
        assertTrue(this.defaultPrefixedTested.check("ONE TWO THREE").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenLongTagFormatExpectValid() {
        assertTrue(this.defaultPrefixedTested.check(
                "ONE <bot name=\"DFD\"></bot> THREE").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenMissingNameAttributeExpectInvalid() {
        assertFalse(this.defaultPrefixedTested.check(
                "ONE <bot notname=\"DFD\"/> THREE").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public
            void
            testCheckWhenMixedPatternWithPrefixedBotTagAndWildcardsExpectValid() {
        assertTrue(this.customPrefixedTested.check(
                "_ ONE <aiml:bot name=\"KAREL\"/> TWO * _").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public
            void
            testCheckWhenNonnormalWordsExpectColumnsPositionFirstNonnormalOccurence() {
        final String input = "One Two";
        final int firstNonnormalIndex =
                CharMatcher.JAVA_LOWER_CASE.indexIn(input);

        final CheckResult result = this.defaultPrefixedTested.check(input);

        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER,
                result.getErrorLineNumber());
        assertEquals(firstNonnormalIndex + 1, result.getErrorColumnNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenNonnormalWordsExpectInvalid() {
        assertFalse(this.defaultPrefixedTested.check("One Two").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenSimplePatternWithOnlyWildcardsExpectValid() {
        assertTrue(this.defaultPrefixedTested.check("_ * _ * *").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenSimplePatternWithStarWildcardExpectValid() {
        assertTrue(this.defaultPrefixedTested.check("ONE * THREE").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenSimplePatternWithUnderscoreWildcardExpectValid() {
        assertTrue(this.defaultPrefixedTested.check("ONE _ THREE").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public
            void
            testCheckWhenSuperfluousSpacesExpectColumnsPositionFirstSuperfluousSpaceOccurence() {
        final String input = "ONE   TWO  THREE";

        final int firstSpaceIndex = CharMatcher.is(' ').indexIn(input);
        final int firstSuperfluousSpaceIndex =
                CharMatcher.is(' ').indexIn(input, firstSpaceIndex + 1);

        final CheckResult result = this.defaultPrefixedTested.check(input);

        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER,
                result.getErrorLineNumber());
        assertEquals(firstSuperfluousSpaceIndex + 1,
                result.getErrorColumnNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenSuperfluousSpacesExpectInvalid() {
        assertFalse(this.defaultPrefixedTested.check("ONE   TWO  THREE")
                .isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenTwoBotAttributesExpectValid() {
        assertTrue(this.defaultPrefixedTested.check(
                "ONE <bot name=\"DFD\"/> THREE <bot name=\"DFD\"/> *")
                .isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.DefaultMixedPatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void
            testCheckWhenTwoBotAttributesOneWithDisallowedPrefixExpectInvalid() {
        assertFalse(this.customPrefixedTested
                .check("ONE <aiml:bot name=\"DFD\"/> THREE <alsoaiml:bot name=\"DFD\"/> *")
                .isValid());
    }
}
