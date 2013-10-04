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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;

import java.util.regex.Pattern;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje rozdělovač textu na věty podle jednoduchého pravidla.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see SimpleSplitterTest
 */
@Category(UnitTest.class)
public final class SimpleSplitterTest {
    
    /**
     * Testovaný rozdělovač.
     */
    private SimpleSplitter splitter = null;
    
    /**
     * Mock definice jazyka. 
     */
    private Language languageMock = null;
    
    /**
     * Vzor k dělení.
     */
    private Pattern dotPattern = null;

    /**
     * Nastaví mock a testovaný objekt.
     */
    @Before
    public void setUp() {
        dotPattern = Pattern.compile("\\.");
        
        languageMock = EasyMock.createMock(Language.class);
        expect(languageMock.getSentenceDelim()).andReturn(dotPattern);
        replay(languageMock);
        
        
        splitter = new SimpleSplitter(languageMock);
    }

    /**
     * Uklidí mock a testovaný objekt.
     */
    @After
    public void tearDown() {
        languageMock = null;
        
        splitter = null;
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleSplitter#splitToSentences(java.lang.String)}.
     */
    @Test(expected = NullPointerException.class)
    public void testSplitToSentencesWhenTextNull() {
        splitter.splitToSentences(null);
    }
    
    /**
    * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleSplitter#splitToSentences(java.lang.String)}.
    */
   @Test
   public void testSplitToSentencesWhenValidText() {
       final String[] result = splitter.splitToSentences("Valid.Text.");
       
       assertArrayEquals(new String[] { "Valid", "Text" }, result);
       
       verify(languageMock);
   }

}
