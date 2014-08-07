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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JButton;

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

        public void actionPerformed(final ActionEvent e) {
            projectController.openSettings();
        }
    }

    private final class NewAction implements ActionListener {

        private final SaveAction saveAction;

        public NewAction(final SaveAction saveAction) {
            Preconditions.checkNotNull(saveAction);

            this.saveAction = saveAction;
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

            if (projectController.isOpen()) {
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

    private final class ExportAction implements ActionListener {
        public void actionPerformed(final ActionEvent e) {
            final JFileChooser exportDirectoryChooser = new JFileChooser();
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

    private final class OpenAction implements ActionListener {

        private final SaveAction saveAction;

        public OpenAction(final SaveAction saveAction) {
            Preconditions.checkNotNull(saveAction);

            this.saveAction = saveAction;
        }

        public void actionPerformed(final ActionEvent e) {
            final JFileChooser openProjectFileChooser = new JFileChooser();
            openProjectFileChooser
                    .setFileFilter(new ProjectFileFilter());
            
            final int choice = openProjectFileChooser.showOpenDialog(frame);

            switch (choice) {
            case JFileChooser.APPROVE_OPTION:
                if (projectController.isOpen()) {
                    final int saveConfirmationResult =
                            JOptionPane.showOptionDialog(frame,
                                    UiLocalizer.print("SAVE_CONFIRMATION_MESSAGE"),
                                            UiLocalizer.print("SAVE_CONFIRMATION_TITLE"),
                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE, Intended.<Icon>nullReference(),
                                    new Object[] { UiLocalizer.print("SAVE_OPTION"),
                                            UiLocalizer.print("FORFEIT_SAVE_OPTION"),
                                                    UiLocalizer.print("CANCEL_SAVE_OPTION") }, UiLocalizer.print("SAVE_OPTION"));
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
                    projectController.open(openProjectFileChooser
                            .getSelectedFile().toPath());
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
        private final SaveAction saveAction;

        public CloseAction(final SaveAction saveAction) {
            Preconditions.checkNotNull(saveAction);

            this.saveAction = saveAction;
        }

        public void actionPerformed(final ActionEvent e) {
            final int saveConfirmationResult =
                    JOptionPane.showOptionDialog(frame,
                            UiLocalizer.print("SAVE_CONFIRMATION_MESSAGE"), UiLocalizer.print("SAVE_CONFIRMATION_TITLE"),
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE, Intended.<Icon>nullReference(), new Object[] {
                                    UiLocalizer.print("SAVE_OPTION"), UiLocalizer.print("FORFEIT_SAVE_OPTION"),
                                            UiLocalizer.print("CANCEL_SAVE_OPTION") }, UiLocalizer.print("SAVE_OPTION"));

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

    private final class SaveAsAction extends AbstractAction {

        private static final long serialVersionUID = 1L;
        
        private Optional<Path> used = Optional.absent();

        @Override
        public void actionPerformed(final ActionEvent e) {
            final JFileChooser saveProjectFileChooser = new JFileChooser();
            saveProjectFileChooser
                    .setFileFilter(new ProjectFileFilter());
            final int choice = saveProjectFileChooser.showSaveDialog(frame);

            switch (choice) {
            case JFileChooser.APPROVE_OPTION:
                final Path selected =
                        saveProjectFileChooser.getSelectedFile().toPath();
                try {
                    projectController.save(selected);
                } catch (final IOException ex) {
                    JOptionPane.showMessageDialog(frame, UiLocalizer.print("SAVE_ERROR_MESSAGE"),
                            UiLocalizer.print("SAVE_ERROR_TITLE"), JOptionPane.ERROR_MESSAGE);
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

        private static final long serialVersionUID = 1L;
        private final SaveAsAction saveAsAction;

        public SaveAction(final SaveAsAction saveAsAction) {
            Preconditions.checkNotNull(saveAsAction);

            this.saveAsAction = saveAsAction;
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
    private static final String PATH_TO_ICON = "images/botnicek.png";
    
    private static final int RUNTIME_TAB_INDEX = 1;    

    private final JFrame frame = new JFrame();
    private final JMenuBar menuBar = new JMenuBar();
    private final JPanel contentPane = new JPanel();

    private final JMenu projectMenu = new JMenu(UiLocalizer.print("Project"));
    private final JMenuItem newMenuItem = new JMenuItem(UiLocalizer.print("New"));
    private final JMenuItem openMenuItem = new JMenuItem(UiLocalizer.print("Open"));
    private final JMenuItem closeMenuItem = new JMenuItem(UiLocalizer.print("Close"));
    private final JMenuItem saveMenuItem = new JMenuItem(UiLocalizer.print("Save"));
    private final JMenuItem saveAsMenuItem = new JMenuItem(UiLocalizer.print("SaveAs"));
    private final JMenuItem exportMenuItem = new JMenuItem(UiLocalizer.print("Export"));
    private final JMenuItem settingsMenuItem = new JMenuItem(UiLocalizer.print("Settings"));
    private final JMenuItem quitMenuItem = new JMenuItem(UiLocalizer.print("Quit"));

    private final JMenu runtimeMenu = new JMenu(UiLocalizer.print("Runtime"));
    private final JMenuItem testMenuItem = new JMenuItem(UiLocalizer.print("Test"));
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

    private final SaveAsAction saveAsAction = new SaveAsAction();
    private final SaveAction saveAction = new SaveAction(this.saveAsAction);
    private final CloseAction closeAction = new CloseAction(this.saveAction);
    private final NewAction newAction = new NewAction(this.saveAction);
    private final OpenAction openAction = new OpenAction(this.saveAction);
    private final ExportAction exportAction = new ExportAction();
    private final SettingsOpenAction settingsOpenAction =
            new SettingsOpenAction();
    private final RuntimeRunAction runtimeRunAction = new RuntimeRunAction();

    private final ProjectController projectController;
    private ImageIcon icon;

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
                
                JFileChooser.setDefaultLocale(Locale.getDefault());

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

        initializeIcon();
        initializeProjectMenu();
        initializeRuntimeMenu();
        initializeHelpMenu();
        initializeTestPane();
        initializeMainTab();
        initializeContentPane();
        setProjectControlsEnabled(false);
        initializeFrame();
    }

    private void initializeIcon() {
        final URL iconUrl = getClass().getResource(PATH_TO_ICON);
        if (Presence.isPresent(iconUrl)) {
            this.icon = new ImageIcon(iconUrl);
        } else {
            LOGGER.warning("MissingIcon");
        }
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
        
        if (Presence.isPresent(this.icon)) {
            this.frame.setIconImage(icon.getImage());
        }
        
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        this.testButton.addActionListener(this.runtimeRunAction);
        this.runtimePanel.add(this.testButton);
    }

    private void initializeMainTab() {
        this.tabbedPane.addTab(UiLocalizer.print("Problems"), Intended.<Icon>nullReference(), this.resultsScrollPane, UiLocalizer.print("ProblemsTip"));
        this.tabbedPane.addTab(UiLocalizer.print("TestShow"), Intended.<Icon>nullReference(), this.runtimePanel, UiLocalizer.print("TestTip"));
    }

    private void initializeHelpMenu() {
        this.aboutMenuItem.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent e) {
                JOptionPane.showMessageDialog(frame, UiLocalizer.print("AboutMessage"), UiLocalizer.print("AboutTitle"), JOptionPane.INFORMATION_MESSAGE, icon);
            }
        });
        
        this.helpMenu.add(aboutMenuItem);
        this.menuBar.add(this.helpMenu);
    }

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

        this.tabbedPane.setTabComponentAt(RUNTIME_TAB_INDEX,
                TestPanel.create(runController));
        this.tabbedPane.setSelectedIndex(RUNTIME_TAB_INDEX);
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
}
