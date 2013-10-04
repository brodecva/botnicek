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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.IntegrationTest;

/**
 * Testuje porovnávač vzoru, který pracuje pomocí vytváření miniaturní porovnávací struktury.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AIMLStructuralMatcher
 */
@Category(IntegrationTest.class)
public final class AIMLStructuralMatcherTest {
    
    /**
     * Porovnávač.
     */
    private AIMLStructuralMatcher matcher = null;
    
    
    /**
     * Inicializuje testovaný objekt.
     */
    @Before
    public void setUp() {
        matcher = new AIMLStructuralMatcher();
    }

    /**
     * Uklidí testovaný objekt.
     */
    @After
    public void tearDown() {
        matcher = null;
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithoutWildcardsReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT", "SIMPLE TEXT"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenNotMatchingPatternWithoutWildcardsReturnsFalse() {
        assertFalse(matcher.matches("ANOTHER SIMPLE TEXT", "SIMPLE TEXT"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithAsteriskAtEndOneCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT", "SIMPLE *"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithAsteriskAtEndMoreCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT ABOUT SOMETHING", "SIMPLE *"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithAsteriskAtStartOneCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT", "* TEXT"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithAsteriskAtStartMoreCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT ABOUT SOMETHING", "* SOMETHING"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithAsteriskOnlyOneCapturedReturnsTrue() {
        assertTrue(matcher.matches("SOMETHING", "*"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithAsteriskOnlyMoreCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT ABOUT SOMETHING", "*"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithAsteriskInsideOneCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE SAD TEXT", "SIMPLE * TEXT"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithAsteriskInsideMoreCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT ABOUT SOMETHING", "SIMPLE * SOMETHING"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenNotMatchingPatternWithAsteriskAtEndReturnsFalse() {
        assertFalse(matcher.matches("REALLY LONG TEXT", "SIMPLE *"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenNotMatchingPatternWithAsteriskAtStartReturnsFalse() {
        assertFalse(matcher.matches("SIMPLE ENOUGH TASK", "* TEXT"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenNotMatchingPatternWithAsteriskInsideReturnsFalse() {
        assertFalse(matcher.matches("LONG SAD COLORFUL TEXT", "SIMPLE * TASK"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithMoreAsterisksSeparatedReturnsTrue() {
        assertTrue(matcher.matches("LONG SAD COLORFUL SIMPLE TEXT AND TASK", "LONG * SIMPLE * TASK"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenNotMatchingPatternWithMoreAsterisksSeparatedReturnsFalse() {
        assertFalse(matcher.matches("LONG SAD COLORFUL NICE TEXT AND TASK", "LONG * SIMPLE * TASK"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithMoreAsterisksNextToEachOtherReturnsTrue() {
        assertTrue(matcher.matches("LONG SAD COLORFUL SIMPLE TEXT AND TASK", "LONG * * TASK"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenNotMatchingPatternWithMoreAsterisksNextToEachOtherReturnsFalse() {
        assertFalse(matcher.matches("SAD COLORFUL TEXT", "* * TASK"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithUnderscoreAtEndOneCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT", "SIMPLE _"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithUnderscoreAtEndMoreCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT ABOUT SOMETHING", "SIMPLE _"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithUnderscoreAtStartOneCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT", "_ TEXT"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithUnderscoreAtStartMoreCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT ABOUT SOMETHING", "_ SOMETHING"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithUnderscoreOnlyOneCapturedReturnsTrue() {
        assertTrue(matcher.matches("SOMETHING", "_"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithUnderscoreOnlyMoreCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT ABOUT SOMETHING", "_"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithUnderscoreInsideOneCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE SAD TEXT", "SIMPLE _ TEXT"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithUnderscoreInsideMoreCapturedReturnsTrue() {
        assertTrue(matcher.matches("SIMPLE TEXT ABOUT SOMETHING", "SIMPLE _ SOMETHING"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenNotMatchingPatternWithUnderscoreAtEndReturnsFalse() {
        assertFalse(matcher.matches("REALLY LONG TEXT", "SIMPLE _"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenNotMatchingPatternWithUnderscoreAtStartReturnsFalse() {
        assertFalse(matcher.matches("SIMPLE ENOUGH TASK", "_ TEXT"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenNotMatchingPatternWithUnderscoreInsideReturnsFalse() {
        assertFalse(matcher.matches("LONG SAD COLORFUL TEXT", "SIMPLE _ TASK"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithMoreUnderscoresSeparatedReturnsTrue() {
        assertTrue(matcher.matches("LONG SAD COLORFUL SIMPLE TEXT AND TASK", "LONG _ SIMPLE _ TASK"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenNotMatchingPatternWithMoreUnderscoresSeparatedReturnsFalse() {
        assertFalse(matcher.matches("LONG SAD COLORFUL NICE TEXT AND TASK", "LONG _ SIMPLE _ TASK"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenMatchingPatternWithMoreUnderscoresNextToEachOtherReturnsTrue() {
        assertTrue(matcher.matches("LONG SAD COLORFUL SIMPLE TEXT AND TASK", "LONG _ _ TASK"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenNotMatchingPatternWithMoreUnderscoresNextToEachOtherReturnsFalse() {
        assertFalse(matcher.matches("SAD COLORFUL TEXT", "_ _ TASK"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenEmptyWithStarReturnsTrue() {
        assertTrue(matcher.matches("", "*"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.AIMLStructuralMatcher#matches(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testMatchesWhenOneMatchingWordWithStarAndTheWordReturnsFalse() {
        assertFalse(matcher.matches("TEXT", "* TEXT"));
    }
}
