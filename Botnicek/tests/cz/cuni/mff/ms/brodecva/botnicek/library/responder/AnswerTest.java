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
package cz.cuni.mff.ms.brodecva.botnicek.library.responder;

import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje odpověď bota.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Answer
 */
@Category(UnitTest.class)
public final class AnswerTest {

    /**
     * Testovaná odpověď.
     */
    private Answer answer = null;

    /**
     * Nastaví testovaný objekt.
     */
    @Before
    public void setUp() {
        final Conversation conversationStub =
                EasyMock.createMock(Conversation.class);
        replay(conversationStub);

        answer = new Answer(conversationStub, "dummy");
    }

    /**
     * Uklidí testovaný objekt.
     */
    @After
    public void tearDown() {
        answer = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.Answer#equals(java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.Answer#hashCode()}
     * .
     */
    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(Answer.class).usingGetClass();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.Answer#Answer(cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation, java.lang.String)}
     * .
     */
    @Test
    public void testAnswer() {
        final Conversation conversationStub =
                EasyMock.createMock(Conversation.class);
        replay(conversationStub);

        new Answer(conversationStub, "dummy");
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
        final Answer original = answer;

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        final byte[] pickled = out.toByteArray();
        final InputStream in = new ByteArrayInputStream(pickled);
        final ObjectInputStream ois = new ObjectInputStream(in);
        final Object o = ois.readObject();
        final Answer copy = (Answer) o;

        assertEquals(original, copy);
    }

}
