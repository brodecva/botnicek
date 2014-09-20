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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Základ GUI demonstrační aplikace.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractGui implements Gui {

    /**
     * Lokalizované popisky.
     */
    public static final ResourceBundle LABELS = ResourceBundle
            .getBundle("cz.cuni.mff.ms.brodecva.botnicek.library."
                    + "demo.alice.labels");

    /**
     * Šířka rámu.
     */
    public static final int FRAME_WIDTH = 600;

    /**
     * Výška rámu.
     */
    public static final int FRAME_HEIGHT = 400;

    /**
     * Váha tlačítka k poslaní ve vertikálním směru.
     */
    public static final int SEND_WEIGHT_Y = 0;

    /**
     * Váha tlačítka k poslaní v horizontálním směru.
     */
    public static final int SEND_WEIGHT_X = 0;

    /**
     * Výchozí padding tlačítka k poslaní.
     */
    public static final int SEND_DEFAULT_PADDING = 2;

    /**
     * Zmenšená velikost rámce tlačítka k poslaní.
     */
    public static final int SEND_REDUCED_INSET = 1;

    /**
     * Výchozí velikost rámce tlačítka k poslaní.
     */
    public static final int SEND_DEFAULT_INSET = 2;

    /**
     * Umístění tlačítka k poslaní v horizontálním směru.
     */
    public static final int SEND_GRID_X = 1;

    /**
     * Umístění tlačítka k poslaní ve vertikálním směru.
     */
    public static final int SEND_GRID_Y = 10;

    /**
     * Počet políček mřížky s tlačítkem k poslaní v horizontálním směru.
     */
    public static final int SEND_GRID_WIDTH = 1;

    /**
     * Počet políček mřížky s tlačítkem k poslaní ve vertikálním směru.
     */
    public static final int SEND_GRID_HEIGHT = 1;

    /**
     * Váha vstupního pole ve vertikálním směru.
     */
    public static final int INPUT_WEIGHT_Y = 0;

    /**
     * Váha vstupního pole v horizontálním směru.
     */
    public static final int INPUT_WEIGHT_X = 1;

    /**
     * Zmenšená velikost rámce vstupního pole.
     */
    public static final int INPUT_REDUCED_INSET = 1;

    /**
     * Výchozí velikost rámce vstupního pole.
     */
    public static final int INPUT_DEFAULT_INSET = 2;

    /**
     * Výchozí padding vstupního pole.
     */
    public static final int INPUT_DEFAULT_PADDING = 2;

    /**
     * Umístění vstupního pole ve vertikálním směru.
     */
    public static final int INPUT_GRID_Y = 10;

    /**
     * Umístění vstupního pole v horizontálním směru.
     */
    public static final int INPUT_GRID_X = 0;

    /**
     * Počet políček mřížky s vstupním polem v horizontálním směru.
     */
    public static final int INPUT_GRID_WIDTH = 1;

    /**
     * Počet políček mřížky s vstupním polem ve vertikálním směru.
     */
    public static final int INPUT_GRID_HEIGHT = 1;

    /**
     * Váha zobrazeného textu v horizontálním směru.
     */
    public static final int VIEW_WEIGHT_X = 0;

    /**
     * Váha zobrazeného textu ve vertikálním směru.
     */
    public static final int VIEW_WEIGHT_Y = 1;

    /**
     * Počet políček mřížky zobrazeného textu v horizontálním směru.
     */
    public static final int VIEW_GRID_X = 0;

    /**
     * Počet políček mřížky zobrazeného textu ve vertikálním směru.
     */
    public static final int VIEW_GRID_Y = 0;

    /**
     * Počet políček mřížky zobrazeného textu ve vertikálním směru.
     */
    public static final int VIEW_GRID_HEIGHT = 10;

    /**
     * Počet políček mřížky zobrazeného textu v horizontálním směru.
     */
    public static final int VIEW_GRID_WIDTH = 2;

    /**
     * Výchozí velikost rámce zobrazeného textu.
     */
    public static final int VIEW_INSET_DEFAULT_VALUE = 5;

    /**
     * Výchozí padding zobrazeného textu.
     */
    public static final int VIEW_PADDING_DEFAULT_VALUE = 5;

    /**
     * Okno konverzace.
     */
    private JFrame frame;

    /**
     * Vstupní pole.
     */
    private JTextField inputField;

    /**
     * Jméno robota.
     */
    private final String robotName;

    /**
     * Vytvoří základ GUI pro konverzaci.
     * 
     * @param robotName jméno robota
     */
    protected AbstractGui(final String robotName) {
        this.robotName = robotName;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.demo.alice.Gui#getRobotName()
     */
    @Override
    public final String getRobotName() {
        return robotName;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.demo.alice.Gui#createAndShowGUI(java.awt.event.ActionListener)
     */
    @Override
    public final void createAndShowGUI(final ActionListener inputListener) {
        frame = new JFrame(LABELS.getString("MainFrameLabel"));

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

        final Container pane = frame.getContentPane();
        pane.setLayout(new GridBagLayout());

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

        registerView(pane); // Implementace je na odvozených třídách.

        final GridBagConstraints inputFieldConstraint =
                new GridBagConstraints();

        inputFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
        inputFieldConstraint.gridheight = INPUT_GRID_HEIGHT;
        inputFieldConstraint.gridwidth = INPUT_GRID_WIDTH;
        inputFieldConstraint.gridx = INPUT_GRID_X;
        inputFieldConstraint.gridy = INPUT_GRID_Y;
        inputFieldConstraint.insets =
                new Insets(INPUT_DEFAULT_INSET, INPUT_DEFAULT_INSET,
                        INPUT_DEFAULT_INSET, INPUT_REDUCED_INSET);
        inputFieldConstraint.ipadx = INPUT_DEFAULT_PADDING;
        inputFieldConstraint.ipady = INPUT_DEFAULT_PADDING;
        inputFieldConstraint.weightx = INPUT_WEIGHT_X;
        inputFieldConstraint.weighty = INPUT_WEIGHT_Y;

        inputField = new JTextField();
        pane.add(inputField, inputFieldConstraint);

        inputField.addActionListener(inputListener);

        final GridBagConstraints sendButtonConstraint =
                new GridBagConstraints();

        sendButtonConstraint.fill = GridBagConstraints.HORIZONTAL;
        sendButtonConstraint.gridheight = SEND_GRID_HEIGHT;
        sendButtonConstraint.gridwidth = SEND_GRID_WIDTH;
        sendButtonConstraint.gridx = SEND_GRID_X;
        sendButtonConstraint.gridy = SEND_GRID_Y;
        sendButtonConstraint.insets =
                new Insets(SEND_DEFAULT_INSET, SEND_REDUCED_INSET,
                        SEND_DEFAULT_INSET, SEND_DEFAULT_INSET);
        sendButtonConstraint.ipadx = SEND_DEFAULT_PADDING;
        sendButtonConstraint.ipady = SEND_DEFAULT_PADDING;
        sendButtonConstraint.weightx = SEND_WEIGHT_X;
        sendButtonConstraint.weighty = SEND_WEIGHT_Y;

        final JButton sendButton =
                new JButton(LABELS.getString("SendButtonLabel"));
        pane.add(sendButton, sendButtonConstraint);

        sendButton.addActionListener(inputListener);
        
        frame.revalidate();

        inputField.requestFocusInWindow();

        frame.setVisible(true);
    }

    /**
     * Odvozené třídy mohou nadefinováním této metody přidat zobrazovací prvek
     * tím, že jej vloží do nosného kontejneru z parametru.
     * 
     * @param holder
     *            kontejner nesoucí zobrazovací prvek
     */
    protected abstract void registerView(final Container holder);

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.demo.Gui#readInput()
     */
    @Override
    public synchronized String readInput() {
        inputField.setEnabled(false);
        final String input = inputField.getText();
        inputField.setText("");

        appendInput(LABELS.getString("UserPrompt"), input);

        return input;
    }

    /**
     * Přidá daný vstup k zobrazení.
     * 
     * @param author
     *            jméno autora
     * @param input
     *            přidaný vstup
     */
    protected abstract void appendInput(final String author, final String input);

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.demo.Gui#addResponse(java.lang.String)
     */
    @Override
    public synchronized void addResponse(final String response) {
        appendOutput(robotName, response);

        inputField.setEnabled(true);
        inputField.requestFocusInWindow();
    }

    /**
     * Přidá daný výstup k zobrazení.
     * 
     * @param author jméno autora
     * @param output
     *            přidaný výstup
     */
    protected abstract void appendOutput(final String author, final String output);

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.demo.Gui#alert(java.lang.Throwable)
     */
    @Override
    public void alert(final Throwable throwable) {
        JOptionPane.showMessageDialog(frame, throwable.toString(),
                LABELS.getString("AlertCaption"), JOptionPane.ERROR_MESSAGE);
    }
}
