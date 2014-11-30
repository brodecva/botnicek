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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.base.CharMatcher;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.IntegrationTest;

/**
 * Testuje výchozí implementaci validátoru prostého vzoru jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultSimplePatternChecker
 */
@Category(IntegrationTest.class)
public class DefaultSimplePatternCheckerTest {

    private DefaultSimplePatternChecker tested = Intended.nullReference();

    /**
     * Vytvoří testovanou instanci.
     * 
     * @throws java.lang.Exception
     *             pokud dojde vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.tested = DefaultSimplePatternChecker.create();
    }

    /**
     * Uklidí testovanou instanci.
     * 
     * @throws java.lang.Exception
     *             pokud dojde vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.tested = Intended.nullReference();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenEmptyPatternExpectColumnPositionZero() {
        final CheckResult result = this.tested.check("");

        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER,
                result.getErrorLineNumber());
        assertEquals(0, result.getErrorColumnNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenEmptyPatternExpectInvalid() {
        assertFalse(this.tested.check("").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenLiteralSimplePatternExpectValid() {
        assertTrue(this.tested.check("ONE TWO THREE").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public
            void
            testCheckWhenNonnormalWordsExpectColumnsPositionFirstNonnormalOccurence() {
        final String input = "One Two";
        final int firstNonnormalIndex =
                CharMatcher.JAVA_LOWER_CASE.indexIn(input);

        final CheckResult result = this.tested.check(input);

        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER,
                result.getErrorLineNumber());
        assertEquals(firstNonnormalIndex + 1, result.getErrorColumnNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenNonnormalWordsExpectInvalid() {
        assertFalse(this.tested.check("One Two").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenSimplePatternWithOnlyWildcardsExpectValid() {
        assertTrue(this.tested.check("_ * _ * *").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenSimplePatternWithStarWildcardExpectValid() {
        assertTrue(this.tested.check("ONE * THREE").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenSimplePatternWithUnderscoreWildcardExpectValid() {
        assertTrue(this.tested.check("ONE _ THREE").isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker#check(java.lang.String)}
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

        final CheckResult result = this.tested.check(input);

        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER,
                result.getErrorLineNumber());
        assertEquals(firstSuperfluousSpaceIndex + 1,
                result.getErrorColumnNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenSuperfluousSpacesExpectInvalid() {
        assertFalse(this.tested.check("ONE   TWO  THREE").isValid());
    }
}
