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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;


/**
 * Testuje nástroje pro práci s textem.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Text
 */
@Category(UnitTest.class)
public final class TextTest {

    /**
     * Jednoduchý přirozený vstup.
     */
    private static final String BASIC_NATURAL_INPUT =
            "One is less than two, four is more than three.";

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testReplaceInputNull() {
        Text.replace(null, new HashMap<Pattern, String>());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testReplaceReplacementsNull() {
        Text.replace("", null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testReplaceReplacementsNullKey() {
        final String input = BASIC_NATURAL_INPUT;

        final Map<Pattern, String> replacements = new HashMap<Pattern, String>();
        replacements.put(null, "five");

        Text.replace(input, replacements);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testReplaceReplacementsNullValue() {
        final String input = BASIC_NATURAL_INPUT;

        final Map<Pattern, String> replacements = new HashMap<Pattern, String>();
        replacements.put(Pattern.compile("two"), null);

        Text.replace(input, replacements);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test
    public void testReplaceReplacementsEmptyReturnsEqualInput() {
        final String input = "dummy";

        final String output =
                Text.replace(input, new HashMap<Pattern, String>());

        assertEquals(input, output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test
    public void testReplaceInputEmptyReturnsEqualInput() {
        final String input = "";

        final Map<Pattern, String> replacements = new HashMap<Pattern, String>();
        replacements.put(Pattern.compile("one"), "two");
        replacements.put(Pattern.compile("three"), "four");

        final String output = Text.replace(input, replacements);

        assertEquals(input, output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test
    public void testReplaceWhenOneReplacementKeyAndValue() {
        final String input = "one";

        final Map<Pattern, String> replacements = new HashMap<Pattern, String>();
        replacements.put(Pattern.compile("one"), "two");

        final String output = Text.replace(input, replacements);

        assertEquals("two", output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test
    public void testReplaceWhenTwoReplacementKeysAndValues() {
        final String input = "onethree";

        final Map<Pattern, String> replacements = new HashMap<Pattern, String>();
        replacements.put(Pattern.compile("one"), "two");
        replacements.put(Pattern.compile("three"), "four");

        final String output = Text.replace(input, replacements);

        assertEquals("twofour", output);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test
    public void testReplaceWhenTwoRegexes() {
        final String input = "one two three four five";

        final Map<Pattern, String> replacements = new HashMap<Pattern, String>();
        replacements.put(Pattern.compile("\\btwo three\\b"), "23");
        replacements.put(Pattern.compile("four five\\z"), "45");

        final String output = Text.replace(input, replacements);

        assertEquals("one 23 45", output);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test
    public void testReplaceWhenTwoRegexesWithGroups() {
        final String input = "one two three four five";

        final Map<Pattern, String> replacements = new HashMap<Pattern, String>();
        replacements.put(Pattern.compile("\\b(two) (three)\\b"), "23 is $1 and $2");
        replacements.put(Pattern.compile("(?:four) (?<five>five)\\z"), "45 $0 ${five}");

        final String output = Text.replace(input, replacements);

        assertEquals("one 23 is two and three 45 four five five", output);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test
    public void testReplaceWithEmptyReplacementKey() {
        final String input = "bat";

        final Map<Pattern, String> replacements = new HashMap<Pattern, String>();
        replacements.put(Pattern.compile(""), "super");

        final String output = Text.replace(input, replacements);

        assertEquals("superbsuperasupertsuper", output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test
    public void testReplaceWithEmptyReplacementValue() {
        final String input = "batman";

        final Map<Pattern, String> replacements = new HashMap<Pattern, String>();
        replacements.put(Pattern.compile("bat"), "");

        final String output = Text.replace(input, replacements);

        assertEquals("man", output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#replace(java.lang.String, java.util.Map)}
     * .
     */
    @Test
    public void testReplaceOnePassBehavior() {
        final String input = "batman";

        final Map<Pattern, String> replacements = new HashMap<Pattern, String>();
        replacements.put(Pattern.compile("superman"), "kryptonite");
        replacements.put(Pattern.compile("bat"), "super");

        final String output = Text.replace(input, replacements);

        assertEquals("superman", output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#substituteBraceWildcards(java.lang.String)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testSubstituteBraceWildcardsNullText() {
        Text.substituteBraceWildcards(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#substituteBraceWildcards(java.lang.String)}
     * .
     */
    @Test
    public void testSubstituteBraceWildcardsEmptyTextReturnsEqual() {
        final String emptyText = "";

        assertEquals(emptyText, Text.substituteBraceWildcards(emptyText));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#substituteBraceWildcards(java.lang.String)}
     * .
     */
    @Test
    public void testSubstituteBraceWildcardsTextWithoutBracesReturnsEqual() {
        final String textWithoutBraces = "Text without braces.";

        assertEquals(textWithoutBraces,
                Text.substituteBraceWildcards(textWithoutBraces));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#substituteBraceWildcards(java.lang.String)}
     * .
     */
    @Test
    public void testSubstituteBraceWildcardsTextWithEmptyBracesReturnsEqual() {
        final String textWithoutEmptyBraces = "Text with {} a {}";

        assertEquals(textWithoutEmptyBraces,
                Text.substituteBraceWildcards(textWithoutEmptyBraces));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#substituteBraceWildcards(java.lang.String)}
     * .
     */
    @Test
    public
            void
            testSubstituteBraceWildcardsTextWithoutOnlyDigitsInsideBracesReturnsEqual() {
        final String textWithoutEmptyBraces =
                "Text {6 5} with {dsasdfd} a {8897sa78}";

        assertEquals(textWithoutEmptyBraces,
                Text.substituteBraceWildcards(textWithoutEmptyBraces));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#substituteBraceWildcards(java.lang.String)}
     * .
     */
    @Test
    public
            void
            testSubstituteBraceWildcardsTextWithIllformatedDigitsInsideBracesReturnsEqual() {
        final String textWithoutEmptyBraces =
                "Text {00} with {001} a {012312}";

        assertEquals(textWithoutEmptyBraces,
                Text.substituteBraceWildcards(textWithoutEmptyBraces));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text#substituteBraceWildcards(java.lang.String)}
     * .
     */
    @Test
    public
            void
            testSubstituteBraceWildcardsTextWithNestedBracesReturnsSubstituted() {
        final String textWithoutEmptyBraces =
                "Text {{0}} with {0{55}} a {another {1} with {a {new {3}}}}";

        final String expected =
                "Text {%1$s} with {0%56$s} a {another %2$s with {a {new %4$s}}}";

        assertEquals(expected,
                Text.substituteBraceWildcards(textWithoutEmptyBraces));
    }
}
