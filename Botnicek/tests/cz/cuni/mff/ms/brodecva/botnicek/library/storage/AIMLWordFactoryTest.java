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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.anyChar;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje továrnu na AIML slova.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
@Category(UnitTest.class)
public final class AIMLWordFactoryTest {
    
    /**
     * Stub normalizéru.
     */
    private Normalizer normalizerStub;
    
    /**
     * Testovaná továrna.
     */
    private AIMLWordFactory factory = null;
    
    /**
     * Vytvoří testovaný objekt.
     */
    @Before
    public void setUp() {
        normalizerStub = EasyMock.createMock(Normalizer.class);
        expect(normalizerStub.isNormal(anyChar())).andStubReturn(true);
        replay(normalizerStub);
        
        factory = new AIMLWordFactory(normalizerStub);
    }

    /**
     * Uklidí testovaný objekt.
     */
    @After
    public void tearDown() {
        factory = null;
        
        normalizerStub = null;
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWordFactory#AIMLWordFactory(Normalizer)}.
     */
    @Test
    public void testAIMLWordFactory() {
        new AIMLWordFactory(normalizerStub);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWordFactory#AIMLWordFactory(Normalizer)}.
     */
    @Test(expected = NullPointerException.class)
    public void testAIMLWordFactoryWhenNormalizerNull() {
        new AIMLWordFactory(null);
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWordFactory#create(java.lang.String)}.
     */
    @Test
    public void testCreateWhenAsteriskReturnsWildcard() {
        assertEquals(AIMLWildcard.ASTERISK, factory.create("*"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWordFactory#create(java.lang.String)}.
     */
    @Test
    public void testCreateWhenUnderscoreReturnsWildcard() {
        assertEquals(AIMLWildcard.UNDERSCORE, factory.create("_"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWordFactory#create(java.lang.String)}.
     */
    @Test
    public void testCreateWhenThatReturnsMarker() {
        assertEquals(AIMLPartMarker.THAT, factory.create("<that>"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWordFactory#create(java.lang.String)}.
     */
    @Test
    public void testCreateWhenTopicReturnsMarker() {
        assertEquals(AIMLPartMarker.TOPIC, factory.create("<topic>"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWordFactory#create(java.lang.String)}.
     */
    @Test
    public void testCreateWhenNormalTextReturnsWord() {
        assertEquals(new AIMLWord("TEXT", normalizerStub), factory.create("TEXT"));
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWordFactory#create(java.lang.String)}.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateWhenValueNull() {
        factory.create(null);
    }
}
