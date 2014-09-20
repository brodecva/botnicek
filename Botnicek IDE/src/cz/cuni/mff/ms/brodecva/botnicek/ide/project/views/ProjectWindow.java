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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.views;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.io.Files;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.ResultsTable;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemOverview;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemPane;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.controllers.DefaultProjectController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.controllers.ProjectController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.controllers.SettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.BotSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.ConversationSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.LanguageSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.RunController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.RunException;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.BotSettingsDialog;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.ConversationSettingsDialog;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.LanguageSettingsDialog;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.TestPanel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.DefaultEventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.logging.LocalizedLogger;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.Components;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.Localization;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.OverwriteCheckingFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.filechooser.FileFilter;

/**
 * <p>
 * Hlavní okno.
 * </p>
 * <p>
 * Obsahuje hlavní menu s možnostmi pro správu a nastavení projektu a volbami
 * testovacího běhového prostředí. Dále pak vlevo panel se strukturou projektu,
 * potažmo systému a sítí, jež obsahuje. Většinu okna vyplňuje pracovní plocha
 * pro práci se sítěmi a hranami v nich. Na spodu se nachází přepínatelný panel
 * se zobrazením chyb v upravovaných hranách a okno pro konverzaci s botem.
 * </p>
 * <p>
 * Tato třída též obsluhuje některé uživatelské události spojené se správou a testováním projektu.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ProjectWindow implements ProjectView {

    private final class RuntimeRunAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public RuntimeRunAction(final String name, final Icon icon) {
            super(name, icon);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            try {
                projectController.test();
            } catch (final RunException ex) {
                JOptionPane.showMessageDialog(frame, UiLocalizer.print("RUNTIME_ERROR_MESSAGE"),
                        UiLocalizer.print("RUNTIME_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private final class SettingsOpenAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public SettingsOpenAction(final String name, final Icon icon) {
            super(name, icon);
        }

        public void actionPerformed(final ActionEvent e) {
            projectController.openSettings();
        }
    }
    
    private final class NewAction extends AbstractAction {

        private static final long serialVersionUID = 1L;
        
        public NewAction(final String name, final Icon icon) {
            super(name, icon);
        }

        public void actionPerformed(final ActionEvent e) {
            final Object nameInput =
                    JOptionPane.showInputDialog(frame, UiLocalizer.print("NAME_INPUT_MESSAGE"), UiLocalizer.print("NAME_INPUT_TITLE"), JOptionPane.PLAIN_MESSAGE);
            if (Components.hasUserCanceledInput(nameInput)) {
                return;
            }

            final String name = nameInput.toString();
            if (name.isEmpty()) {
                return;
            }

            if (!continueAfterClosing(e)) {
                return;
            }
            
            try {
                projectController.createNew(name);
            } catch (final IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(Intended.<Component>nullReference(),
                        UiLocalizer.print("ProjectNameImpossibleMessage"),
                                UiLocalizer.print("ProjectNameImpossibleTitle"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private final class ExportAction extends AbstractAction {
        
        private static final long serialVersionUID = 1L;

        public ExportAction(final String name, final Icon icon) {
            super(name, icon);
        }

        public void actionPerformed(final ActionEvent e) {
            final JFileChooser exportDirectoryChooser = new JFileChooser();
            exportDirectoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            exportDirectoryChooser.setFileFilter(new DirectoryFileFilter());
            exportDirectoryChooser.setAcceptAllFileFilterUsed(false);
            final int choice = exportDirectoryChooser.showSaveDialog(frame);

            switch (choice) {
            case JFileChooser.APPROVE_OPTION:
                try {
                    projectController.export(exportDirectoryChooser
                            .getSelectedFile().toPath());
                } catch (final IOException ex) {
                    JOptionPane.showMessageDialog(frame, UiLocalizer.print("EXPORT_ERROR_MESSAGE"),
                            UiLocalizer.print("EXPORT_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
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

    private final class OpenAction extends AbstractAction {

        private static final long serialVersionUID = 1L;
        
        private final SaveAction saveAction;

        public OpenAction(final SaveAction saveAction, final String name, final Icon icon) {
            super(name, icon);
            
            Preconditions.checkNotNull(saveAction);

            this.saveAction = saveAction;
        }

        public void actionPerformed(final ActionEvent e) {
            final JFileChooser openProjectFileChooser = new JFileChooser();
            openProjectFileChooser.setLocale(Locale.getDefault());
            openProjectFileChooser.updateUI();
            
            openProjectFileChooser
                    .setFileFilter(new ProjectFileFilter());
            
            final int choice = openProjectFileChooser.showOpenDialog(frame);

            switch (choice) {
            case JFileChooser.APPROVE_OPTION:
                if (!continueAfterClosing(e)) {
                    return;
                }

                try {
                    final Path openedPath = openProjectFileChooser
                    .getSelectedFile().toPath();
                    
                    projectController.open(openedPath);
                    this.saveAction.setUsed(openedPath);
                } catch (final FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(frame,
                            UiLocalizer.print("OPEN_ERROR_NOT_FOUND_MESSAGE"),
                                    UiLocalizer.print("OPEN_ERROR_NOT_FOUND_TITLE"),
                            JOptionPane.ERROR_MESSAGE);
                } catch (final ClassNotFoundException | ClassCastException ex) {
                    JOptionPane.showMessageDialog(frame,
                            UiLocalizer.print("OPEN_ERROR_INCOMPATIBLE_MESSAGE"),
                                    UiLocalizer.print("OPEN_ERROR_INCOMPATIBLE_TITLE"),
                            JOptionPane.ERROR_MESSAGE);
                } catch (final IOException ex) {
                    JOptionPane.showMessageDialog(frame, UiLocalizer.print("OPEN_ERROR_MESSAGE"),
                            UiLocalizer.print("OPEN_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
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

    private final class CloseAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public CloseAction(final String name, final Icon icon) {
            super(name, icon);
        }

        public void actionPerformed(final ActionEvent e) {
            continueAfterClosing(e);
        }
    }
    
    private final class ExitAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public ExitAction(final String name, final Icon icon) {
            super(name, icon);
        }

        public void actionPerformed(final ActionEvent e) {
            if (!continueAfterClosing(e)) {
                return;
            }
            
            frame.dispose();
        }
    }

    private final class SaveAsAction extends AbstractAction {

        private static final long serialVersionUID = 1L;
        
        private Optional<Path> used = Optional.absent();

        public SaveAsAction(final String name, final Icon icon) {
            super(name, icon);
        }
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            final FileFilter botnicekFileFilter = new ProjectFileFilter();
            
            final JFileChooser saveProjectFileChooser = OverwriteCheckingFileChooser.create();
            saveProjectFileChooser
                    .setFileFilter(botnicekFileFilter);
            final int choice = saveProjectFileChooser.showSaveDialog(frame);

            switch (choice) {
            case JFileChooser.APPROVE_OPTION:
                final Path saved =
                        save(saveProjectFileChooser, botnicekFileFilter);
                
                setUsed(saved);
                break;
            case JFileChooser.CANCEL_OPTION:
                break;
            case JFileChooser.ERROR_OPTION:
                break;
            default:
                assert false;
            }
        }

        private Path save(final JFileChooser saveProjectFileChooser,
                final FileFilter projectFileFilter) {
            final Path selected =
                    saveProjectFileChooser.getSelectedFile().toPath();
            final FileFilter usedFileFilter = saveProjectFileChooser.getFileFilter();
            
            try {
                final Path created =
                        createSavePath(selected, usedFileFilter,
                                projectFileFilter);
                
                projectController.save(created);
            } catch (final IOException ex) {
                JOptionPane.showMessageDialog(frame, UiLocalizer.print("SAVE_ERROR_MESSAGE"),
                        UiLocalizer.print("SAVE_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
            }
            return selected;
        }

        private Path createSavePath(final Path selected,
                final FileFilter usedFileFilter,
                final FileFilter projectFilesFilter) {
            final String selectedFileName = selected.getFileName().toString();
            
            if (usedFileFilter != projectFilesFilter || Files.getFileExtension(selectedFileName).equals(ProjectFileFilter.PROJECT_FILE_EXTENSION)) {
                return selected;
            }
            
            final String extensionAppendedFileName = selectedFileName.concat(ProjectFileFilter.EXTENSION_SEPARATOR + ProjectFileFilter.PROJECT_FILE_EXTENSION);
            return selected.resolveSibling(extensionAppendedFileName);
        }
        
        public Path getUsed() {
            return this.used.orNull();
        }

        public void setUsed(final Path used) {
            Preconditions.checkNotNull(used);
            
            this.used = Optional.of(used);
        }

        public void clearUsed() {
            this.used = Optional.absent();
        }
    }

    private final class SaveAction extends AbstractAction {

        private static final long serialVersionUID = 1L;
        private final SaveAsAction saveAsAction;

        public SaveAction(final SaveAsAction saveAsAction, final String name, final Icon icon) {
            super(name, icon);
            
            Preconditions.checkNotNull(saveAsAction);

            this.saveAsAction = saveAsAction;
        }

        public void setUsed(final Path used) {
            Preconditions.checkNotNull(used);
            
            this.saveAsAction.setUsed(used);
        }
        
        public void clearUsed() {
            this.saveAsAction.clearUsed();
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            final Optional<Path> used =
                    Optional.fromNullable(this.saveAsAction.getUsed());

            if (used.isPresent()) {
                try {
                    projectController.save(used.get());
                } catch (final IOException ex) {
                    JOptionPane.showMessageDialog(frame, UiLocalizer.print("SAVE_ERROR_MESSAGE"),
                            UiLocalizer.print("SAVE_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                this.saveAsAction.actionPerformed(e);
            }
        }
    }

    private final static Logger LOGGER = LocalizedLogger.getLogger(ProjectWindow.class);
    
    private static final String BOTNICEK_ICON_NAME = "botnicekIcon";
    private static final BiMap<String, String> iconNamesToPaths = ImmutableBiMap.<String, String>builder().put(BOTNICEK_ICON_NAME, "images/botnicek.png").put("newIcon", "images/page.png").put("saveIcon", "images/disk.png").put("saveAsIcon", "images/disk_edit.png").put("openIcon", "images/folder_page.png").put("closeIcon", "images/cross.png").put("exitIcon", "images/control_power.png").put("exportIcon", "images/folder_go.png").put("settingsIcon", "images/cog.png").put("testIcon", "images/application_go.png").put("aboutIcon", "images/information.png").build();
    
    private static final int RUNTIME_TAB_INDEX = 1;
    private static final int TEST_BUTTON_WIDTH = 260;
    private static final int TEST_BUTTON_HEIGHT = 40;    

    private final JFrame frame = new JFrame();
    private final JMenuBar menuBar = new JMenuBar();
    private final JPanel contentPane = new JPanel();

    private final JMenu projectMenu = new JMenu(UiLocalizer.print("Project"));
    private final JMenuItem newMenuItem = new JMenuItem();
    private final JMenuItem openMenuItem = new JMenuItem();
    private final JMenuItem closeMenuItem = new JMenuItem();
    private final JMenuItem saveMenuItem = new JMenuItem();
    private final JMenuItem saveAsMenuItem = new JMenuItem();
    private final JMenuItem exportMenuItem = new JMenuItem();
    private final JMenuItem settingsMenuItem = new JMenuItem();
    private final JMenuItem quitMenuItem = new JMenuItem();

    private final JMenu runtimeMenu = new JMenu(UiLocalizer.print("Runtime"));
    private final JMenuItem testMenuItem = new JMenuItem();
    private final JMenuItem botMenuItem = new JMenuItem(UiLocalizer.print("Bot"));
    private final JMenuItem languageMenuItem = new JMenuItem(UiLocalizer.print("Language"));
    private final JMenuItem conversationMenuItem = new JMenuItem(
            UiLocalizer.print("Conversation"));

    private final JMenu helpMenu = new JMenu(UiLocalizer.print("Help"));
    private final JMenuItem aboutMenuItem = new JMenuItem(UiLocalizer.print("About"));

    private final JScrollPane networksScrollPane = new JScrollPane();
    private final JPanel networksAndArcPropertiesPane = new JPanel(new CardLayout());
    
    private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    private final JScrollPane resultsScrollPane = new JScrollPane();
    private final JPanel runtimePanel = new JPanel();
    private final JButton testButton = new JButton(UiLocalizer.print("Test"));

    private final JSplitPane systemSplitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT, this.networksScrollPane,
            this.networksAndArcPropertiesPane);
    private final JSplitPane mainSplitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT, this.systemSplitPane, this.tabbedPane);

    private final SaveAsAction saveAsAction;
    private final SaveAction saveAction;
    private final CloseAction closeAction;
    private final ExitAction exitAction;
    private final NewAction newAction;
    private final OpenAction openAction;
    private final ExportAction exportAction;
    private final SettingsOpenAction settingsOpenAction;
    private final RuntimeRunAction runtimeRunAction;

    private final ProjectController projectController;
    
    private final BiMap<String, ImageIcon> icons;

    /**
     * Spustí aplikaci.
     * 
     * @param args argumenty
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (final UnsupportedLookAndFeelException
                        | ClassNotFoundException | InstantiationException
                        | IllegalAccessException e) {
                    LOGGER.severe("LookAndFeelNotAvailable");
                    System.exit(1);
                }
                
                Localization.localize();
                
                final EventManager eventManager =
                        DefaultEventManager.create();
                final ProjectController projectController =
                        DefaultProjectController.create(eventManager);
                final ProjectWindow window =
                        ProjectWindow.create(projectController);
                window.show();
            }
        });
    }

    private void show() {
        this.frame.setVisible(true);
    }

    /**
     * Vytvoří prázdné testovací okno.
     * 
     * @return testovací okno
     */
    public static ProjectWindow create() {
        return new ProjectWindow(DummyProjectController.create());
    }

    /**
     * Vrátí hlavní okno aplikace.
     * 
     * @param projectController řadič projektu
     * @return hlavní okno
     */
    public static ProjectWindow
            create(final ProjectController projectController) {
        final ProjectWindow newInstance = new ProjectWindow(projectController);

        projectController.addView(newInstance);

        return newInstance;
    }

    private ProjectWindow(final ProjectController projectController) {
        Preconditions.checkNotNull(projectController);

        this.projectController = projectController;

        this.icons = loadIcons();
        
        saveAsAction = new SaveAsAction(UiLocalizer.print("SaveAs"), icons.get("saveAsIcon"));
        saveAction = new SaveAction(this.saveAsAction, UiLocalizer.print("Save"), icons.get("saveIcon"));
        closeAction = new CloseAction(UiLocalizer.print("Close"), icons.get("closeIcon"));
        exitAction = new ExitAction(UiLocalizer.print("Quit"), icons.get("exitIcon"));
        newAction = new NewAction(UiLocalizer.print("New"), icons.get("newIcon"));
        openAction = new OpenAction(this.saveAction, UiLocalizer.print("Open"), icons.get("openIcon"));
        exportAction = new ExportAction(UiLocalizer.print("Export"), icons.get("exportIcon"));
        settingsOpenAction = new SettingsOpenAction(UiLocalizer.print("Settings"), icons.get("settingsIcon"));
        runtimeRunAction = new RuntimeRunAction(UiLocalizer.print("Test"), icons.get("testIcon"));
        
        initializeProjectMenu();
        initializeRuntimeMenu();
        initializeHelpMenu();
        initializeTestPane();
        initializeMainTab();
        initializeContentPane();
        setProjectControlsEnabled(false);
        initializeFrame();
    }

    private BiMap<String, ImageIcon> loadIcons() {
        final ImmutableBiMap.Builder<String, ImageIcon> iconsBuilder = ImmutableBiMap.builder();
        for (final Entry<String, String> iconEntry : iconNamesToPaths.entrySet()) {
            final String name = iconEntry.getKey();
            final String path = iconEntry.getValue();
            
            final URL iconUrl = getClass().getResource(path);
            if (Presence.isPresent(iconUrl)) {
                iconsBuilder.put(name, new ImageIcon(iconUrl));
            } else {
                LOGGER.log(Level.WARNING, "MissingIcon", path);
            }
        }
        return iconsBuilder.build();
    }
    
    private void initializeFrame() {
        this.networksScrollPane.setMinimumSize(new Dimension(100, 400));
        this.networksAndArcPropertiesPane.setMinimumSize(new Dimension(450, 400));
        this.resultsScrollPane.setMinimumSize(new Dimension(900, 100));
        
        this.networksScrollPane.setPreferredSize(new Dimension(150, 400));
        this.networksAndArcPropertiesPane.setPreferredSize(new Dimension(750, 400));
        this.resultsScrollPane.setPreferredSize(new Dimension(900, 120));
        
        this.mainSplitPane.setResizeWeight(1);
        this.systemSplitPane.setResizeWeight(0);
        
        final ImageIcon botnicekIcon = this.icons.get(BOTNICEK_ICON_NAME);
        if (Presence.isPresent(botnicekIcon)) {
            this.frame.setIconImage(botnicekIcon.getImage());
        }
        
        this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.frame.addWindowListener(new WindowAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
             */
            @Override
            public void windowClosing(WindowEvent e) {
                exitAction.actionPerformed(new ActionEvent(e.getSource(), e.getID(), Intended.<String>nullReference()));
            }
        });
        this.frame.setTitle(UiLocalizer.print("EnvironmentTitle"));
        this.frame.setJMenuBar(this.menuBar);
        this.frame.setContentPane(this.contentPane);
        this.frame.pack();
        this.frame.setExtendedState(this.frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    private void initializeContentPane() {
        this.contentPane.setLayout(new CardLayout());
        this.contentPane.add(this.mainSplitPane);
    }

    private void initializeTestPane() {
        this.testButton.setAction(this.runtimeRunAction);
        this.testButton.setPreferredSize(new Dimension(TEST_BUTTON_WIDTH, TEST_BUTTON_HEIGHT));
        this.runtimePanel.add(this.testButton);
    }

    private void initializeMainTab() {
        this.tabbedPane.addTab(UiLocalizer.print("Problems"), Intended.<Icon>nullReference(), this.resultsScrollPane, UiLocalizer.print("ProblemsTip"));
        this.tabbedPane.addTab(UiLocalizer.print("TestShow"), Intended.<Icon>nullReference(), this.runtimePanel, UiLocalizer.print("TestTip"));
    }

    private void initializeHelpMenu() {
        this.helpMenu.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("HelpMenuMnemonic")).getKeyCode());
        
        final ImageIcon aboutIcon = this.icons.get("aboutIcon");
        if (Presence.isPresent(aboutIcon)) {
            this.aboutMenuItem.setIcon(aboutIcon);
        }
        
        this.aboutMenuItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                JOptionPane.showMessageDialog(frame, UiLocalizer.print("AboutMessage"), UiLocalizer.print("AboutTitle"), JOptionPane.INFORMATION_MESSAGE, icons.get(BOTNICEK_ICON_NAME));
            }
        });
        this.aboutMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("AboutMnemonic")).getKeyCode());
        this.aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        this.helpMenu.add(aboutMenuItem);
        
        this.menuBar.add(this.helpMenu);
    }

    private void initializeRuntimeMenu() {
        this.runtimeMenu.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("RuntimeMenuMnemonic")).getKeyCode());
        menuBar.add(runtimeMenu);

        this.testMenuItem.setAction(this.runtimeRunAction);
        this.testMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("TestMnemonic")).getKeyCode());
        this.testMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
        this.runtimeMenu.add(testMenuItem);

        this.runtimeMenu.addSeparator();

        this.botMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                projectController.openBotSettings();
            }
        });
        this.botMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("BotMnemonic")).getKeyCode());
        this.botMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
        this.runtimeMenu.add(botMenuItem);

        this.languageMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                projectController.openLanguageSettings();
            }
        });
        this.languageMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("LanguageMnemonic")).getKeyCode());
        this.languageMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        this.runtimeMenu.add(languageMenuItem);

        this.conversationMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                projectController.openConversationSettings();
            }
        });
        this.conversationMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("ConversationMnemonic")).getKeyCode());
        this.conversationMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK));
        this.runtimeMenu.add(conversationMenuItem);
    }

    private void initializeProjectMenu() {
        this.projectMenu.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("ProjectMenuMnemonic")).getKeyCode());
        
        this.newMenuItem.setAction(this.newAction);
        this.newMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("NewMnemonic")).getKeyCode());
        this.newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        this.projectMenu.add(newMenuItem);

        this.openMenuItem.setAction(this.openAction);
        this.openMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("OpenMnemonic")).getKeyCode());
        this.openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        this.projectMenu.add(openMenuItem);

        this.projectMenu.addSeparator();

        this.closeMenuItem.setAction(this.closeAction);
        this.closeMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("CloseMnemonic")).getKeyCode());
        this.closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        this.projectMenu.add(closeMenuItem);

        this.projectMenu.addSeparator();

        this.saveMenuItem.setAction(this.saveAction);
        this.saveMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("SaveMnemonic")).getKeyCode());
        this.saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        this.projectMenu.add(saveMenuItem);

        this.saveAsMenuItem.setAction(this.saveAsAction);
        this.saveAsMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("SaveAsMnemonic")).getKeyCode());
        this.saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        this.projectMenu.add(saveAsMenuItem);

        this.exportMenuItem.setAction(this.exportAction);
        this.exportMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("ExportMnemonic")).getKeyCode());
        this.exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        this.projectMenu.add(this.exportMenuItem);

        this.projectMenu.addSeparator();

        this.settingsMenuItem.setAction(this.settingsOpenAction);
        this.settingsMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("SettingsMnemonic")).getKeyCode());
        this.settingsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        this.projectMenu.add(settingsMenuItem);

        this.projectMenu.addSeparator();

        this.quitMenuItem.setAction(this.exitAction);
        this.quitMenuItem.setMnemonic(KeyStroke.getKeyStroke(UiLocalizer.print("QuitMnemonic")).getKeyCode());
        this.quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
        this.projectMenu.add(quitMenuItem);

        this.menuBar.add(projectMenu);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.project.views.ProjectView#opened(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController, cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkPropertiesController, cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesController, java.util.Set)
     */
    @Override
    public void open(final SystemController systemController,
            final NetworkDisplayController networkPropertiesController,
            final ArcPropertiesDisplayController arcPropertiesController,
            final Set<CheckController> checkControllers) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(systemController);
        Preconditions.checkNotNull(networkPropertiesController);
        Preconditions.checkNotNull(arcPropertiesController);
        Preconditions.checkNotNull(checkControllers);

        SystemOverview.create(this.networksScrollPane, systemController,
                networkPropertiesController);
        SystemPane.create(this.networksAndArcPropertiesPane, systemController,
                networkPropertiesController, arcPropertiesController);
        ResultsTable.create(this.resultsScrollPane, checkControllers);

        setProjectControlsEnabled(true);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.project.views.ProjectView#run(cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.RunController)
     */
    @Override
    public void run(final RunController runController) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(runController);

        final TestPanel testPanel = TestPanel.create(runController);
        testPanel.addComponentListener(new ComponentAdapter() {
            /* (non-Javadoc)
             * @see java.awt.event.ComponentAdapter#componentShown(java.awt.event.ComponentEvent)
             */
            @Override
            public void componentShown(ComponentEvent e) {
                e.getComponent().requestFocusInWindow();
                
                super.componentShown(e);
            }
        });
        
        this.tabbedPane.setComponentAt(RUNTIME_TAB_INDEX, testPanel);
        this.tabbedPane.setSelectedIndex(RUNTIME_TAB_INDEX);
        
        testPanel.requestFocusInWindow();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.ProjectView#
     * settingsOpened
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.SettingsController
     * )
     */
    @Override
    public void settingsOpened(final SettingsController settingsController) {
        assert SwingUtilities.isEventDispatchThread();
        
        Preconditions.checkNotNull(settingsController);

        SettingsDialog.create(this.frame, settingsController).show();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.ProjectView#
     * botSettingsOpened
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers
     * .BotSettingsController)
     */
    @Override
    public void botSettingsOpened(
            final BotSettingsController botSettingsController) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(botSettingsController);

        BotSettingsDialog.create(this.frame, botSettingsController).show();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.ProjectView#
     * languageSettingsOpened
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers
     * .LanguageSettingsController)
     */
    @Override
    public void languageSettingsOpened(
            final LanguageSettingsController languageSettingsController) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(languageSettingsController);

        LanguageSettingsDialog.create(this.frame, languageSettingsController)
                .show();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.ProjectView#
     * conversationSettingsOpened
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers
     * .ConversationSettingsController)
     */
    @Override
    public
            void
            conversationSettingsOpened(
                    final ConversationSettingsController conversationSettingsController) {
        assert SwingUtilities.isEventDispatchThread();

        Preconditions.checkNotNull(conversationSettingsController);

        ConversationSettingsDialog.create(this.frame,
                conversationSettingsController).show();
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.ProjectView#closed()
     */
    @Override
    public void close() {
        assert SwingUtilities.isEventDispatchThread();

        setProjectControlsEnabled(false);

        this.tabbedPane.setComponentAt(RUNTIME_TAB_INDEX, this.runtimePanel);
    }
    
    /**
     * Zeptá se uživatele, zda-li chce před akcí uložit případně otevřený projekt (pokud ano, uloží jej). Indikuje, zda-li se má pokračovat v akci, kvůli které bylo uložení nabídnuto.
     * 
     * @param e událost akce, která předchází výzvě
     * 
     * @return zda-li se má pokračovat v akci, kvůli které bylo uložení nabídnuto
     */
    public boolean continueAfterSaving(final ActionEvent e) {
        final int saveConfirmationResult =
                JOptionPane
                        .showOptionDialog(frame,
                                UiLocalizer.print("SAVE_CONFIRMATION_MESSAGE"),
                                        UiLocalizer.print("SAVE_CONFIRMATION_TITLE"),
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE, Intended.<Icon>nullReference(),
                                new Object[] { UiLocalizer.print("SAVE_OPTION"),
                                        UiLocalizer.print("FORFEIT_SAVE_OPTION"),
                                                UiLocalizer.print("CANCEL_SAVE_OPTION") },
                                                UiLocalizer.print("SAVE_OPTION"));
        
        switch (saveConfirmationResult) {
            case JOptionPane.YES_OPTION:
                saveAction.actionPerformed(e);
                return true;
            case JOptionPane.NO_OPTION:
                return true;
            case JOptionPane.CLOSED_OPTION:
                return false;
            case JOptionPane.CANCEL_OPTION:
                return false;
            default:
                throw new AssertionError();
        }
    }
    
    /**
     * Pokusí se uzavřít projekt a indikuje, zda-li se má po pokusu pokračovat v akci, které vedla k ukončení. 
     * 
     * @param e událost akce, která předchází pokusu
     * 
     * @return zda-li se má pokračovat v akci, kvůli které byl pokus proveden
     */
    private boolean continueAfterClosing(final ActionEvent e) {
        if (!projectController.isOpen()) {
            return true;
        }
        
        if (continueAfterSaving(e)) {
            projectController.close();
            saveAction.clearUsed();
            return true;
        } else {
            return false;
        }
    }
}
