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
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;

/**
 * Posluchač blokující při čekání na odpověď v konverzaci.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class BlockingInputActionListener extends AbstractInputActionListener {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(BlockingInputActionListener.class);
    
    /**
     * Vytvoří blokujícího posluchače pro konverzaci.
     * 
     * @param session připojená konverzace
     * @param gui poslouchané GUI
     */
    public BlockingInputActionListener(final Session session, final Gui gui) {
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
        
        LOGGER.log(Level.INFO, "demo.alice.GuiTextInput", new Object[] { this, input });
        
        final Conversation conversation = getSession().getConversation();
        
        try {
            conversation.talk(input);
        } catch (final Throwable e) {
            e.printStackTrace();
            gui.alert(e);
        }
        
        final String response = conversation.listen();
        LOGGER.log(Level.INFO, "demo.alice.GuiAnswerReceived", new Object[] { this, response });
        
        gui.addResponse(response);
    }
}
