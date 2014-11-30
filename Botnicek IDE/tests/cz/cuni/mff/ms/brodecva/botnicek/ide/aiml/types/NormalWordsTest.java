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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Checker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;

/**
 * Testuje pomocné metody pro práci s normálními slovy.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see NormalWords
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(NormalWords.class)
public class NormalWordsTest {

    private static final String CREATED_WORD_VALID_NAME = "CREATED";
    private static final String CREATED_WORD_FIXABLE_NAME = "cReATeD";
    private static final String CREATED_WORD_NOT_FIXABLE_NAME = ".;'#";
    private static final String INVALID_RESULT_MESSAGE = "Invalid";
    private static final String FIRST_WORD_TEXT = "FIRST";
    private static final String SECOND_WORD_TEXT = "SECOND";

    private NormalWord firstWordStub = Intended.nullReference();
    private NormalWord secondWordStub = Intended.nullReference();
    private Object firstValueDummy = Intended.nullReference();
    private Object secondValueDummy = Intended.nullReference();

    /**
     * Vytvoří testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.firstWordStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(this.firstWordStub.getText()).andStubReturn(
                FIRST_WORD_TEXT);
        EasyMock.replay(this.firstWordStub);

        this.secondWordStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(this.secondWordStub.getText()).andStubReturn(
                SECOND_WORD_TEXT);
        EasyMock.replay(this.secondWordStub);

        this.firstValueDummy = new Object();
        this.secondValueDummy = new Object();
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.firstWordStub = Intended.nullReference();
        this.secondWordStub = Intended.nullReference();

        this.firstValueDummy = Intended.nullReference();
        this.secondValueDummy = Intended.nullReference();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#from(String)}
     * .
     */
    @Test
    public void testFromWhenFixable() {
        final Normalizer normalizerStub =
                EasyMock.createStrictMock(Normalizer.class);
        EasyMock.expect(
                normalizerStub.convertToNormalChars(CREATED_WORD_FIXABLE_NAME))
                .andStubReturn(CREATED_WORD_VALID_NAME);
        EasyMock.replay(normalizerStub);
        Whitebox.setInternalState(NormalWords.class, "normalizer",
                normalizerStub);

        assertEquals(CREATED_WORD_VALID_NAME,
                NormalWords.from(CREATED_WORD_FIXABLE_NAME).getText());

        EasyMock.verify(normalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#from(String)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFromWhenNotFixable() {
        final Normalizer normalizerStub =
                EasyMock.createStrictMock(Normalizer.class);
        EasyMock.expect(
                normalizerStub
                        .convertToNormalChars(CREATED_WORD_NOT_FIXABLE_NAME))
                .andStubReturn("");
        EasyMock.replay(normalizerStub);
        Whitebox.setInternalState(NormalWords.class, "normalizer",
                normalizerStub);

        try {
            NormalWords.from(CREATED_WORD_NOT_FIXABLE_NAME);
        } finally {
            EasyMock.verify(normalizerStub);
        }
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#from(String)}
     * .
     */
    @Test
    public void testFromWhenValid() {
        final Normalizer normalizerStub =
                EasyMock.createStrictMock(Normalizer.class);
        EasyMock.expect(
                normalizerStub.convertToNormalChars(CREATED_WORD_VALID_NAME))
                .andStubReturn(CREATED_WORD_VALID_NAME);
        EasyMock.replay(normalizerStub);
        Whitebox.setInternalState(NormalWords.class, "normalizer",
                normalizerStub);

        assertEquals(CREATED_WORD_VALID_NAME,
                NormalWords.from(CREATED_WORD_VALID_NAME).getText());

        EasyMock.verify(normalizerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#join(java.util.List)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testJoinListOfNormalWordWhenEmpty() {
        NormalWords.join(ImmutableList.<NormalWord> of());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#join(java.util.List)}
     * .
     */
    @Test
    public void testJoinListOfNormalWordWhenNotEmpty() {
        final NormalWord resultDummy = EasyMock.createMock(NormalWord.class);
        EasyMock.replay(resultDummy);

        PowerMock.mockStaticPartialStrict(NormalWords.class, "of");
        EasyMock.expect(NormalWords.of("FIRSTSECOND")).andReturn(resultDummy);
        PowerMock.replay(NormalWords.class);

        NormalWords.join(ImmutableList.of(this.firstWordStub,
                this.secondWordStub));

        PowerMock.verify(NormalWords.class);
        EasyMock.verify(this.firstWordStub);
        EasyMock.verify(this.secondWordStub);
        EasyMock.verify(resultDummy);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#of(String)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOfWhenInvalid() {
        final CheckResult invalidCheckResultStub =
                EasyMock.createNiceMock(CheckResult.class);
        EasyMock.expect(invalidCheckResultStub.isValid()).andStubReturn(false);
        EasyMock.expect(invalidCheckResultStub.getMessage()).andStubReturn(
                INVALID_RESULT_MESSAGE);
        EasyMock.replay(invalidCheckResultStub);

        final Checker checkerStub =
                EasyMock.createStrictMock(Checker.class);
        EasyMock.expect(checkerStub.check(CREATED_WORD_NOT_FIXABLE_NAME))
                .andStubReturn(invalidCheckResultStub);
        EasyMock.replay(checkerStub);
        Whitebox.setInternalState(NormalWords.class, "checker", checkerStub);

        try {
            NormalWords.of(CREATED_WORD_NOT_FIXABLE_NAME);
        } finally {
            EasyMock.verify(invalidCheckResultStub);
            EasyMock.verify(checkerStub);
        }
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#of(String)}
     * .
     */
    @Test
    public void testOfWhenValid() {
        final CheckResult validCheckResultStub =
                EasyMock.createNiceMock(CheckResult.class);
        EasyMock.expect(validCheckResultStub.isValid()).andStubReturn(true);
        EasyMock.replay(validCheckResultStub);

        final Checker checkerStub =
                EasyMock.createStrictMock(Checker.class);
        EasyMock.expect(checkerStub.check(CREATED_WORD_VALID_NAME))
                .andStubReturn(validCheckResultStub);
        EasyMock.replay(checkerStub);
        Whitebox.setInternalState(NormalWords.class, "checker", checkerStub);

        assertEquals(CREATED_WORD_VALID_NAME,
                NormalWords.of(CREATED_WORD_VALID_NAME).getText());

        EasyMock.verify(validCheckResultStub);
        EasyMock.verify(checkerStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#toTyped(java.util.Map)}
     * .
     */
    @Test
    public void testToTyped() {
        PowerMock.mockStaticPartial(NormalWords.class, "of");
        EasyMock.expect(NormalWords.of("FIRST")).andStubReturn(
                this.firstWordStub);
        EasyMock.expect(NormalWords.of("SECOND")).andStubReturn(
                this.secondWordStub);
        PowerMock.replay(NormalWords.class);

        assertEquals(ImmutableMap.of(this.firstWordStub, this.firstValueDummy,
                this.secondWordStub, this.secondValueDummy),
                NormalWords.toTyped(ImmutableMap.of(FIRST_WORD_TEXT,
                        this.firstValueDummy, SECOND_WORD_TEXT,
                        this.secondValueDummy)));

        PowerMock.verify(NormalWords.class);
        EasyMock.verify(this.firstWordStub);
        EasyMock.verify(this.secondWordStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords#toUntyped(java.util.Map)}
     * .
     */
    @Test
    public void testToUntyped() {
        assertEquals(ImmutableMap.of(FIRST_WORD_TEXT, this.firstValueDummy,
                SECOND_WORD_TEXT, this.secondValueDummy),
                NormalWords.toUntyped(ImmutableMap.of(this.firstWordStub,
                        this.firstValueDummy, this.secondWordStub,
                        this.secondValueDummy)));

        EasyMock.verify(this.firstWordStub);
        EasyMock.verify(this.secondWordStub);
    }

}
