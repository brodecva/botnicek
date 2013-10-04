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
import nl.jqno.equalsverifier.EqualsVerifier;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje výjimečný stav konverzace.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see ExceptionalState
 */
@Category(UnitTest.class)
public final class ExceptionalStateTest {

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.ExceptionalState#equals(java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.ExceptionalState#hashCode()}
     * .
     */
    @Test
    public void testEqualsAndHashCode() {
        EqualsVerifier.forClass(ExceptionalState.class).usingGetClass();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.ExceptionalState#ExceptionalState(Conversation, Throwable)}
     * .
     */
    @Test
    public void testExceptionalState() {
        final Conversation conversationStub =
                EasyMock.createMock(Conversation.class);
        replay(conversationStub);
        
        final Throwable throwableStub = EasyMock.createMock(Throwable.class);
        replay(throwableStub);

        new ExceptionalState(conversationStub, throwableStub);
    }
}
