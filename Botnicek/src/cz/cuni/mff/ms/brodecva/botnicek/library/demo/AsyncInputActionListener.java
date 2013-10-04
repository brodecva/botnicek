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
package cz.cuni.mff.ms.brodecva.botnicek.library.demo;

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.api.Session;
import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Answer;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.ExceptionalState;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Listener;

/**
 * Posluchač s asynchronními reakcemi na vstupy do konverzace.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AsyncInputActionListener extends AbstractInputActionListener {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AsyncInputActionListener.class);
    
    /**
     * Vytvoří asynchronního posluchače pro konverzaci.
     * 
     * @param session připojená konverzace
     * @param gui poslouchané GUI
     */
    public AsyncInputActionListener(final Session session, final Gui gui) {
        super(session, gui);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent event) {
        final Gui gui = getGui();
        final String input = gui.readInput();
        
        LOGGER.log(Level.INFO, "demo.alice.GuiTextInput", new Object[] { gui, input });
        
        getSession().getConversation().talk(input, new Listener() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Listener
             * #answerReceived
             * (cz.cuni.mff.ms.brodecva.botnicek.library.responder.Answer)
             */
            @Override
            public void answerReceived(final Answer answer) {
                LOGGER.log(Level.INFO, "demo.alice.GuiAnswerReceived", new Object[] { gui, answer });
                
                final String response = answer.getAnswer();

                gui.addResponse(response);
            }

            /* (non-Javadoc)
             * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Listener#exceptionCaught(cz.cuni.mff.ms.brodecva.botnicek.library.responder.ExceptionalState)
             */
            @Override
            public void exceptionalStateCaught(final ExceptionalState state) {
                state.getThrowable().printStackTrace();
                gui.alert(state.getThrowable());
            }

        });
    }
}
