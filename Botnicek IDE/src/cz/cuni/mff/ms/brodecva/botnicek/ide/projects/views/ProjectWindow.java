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
package cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkPropertiesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.DefaultfSystemOverview;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemPane;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.DefaultSystemTreeModel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.DefaultProjectController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.SettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.BotSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.ConversationSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.LanguageSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.RunController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.RunException;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.BotSettingsDialog;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.ConversationSettingsDialog;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.LanguageSettingsDialog;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.TestPanel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManagerTest;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.JButton;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class ProjectWindow implements ProjectView {

    /**
     * @author Václav Brodec
     * @version 1.0
     */
    private final class RuntimeRunAction extends AbstractAction {
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            try {
                projectController.test();
            } catch (final RunException ex) {
                JOptionPane.showMessageDialog(frame, RUNTIME_ERROR_MESSAGE, RUNTIME_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * @author Václav Brodec
     * @version 1.0
     */
    private final class SettingsOpenAction extends AbstractAction {
        
        public void actionPerformed(final ActionEvent e) {
            projectController.openSettings();
        }
    }

    /**
     * @author Václav Brodec
     * @version 1.0
     */
    private final class NewAction implements ActionListener {
        
        private final SaveAction saveAction;
        
        public NewAction(final SaveAction saveAction) {
            Preconditions.checkNotNull(saveAction);
            
            this.saveAction = saveAction;
        }
        
        public void actionPerformed(final ActionEvent e) {
            final Object nameInput = JOptionPane.showInputDialog(frame, NAME_INPUT_MESSAGE);
            if (nameInput == null) {
                return;
            }
            
            final String name = nameInput.toString();
            if (name.isEmpty()) {
                return;
            }
            
            if (projectController.isOpen()) {
                final int saveConfirmationResult = JOptionPane.showOptionDialog(frame, SAVE_CONFIRMATION_MESSAGE, SAVE_CONFIRMATION_TITLE, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] { SAVE_OPTION, FORFEIT_SAVE_OPTION, CANCEL_SAVE_OPTION }, SAVE_OPTION);
                switch (saveConfirmationResult) {
                case JOptionPane.YES_OPTION:
                    saveAction.actionPerformed(e);
                    break;
                case JOptionPane.NO_OPTION:
                    break;
                case JOptionPane.CLOSED_OPTION:
                    return;
                default:
                    assert false;
                    break;
                }
            }
            
            projectController.createNew(name);
        }
    }

    /**
     * @author Václav Brodec
     * @version 1.0
     */
    private final class ExportAction implements ActionListener {
        public void actionPerformed(final ActionEvent e) {
            final JFileChooser exportDirectoryChooser = new JFileChooser();
            exportDirectoryChooser.setFileFilter(new DirectoryFileFilter());
            final int choice = exportDirectoryChooser.showSaveDialog(frame);
            
            switch (choice) {
            case JFileChooser.APPROVE_OPTION:
                try {
                    projectController.export(exportDirectoryChooser.getSelectedFile().toPath());
                } catch (final IOException ex) {
                    JOptionPane.showMessageDialog(frame, EXPORT_ERROR_MESSAGE, EXPORT_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                }
                break;
            case JFileChooser.CANCEL_OPTION:
                break;
            case JFileChooser.ERROR_OPTION:
                break;
            default:
                assert false;
            }
        }
    }

    /**
     * @author Václav Brodec
     * @version 1.0
     */
    private final class OpenAction implements ActionListener {
        
        private final SaveAction saveAction;
        
        public OpenAction(final SaveAction saveAction) {
            Preconditions.checkNotNull(saveAction);
            
            this.saveAction = saveAction;
        }
        
        public void actionPerformed(final ActionEvent e) {
            final JFileChooser openProjectFileChooser = new JFileChooser();
            openProjectFileChooser.addChoosableFileFilter(new ProjectFileFilter());
            final int choice = openProjectFileChooser.showOpenDialog(frame);
            
            switch (choice) {
            case JFileChooser.APPROVE_OPTION:
                if (projectController.isOpen()) {
                    final int saveConfirmationResult = JOptionPane.showOptionDialog(frame, SAVE_CONFIRMATION_MESSAGE, SAVE_CONFIRMATION_TITLE, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] { SAVE_OPTION, FORFEIT_SAVE_OPTION, CANCEL_SAVE_OPTION }, SAVE_OPTION);
                    switch (saveConfirmationResult) {
                    case JOptionPane.YES_OPTION:
                        this.saveAction.actionPerformed(e);
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        return;
                    case JOptionPane.CLOSED_OPTION:
                        return;
                    default:
                        assert false;
                        break; 
                    }
                }
                
                try {
                    projectController.open(openProjectFileChooser.getSelectedFile().toPath());
                } catch (final FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(frame, OPEN_ERROR_NOT_FOUND_MESSAGE, OPEN_ERROR_NOT_FOUND_TITLE, JOptionPane.ERROR_MESSAGE);
                } catch (final ClassNotFoundException | ClassCastException ex) {
                    JOptionPane.showMessageDialog(frame, OPEN_ERROR_INCOMPATIBLE_MESSAGE, OPEN_ERROR_INCOMPATIBLE_TITLE, JOptionPane.ERROR_MESSAGE);
                } catch (final IOException ex) {
                    JOptionPane.showMessageDialog(frame, OPEN_ERROR_MESSAGE, OPEN_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                }
                break;
            case JFileChooser.CANCEL_OPTION:
                break;
            case JFileChooser.ERROR_OPTION:
                break;
            default:
                assert false;
            }
        }
    }

    /**
     * @author Václav Brodec
     * @version 1.0
     */
    private final class CloseAction extends AbstractAction {
        
        private final SaveAction saveAction;
        
        public CloseAction(final SaveAction saveAction) {
            Preconditions.checkNotNull(saveAction);
            
            this.saveAction = saveAction;
        }
        
        public void actionPerformed(final ActionEvent e) {
            final int saveConfirmationResult = JOptionPane.showOptionDialog(frame, SAVE_CONFIRMATION_MESSAGE, SAVE_CONFIRMATION_TITLE, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] { SAVE_OPTION, FORFEIT_SAVE_OPTION, CANCEL_SAVE_OPTION }, SAVE_OPTION);
            
            switch (saveConfirmationResult) {
            case JOptionPane.YES_OPTION:
                this.saveAction.actionPerformed(e);
                break;
            case JOptionPane.NO_OPTION:
                break;
            case JOptionPane.CANCEL_OPTION:
                return;
            case JOptionPane.CLOSED_OPTION:
                return;
            default:
                assert false;
                break;
            }
            
            projectController.close();
        }
    }

    /**
     * @author Václav Brodec
     * @version 1.0
     */
    private static final class DirectoryFileFilter extends FileFilter {
        private static final String DIRECTORY_FILE_FILTER_DESCRIPTION = "Directories";

        @Override
        public boolean accept(final File file) {
            return file.isDirectory();
        }

        @Override
        public String getDescription() {
            return DIRECTORY_FILE_FILTER_DESCRIPTION;
        }
    }

    /**
     * @author Václav Brodec
     * @version 1.0
     */
    private static final class ProjectFileFilter extends FileFilter {
        
        private final static String PROJECT_FILE_FILTER_DESCRIPTION = "Botníček project files";
        private final static String PROJECT_FILE_EXTENSION = "btk";
        
        @Override
        public boolean accept(final File f) {
            if (f.isDirectory()) {
                return true;
            }

            final String extension = Files.getFileExtension(f.getName());
            if (extension.isEmpty()) {
                return false;
            }
            
            return extension.equals(PROJECT_FILE_EXTENSION);
        }

        @Override
        public String getDescription() {
            return PROJECT_FILE_FILTER_DESCRIPTION;
        }
    }

    private final class SaveAsAction extends AbstractAction {
        
        private Optional<Path> used = Optional.absent();
        
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            final JFileChooser saveProjectFileChooser = new JFileChooser();
            saveProjectFileChooser.addChoosableFileFilter(new ProjectFileFilter());
            final int choice = saveProjectFileChooser.showSaveDialog(frame);
            
            switch (choice) {
                case JFileChooser.APPROVE_OPTION:
                    final Path selected = saveProjectFileChooser.getSelectedFile().toPath();
                    try {
                        projectController.save(selected);
                    } catch (final IOException ex) {
                        JOptionPane.showMessageDialog(frame, SAVE_ERROR_MESSAGE, SAVE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                    }
                    this.used = Optional.of(selected);
                    break;
                case JFileChooser.CANCEL_OPTION:
                    break;
                case JFileChooser.ERROR_OPTION:
                    break;
                default:
                    assert false;
            }
        }
        
        public Path getUsed() {
            return this.used.orNull();
        }
    }
    
    private final class SaveAction extends AbstractAction {
        
        private final SaveAsAction saveAsAction;
        
        public SaveAction(final SaveAsAction saveAsAction) {
            Preconditions.checkNotNull(saveAsAction);
            
            this.saveAsAction = saveAsAction;
        }
        
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            final Optional<Path> used = Optional.fromNullable(this.saveAsAction.getUsed());
            
            if (used.isPresent()) {
                try {
                    projectController.save(used.get());
                } catch (final IOException ex) {
                    JOptionPane.showMessageDialog(frame, SAVE_ERROR_MESSAGE, SAVE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                this.saveAsAction.actionPerformed(e);
            }
        }
    }
    
    private static final int RUNTIME_TAB_INDEX = 1;
    
    private static final String CANCEL_SAVE_OPTION = "Cancel";
    private static final String FORFEIT_SAVE_OPTION = "Forfeit";
    private static final String SAVE_OPTION = "Save";
    private static final String SAVE_CONFIRMATION_TITLE = SAVE_OPTION;
    private static final String SAVE_CONFIRMATION_MESSAGE = "All changes will be lost. Dou you want to save the project?";
    
    private static final String EXPORT_ERROR_MESSAGE = "An I/O error occured during the export.";
    private static final String EXPORT_ERROR_TITLE = "Export failed";
    
    private static final String OPEN_ERROR_NOT_FOUND_MESSAGE = "Loaded project file not found.";
    private static final String OPEN_ERROR_NOT_FOUND_TITLE = "Not found";
    private static final String OPEN_ERROR_INCOMPATIBLE_MESSAGE = "The opening file is not a valid project file.";
    private static final String OPEN_ERROR_INCOMPATIBLE_TITLE = "Incompatible file";
    private static final String OPEN_ERROR_MESSAGE = "Project load failed";
    private static final String OPEN_ERROR_TITLE = "An I/O error occured during the loading.";
    
    private static final String SAVE_ERROR_MESSAGE = "An I/O error occured during the saving.";
    private static final String SAVE_ERROR_TITLE = "Save failed";
    
    private static final Object RUNTIME_ERROR_MESSAGE = "Runtime error. Please restart.";
    private static final String RUNTIME_ERROR_TITLE = "Runtime error";
    
    private static final String NAME_INPUT_MESSAGE = "Enter the new project name.";
    
    private final JFrame frame = new JFrame();
    private final JMenuBar menuBar = new JMenuBar();
    private final JPanel contentPane = new JPanel();
        
    private final JMenu projectMenu = new JMenu("Project");
    private final JMenuItem newMenuItem = new JMenuItem("New");
    private final JMenuItem openMenuItem = new JMenuItem("Open...");
    private final JMenuItem closeMenuItem = new JMenuItem("Close");
    private final JMenuItem saveMenuItem = new JMenuItem("Save");
    private final JMenuItem saveAsMenuItem = new JMenuItem("Save as...");
    private final JMenuItem exportMenuItem = new JMenuItem("Export...");
    private final JMenuItem settingsMenuItem = new JMenuItem("Settings...");
    private final JMenuItem quitMenuItem = new JMenuItem("Quit");
    
    private final JMenu runtimeMenu = new JMenu("Runtime");
    private final JMenuItem testMenuItem = new JMenuItem("Test");
    private final JMenuItem botMenuItem = new JMenuItem("Bot...");
    private final JMenuItem languageMenuItem = new JMenuItem("Language...");
    private final JMenuItem conversationMenuItem = new JMenuItem("Conversation...");
    
    private final JMenu helpMenu = new JMenu("Help");
    private final JMenuItem aboutMenuItem = new JMenuItem("About");
    
    private final JScrollPane networksScrollPane = new JScrollPane();
    private final JScrollPane systemScrollPane = new JScrollPane();
    
    
    private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    private final JScrollPane resultsScrollPane = new JScrollPane();
    private final JPanel runtimePanel = new JPanel();
    private final JButton testButton = new JButton("Test");
    
    private final JSplitPane systemSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.networksScrollPane, this.systemScrollPane);
    private final JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.systemSplitPane, this.tabbedPane);
    
    private final SaveAsAction saveAsAction = new SaveAsAction();
    private final SaveAction saveAction = new SaveAction(this.saveAsAction);
    private final CloseAction closeAction = new CloseAction(this.saveAction);
    private final NewAction newAction = new NewAction(this.saveAction);
    private final OpenAction openAction = new OpenAction(this.saveAction);
    private final ExportAction exportAction = new ExportAction();
    private final SettingsOpenAction settingsOpenAction = new SettingsOpenAction(); 
    private final RuntimeRunAction runtimeRunAction = new RuntimeRunAction();
    
    private final ProjectController projectController;
    
    /**
     * Launch the application.
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (final UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                        //TODO:
                    }
                    
                    final EventManager eventManager = DefaultEventManager.create();
                    final ProjectController projectController = DefaultProjectController.create(eventManager);
                    final ProjectWindow window = ProjectWindow.create(projectController);
                    window.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * 
     */
    protected void show() {
        this.frame.setVisible(true);
    }

    public static ProjectWindow create() {
        return new ProjectWindow(DummyProjectController.create());
    }
    
    public static ProjectWindow create(final ProjectController projectController) {
        final ProjectWindow newInstance = new ProjectWindow(projectController);
        
        projectController.addView(newInstance);
        
        return newInstance;
    }
    
    private ProjectWindow(final ProjectController projectController) {
        Preconditions.checkNotNull(projectController);
        
        this.projectController = projectController;
        
        this.frame.setTitle("Botníček");
        this.frame.setBounds(100, 100, 450, 300);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.frame.setJMenuBar(menuBar);
        
        initializeProjectMenu();
        initializeRuntimeMenu();
        initializeHelpMenu();
                
        this.testButton.addActionListener(this.runtimeRunAction);
        this.runtimePanel.add(testButton);
        
        this.tabbedPane.addTab("Problems", null, this.resultsScrollPane, null);
        this.tabbedPane.addTab("Test", null, this.runtimePanel, null);
        
        setProjectControlsEnabled(false);
        
        this.contentPane.setLayout(new CardLayout());
        this.contentPane.add(this.mainSplitPane);        
        
        testColorize();
        
        this.frame.setContentPane(this.contentPane);
    }
    
    private void testColorize() {
        this.networksScrollPane.setBackground(Color.YELLOW);
        this.systemScrollPane.setBackground(Color.CYAN);
        this.contentPane.setBackground(Color.MAGENTA);
    }

    /**
     * 
     */
    private void initializeHelpMenu() {
        this.menuBar.add(helpMenu);
        this.helpMenu.add(aboutMenuItem);
    }

    /**
     * 
     */
    private void initializeRuntimeMenu() {
        menuBar.add(runtimeMenu);
        
        this.testMenuItem.addActionListener(this.runtimeRunAction);
        this.runtimeMenu.add(testMenuItem);
        
        this.runtimeMenu.addSeparator();
        
        this.botMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                projectController.openBotSettings();
            }
        });
        this.runtimeMenu.add(botMenuItem);
        
        this.languageMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                projectController.openLanguageSettings();
            }
        });
        this.runtimeMenu.add(languageMenuItem);
        
        this.conversationMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                projectController.openConversationSettings();
            }
        });
        this.runtimeMenu.add(conversationMenuItem);
    }

    /**
     * 
     */
    private void initializeProjectMenu() {
        this.newMenuItem.addActionListener(this.newAction);
        this.projectMenu.add(newMenuItem);
        
        this.openMenuItem.addActionListener(this.openAction);
        this.projectMenu.add(openMenuItem);
        
        this.projectMenu.addSeparator();
        
        this.closeMenuItem.addActionListener(this.closeAction);
        this.projectMenu.add(closeMenuItem);
        
        this.projectMenu.addSeparator();
        
        this.saveMenuItem.addActionListener(this.saveAction);
        this.projectMenu.add(saveMenuItem);
        
        this.saveAsMenuItem.addActionListener(this.saveAsAction);
        this.projectMenu.add(saveAsMenuItem);
        
        this.exportMenuItem.addActionListener(this.exportAction);
        this.projectMenu.add(this.exportMenuItem);
        
        this.projectMenu.addSeparator();
        
        this.settingsMenuItem.addActionListener(this.settingsOpenAction);
        this.projectMenu.add(settingsMenuItem);
        
        this.projectMenu.addSeparator();
        
        this.quitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                closeAction.actionPerformed(e);
            }
        });
        this.projectMenu.add(quitMenuItem);
        
        this.menuBar.add(projectMenu);
    }

    @Override
    public void opened(final SystemController systemController, final NetworkPropertiesController networkPropertiesController, final ArcPropertiesController arcPropertiesController) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(systemController);
        Preconditions.checkNotNull(networkPropertiesController);
        Preconditions.checkNotNull(arcPropertiesController);
        
        DefaultfSystemOverview.create(this.networksScrollPane, DefaultSystemTreeModel.create(systemController), systemController, networkPropertiesController);
        
        final SystemPane systemPane = SystemPane.create(networkPropertiesController, arcPropertiesController);
        this.systemScrollPane.setViewportView(systemPane);
        
        setProjectControlsEnabled(true);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.RuntimeView#provided(cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.RunController)
     */
    @Override
    public void provided(final RunController runController) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(runController);
        
        this.tabbedPane.setTabComponentAt(RUNTIME_TAB_INDEX, TestPanel.create(runController));
        this.tabbedPane.setSelectedIndex(RUNTIME_TAB_INDEX);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.ProjectView#settingsOpened(cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.SettingsController)
     */
    @Override
    public void settingsOpened(final SettingsController settingsController) {
        assert SwingUtilities.isEventDispatchThread();
        
        SettingsDialog.create(this.frame, settingsController).show();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.ProjectView#botSettingsOpened(cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.BotSettingsController)
     */
    @Override
    public void botSettingsOpened(final BotSettingsController botSettingsController) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(botSettingsController);  
        
        BotSettingsDialog.create(this.frame, botSettingsController).show();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.ProjectView#languageSettingsOpened(cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.LanguageSettingsController)
     */
    @Override
    public void languageSettingsOpened(
            final LanguageSettingsController languageSettingsController) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(languageSettingsController);
        
        LanguageSettingsDialog.create(this.frame, languageSettingsController).show();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.ProjectView#conversationSettingsOpened(cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.ConversationSettingsController)
     */
    @Override
    public void conversationSettingsOpened(
            final ConversationSettingsController conversationSettingsController) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(conversationSettingsController);
        
        ConversationSettingsDialog.create(this.frame, conversationSettingsController).show();
    }
    
    private void setProjectControlsEnabled(final boolean value) {
        this.saveMenuItem.setEnabled(value);
        this.saveAsMenuItem.setEnabled(value);
        this.exportMenuItem.setEnabled(value);
        this.settingsMenuItem.setEnabled(value);
        this.closeMenuItem.setEnabled(value);
        this.runtimeMenu.setEnabled(value);
        this.testMenuItem.setEnabled(value);
        this.botMenuItem.setEnabled(value);
        this.languageMenuItem.setEnabled(value);
        this.conversationMenuItem.setEnabled(value);
        this.testButton.setEnabled(value);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.ProjectView#closed()
     */
    @Override
    public void closed() {
        assert SwingUtilities.isEventDispatchThread();
        
        setProjectControlsEnabled(false);
        
        this.tabbedPane.setComponentAt(RUNTIME_TAB_INDEX, this.runtimePanel);
    }
}
