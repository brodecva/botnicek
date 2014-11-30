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
package cz.cuni.mff.ms.brodecva.botnicek.library.loader;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testuje strategie pro výchozí nakládání znaku dle AIML. 
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultSpaceStrategy
 */
public final class DefautSpaceStrategyTest {
    
    /**
     * Testovaná strategie.
     */
    private DefaultSpaceStrategy strategy = null;

    /**
     * Nastaví testovaný objekt.
     */
    @Before
    public void setUp() {
        strategy = DefaultSpaceStrategy.create();
    }

    /**
     * Uklidí testovaný objekt.
     */
    @After
    public void tearDown() {
        strategy = null;
    }

    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.DefaultSpaceStrategy#transformOpenTag(java.lang.StringBuilder)}.
     */
    @Test
    public void testTransformOpenTagWhenNoSpacesAtEndRemainsUnchanged() {
        final String expected = "something";
        
        final StringBuilder chars = new StringBuilder(expected);
        strategy.transformOpenTag(chars);
        
        assertEquals(expected, chars.toString());
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.DefaultSpaceStrategy#transformOpenTag(java.lang.StringBuilder)}.
     */
    @Test
    public void testTransformOpenTagRemovesAllTrailingSpacesButOne() {
        final StringBuilder chars = new StringBuilder("something    ");
        strategy.transformOpenTag(chars);
        
        assertEquals("something ", chars.toString());
    }

    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.DefaultSpaceStrategy#transformCloseTag(java.lang.StringBuilder)}.
     */
    @Test
    public void testTransformCloseTagWhenNoSpacesAtEndRemainsUnchanged() {
        final String expected = "something";
        
        final StringBuilder chars = new StringBuilder(expected);
        strategy.transformCloseTag(chars);
        
        assertEquals(expected, chars.toString());
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.DefaultSpaceStrategy#transformCloseTag(java.lang.StringBuilder)}.
     */
    @Test
    public void testTransformCloseTagRemovesAllTrailingSpacesButOne() {
        final StringBuilder chars = new StringBuilder("something    ");
        strategy.transformCloseTag(chars);
        
        assertEquals("something ", chars.toString());
    }

    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.DefaultSpaceStrategy#transformChars(java.lang.StringBuilder, char[], int, int)}.
     */
    @Test
    public void testTransformCharsWhenNoTagAtEndAddsAll() {
        final String chars = "   else";
        
        final StringBuilder result = new StringBuilder("something");
        strategy.transformChars(result, chars.toCharArray(), 0, chars.length());
        
        assertEquals("something   else", result.toString());
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.DefaultSpaceStrategy#transformChars(java.lang.StringBuilder, char[], int, int)}.
     */
    @Test
    public void testTransformCharsWhenNoSpacesAddsAll() {
        final String chars = "else";
        
        final StringBuilder result = new StringBuilder("<something>");
        strategy.transformChars(result, chars.toCharArray(), 0, chars.length());
        
        assertEquals("<something>else", result.toString());
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.DefaultSpaceStrategy#transformChars(java.lang.StringBuilder, char[], int, int)}.
     */
    @Test
    public void testTransformCharsWhenTagEndsAndMultipleSpacesAddsOne() {
        final String chars = "   else";
        
        final StringBuilder result = new StringBuilder("<something>");
        strategy.transformChars(result, chars.toCharArray(), 0, chars.length());
        
        assertEquals("<something> else", result.toString());
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.DefaultSpaceStrategy#transformChars(java.lang.StringBuilder, char[], int, int)}.
     */
    @Test
    public void testTransformCharsWhenTagEndsAndOneSpaceAdds() {
        final String chars = " else";
        
        final StringBuilder result = new StringBuilder("<something>");
        strategy.transformChars(result, chars.toCharArray(), 0, chars.length());
        
        assertEquals("<something> else", result.toString());
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.DefaultSpaceStrategy#transformChars(java.lang.StringBuilder, char[], int, int)}.
     */
    @Test
    public void testTransformCharsWhenTagEndsAndOneSpaceFollowsAddsNoSpace() {
        final String chars = "   else";
        
        final StringBuilder result = new StringBuilder("<something> ");
        strategy.transformChars(result, chars.toCharArray(), 0, chars.length());
        
        assertEquals("<something> else", result.toString());
    }
    
    /**
     * Testovací metoda pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.loader.DefaultSpaceStrategy#transformChars(java.lang.StringBuilder, char[], int, int)}.
     */
    @Test
    public void testTransformCharsSubsequentCallsDoesNotAddMultipleSpaces() {
        final String firstChars = " ";
        final String secondChars = " ";
        final String thirdChars = "   else";
        
        
        final StringBuilder result = new StringBuilder("<something>");
        strategy.transformChars(result, firstChars.toCharArray(), 0, firstChars.length());
        strategy.transformChars(result, secondChars.toCharArray(), 0, secondChars.length());
        strategy.transformChars(result, thirdChars.toCharArray(), 0, thirdChars.length());
        
        assertEquals("<something> else", result.toString());
    }
}
