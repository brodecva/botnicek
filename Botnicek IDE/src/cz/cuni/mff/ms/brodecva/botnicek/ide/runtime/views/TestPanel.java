/**
 * Copyright Václav Brodec 2014.
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

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

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

    private final class SendAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            final String input = TestPanel.this.inputTextField.getText();
            TestPanel.this.inputTextField.setText("");

            TestPanel.this.runController.tell(input);
        }
    }

    private static final long serialVersionUID = 1L;

    private static final float RESULTS_AREA_FONT_SIZE = 12f;

    private static final String SPEECH_SEPARATOR = System.lineSeparator();

    private static final String AUTHOR_CONTENT_SEPARATOR = ": ";

    private static final int BUTTON_PREFERRED_WIDTH = 100;

    private static final int AREA_PREFERRED_HEIGHT = 100;

    private static final int FIELD_PREFERRED_HEIGHT = 20;

    private static final int BUTTON_PREFERRED_HEIGHT = 20;

    private static final int BORDER_SIZE = 2;

    private static TestPanel create() {
        return create(DummyRunController.create());
    }

    /**
     * Vytvoří panel konverzace.
     * 
     * @param runController
     *            řadič běžící konverzace
     * @return panel konverzace
     */
    public static TestPanel create(final RunController runController) {
        Preconditions.checkNotNull(runController);

        final TestPanel newInstance = new TestPanel(runController);

        runController.addView(newInstance);

        newInstance.sendButton.addActionListener(newInstance.sendAction);
        newInstance.inputTextField.addActionListener(newInstance.sendAction);

        newInstance.addFocusListener(new FocusAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.FocusAdapter#focusGained(java.awt.event.FocusEvent
             * )
             */
            @Override
            public void focusGained(final FocusEvent e) {
                newInstance.inputTextField.requestFocusInWindow();
            }
        });

        return newInstance;
    }

    /**
     * Spustí testovací verzi.
     * 
     * @param args
     *            argumenty
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    frame.setContentPane(TestPanel.create());

                    frame.pack();
                    frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final RunController runController;
    private final JTextArea resultsTextArea = new JTextArea();
    private final JScrollPane resultsTextScrollPane = new JScrollPane(
            this.resultsTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private final JTextField inputTextField = new JTextField();

    private final JButton sendButton = new JButton(UiLocalizer.print("Input"));

    private final Action sendAction = new SendAction();

    private final GroupLayout contentPaneLayout = new GroupLayout(this);

    private TestPanel(final RunController runController) {
        Preconditions.checkNotNull(runController);

        this.runController = runController;

        this.resultsTextArea.setFont(this.resultsTextArea.getFont().deriveFont(
                RESULTS_AREA_FONT_SIZE));
        this.resultsTextArea.setEditable(false);
        this.resultsTextArea.setLineWrap(true);
        this.resultsTextArea.setWrapStyleWord(true);

        this.contentPaneLayout.setHorizontalGroup(this.contentPaneLayout
                .createParallelGroup(Alignment.LEADING)
                .addGroup(
                        this.contentPaneLayout.createParallelGroup(
                                Alignment.LEADING).addComponent(
                                this.resultsTextScrollPane,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.DEFAULT_SIZE))
                .addGroup(
                        this.contentPaneLayout
                                .createSequentialGroup()
                                .addComponent(this.inputTextField,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)
                                .addComponent(this.sendButton,
                                        GroupLayout.PREFERRED_SIZE,
                                        BUTTON_PREFERRED_WIDTH,
                                        GroupLayout.PREFERRED_SIZE)));
        this.contentPaneLayout.setVerticalGroup(this.contentPaneLayout
                .createSequentialGroup()
                .addGroup(
                        this.contentPaneLayout.createParallelGroup(
                                Alignment.BASELINE).addComponent(
                                this.resultsTextScrollPane,
                                GroupLayout.DEFAULT_SIZE,
                                AREA_PREFERRED_HEIGHT, Short.MAX_VALUE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(
                        this.contentPaneLayout
                                .createParallelGroup(Alignment.BASELINE)
                                .addComponent(this.inputTextField,
                                        GroupLayout.PREFERRED_SIZE,
                                        FIELD_PREFERRED_HEIGHT,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(this.sendButton,
                                        GroupLayout.PREFERRED_SIZE,
                                        BUTTON_PREFERRED_HEIGHT,
                                        GroupLayout.PREFERRED_SIZE)));
        setLayout(this.contentPaneLayout);
        setBorder(new EmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE,
                BORDER_SIZE));
        revalidate();
    }

    private void appendText(final String author, final String content) {
        this.resultsTextArea.append(String.format("%1$s%2$s%3$s%4$s", author,
                AUTHOR_CONTENT_SEPARATOR, content, SPEECH_SEPARATOR));
        this.resultsTextArea.setCaretPosition(this.resultsTextArea
                .getDocument().getLength());
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.RunView#
     * exceptionalStateCaught
     * (cz.cuni.mff.ms.brodecva.botnicek.library.responder.ExceptionalState)
     */
    @Override
    public void exceptionalStateCaught(final ExceptionalState state) {
        Preconditions.checkNotNull(state);

        JOptionPane.showMessageDialog(
                this,
                UiLocalizer.print("EXCEPTIONAL_STATE_MESSAGE"),
                UiLocalizer.print("EXCEPTIONAL_STATE_TITLE"),
                JOptionPane.ERROR_MESSAGE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.RunView#received(java
     * .lang.String, java.lang.String)
     */
    @Override
    public void receive(final String author, final String content) {
        Preconditions.checkNotNull(author);
        Preconditions.checkNotNull(content);

        appendText(author, content);

        this.inputTextField.requestFocusInWindow();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.RunView#terminated()
     */
    @Override
    public void terminate() {
        this.runController.removeView(this);
    }
}
