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
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.LanguageSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables.DefaultSubstitutionsTableModelFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables.SubstitutionsTableModel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables.SubstitutionsTableModelFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.AIMLLanguageConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

/**
 * Správce dialogu nastavení jazyka robota a běhového prostředí.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class LanguageSettingsDialog implements LanguageSettingsView {

    private static final int CONTENT_PANE_BORDER_SIZE = 5;

    private static final int SET_BUTTON_PREFERRED_HEIGHT = 30;

    private static final int SET_BUTTON_PREFERRED_WIDTH = 158;


    private static final int FIELD_PREFERRED_WIDTH = 186;

    private static final int FIELD_PREFERRED_HEIGHT = 20;

    private static final int LABEL_FIELD_GAP_SIZE = 5;

    private final JDialog dialog;
    private final JPanel contentPane = new JPanel();
    private final GroupLayout contentPaneLayout = new GroupLayout(this.contentPane);
    
    private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

    private final JPanel settingsPane = new JPanel();
    private final GroupLayout settingsLayout = new GroupLayout(this.settingsPane);
    private final JLabel nameLabel = new JLabel(UiLocalizer.print("LANGUAGE_NAME_LABEL_TEXT"));
    private final JTextField nameTextField = new JTextField();
    private final JLabel sentencesDelimLabel = new JLabel(UiLocalizer.print("SENTENCES_DELIMI_LABEL_TEXT"));
    private final JTextField sentencesDelimTextField = new JTextField();
    
    private final JPanel genderPane = new JPanel();
    private final GroupLayout genderLayout = new GroupLayout(this.genderPane);
    private final SubstitutionsTableModel genderSubsTableModel;
    private final JTable genderSubsTable;
    private final JButton addGenderSubButton = new JButton(UiLocalizer.print("Add"));
    
    private final JPanel personPane = new JPanel();
    private final GroupLayout personLayout = new GroupLayout(this.personPane);
    private final SubstitutionsTableModel personSubsTableModel;
    private final JTable personSubsTable;
    private final JButton addPersonSubButton = new JButton(UiLocalizer.print("Add"));
    
    private final JPanel person2Pane = new JPanel();
    private final GroupLayout person2Layout = new GroupLayout(this.person2Pane);
    private final SubstitutionsTableModel person2SubsTableModel;
    private final JTable person2SubsTable;
    private final JButton addPerson2SubButton = new JButton(UiLocalizer.print("Add"));
    
    private final JPanel abbsPane = new JPanel();
    private final GroupLayout abbsLayout = new GroupLayout(this.abbsPane);
    private final SubstitutionsTableModel abbreviationsSubsTableModel;
    private final JTable abbreviationsSubsTable;
    private final JButton addAbbsSubButton = new JButton(UiLocalizer.print("Add"));
    
    private final JPanel spellingPane = new JPanel();
    private final GroupLayout spellingLayout = new GroupLayout(this.spellingPane);
    private final SubstitutionsTableModel spellingSubsTableModel;
    private final JTable spellingSubsTable;
    private final JButton addSpellingSubButton = new JButton(UiLocalizer.print("Add"));
    
    private final JPanel emoPane = new JPanel();
    private final GroupLayout emoLayout = new GroupLayout(this.emoPane);
    private final SubstitutionsTableModel emoticonsSubsTableModel;
    private final JTable emoticonsSubsTable;
    private final JButton addEmoSubButton = new JButton(UiLocalizer.print("Add"));
    
    private final JPanel punctuationPane = new JPanel();
    private final GroupLayout punctuationLayout = new GroupLayout(this.punctuationPane);
    private final SubstitutionsTableModel innerPunctuationSubsTableModel;
    private final JTable innerPunctuationSubsTable;
    private final JButton addPunctuationSubButton = new JButton(UiLocalizer.print("Add"));
    
    private final JButton setButton = new JButton(UiLocalizer.print("Save"));
    

    /**
     * Spustí testovací dialog.
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
                    final LanguageSettingsDialog dialog = LanguageSettingsDialog.create(frame);
                    frame.setVisible(true);
                    dialog.dialog.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Vytvoří správce dialogu, navěsí události na obslužné prvky.
     * 
     * @param owner vlastník dialogu
     * @param languageSettingsController řadič nastavení jazyka
     * @param substitutionsTableModelFactory továrna na modely tabulek substitucí
     * @return správce dialogu
     */
    public static LanguageSettingsDialog create(final Window owner, final LanguageSettingsController languageSettingsController, final SubstitutionsTableModelFactory substitutionsTableModelFactory) {
        final LanguageSettingsDialog newInstance = new LanguageSettingsDialog(owner, substitutionsTableModelFactory);
        
        languageSettingsController.addView(newInstance);
        languageSettingsController.fill(newInstance);
        
        newInstance.addAbbsSubButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                newInstance.abbreviationsSubsTableModel.addRow();
                
                newInstance.contentPane.revalidate();
                newInstance.dialog.pack();
            }
        });
        
        newInstance.addEmoSubButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                newInstance.emoticonsSubsTableModel.addRow();
                
                newInstance.contentPane.revalidate();
                newInstance.dialog.pack();
            }
        });
        
        newInstance.addGenderSubButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                newInstance.genderSubsTableModel.addRow();
                
                newInstance.contentPane.revalidate();
                newInstance.dialog.pack();
            }
        });
        
        newInstance.addPerson2SubButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                newInstance.person2SubsTableModel.addRow();
                
                newInstance.contentPane.revalidate();
                newInstance.dialog.pack();
            }
        });
        
        newInstance.addPersonSubButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                newInstance.personSubsTableModel.addRow();
                
                newInstance.contentPane.revalidate();
                newInstance.dialog.pack();
            }
        });
        
        newInstance.addPunctuationSubButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                newInstance.innerPunctuationSubsTableModel.addRow();
                
                newInstance.contentPane.revalidate();
                newInstance.dialog.pack();
            }
        });
        
        newInstance.addSpellingSubButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                newInstance.spellingSubsTableModel.addRow();
                
                newInstance.contentPane.revalidate();
                newInstance.dialog.pack();
            }
        });
        
        newInstance.setButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                languageSettingsController.set(AIMLLanguageConfiguration.of(
                        newInstance.nameTextField.getText(),
                        Pattern.compile(newInstance.sentencesDelimTextField.getText()),
                        newInstance.genderSubsTableModel.getNamesToValues(),
                        newInstance.personSubsTableModel.getNamesToValues(),
                        newInstance.person2SubsTableModel.getNamesToValues(),
                        newInstance.abbreviationsSubsTableModel.getNamesToValues(),
                        newInstance.spellingSubsTableModel.getNamesToValues(),
                        newInstance.emoticonsSubsTableModel.getNamesToValues(),
                        newInstance.innerPunctuationSubsTableModel.getNamesToValues()));
                } catch (final IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(newInstance.dialog, UiLocalizer.print("SETTINGS_ERROR_MESSAGE"), UiLocalizer.print("SETTINGS_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });
        
        newInstance.dialog.addWindowListener(new WindowAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
             */
            @Override
            public void windowClosed(final WindowEvent e) {
                languageSettingsController.removeView(newInstance);
                
                super.windowClosed(e);
            }
        });
        
        return newInstance;
    }
    
    private static LanguageSettingsDialog create(final Window owner) {
        return create(owner, DummyLanguageSettingsController.create());
    }
    
    /**
     * Vytvoří správce dialogu, navěsí události na obslužné prvky.
     * 
     * @param owner vlastník dialogu
     * @param languageSettingsController řadič nastavení jazyka
     * @return správce dialogu
     */
    public static LanguageSettingsDialog create(final Window owner, final LanguageSettingsController languageSettingsController) {
        return create(owner, languageSettingsController, DefaultSubstitutionsTableModelFactory.create());
    }
    
    /**
     * Create the frame.
     * @param substitutionsTableModelFactory 
     */
    private LanguageSettingsDialog(final Window owner, final SubstitutionsTableModelFactory substitutionsTableModelFactory) {      
        Preconditions.checkNotNull(owner);
        Preconditions.checkNotNull(substitutionsTableModelFactory);
        
        this.abbreviationsSubsTableModel = substitutionsTableModelFactory.produce();
        this.abbreviationsSubsTable = new JTable(this.abbreviationsSubsTableModel);
        
        this.genderSubsTableModel = substitutionsTableModelFactory.produce();
        this.genderSubsTable = new JTable(this.genderSubsTableModel);
        
        this.spellingSubsTableModel = substitutionsTableModelFactory.produce();
        this.spellingSubsTable = new JTable(this.spellingSubsTableModel);
        
        this.emoticonsSubsTableModel = substitutionsTableModelFactory.produce();
        this.emoticonsSubsTable = new JTable(this.emoticonsSubsTableModel);
        
        this.innerPunctuationSubsTableModel = substitutionsTableModelFactory.produce();
        this.innerPunctuationSubsTable = new JTable(this.innerPunctuationSubsTableModel);
        
        this.personSubsTableModel = substitutionsTableModelFactory.produce();
        this.personSubsTable = new JTable(this.personSubsTableModel);
        
        this.person2SubsTableModel = substitutionsTableModelFactory.produce();
        this.person2SubsTable = new JTable(this.person2SubsTableModel);
        
        int tabIndex = 0;
        
        this.nameLabel.setLabelFor(this.nameTextField);
        this.nameLabel.setDisplayedMnemonic(KeyEvent.VK_N);
        this.sentencesDelimLabel.setLabelFor(this.sentencesDelimTextField);
        this.sentencesDelimLabel.setDisplayedMnemonic(KeyEvent.VK_D);
        
        this.settingsLayout.setHorizontalGroup(
                settingsLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(settingsLayout.createSequentialGroup()
                    .addGroup(settingsLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(this.nameLabel)
                        .addComponent(this.sentencesDelimLabel))
                    .addGroup(settingsLayout.createParallelGroup(Alignment.LEADING)
                        .addGap(LABEL_FIELD_GAP_SIZE)
                        .addGap(LABEL_FIELD_GAP_SIZE))
                    .addGroup(settingsLayout.createParallelGroup(Alignment.LEADING)
                            .addComponent(this.nameTextField, GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                            .addComponent(this.sentencesDelimTextField, GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)))
        );
        this.settingsLayout.setVerticalGroup(
                settingsLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(settingsLayout.createSequentialGroup()
                    .addGroup(settingsLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(this.nameLabel)
                        .addComponent(this.nameTextField, GroupLayout.PREFERRED_SIZE, FIELD_PREFERRED_HEIGHT, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(settingsLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(this.sentencesDelimLabel)
                        .addComponent(this.sentencesDelimTextField, GroupLayout.PREFERRED_SIZE, FIELD_PREFERRED_HEIGHT, GroupLayout.PREFERRED_SIZE)))
        );
        this.settingsPane.setLayout(this.settingsLayout);
        this.settingsPane.setBorder(new EmptyBorder(CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE));
        this.tabbedPane.insertTab(UiLocalizer.print("SETTINGS_TAB_TITLE"), Intended.<Icon>nullReference(), this.settingsPane, UiLocalizer.print("LanguageSettingsTabTip"), tabIndex++);
        
        this.genderLayout.setHorizontalGroup(this.genderLayout
                .createParallelGroup(Alignment.LEADING)
                    .addComponent(this.genderSubsTable.getTableHeader(), GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.genderSubsTable, GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.addGenderSubButton,
                        GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH,
                        Short.MAX_VALUE)
        );
        this.genderLayout.setVerticalGroup(this.genderLayout
                .createSequentialGroup()
                    .addComponent(this.genderSubsTable.getTableHeader())
                    .addComponent(this.genderSubsTable)
                    .addComponent(this.addGenderSubButton)
        );
        this.genderPane.setLayout(this.genderLayout);
        this.genderPane.setBorder(new EmptyBorder(CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE));
        this.tabbedPane.insertTab(UiLocalizer.print("GENDER_SUBS_TAB_TITLE"), Intended.<Icon>nullReference(), this.genderPane, UiLocalizer.print("GenderSubsTabTip"), tabIndex++);
        
        this.personLayout.setHorizontalGroup(this.personLayout
                .createParallelGroup(Alignment.LEADING)
                    .addComponent(this.personSubsTable.getTableHeader(), GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.personSubsTable, GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.addPersonSubButton,
                        GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH,
                        Short.MAX_VALUE)
        );
        this.personLayout.setVerticalGroup(this.personLayout
                .createSequentialGroup()
                    .addComponent(this.personSubsTable.getTableHeader())
                    .addComponent(this.personSubsTable)
                    .addComponent(this.addPersonSubButton)
        );
        this.personPane.setLayout(this.personLayout);
        this.personPane.setBorder(new EmptyBorder(CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE));
        this.tabbedPane.insertTab(UiLocalizer.print("PERSON_SUBS_TAB_TITLE"), Intended.<Icon>nullReference(), this.personPane, UiLocalizer.print("PersonSubsTabTip"), tabIndex++);
        
        this.person2Layout.setHorizontalGroup(this.person2Layout
                .createParallelGroup(Alignment.LEADING)
                    .addComponent(this.person2SubsTable.getTableHeader(), GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.person2SubsTable, GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.addPerson2SubButton,
                        GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH,
                        Short.MAX_VALUE)
        );
        this.person2Layout.setVerticalGroup(this.person2Layout
                .createSequentialGroup()
                    .addComponent(this.person2SubsTable.getTableHeader())
                    .addComponent(this.person2SubsTable)
                    .addComponent(this.addPerson2SubButton)
        );
        this.person2Pane.setLayout(this.person2Layout);
        this.person2Pane.setBorder(new EmptyBorder(CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE));
        this.tabbedPane.insertTab(UiLocalizer.print("PERSON2_SUBS_TAB_TITLE"), Intended.<Icon>nullReference(), this.person2Pane, UiLocalizer.print("Person2SubsTabTip"), tabIndex++);
        
        this.abbsLayout.setHorizontalGroup(this.abbsLayout
                .createParallelGroup(Alignment.LEADING)
                    .addComponent(this.abbreviationsSubsTable.getTableHeader(), GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.abbreviationsSubsTable, GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.addAbbsSubButton,
                        GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH,
                        Short.MAX_VALUE)
        );
        this.abbsLayout.setVerticalGroup(this.abbsLayout
                .createSequentialGroup()
                    .addComponent(this.abbreviationsSubsTable.getTableHeader())
                    .addComponent(this.abbreviationsSubsTable)
                    .addComponent(this.addAbbsSubButton)
        );
        this.abbsPane.setLayout(this.abbsLayout);
        this.abbsPane.setBorder(new EmptyBorder(CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE));
        this.tabbedPane.insertTab(UiLocalizer.print("ABBS_SUBS_TAB_TITLE"), Intended.<Icon>nullReference(), this.abbsPane, UiLocalizer.print("AbbsSubsTabTip"), tabIndex++);
        
        this.spellingLayout.setHorizontalGroup(this.spellingLayout
                .createParallelGroup(Alignment.LEADING)
                    .addComponent(this.spellingSubsTable.getTableHeader(), GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.spellingSubsTable, GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.addSpellingSubButton,
                        GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH,
                        Short.MAX_VALUE)
        );
        this.spellingLayout.setVerticalGroup(this.spellingLayout
                .createSequentialGroup()
                    .addComponent(this.spellingSubsTable.getTableHeader())
                    .addComponent(this.spellingSubsTable)
                    .addComponent(this.addSpellingSubButton)
        );
        this.spellingPane.setLayout(this.spellingLayout);
        this.spellingPane.setBorder(new EmptyBorder(CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE));
        this.tabbedPane.insertTab(UiLocalizer.print("SPELLING_SUBS_TAB_TITLE"), Intended.<Icon>nullReference(), this.spellingPane, UiLocalizer.print("SpellingSubsTabTip"), tabIndex++);
        
        this.emoLayout.setHorizontalGroup(this.emoLayout
                .createParallelGroup(Alignment.LEADING)
                    .addComponent(this.emoticonsSubsTable.getTableHeader(), GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.emoticonsSubsTable, GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.addEmoSubButton,
                        GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH,
                        Short.MAX_VALUE)
        );
        this.emoLayout.setVerticalGroup(this.emoLayout
                .createSequentialGroup()
                    .addComponent(this.emoticonsSubsTable.getTableHeader())
                    .addComponent(this.emoticonsSubsTable)
                    .addComponent(this.addEmoSubButton)
        );
        this.emoPane.setLayout(this.emoLayout);
        this.emoPane.setBorder(new EmptyBorder(CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE));
        this.tabbedPane.insertTab(UiLocalizer.print("EMOTICONS_SUBS_TAB_TITLE"), Intended.<Icon>nullReference(), this.emoPane, UiLocalizer.print("EmoSubsTabTip"), tabIndex++);
        
        this.punctuationLayout.setHorizontalGroup(this.punctuationLayout
                .createParallelGroup(Alignment.LEADING)
                    .addComponent(this.innerPunctuationSubsTable.getTableHeader(), GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.innerPunctuationSubsTable, GroupLayout.DEFAULT_SIZE,
                            FIELD_PREFERRED_WIDTH, Short.MAX_VALUE)
                    .addComponent(this.addPunctuationSubButton,
                        GroupLayout.DEFAULT_SIZE, FIELD_PREFERRED_WIDTH,
                        Short.MAX_VALUE)
        );
        this.punctuationLayout.setVerticalGroup(this.punctuationLayout
                .createSequentialGroup()
                    .addComponent(this.innerPunctuationSubsTable.getTableHeader())
                    .addComponent(this.innerPunctuationSubsTable)
                    .addComponent(this.addPunctuationSubButton)
        );
        this.punctuationPane.setLayout(this.punctuationLayout);
        this.punctuationPane.setBorder(new EmptyBorder(CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE));
        this.tabbedPane.insertTab(UiLocalizer.print("PUNCTUATION_SUBS_TAB_TITLE"), Intended.<Icon>nullReference(), this.punctuationPane, UiLocalizer.print("PunctuationSubsTabTip"), tabIndex++);
        
        this.contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(this.tabbedPane)
                    .addGroup(contentPaneLayout.createSequentialGroup()
                        .addContainerGap(0, Short.MAX_VALUE)
                        .addComponent(this.setButton, GroupLayout.PREFERRED_SIZE, SET_BUTTON_PREFERRED_WIDTH, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(0, Short.MAX_VALUE))
        );
        this.contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addComponent(this.tabbedPane)
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addComponent(this.setButton, GroupLayout.PREFERRED_SIZE, SET_BUTTON_PREFERRED_HEIGHT, GroupLayout.PREFERRED_SIZE))
        );
        this.contentPane.setLayout(this.contentPaneLayout);
        this.contentPane.setBorder(new EmptyBorder(CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE, CONTENT_PANE_BORDER_SIZE));
        
        this.dialog = new JDialog(owner, UiLocalizer.print("LANGUAGE_SETTINGS_TITLE"), ModalityType.APPLICATION_MODAL);
        this.dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.dialog.setContentPane(this.contentPane);
        this.dialog.pack();
    }


    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.LanguageSettingsView#updatedLanguageConfiguration(cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration)
     */
    @Override
    public void updateLanguageConfiguration(
            LanguageConfiguration languageConfiguration) {
        Preconditions.checkNotNull(languageConfiguration);
        
        this.nameTextField.setText(languageConfiguration.getName());
        this.sentencesDelimTextField.setText(languageConfiguration.getSentenceDelim().toString());
        this.genderSubsTableModel.update(languageConfiguration.getGenderSubs());
        this.personSubsTableModel.update(languageConfiguration.getPersonSubs());
        this.person2SubsTableModel.update(languageConfiguration.getPerson2Subs());
        this.abbreviationsSubsTableModel.update(languageConfiguration.getAbbreviationsSubs());
        this.spellingSubsTableModel.update(languageConfiguration.getSpellingSubs());
        this.emoticonsSubsTableModel.update(languageConfiguration.getEmoticonsSubs());
        this.innerPunctuationSubsTableModel.update(languageConfiguration.getInnerPunctuationSubs());
    }

    /**
     * 
     */
    public void show() {
        this.dialog.setVisible(true);
    }    
}
