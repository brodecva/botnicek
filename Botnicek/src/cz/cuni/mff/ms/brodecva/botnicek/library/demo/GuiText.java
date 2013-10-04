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

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * GUI demonstrační aplikace využívající textovou oblast pro implementaci zobrazení konverzace.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class GuiText extends AbstractGui {
    
    /**
     * Znak nové řádky.
     */
    private static final char NEW_LINE = '\n';

    /**
     * Uvozující dvojtečka (za jménem autora).
     */
    private static final String LEADING_COLON = ": ";
    
    /**
     * Pohled implementovaný textovou oblastí.
     */
    private JTextArea consoleView;

    /**
     * Vytvoří implementaci GUI pro konverzaci.
     * 
     * @param robotName jméno robota
     */
    public GuiText(final String robotName) {
        super(robotName);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.demo.alice.AbstractGui#registerView(java.awt.Container)
     */
    @Override
    protected void registerView(final Container holder) {
        final GridBagConstraints consoleScrollPaneConstraint =
                new GridBagConstraints();

        consoleScrollPaneConstraint.fill = GridBagConstraints.BOTH;
        consoleScrollPaneConstraint.gridheight = VIEW_GRID_HEIGHT;
        consoleScrollPaneConstraint.gridwidth = VIEW_GRID_WIDTH;
        consoleScrollPaneConstraint.gridx = VIEW_GRID_X;
        consoleScrollPaneConstraint.gridy = VIEW_GRID_Y;
        consoleScrollPaneConstraint.insets =
                new Insets(VIEW_INSET_DEFAULT_VALUE, VIEW_INSET_DEFAULT_VALUE,
                        VIEW_INSET_DEFAULT_VALUE, VIEW_INSET_DEFAULT_VALUE);
        consoleScrollPaneConstraint.ipadx = VIEW_PADDING_DEFAULT_VALUE;
        consoleScrollPaneConstraint.ipady = VIEW_PADDING_DEFAULT_VALUE;
        consoleScrollPaneConstraint.weightx = VIEW_WEIGHT_X;
        consoleScrollPaneConstraint.weighty = VIEW_WEIGHT_Y;
        
        consoleView = new JTextArea();
        consoleView.setEditable(false);
        consoleView.setLineWrap(true);
        consoleView.setWrapStyleWord(true);
        
        final JScrollPane consoleScrollPane = new JScrollPane(consoleView);
        consoleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        holder.add(consoleScrollPane, consoleScrollPaneConstraint);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.demo.alice.AbstractGui#appendInput(java.lang.String, java.lang.String)
     */
    @Override
    protected void appendInput(final String author, final String input) {
        appendText(author + LEADING_COLON + input);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.demo.alice.AbstractGui#appendOutput(java.lang.String, java.lang.String)
     */
    @Override
    protected void appendOutput(final String author, final String output) {
        appendText(author + LEADING_COLON + output);
    }
    
    /**
     * Přidá text a odřádkuje.
     * 
     * @param text text k přidání do pohledu na konec
     */
    private void appendText(final String text) {
        consoleView.append(text + NEW_LINE);
        consoleView.setCaretPosition(consoleView.getDocument().getLength());
    }
}
