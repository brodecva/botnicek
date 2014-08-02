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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.RunController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.ExceptionalState;

/**
 * Panel s běžící testovací konverzací.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class TestPanel extends JPanel implements RunView {

    private static final long serialVersionUID = 1L;

    private final class SendAction extends AbstractAction {
        
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final String input = inputTextField.getText();
            inputTextField.setText("");
            
            runController.tell(input);
        }
    }

    private static final String SPEECH_SEPARATOR = System.lineSeparator();

    private static final String AUTHOR_CONTENT_SEPARATOR = ": ";

    private final RunController runController;
    
    private final JTextArea resultsTextArea = new JTextArea();
    private final JTextField inputTextField = new JTextField();
    private final JButton sendButton = new JButton(UiLocalizer.print("Input"));
    private final Action sendAction = new SendAction();
    
    /**
     * Vytvoří panel konverzace.
     * 
     * @param runController řadič běžící konverzace
     * @return panel konverzace
     */
    public static TestPanel create(final RunController runController) {
        Preconditions.checkNotNull(runController);
        
        final TestPanel newInstance = new TestPanel(runController);
        
        runController.addView(newInstance);
        
        newInstance.sendButton.addActionListener(newInstance.sendAction);
        newInstance.inputTextField.addActionListener(newInstance.sendAction);
        
        return newInstance;
    }
    
    private TestPanel(final RunController runController) {
        Preconditions.checkNotNull(runController);
        
        this.runController = runController;
    }
    
    private void appendText(final String author, final String content) {
        this.resultsTextArea.append(String.format("%1s$%2s$%3s$%4s$", author, AUTHOR_CONTENT_SEPARATOR, content, SPEECH_SEPARATOR));
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.RunView#received(java.lang.String, java.lang.String)
     */
    @Override
    public void receive(final String author, final String content) {
        Preconditions.checkNotNull(author);
        Preconditions.checkNotNull(content);
        
        appendText(author, content);
    }


    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.RunView#exceptionalStateCaught(cz.cuni.mff.ms.brodecva.botnicek.library.responder.ExceptionalState)
     */
    @Override
    public void exceptionalStateCaught(final ExceptionalState state) {
        Preconditions.checkNotNull(state);
        
        JOptionPane.showMessageDialog(this, UiLocalizer.print("EXCEPTIONAL_STATE_MESSAGE"), UiLocalizer.print("EXCEPTIONAL_STATE_TITLE"), JOptionPane.ERROR_MESSAGE);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.RunView#terminated()
     */
    @Override
    public void terminate() {
        this.runController.removeView(this);
    }
}
