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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.views;

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
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.controllers.SettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;

/**
 * <p>
 * Spravuje dialog zobrazující nastavení projektu
 * </p>
 * <p>
 * Tento dialog dovoluje nastavit prefix, který umožňuje rozlišit provozní stavy
 * od uživatelských. Dále pak též výchozí prefixy prostorů jmen exportovaných
 * dokumentů a konečně samotné provozní stavy.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class SettingsDialog implements SettingsView {

    private static final int CONTENT_PANE_BORDER_SIZE = 5;

    private static final int SET_BUTTON_PREFERRED_HEIGHT = 30;

    private static final int FIELD_PREFERRED_HEIGHT = 20;

    private static final int SET_BUTTON_PREFERRED_WIDTH = 158;

    private static final int FIELD_PREFERRED_WIDTH = 266;

    private static final int LABEL_FIELD_GAP_SIZE = 5;

    private static SettingsDialog create(final Window owner) {
        return create(owner, DummySettingsController.create());
    }

    /**
     * Vytvoří dialog s nastaveními projektu ve vlastníkovi.
     * 
     * @param owner
     *            vlastník
     * @param settingsController
     *            řadič nastavení projektu
     * @return instance spravující dialog
     */
    public static SettingsDialog create(final Window owner,
            final SettingsController settingsController) {
        return create(owner, settingsController,
                DefaultPrefixesSettingsTableModelFactory.create());
    }

    /**
     * Vytvoří dialog s nastaveními projektu ve vlastníkovi.
     * 
     * @param owner
     *            vlastník
     * @param settingsController
     *            řadič nastavení projektu, na který je pohled napojen
     * @param prefixesSettingTableModelFactory
     *            továrna na modely tabulky prefixů pro prostory jmen
     * @return instance spravující dialog
     */
    public static
            SettingsDialog
            create(final Window owner,
                    final SettingsController settingsController,
                    final PrefixesSettingsTableModelFactory prefixesSettingTableModelFactory) {
        Preconditions.checkNotNull(settingsController);
        Preconditions.checkNotNull(prefixesSettingTableModelFactory);

        final SettingsDialog newInstance =
                new SettingsDialog(owner,
                        prefixesSettingTableModelFactory.produce());

        settingsController.addView(newInstance);
        settingsController.fill(newInstance);

        newInstance.addPrefixSettingButton
                .addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        newInstance.prefixesSettingTableModel.addRow();

                        newInstance.updateSize();
                    }
                });

        newInstance.setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    settingsController.set(Settings.create(
                            newInstance.prefixesSettingTableModel
                                    .getNamesToValues(), NormalWords
                                    .of(newInstance.prefixTextField.getText()),
                            NormalWords.of(newInstance.pullStateTextField
                                    .getText()), NormalWords
                                    .of(newInstance.pullStopStateTextField
                                            .getText()), NormalWords
                                    .of(newInstance.successStateTextField
                                            .getText()), NormalWords
                                    .of(newInstance.failStateTextField
                                            .getText()), NormalWords
                                    .of(newInstance.returnStateTextField
                                            .getText()), NormalWords
                                    .of(newInstance.randomizeStateTextField
                                            .getText()), NormalWords
                                    .of(newInstance.testingPredicateTextField
                                            .getText())));
                } catch (final IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(newInstance.dialog,
                            UiLocalizer.print("SETTINGS_ERROR_MESSAGE"),
                            UiLocalizer.print("SETTINGS_ERROR_TITLE"),
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
                settingsController.removeView(newInstance);
            }
        });

        return newInstance;
    }

    /**
     * Zobrazí testovací instanci.
     * 
     * @param args
     *            argumenty příkazové řádky
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

                    final SettingsDialog dialog = SettingsDialog.create(frame);

                    frame.setVisible(true);
                    dialog.dialog.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final PrefixesSettingsTableModel prefixesSettingTableModel;
    private final JDialog dialog;
    private final JPanel contentPane = new JPanel();

    private final GroupLayout contentPaneLayout = new GroupLayout(
            this.contentPane);
    private final JLabel prefixesSettingLabel = new JLabel(
            UiLocalizer.print("NamespacePrefixes"));

    private final JTable prefixesSettingTable;
    private final JButton addPrefixSettingButton = new JButton(
            UiLocalizer.print("Add"));

    private final JLabel prefixLabel = new JLabel(
            UiLocalizer.print("AllStatesPrefix"));
    private final JTextField prefixTextField = new JTextField();

    private final JLabel pullStateLabel = new JLabel(
            UiLocalizer.print("PullState"));
    private final JTextField pullStateTextField = new JTextField();

    private final JLabel pullStopStateLabel = new JLabel(
            UiLocalizer.print("PullStateStop"));
    private final JTextField pullStopStateTextField = new JTextField();

    private final JLabel testingPredicateLabel = new JLabel(
            UiLocalizer.print("TestingPredicate"));
    private final JTextField testingPredicateTextField = new JTextField();

    private final JLabel randomizeStateLabel = new JLabel(
            UiLocalizer.print("RandomizeState"));
    private final JTextField randomizeStateTextField = new JTextField();

    private final JLabel successStateLabel = new JLabel(
            UiLocalizer.print("SuccessState"));
    private final JTextField successStateTextField = new JTextField();

    private final JLabel failStateLabel = new JLabel(
            UiLocalizer.print("FailState"));

    private final JTextField failStateTextField = new JTextField();

    private final JLabel returnStateLabel = new JLabel(
            UiLocalizer.print("ReturnState"));

    private final JTextField returnStateTextField = new JTextField();

    private final JButton setButton = new JButton(UiLocalizer.print("Save"));

    private SettingsDialog(final Window owner,
            final PrefixesSettingsTableModel prefixesSettingTableModel) {
        Preconditions.checkNotNull(owner);
        Preconditions.checkNotNull(prefixesSettingTableModel);

        this.prefixesSettingTableModel = prefixesSettingTableModel;
        this.prefixesSettingTable = new JTable(prefixesSettingTableModel);

        this.prefixesSettingLabel.setLabelFor(this.prefixesSettingTable);
        this.prefixesSettingLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(
                UiLocalizer.print("PrefixesMnemonics")).getKeyCode());

        this.pullStateLabel.setLabelFor(this.pullStateTextField);
        this.pullStateLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(
                UiLocalizer.print("PullMnemonics")).getKeyCode());

        this.pullStopStateLabel.setLabelFor(this.pullStopStateTextField);
        this.pullStopStateLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(
                UiLocalizer.print("PullStopMnemonics")).getKeyCode());

        this.testingPredicateLabel.setLabelFor(this.testingPredicateTextField);
        this.testingPredicateLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(
                UiLocalizer.print("TestingMnemonics")).getKeyCode());

        this.randomizeStateLabel.setLabelFor(this.randomizeStateTextField);
        this.randomizeStateLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(
                UiLocalizer.print("RandomizeMnemonics")).getKeyCode());

        this.successStateLabel.setLabelFor(this.successStateTextField);
        this.successStateLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(
                UiLocalizer.print("SuccessMnemonics")).getKeyCode());

        this.failStateLabel.setLabelFor(this.failStateTextField);
        this.failStateLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(
                UiLocalizer.print("FailMnemonics")).getKeyCode());

        this.returnStateLabel.setLabelFor(this.returnStateTextField);
        this.returnStateLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(
                UiLocalizer.print("ReturnMnemonics")).getKeyCode());

        this.prefixLabel.setLabelFor(this.prefixTextField);
        this.prefixLabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(
                UiLocalizer.print("PrefixMnemonics")).getKeyCode());

        this.addPrefixSettingButton.setMnemonic(KeyStroke.getKeyStroke(
                UiLocalizer.print("AddPrefixMnemonics")).getKeyCode());
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
                                                                this.randomizeStateLabel)
                                                        .addComponent(
                                                                this.prefixesSettingLabel)
                                                        .addComponent(
                                                                this.testingPredicateLabel)
                                                        .addComponent(
                                                                this.pullStateLabel)
                                                        .addComponent(
                                                                this.prefixLabel)
                                                        .addComponent(
                                                                this.successStateLabel)
                                                        .addComponent(
                                                                this.failStateLabel)
                                                        .addComponent(
                                                                this.returnStateLabel)
                                                        .addComponent(
                                                                this.pullStopStateLabel))
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.LEADING)
                                                        .addGap(LABEL_FIELD_GAP_SIZE)
                                                        .addGap(LABEL_FIELD_GAP_SIZE)
                                                        .addGap(LABEL_FIELD_GAP_SIZE)
                                                        .addGap(LABEL_FIELD_GAP_SIZE)
                                                        .addGap(LABEL_FIELD_GAP_SIZE)
                                                        .addGap(LABEL_FIELD_GAP_SIZE)
                                                        .addGap(LABEL_FIELD_GAP_SIZE)
                                                        .addGap(LABEL_FIELD_GAP_SIZE))
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.LEADING)
                                                        .addComponent(
                                                                this.randomizeStateTextField,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                FIELD_PREFERRED_WIDTH,
                                                                Short.MAX_VALUE)
                                                        .addComponent(
                                                                this.prefixTextField,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                FIELD_PREFERRED_WIDTH,
                                                                Short.MAX_VALUE)
                                                        .addComponent(
                                                                this.testingPredicateTextField,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                FIELD_PREFERRED_WIDTH,
                                                                Short.MAX_VALUE)
                                                        .addComponent(
                                                                this.pullStateTextField,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                FIELD_PREFERRED_WIDTH,
                                                                Short.MAX_VALUE)
                                                        .addGroup(
                                                                this.contentPaneLayout
                                                                        .createParallelGroup(
                                                                                Alignment.TRAILING)
                                                                        .addComponent(
                                                                                this.prefixesSettingTable
                                                                                        .getTableHeader(),
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                FIELD_PREFERRED_WIDTH,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(
                                                                                this.prefixesSettingTable,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                FIELD_PREFERRED_WIDTH,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(
                                                                                this.addPrefixSettingButton,
                                                                                GroupLayout.DEFAULT_SIZE,
                                                                                FIELD_PREFERRED_WIDTH,
                                                                                Short.MAX_VALUE))
                                                        .addComponent(
                                                                this.successStateTextField,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                FIELD_PREFERRED_WIDTH,
                                                                Short.MAX_VALUE)
                                                        .addComponent(
                                                                this.failStateTextField,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                FIELD_PREFERRED_WIDTH,
                                                                Short.MAX_VALUE)
                                                        .addComponent(
                                                                this.returnStateTextField,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                FIELD_PREFERRED_WIDTH,
                                                                Short.MAX_VALUE)
                                                        .addComponent(
                                                                this.pullStopStateTextField,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                FIELD_PREFERRED_WIDTH,
                                                                Short.MAX_VALUE)))
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
                                                                this.prefixLabel)
                                                        .addComponent(
                                                                this.prefixTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                FIELD_PREFERRED_HEIGHT,
                                                                GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(
                                                ComponentPlacement.UNRELATED)
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.BASELINE)
                                                        .addComponent(
                                                                this.pullStateLabel)
                                                        .addComponent(
                                                                this.pullStateTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                FIELD_PREFERRED_HEIGHT,
                                                                GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(
                                                ComponentPlacement.RELATED)
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.BASELINE)
                                                        .addComponent(
                                                                this.pullStopStateLabel)
                                                        .addComponent(
                                                                this.pullStopStateTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                FIELD_PREFERRED_HEIGHT,
                                                                GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(
                                                ComponentPlacement.RELATED)
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.BASELINE)
                                                        .addComponent(
                                                                this.randomizeStateLabel)
                                                        .addComponent(
                                                                this.randomizeStateTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                FIELD_PREFERRED_HEIGHT,
                                                                GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(
                                                ComponentPlacement.RELATED)
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.BASELINE)
                                                        .addComponent(
                                                                this.successStateLabel)
                                                        .addComponent(
                                                                this.successStateTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                FIELD_PREFERRED_HEIGHT,
                                                                GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(
                                                ComponentPlacement.RELATED)
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.BASELINE)
                                                        .addComponent(
                                                                this.failStateLabel)
                                                        .addComponent(
                                                                this.failStateTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                FIELD_PREFERRED_HEIGHT,
                                                                GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(
                                                ComponentPlacement.RELATED)
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.BASELINE)
                                                        .addComponent(
                                                                this.returnStateLabel)
                                                        .addComponent(
                                                                this.returnStateTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                FIELD_PREFERRED_HEIGHT,
                                                                GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(
                                                ComponentPlacement.UNRELATED)
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.BASELINE)
                                                        .addComponent(
                                                                this.prefixesSettingLabel)
                                                        .addGroup(
                                                                this.contentPaneLayout
                                                                        .createSequentialGroup()
                                                                        .addComponent(
                                                                                this.prefixesSettingTable
                                                                                        .getTableHeader())
                                                                        .addComponent(
                                                                                this.prefixesSettingTable)
                                                                        .addComponent(
                                                                                this.addPrefixSettingButton)))
                                        .addPreferredGap(
                                                ComponentPlacement.UNRELATED)
                                        .addGroup(
                                                this.contentPaneLayout
                                                        .createParallelGroup(
                                                                Alignment.BASELINE)
                                                        .addComponent(
                                                                this.testingPredicateLabel)
                                                        .addComponent(
                                                                this.testingPredicateTextField,
                                                                GroupLayout.PREFERRED_SIZE,
                                                                FIELD_PREFERRED_HEIGHT,
                                                                GroupLayout.PREFERRED_SIZE))
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
                new JDialog(owner, UiLocalizer.print("SETTINGS_DIALOG_TITLE"),
                        ModalityType.APPLICATION_MODAL);
        this.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.dialog.setContentPane(this.contentPane);
        this.dialog.pack();
    }

    /**
     * Zobrazí spravovaný dialog.
     */
    public void show() {
        this.dialog.setLocationRelativeTo(this.dialog.getParent());
        this.dialog.setVisible(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.SettingsView#
     * updatedSettings
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.projects.model.Settings)
     */
    @Override
    public void updateSettings(final Settings settings) {
        Preconditions.checkNotNull(settings);

        this.prefixesSettingTableModel.update(settings
                .getNamespacesToPrefixes());
        this.prefixTextField.setText(settings.getPrefix().getText());
        this.pullStateTextField.setText(settings.getPullState().getText());
        this.pullStopStateTextField.setText(settings.getPullStopState()
                .getText());
        this.testingPredicateTextField.setText(settings.getTestingPredicate()
                .getText());
        this.randomizeStateTextField.setText(settings.getRandomizeState()
                .getText());
        this.returnStateTextField.setText(settings.getReturnState().getText());
        this.successStateTextField
                .setText(settings.getSuccessState().getText());
        this.failStateTextField.setText(settings.getFailState().getText());

        updateSize();
    }

    private void updateSize() {
        this.contentPane.revalidate();
        this.dialog.pack();
    }
}
