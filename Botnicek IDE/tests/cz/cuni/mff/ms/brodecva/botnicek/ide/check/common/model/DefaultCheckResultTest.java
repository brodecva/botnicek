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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje výchozí implementaci výsledku kontroly.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultCheckResult
 */
@Category(UnitTest.class)
public class DefaultCheckResultTest {

    private Object sourceDummy = Intended.nullReference();
    private Object subjectDummy = Intended.nullReference();

    /**
     * Sestaví testovaný objekt.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.sourceDummy = new Object();
        this.subjectDummy = new Object();
    }

    /**
     * Uklidí testovaný objekt.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.sourceDummy = Intended.nullReference();
        this.subjectDummy = Intended.nullReference();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#hashCode()}
     * .
     */
    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(DefaultCheckResult.class).suppress(
                Warning.NULL_FIELDS);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testFailExpectErrorColumnNumberEqualsToGiven() {
        final int errorColumnNumber = 77;

        assertEquals(
                errorColumnNumber,
                DefaultCheckResult.fail(errorColumnNumber, "FailMessage",
                        this.sourceDummy, this.subjectDummy)
                        .getErrorColumnNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testFailExpectErrorLineNumberEqualsToNoRowDefaultNumber() {
        assertEquals(CheckResult.NO_ROWS_DEFAULT_ROW_NUMBER, DefaultCheckResult
                .fail(19, "FailMessage", this.sourceDummy, this.subjectDummy)
                .getErrorLineNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testFailExpectInvalid() {
        assertFalse(DefaultCheckResult.fail(0, "FailMessage", this.sourceDummy,
                this.subjectDummy).isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testFailExpectMessageEqualsToGiven() {
        final String message = "FailMessage";

        assertEquals(
                message,
                DefaultCheckResult.fail(19, message, this.sourceDummy,
                        this.subjectDummy).getMessage());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testFailExpectSourceEqualsToGiven() {
        assertEquals(
                this.sourceDummy,
                DefaultCheckResult.fail(19, "FailMessage", this.sourceDummy,
                        this.subjectDummy).getSource());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testFailExpectSubjectEqualsToGiven() {
        assertEquals(
                this.subjectDummy,
                DefaultCheckResult.fail(19, "FailMessage", this.sourceDummy,
                        this.subjectDummy).getSubject());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testFailMultilineExpectErrorColumnNumberEqualsToGiven() {
        final int errorColumnNumber = 77;

        assertEquals(
                errorColumnNumber,
                DefaultCheckResult.fail(19, errorColumnNumber, "FailMessage",
                        this.sourceDummy, this.subjectDummy)
                        .getErrorColumnNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testFailMultilineExpectErrorLineNumberEqualsToGiven() {
        final int errorLineNumber = 19;

        assertEquals(
                errorLineNumber,
                DefaultCheckResult.fail(errorLineNumber, 77, "FailMessage",
                        this.sourceDummy, this.subjectDummy)
                        .getErrorLineNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testFailMultilineExpectInvalid() {
        assertFalse(DefaultCheckResult.fail(0, 0, "FailMessage",
                this.sourceDummy, this.subjectDummy).isValid());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testFailMultilineExpectMessageEqualsToGiven() {
        final String message = "FailMessage";

        assertEquals(
                message,
                DefaultCheckResult.fail(19, 77, message, this.sourceDummy,
                        this.subjectDummy).getMessage());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testFailMultilineExpectSourceEqualsToGiven() {
        assertEquals(
                this.sourceDummy,
                DefaultCheckResult.fail(19, 77, "FailMessage",
                        this.sourceDummy, this.subjectDummy).getSource());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testFailMultilineExpectSubjectEqualsToGiven() {
        assertEquals(
                this.subjectDummy,
                DefaultCheckResult.fail(19, 77, "FailMessage",
                        this.sourceDummy, this.subjectDummy).getSubject());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#succeed(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testSucceedExpectErrorColumnsNumberEqualsToOkNumber() {
        assertEquals(CheckResult.OK_NUMBER,
                DefaultCheckResult.succeed(this.sourceDummy, this.subjectDummy)
                        .getErrorColumnNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#succeed(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testSucceedExpectErrorLineNumberEqualsToOkNumber() {
        assertEquals(CheckResult.OK_NUMBER,
                DefaultCheckResult.succeed(this.sourceDummy, this.subjectDummy)
                        .getErrorLineNumber());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testSucceedExpectSourceEqualsToGiven() {
        assertEquals(this.sourceDummy,
                DefaultCheckResult.succeed(this.sourceDummy, this.subjectDummy)
                        .getSource());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#fail(int, int, java.lang.String, java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testSucceedExpectSubjectEqualsToGiven() {
        assertEquals(this.subjectDummy,
                DefaultCheckResult.succeed(this.sourceDummy, this.subjectDummy)
                        .getSubject());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult#succeed(java.lang.Object, java.lang.Object)}
     * .
     */
    @Test
    public void testSucceedExpectValid() {
        assertTrue(DefaultCheckResult.succeed(this.sourceDummy,
                this.subjectDummy).isValid());
    }
}
