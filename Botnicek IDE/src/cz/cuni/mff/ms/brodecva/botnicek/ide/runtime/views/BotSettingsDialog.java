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

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import com.google.common.base.Preconditions;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.BotSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables.DefaultPredicatesTableModelFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables.PredicatesTableModel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables.PredicatesTableModelFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.Components;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.draglist.DragOrderableList;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.AIMLBotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Paths;

/**
 * Správce dialogu a nastavením bota.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class BotSettingsDialog implements BotSettingsView {

    private static final int FIELD_PREFERRED_WIDTH = 266;
    private static final int LABEL_FIELD_GAP_SIZE = 5;
    private static final int SET_BUTTON_PREFERRED_WIDTH = 158;
    private static final int FIELD_PREFERRED_HEIGHT = 20;
    private static final int SET_BUTTON_PREFERRED_HEIGHT = 30;
    private static final int CONTENT_PANE_BORDER_SIZE = 5;

    private final JDialog dialog;
    private final JPanel contentPane = new JPanel();
    private final GroupLayout contentPaneLayout = new GroupLayout(this.contentPane);

    private final JLabel nameLabel = new JLabel(UiLocalizer.print("BotName"));
    private final JTextField nameTextField = new JTextField();
    
    private final JLabel filesLocationLabel = new JLabel(UiLocalizer.print("PathToFiles"));
    private final JTextField filesLocationTextField = new JTextField();
    
    private final JLabel gossipPathLabel = new JLabel(UiLocalizer.print("PathToGossips"));
    private final JTextField gossipPathTextField = new JTextField();
    
    private final JLabel beforeLoadingOrderLabel = new JLabel(UiLocalizer.print("BeforeLoadedFiles"));
    private final DragOrderableList<String> beforeLoadingOrderList = DragOrderableList.create();
    private final JButton addBeforeLoadingOrderItemButton = new JButton(UiLocalizer.print("AddStart"));
    
    private final JLabel afterLoadingOrderLabel = new JLabel(UiLocalizer.print("AfterLoadedFiles"));
    private final DragOrderableList<String> afterLoadingOrderList = DragOrderableList.create();
    private final JButton addAfterLoadingOrderItemButton = new JButton(UiLocalizer.print("AddStart"));
    
    private final JLabel botPredicatesLabel = new JLabel(UiLocalizer.print("DefaultBotPredicates"));
    private final PredicatesTableModel predicatesTableModel;
    private final JTable botPredicatesTable;
    private final JButton addBotPredicateButton = new JButton(UiLocalizer.print("Add"));
    
    private final JButton setButton = new JButton(UiLocalizer.print("Save"));
    
    /**
     * Spustí testovací verzi dialogu.
     * 
     * @param args argumenty
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    
                    final JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    
                    final BotSettingsDialog dialog = BotSettingsDialog.create(frame);
                    
                    frame.setVisible(true);
                    dialog.dialog.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Vytvoří a vyplní dialog, navěsí na něj obslužné metody.
     * 
     * @param owner vlastník dialogu
     * @param botSettingsController řadič nastavení bota
     * @param predicatesTableModelFactory továrna na modely tabulky predikátů
     * @return správce dialogu
     */
    public static BotSettingsDialog create(final Window owner, final BotSettingsController botSettingsController, final PredicatesTableModelFactory predicatesTableModelFactory) {
        Preconditions.checkNotNull(botSettingsController);
        Preconditions.checkNotNull(predicatesTableModelFactory);
        
        final BotSettingsDialog newInstance = new BotSettingsDialog(owner, predicatesTableModelFactory.produce());
        
        botSettingsController.addView(newInstance);
        botSettingsController.fill(newInstance);
        
        newInstance.beforeLoadingOrderList.addKeyListener(new KeyAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
             */
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    newInstance.beforeLoadingOrderList.removeSelected();
                }
            }
        });
        newInstance.afterLoadingOrderList.addKeyListener(new KeyAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
             */
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    newInstance.afterLoadingOrderList.removeSelected();
                }
            }
        });
        
        newInstance.addBotPredicateButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                newInstance.predicatesTableModel.addRow();
                
                newInstance.contentPane.revalidate();
                newInstance.dialog.pack();
            }
        });
        
        newInstance.addBeforeLoadingOrderItemButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String input = JOptionPane.showInputDialog(newInstance.dialog, UiLocalizer.print("LOADING_ORDER_NEW_ITEM_MESSAGE"), UiLocalizer.print("LOADING_ORDER_NEW_ITEM_TITLE"), JOptionPane.QUESTION_MESSAGE);
                if (Components.hasUserCanceledInput(input) || input.isEmpty()) {
                    return;
                }
                
                newInstance.beforeLoadingOrderList.addElement(input);
                
                newInstance.contentPane.revalidate();
                newInstance.dialog.pack();
            }
        });
        
        newInstance.addAfterLoadingOrderItemButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String input = JOptionPane.showInputDialog(newInstance.dialog, UiLocalizer.print("LOADING_ORDER_NEW_ITEM_MESSAGE"), UiLocalizer.print("LOADING_ORDER_NEW_ITEM_TITLE"), JOptionPane.QUESTION_MESSAGE);
                if (Components.hasUserCanceledInput(input) || input.isEmpty()) {
                    return;
                }
                
                newInstance.afterLoadingOrderList.addElement(input);
                
                newInstance.contentPane.revalidate();
                newInstance.dialog.pack();
            }
        });
        
        newInstance.setButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                try {
                    botSettingsController.set(AIMLBotConfiguration.of(
                            newInstance.nameTextField.getText(),
                            Paths.get(newInstance.filesLocationTextField.getText()),
                            Paths.get(newInstance.gossipPathTextField.getText()),
                            NormalWords.toUntyped(newInstance.predicatesTableModel.getNamesToValues()),
                            newInstance.beforeLoadingOrderList.getValues(),
                            newInstance.afterLoadingOrderList.getValues()));
                } catch (final IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(newInstance.dialog, UiLocalizer.print("BOT_SETTINGS_ERROR_MESSAGE"), UiLocalizer.print("BOT_SETTINGS_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });
        
        newInstance.dialog.addWindowListener(new WindowAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
             */
            @Override
            public void windowClosed(WindowEvent e) {
                botSettingsController.removeView(newInstance);
                
                super.windowClosed(e);
            }
        });
        
        return newInstance;
    }
    
    /**
     * Vytvoří a vyplní dialog, navěsí na něj obslužné metody.
     * 
     * @param owner vlastník dialogu
     * @param botSettingsController řadič nastavení bota
     * @return správce dialogu
     */
    public static BotSettingsDialog create(final Window owner, final BotSettingsController botSettingsController) {
        return create(owner, botSettingsController, DefaultPredicatesTableModelFactory.create());
    }
    
    private static BotSettingsDialog create(final Window owner) {
        return create(owner, DummyBotSettingsController.create());
    }
    
    private BotSettingsDialog(final Window owner, final PredicatesTableModel predicatesTableModel) {
        Preconditions.checkNotNull(owner);
        Preconditions.checkNotNull(predicatesTableModel);
        
        this.predicatesTableModel = predicatesTableModel;
        this.botPredicatesTable = new JTable(predicatesTableModel);
                
        this.nameLabel.setLabelFor(this.nameTextField);
        this.nameLabel.setDisplayedMnemonic(KeyEvent.VK_N);
        
        this.filesLocationLabel.setLabelFor(this.filesLocationTextField);
        this.filesLocationLabel.setDisplayedMnemonic(KeyEvent.VK_F);
        
        this.gossipPathLabel.setLabelFor(this.gossipPathTextField);
        this.gossipPathLabel.setDisplayedMnemonic(KeyEvent.VK_G);
        
        this.beforeLoadingOrderLabel.setLabelFor(this.beforeLoadingOrderList);
        this.beforeLoadingOrderLabel.setDisplayedMnemonic(KeyEvent.VK_B);
        
        this.afterLoadingOrderLabel.setLabelFor(this.afterLoadingOrderList);
        this.afterLoadingOrderLabel.setDisplayedMnemonic(KeyEvent.VK_A);
        
        this.botPredicatesLabel.setLabelFor(this.botPredicatesTable);
        this.botPredicatesLabel.setDisplayedMnemonic(KeyEvent.VK_P);
        
        this.contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup(Alignment.LEADING)
                    .addGroup(contentPaneLayout.createSequentialGroup()
                        .addGroup(contentPaneLayout.createParallelGroup(Alignment.TRAILING)
                            .addComponent(this.nameLabel)
                            .addComponent(this.filesLocationLabel)
                            .addComponent(this.gossipPathLabel)
                            .addComponent(this.beforeLoadingOrderLabel)
                            .addComponent(this.afterLoadingOrderLabel)
                            .addComponent(this.botPredicatesLabel))
                        .addGroup(contentPaneLayout.createParallelGroup(Alignment.LEADING)
                            .addGap(LABEL_FIELD_GAP_SIZE)
                            .addGap(LABEL_FIELD_GAP_SIZE)
                            .addGap(LABEL_FIELD_GAP_SIZE)
                            .addGap(LABEL_FIELD_GAP_SIZE)
                            .addGap(LABEL_FIELD_GAP_SIZE))
                        .addGroup(contentPaneLayout.createParallelGroup(Alignment.LEADING)
                            .addComponent(this.nameTextField, GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                            .addComponent(this.filesLocationTextField, GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                            .addComponent(this.gossipPathTextField, GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                            .addGroup(contentPaneLayout.createParallelGroup(Alignment.TRAILING)
                                    .addComponent(this.beforeLoadingOrderList, GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                                    .addComponent(this.addBeforeLoadingOrderItemButton, GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH, Short.MAX_VALUE))
                            .addGroup(contentPaneLayout.createParallelGroup(Alignment.TRAILING)
                                    .addComponent(this.afterLoadingOrderList, GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                                    .addComponent(this.addAfterLoadingOrderItemButton, GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH, Short.MAX_VALUE))
                            .addGroup(contentPaneLayout.createParallelGroup(Alignment.TRAILING)
                                    .addComponent(this.botPredicatesTable.getTableHeader(), GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                                    .addComponent(this.botPredicatesTable, GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                                    .addComponent(this.addBotPredicateButton, GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH, Short.MAX_VALUE))))
                    .addGroup(contentPaneLayout.createSequentialGroup()
                        .addContainerGap(0, Short.MAX_VALUE)
                        .addComponent(this.setButton, GroupLayout.PREFERRED_SIZE, SET_BUTTON_PREFERRED_WIDTH, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(0, Short.MAX_VALUE))
            );
        this.contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGroup(contentPaneLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(this.nameLabel)
                        .addComponent(this.nameTextField, GroupLayout.PREFERRED_SIZE, FIELD_PREFERRED_HEIGHT, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(this.filesLocationLabel)
                        .addComponent(this.filesLocationTextField, GroupLayout.PREFERRED_SIZE, FIELD_PREFERRED_HEIGHT, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(this.gossipPathLabel)
                        .addComponent(this.gossipPathTextField, GroupLayout.PREFERRED_SIZE, FIELD_PREFERRED_HEIGHT, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(this.beforeLoadingOrderLabel)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(beforeLoadingOrderList)
                            .addComponent(addBeforeLoadingOrderItemButton)))
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(this.afterLoadingOrderLabel)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(afterLoadingOrderList)
                            .addComponent(addAfterLoadingOrderItemButton)))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(this.botPredicatesLabel)
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(this.botPredicatesTable.getTableHeader())
                            .addComponent(this.botPredicatesTable)
                            .addComponent(this.addBotPredicateButton)))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addComponent(this.setButton, GroupLayout.PREFERRED_SIZE, SET_BUTTON_PREFERRED_HEIGHT, GroupLayout.PREFERRED_SIZE))
        );
        this.contentPane.setLayout(this.contentPaneLayout);
        this.contentPane.setBorder(new EmptyBorder(CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE));
        
        this.dialog = new JDialog(owner, UiLocalizer.print("BOT_SETTINGS_TITLE"), ModalityType.APPLICATION_MODAL);
        this.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.dialog.setContentPane(this.contentPane);
        this.dialog.pack();
    }


    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.BotSettingsView#updatedBotConfiguration(cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration)
     */
    @Override
    public void updateBotConfiguration(final BotConfiguration botConfiguration) {
        Preconditions.checkNotNull(botConfiguration);
        
        this.nameTextField.setText(botConfiguration.getName());        
        this.predicatesTableModel.update(NormalWords.toTyped(botConfiguration.getPredicates()));
        this.filesLocationTextField.setText(botConfiguration.getFilesLocation().toString());
        this.gossipPathTextField.setText(botConfiguration.getGossipPath().toString());        
        this.beforeLoadingOrderList.setDefaultModel(botConfiguration.getBeforeLoadingOrder());
        this.afterLoadingOrderList.setDefaultModel(botConfiguration.getAfterLoadingOrder());        
    }

    /**
     * Zobrazí dialog.
     */
    public void show() {
        this.dialog.setVisible(true);
    }
}
