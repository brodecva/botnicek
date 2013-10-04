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

import static org.junit.Assert.assertEquals;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.anyChar;
import static org.easymock.EasyMock.replay;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Test AIML implementace slova.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AIMLWord
 */
@Category(UnitTest.class)
public final class AIMLWordTest {
    
    /**
     * Stub normalizéru.
     */
    private Normalizer normalizerStub;
    
    /**
     * Vytvoří stub normalizéru.
     */
    @Before
    public void setUp() {
        normalizerStub = EasyMock.createMock(Normalizer.class);
        expect(normalizerStub.isNormal(anyChar())).andStubAnswer(new IAnswer<Boolean>() {

            @Override
            public Boolean answer() throws Throwable {
                char argument = (char) EasyMock.getCurrentArguments()[0];
                
                return Character.isUpperCase(argument);
            }
            
        });
        replay(normalizerStub);
    }
    
    /**
     * Uklidí stub normalizéru.
     */
    @After
    public void tearDown() {
        normalizerStub = null;
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWord#equals(java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWord#hashCode()}
     * .
     */
    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(AIMLWord.class).usingGetClass().suppress(Warning.NULL_FIELDS).verify();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWord#AIMLWord(String, Normalizer)}
     * .
     */
    @Test
    public void testAIMLWordValidString() {
        new AIMLWord("DUMMY", normalizerStub);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWord#AIMLWord(String, Normalizer)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAIMLWordStringWithCharNotNormal() {
        new AIMLWord("DuMMY", normalizerStub);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWord#AIMLWord(String, Normalizer)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAIMLWordStringWithSpace() {
        new AIMLWord("DU MMY", normalizerStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWord#AIMLWord(String, Normalizer)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testAIMLWordWhenValueNull() {
        new AIMLWord(null, normalizerStub);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWord#AIMLWord(String, Normalizer)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testAIMLWordWhenNormalizerNull() {
        new AIMLWord("DUMMY", null);
    }

    /**
     * Test serializace.
     * 
     * @throws IOException
     *             pokud dojde k I/O chybě
     * @throws ClassNotFoundException
     *             pokud není nalezena třída
     */
    @Test
    public void testRoundTripSerialization() throws IOException,
            ClassNotFoundException {
        final AIMLWord original = new AIMLWord("DUMMY", normalizerStub);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        final byte[] pickled = out.toByteArray();
        final InputStream in = new ByteArrayInputStream(pickled);
        final ObjectInputStream ois = new ObjectInputStream(in);
        final Object o = ois.readObject();
        final AIMLWord copy = (AIMLWord) o;

        assertEquals(original, copy);
    }
}
