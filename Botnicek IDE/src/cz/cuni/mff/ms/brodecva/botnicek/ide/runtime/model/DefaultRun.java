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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.ExceptionalStateCaughtEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.SpokenEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Answer;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.ExceptionalState;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Listener;

/**
 * Výchozí implementace {@link Run}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultRun implements Run {
    
    private static final String USER = "Uživatel";
    
    private final Conversation conversation;
    private final Dispatcher dispatcher;
    
    /**
     * Vytvoří instanci běžící konverzace.
     * 
     * @param conversation konverzace
     * @param dispatcher rozesílač událostí
     * @return instance běžící konverzace
     */
    public static Run create(final Conversation conversation, final Dispatcher dispatcher) {
        return new DefaultRun(conversation, dispatcher);
    }
    
    private DefaultRun(final Conversation conversation, final Dispatcher dispatcher) {
        Preconditions.checkNotNull(conversation);
        Preconditions.checkNotNull(dispatcher);
        
        this.conversation = conversation;
        this.dispatcher = dispatcher;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.Run#tell(java.lang.String)
     */
    @Override
    public void tell(final String content) {
        Preconditions.checkNotNull(content);
        
        this.dispatcher.fire(SpokenEvent.create(DefaultRun.this, USER, content));
        
        this.conversation.talk(content, new Listener() {
            
            @Override
            public void exceptionalStateCaught(final ExceptionalState status) {
                dispatcher.fire(ExceptionalStateCaughtEvent.create(DefaultRun.this, status));
            }
            
            @Override
            public void answerReceived(final Answer answer) {
                dispatcher.fire(SpokenEvent.create(DefaultRun.this, conversation.getBot().getName(), answer.getAnswer()));
            }
        });
    }
}
