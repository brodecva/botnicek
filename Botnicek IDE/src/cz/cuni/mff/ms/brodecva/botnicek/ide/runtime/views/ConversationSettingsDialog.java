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

import java.awt.Dialog.ModalityType;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.ConversationSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables.DefaultDisplayStrategyTableModelFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables.DefaultPredicatesTableModelFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables.DisplayStrategyTableModel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables.DisplayStrategyTableModelFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables.PredicatesTableModel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables.PredicatesTableModelFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.AIMLConversationConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.ConversationConfiguration;

/**
 * Správce dialogu nastavení konverzace.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ConversationSettingsDialog implements
        ConversationSettingsView {

    private static final int LABEL_FIELD_GAP_SIZE = 5;
    private static final int FIELD_PREFERRED_WIDTH = 266;
    private static final int SET_BUTTON_PREFERRED_WIDTH = 158;
    private static final int SET_BUTTON_PREFERRED_HEIGHT = 30;
    private static final int CONTENT_PANE_BORDER_SIZE = 5;

    private static ConversationSettingsDialog create(final Window owner) {
        return create(owner, DummyConversationSettingsController.create());
    }

    /**
     * Vytvoří dialog, vyplní, navěsí obslužné metody na ovládací prvky.
     * 
     * @param owner
     *            vlastník dialogu
     * @param conversationSettingsController
     *            řadič nastavení konverzace
     * @return správce dialogu
     */
    public static
            ConversationSettingsDialog
            create(final Window owner,
                    final ConversationSettingsController conversationSettingsController) {
        return create(owner, conversationSettingsController,
                DefaultPredicatesTableModelFactory.create(),
                DefaultDisplayStrategyTableModelFactory.create());
    }

    /**
     * Vytvoří dialog, vyplní, navěsí obslužné metody na ovládací prvky.
     * 
     * @param owner
     *            vlastník dialogu
     * @param conversationSettingsController
     *            řadič nastavení konverzace
     * @param predicatesTableModelFactory
     *            továrna na modely tabulky s predikáty
     * @param displayStrategyTableModelFactory
     *            továrna na modely tabulky strategií
     * @return správce dialogu
     */
    public static
            ConversationSettingsDialog
            create(final Window owner,
                    final ConversationSettingsController conversationSettingsController,
                    final PredicatesTableModelFactory predicatesTableModelFactory,
                    final DisplayStrategyTableModelFactory displayStrategyTableModelFactory) {
        Preconditions.checkNotNull(conversationSettingsController);
        Preconditions.checkNotNull(predicatesTableModelFactory);
        Preconditions.checkNotNull(displayStrategyTableModelFactory);

        final ConversationSettingsDialog newInstance =
                new ConversationSettingsDialog(owner,
                        predicatesTableModelFactory.produce(),
                        displayStrategyTableModelFactory.produce());

        conversationSettingsController.addView(newInstance);
        conversationSettingsController.fill(newInstance);

        newInstance.addDefaultPredicateButton
                .addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        newInstance.defaultPredicatesTableModel.addRow();

                        newInstance.updateSize();
                    }
                });

        newInstance.addDisplayStrategyButton
                .addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        newInstance.displayStrategiesTableModel.addRow();

                        newInstance.updateSize();
                    }
                });

        newInstance.setButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    conversationSettingsController.set(AIMLConversationConfiguration.of(
                            NormalWords
                                    .toUntyped(newInstance.defaultPredicatesTableModel
                                            .getNamesToValues()),
                            NormalWords
                                    .toUntyped(newInstance.displayStrategiesTableModel
                                            .getNamesToValues())));
                } catch (final IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(
                            newInstance.dialog,
                            UiLocalizer
                                    .print("CONFIGURATION_SETTING_ERROR_MESSAGE"),
                            UiLocalizer
                                    .print("CONFIGURATION_SETTING_ERROR_TITLE"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });

        newInstance.dialog.addWindowListener(new WindowAdapter() {
            /*
             * (non-Javadoc)
             * 
             * @see
             * java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent
             * )
             */
            @Override
            public void windowClosing(final WindowEvent e) {
                conversationSettingsController.removeView(newInstance);
            }
        });

        return newInstance;
    }

    /**
     * Spustí testovací dialog.
     * 
     * @param args
     *            argumenty
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());

                    final JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    final ConversationSettingsDialog dialog =
                            ConversationSettingsDialog.create(frame);
                    frame.setVisible(true);
                    dialog.dialog.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final JDialog dialog;
    private final JPanel contentPane = new JPanel();
    private final GroupLayout contentPaneLayout = new GroupLayout(
            this.contentPane);

    private final JLabel defaultPredicatesLabel = new JLabel(
            UiLocalizer.print("DefaultPredicates"));
    private final PredicatesTableModel defaultPredicatesTableModel;
    private final JTable defaultPredicatesTable;
    private final JButton addDefaultPredicateButton = new JButton(
            UiLocalizer.print("Add"));

    private final JLabel displayStrategiesLabel = new JLabel(
            UiLocalizer.print("DisplayStrategy"));

    private final DisplayStrategyTableModel displayStrategiesTableModel;

    private final JTable displayStrategiesTable;

    private final JButton addDisplayStrategyButton = new JButton(
            UiLocalizer.print("Add"));

    private final JButton setButton = new JButton(UiLocalizer.print("Save"));

    /**
     * Create the frame.
     * 
     * @param substitutionsTableModelFactory
     */
    private ConversationSettingsDialog(final Window owner,
            final PredicatesTableModel predicatesTableModel,
            final DisplayStrategyTableModel displayStrategyTableModel) {
        Preconditions.checkNotNull(owner);
        Preconditions.checkNotNull(predicatesTableModel);
        Preconditions.checkNotNull(displayStrategyTableModel);

        this.defaultPredicatesTableModel = predicatesTableModel;
        this.defaultPredicatesTable =
                new JTable(this.defaultPredicatesTableModel);

        this.displayStrategiesTableModel = displayStrategyTableModel;
        this.displayStrategiesTable =
                new JTable(this.displayStrategiesTableModel);

        this.addDisplayStrategyButton.setMnemonic(KeyStroke.getKeyStroke(
                UiLocalizer.print("StrategyAddMnemonics")).getKeyCode());
        this.addDefaultPredicateButton
                .setMnemonic(KeyStroke.getKeyStroke(
                        UiLocalizer.print("DefaultPredicateAddMnemonics"))
                        .getKeyCode());

        this.defaultPredicatesLabel.setLabelFor(this.defaultPredicatesTable);
        this.defaultPredicatesLabel.setDisplayedMnemonic(KeyStroke
                .getKeyStroke(UiLocalizer.print("DefaultPredicatesMnemonics"))
                .getKeyCode());

        this.displayStrategiesLabel.setLabelFor(this.displayStrategiesTable);
        this.displayStrategiesLabel.setDisplayedMnemonic(KeyStroke
                .getKeyStroke(UiLocalizer.print("StrategiesMnemonics"))
                .getKeyCode());

        this.setButton.setMnemonic(KeyStroke.getKeyStroke(
                UiLocalizer.print("SetMnemonics")).getKeyCode());

        this.contentPaneLayout
                .setHorizontalGroup(this.contentPaneLayout
                        .createParallelGroup(Alignment.LEADING)
                        .addGroup(
                                this.contentPaneLayout
                                        .createSequentialGroup()
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.TRAILING)
                                                        .addComponent(
                                                                this.defaultPredicatesLabel)
                                                        .addComponent(
                                                                this.displayStrategiesLabel))
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.LEADING)
                                                        .addGap(LABEL_FIELD_GAP_SIZE)
                                                        .addGap(LABEL_FIELD_GAP_SIZE))
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.LEADING)
                                                        .addGroup(
                                                                this.contentPaneLayout
                                                                        .createParallelGroup(
                                                                                Alignment.TRAILING)
                                                                        .addComponent(
                                                                                this.defaultPredicatesTable
                                                                                        .getTableHeader(),
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                FIELD_PREFERRED_WIDTH,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(
                                                                                this.defaultPredicatesTable,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                FIELD_PREFERRED_WIDTH,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(
                                                                                this.addDefaultPredicateButton,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                FIELD_PREFERRED_WIDTH,
                                                                                Short.MAX_VALUE))
                                                        .addGroup(
                                                                this.contentPaneLayout
                                                                        .createParallelGroup(
                                                                                Alignment.TRAILING)
                                                                        .addComponent(
                                                                                this.displayStrategiesTable
                                                                                        .getTableHeader(),
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                FIELD_PREFERRED_WIDTH,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(
                                                                                this.displayStrategiesTable,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                FIELD_PREFERRED_WIDTH,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(
                                                                                this.addDisplayStrategyButton,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                FIELD_PREFERRED_WIDTH,
                                                                                Short.MAX_VALUE))))
                        .addGroup(
                                this.contentPaneLayout
                                        .createSequentialGroup()
                                        .addContainerGap(0, Short.MAX_VALUE)
                                        .addComponent(this.setButton,
                                                GroupLayout.PREFERRED_SIZE,
                                                SET_BUTTON_PREFERRED_WIDTH,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(0, Short.MAX_VALUE)));
        this.contentPaneLayout
                .setVerticalGroup(this.contentPaneLayout
                        .createParallelGroup(Alignment.LEADING)
                        .addGroup(
                                this.contentPaneLayout
                                        .createSequentialGroup()
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.BASELINE)
                                                        .addComponent(
                                                                this.defaultPredicatesLabel)
                                                        .addGroup(
                                                                this.contentPaneLayout
                                                                        .createSequentialGroup()
                                                                        .addComponent(
                                                                                this.defaultPredicatesTable
                                                                                        .getTableHeader())
                                                                        .addComponent(
                                                                                this.defaultPredicatesTable)
                                                                        .addComponent(
                                                                                this.addDefaultPredicateButton)))
                                        .addPreferredGap(
                                                ComponentPlacement.UNRELATED)
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.BASELINE)
                                                        .addComponent(
                                                                this.displayStrategiesLabel)
                                                        .addGroup(
                                                                this.contentPaneLayout
                                                                        .createSequentialGroup()
                                                                        .addComponent(
                                                                                this.displayStrategiesTable
                                                                                        .getTableHeader())
                                                                        .addComponent(
                                                                                this.displayStrategiesTable)
                                                                        .addComponent(
                                                                                this.addDisplayStrategyButton)))
                                        .addPreferredGap(
                                                ComponentPlacement.UNRELATED)
                                        .addComponent(this.setButton,
                                                GroupLayout.PREFERRED_SIZE,
                                                SET_BUTTON_PREFERRED_HEIGHT,
                                                GroupLayout.PREFERRED_SIZE)));
        this.contentPane.setLayout(this.contentPaneLayout);
        this.contentPane.setBorder(new EmptyBorder(CONTENT_PANE_BORDER_SIZE,
                CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE,
                CONTENT_PANE_BORDER_SIZE));

        this.dialog =
                new JDialog(owner,
                        UiLocalizer.print("CONVERSATION_SETTING_TITLE"),
                        ModalityType.APPLICATION_MODAL);
        this.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.dialog.setContentPane(this.contentPane);
        this.dialog.pack();
    }

    /**
     * 
     */
    public void show() {
        this.dialog.setLocationRelativeTo(this.dialog.getParent());
        this.dialog.setVisible(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.ConversationSettingsView
     * #
     * updatedConversationConfiguration(cz.cuni.mff.ms.brodecva.botnicek.library
     * .api.ConversationConfiguration)
     */
    @Override
    public void updateConversationConfiguration(
            final ConversationConfiguration configuration) {
        Preconditions.checkNotNull(configuration);

        this.defaultPredicatesTableModel.update(NormalWords
                .toTyped(configuration.getDefaultPredicates()));
        this.displayStrategiesTableModel.update(NormalWords
                .toTyped(configuration.getDisplayStrategies()));

        updateSize();
    }

    private void updateSize() {
        this.contentPane.revalidate();
        this.dialog.pack();
    }
}
