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
package cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje jednoduchý normalizér textu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see SimpleNormalizer
 */
@Category(UnitTest.class)
public final class SimpleNormalizerTest {

    /**
     * Prázdný text.
     */
    private static final String EMPTY_TEXT = "";

    /**
     * Testovaný normalizér.
     */
    private SimpleNormalizer normalizer = null;

    /**
     * Nastaví mock a testovaný objekt.
     */
    @Before
    public void setUp() {
        normalizer = new SimpleNormalizer();
    }

    /**
     * Uklidí mock a testovaný objekt.
     */
    @After
    public void tearDown() {
        normalizer = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#isNormal(char)}
     * .
     */
    @Test
    public void testIsNormalWhenSmallLetterReturnsFalse() {
        assertFalse(normalizer.isNormal('a'));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#isNormal(char)}
     * .
     */
    @Test
    public void testIsNormalWhenCaptialLetterNotInBasicAsciiReturnsTrue() {
        assertTrue(normalizer.isNormal('Š'));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#isNormal(char)}
     * .
     */
    @Test
    public void testIsNormalWhenSmallLetterNotInBasicAsciiReturnsTrue() {
        assertFalse(normalizer.isNormal('š'));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#isNormal(char)}
     * .
     */
    @Test
    public void testIsNormalWhenCapitalLetterReturnsTrue() {
        assertTrue(normalizer.isNormal('A'));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#isNormal(char)}
     * .
     */
    @Test
    public void testIsNormalWhenDigitReturnsTrue() {
        assertTrue(normalizer.isNormal('5'));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#convertToNormalChars(java.lang.String)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testConvertToNormalCharsWhenTextNull() {
        normalizer.convertToNormalChars(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#convertToNormalChars(java.lang.String)}
     * .
     */
    @Test
    public void testConvertToNormalCharsWhenTextEmpty() {
        assertEquals(EMPTY_TEXT, normalizer.convertToNormalChars(EMPTY_TEXT));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#convertToNormalChars(java.lang.String)}
     * .
     */
    @Test
    public void testConvertToNormalCharsWhenTextAlreadyNormalReturnsTheInput() {
        final String normalText = "THERE WAS DIGIT 8 WRITTEN IN THE BOOK";

        assertEquals(normalText, normalizer.convertToNormalChars(normalText));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#convertToNormalChars(java.lang.String)}
     * .
     */
    @Test
    public void testConvertToNormalCharsWhenTextAllLowercaseReturnsUppercase() {
        final String lowerCaseText = "text in lower case";
        final String upperCaseText = "TEXT IN LOWER CASE";

        assertEquals(upperCaseText,
                normalizer.convertToNormalChars(lowerCaseText));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#convertToNormalChars(java.lang.String)}
     * .
     */
    @Test
    public void testConvertToNormalCharsWhenTextInMixedCaseReturnsUppercase() {
        final String mixedCaseText = "Words In Mixed Case";
        final String upperCaseText = "WORDS IN MIXED CASE";

        assertEquals(upperCaseText,
                normalizer.convertToNormalChars(mixedCaseText));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#convertToNormalChars(java.lang.String)}
     * .
     */
    @Test
    public void testConvertToNormalCharsWhenAllDigitsReturnsInput() {
        final String digits = "091234294238943";

        assertEquals(digits, normalizer.convertToNormalChars(digits));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#convertToNormalChars(java.lang.String)}
     * .
     */
    @Test
    public void
            testConvertToNormalCharsWhenTextFromOtherCharactersReturnsEmpty() {
        final String other = "./*()!#@:\\";
        final String expectedReplacement = EMPTY_TEXT;

        assertEquals(expectedReplacement,
                normalizer.convertToNormalChars(other));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#convertToNormalChars(java.lang.String)}
     * .
     */
    @Test
    public
            void
            testConvertToNormalCharsWhenAllCapitalNationalCharactersReturnsInput() {
        final String allNationalCaps = "ÁÍÉŠČŘÝÁÍÝÍÝŮÚ";

        assertEquals(allNationalCaps,
                normalizer.convertToNormalChars(allNationalCaps));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#convertToNormalChars(java.lang.String)}
     * .
     */
    @Test
    public
            void
            testConvertToNormalCharsWhenAllSmallNationalCharactersReturnsUppercase() {
        final String allNationalSmall = "ěščřžýáíé";
        final String allNationalUppercase = "ĚŠČŘŽÝÁÍÉ";

        assertEquals(allNationalUppercase,
                normalizer.convertToNormalChars(allNationalSmall));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#deconvertFromNormalChars(java.lang.String)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testDeconvertFromNormalCharsWhenTextNull() {
        normalizer.deconvertFromNormalChars(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#deconvertFromNormalChars(java.lang.String)}
     * .
     */
    @Test
    public void testDeconvertFromNormalCharsWhenTextEmpty() {
        assertEquals(EMPTY_TEXT,
                normalizer.deconvertFromNormalChars(EMPTY_TEXT));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#deconvertFromNormalChars(java.lang.String)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeconvertFromNormalCharsWhenTextNotNormalized() {
        normalizer.deconvertFromNormalChars("Not normalized *()!@");
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer#deconvertFromNormalChars(java.lang.String)}
     * .
     */
    @Test
    public void testDeconvertFromNormalCharsReturnsAllLowerCase() {
        final String text = "TEXT NO 1 TO DECONVERT";
        final String lowerCaseText = "text no 1 to deconvert";

        assertEquals(lowerCaseText, normalizer.deconvertFromNormalChars(text));
    }
}
