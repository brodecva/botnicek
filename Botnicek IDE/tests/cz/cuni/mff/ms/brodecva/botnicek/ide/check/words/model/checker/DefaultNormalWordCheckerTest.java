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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.CharMatcher;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * Testuje výchozí implementaci validátoru normálních slov jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultNormalWordChecker
 * @see NormalWord
 */
public class DefaultNormalWordCheckerTest {

    private DefaultNormalWordChecker permissiveTested = Intended
            .nullReference();
    private NamingAuthority permissiveNamingAuthorityStub = Intended
            .nullReference();
    private DefaultNormalWordChecker dismissiveTested = Intended
            .nullReference();
    private NamingAuthority dismissiveNamingAuthorityStub = Intended
            .nullReference();

    /**
     * Vytvoří testované varianty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.permissiveNamingAuthorityStub =
                EasyMock.createStrictMock(NamingAuthority.class);
        EasyMock.expect(
                this.permissiveNamingAuthorityStub.isUsable(EasyMock
                        .notNull(String.class))).andStubReturn(true);
        EasyMock.replay(this.permissiveNamingAuthorityStub);

        this.permissiveTested =
                DefaultNormalWordChecker
                        .create(this.permissiveNamingAuthorityStub);

        this.dismissiveNamingAuthorityStub =
                EasyMock.createStrictMock(NamingAuthority.class);
        EasyMock.expect(
                this.dismissiveNamingAuthorityStub.isUsable(EasyMock
                        .notNull(String.class))).andStubReturn(false);
        EasyMock.replay(this.dismissiveNamingAuthorityStub);

        this.dismissiveTested =
                DefaultNormalWordChecker
                        .create(this.dismissiveNamingAuthorityStub);
    }

    /**
     * Uklidí testované varianty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.permissiveTested = Intended.nullReference();
        this.permissiveNamingAuthorityStub = Intended.nullReference();
        this.dismissiveTested = Intended.nullReference();
        this.dismissiveNamingAuthorityStub = Intended.nullReference();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenCaselessLettersOnlyExpectValid() {
        assertTrue(this.permissiveTested.check("אبض").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenContainsLowercaseLetterExpectInvalid() {
        assertFalse(this.permissiveTested.check("אبضAŠs890").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void
            testCheckWhenContainsNonnormalExpectColumnNumberFirstOccurence() {
        final String input = "אبضAŠ89-0";
        final int firstNonnormalIndex = CharMatcher.is('-').indexIn(input);

        final CheckResult result = this.permissiveTested.check(input);

        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER,
                result.getErrorLineNumber());
        assertEquals(firstNonnormalIndex + 1, result.getErrorColumnNumber());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenContainsNonnormalExpectInvalid() {
        assertFalse(this.permissiveTested.check("אبضAŠ89-0").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenContainsTitleCaseLetterExpectInvalid() {
        assertFalse(this.permissiveTested.check("ǅᾊῌ").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenContainsWhitespaceExpectInvalid() {
        assertFalse(this.permissiveTested.check("אبضAŠ 890").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenDigitsOnlyExpectValid() {
        assertTrue(this.permissiveTested.check("1234567890").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenEmptyExpectColumnNumberZero() {
        final CheckResult result = this.permissiveTested.check("");

        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER,
                result.getErrorLineNumber());
        assertEquals(0, result.getErrorColumnNumber());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenEmptyExpectInvalid() {
        assertFalse(this.permissiveTested.check("").isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenNotUsableExpectColumnNumberZero() {
        final CheckResult result =
                this.dismissiveTested.check("ALREADYCONTAINEDFOREXAMPLE");

        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER,
                result.getErrorLineNumber());
        assertEquals(0, result.getErrorColumnNumber());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenNotUsableExpectInvalid() {
        assertFalse(this.dismissiveTested.check("ALREADYCONTAINEDFOREXAMPLE")
                .isValid());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.model.checker.DefaultNormalWordChecker#check(java.lang.String)}
     * .
     */
    @Test
    public void testCheckWhenUppercaseLettersOnlyExpectValid() {
        assertTrue(this.permissiveTested.check("JSADJČÁŠKLK").isValid());
    }
}
