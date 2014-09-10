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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.controllers.CodeValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.controllers.DefaultCodeValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker.DefaultCodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.controllers.DefaultMixedPatternValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.controllers.MixedPatternValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.controllers.DefaultSimplePatternValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.controllers.SimplePatternValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.controllers.DefaultNormalWordValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.controllers.NormalWordValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.AvailableReferencesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.DefaultArcPropertiesDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.DefaultAvailableReferencesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.DefaultNetworkDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.DefaultSystemController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.SystemClosedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.BotSettingsOpenedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.BotSettingsOpenedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.ConversationSettingsOpenedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.ConversationSettingsOpenedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.LanguageSettingsOpenedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.LanguageSettingsOpenedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.ProjectClosedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.ProjectClosedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.ProjectOpenedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.ProjectOpenedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.SettingsOpenedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.SettingsOpenedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Project;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.views.ProjectView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.BotSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.ConversationSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.DefaultBotSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.DefaultConversationSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.DefaultLanguageSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.DefaultRunController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.LanguageSettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.RunController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.RuntimeRunEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.RuntimeRunListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.Run;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.RunException;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;

/**
 * Výchozí implementace řadiče projektů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultProjectController extends AbstractController<ProjectView>
        implements ProjectController {

    private final class DefaultProjectOpenedListener implements
            ProjectOpenedListener {

        /**
         * {@inheritDoc}
         * 
         * <p>Nastaví příslušné posluchače pro projektové události a vytvoří řadiče pro jeho funkce.</p>
         * <p>Zpraví pohledy o otevření projektu.</p>
         */
        @Override
        public void opened(final Project opened) {
            Preconditions.checkNotNull(opened);

            addListener(ProjectClosedEvent.class, opened,
                    new DefaultProjectClosedListener());
            addListener(RuntimeRunEvent.class, opened,
                    new DefaultRuntimeRunListener());
            addListener(SettingsOpenedEvent.class, opened,
                    new DefaultSettingsOpenedListener());
            addListener(BotSettingsOpenedEvent.class, opened,
                    new DefaultBotSettingsOpenedListener());
            addListener(LanguageSettingsOpenedEvent.class, opened,
                    new DefaultLanguageSettingsOpenedListener());
            addListener(ConversationSettingsOpenedEvent.class, opened,
                    new DefaultConversationSettingsOpenedListener());

            final System system = opened.getSystem();
            final EventManager eventManager = getEventManager();

            final BotConfiguration botSettings = opened.getBotConfiguration();
            final LanguageConfiguration languageSettings =
                    opened.getLanguageConfiguration();
            final Map<URI, String> namespacesToPrefixes =
                    opened.getSettings().getNamespacesToPrefixes();

            final CodeChecker codeChecker = DefaultCodeChecker.create(botSettings, languageSettings, namespacesToPrefixes);
            
            final AvailableReferencesController availableReferencesController =
                    DefaultAvailableReferencesController.create(system,
                            eventManager);
            final NormalWordValidationController nameValidationController =
                    DefaultNormalWordValidationController.create(
                            system.getStatesNamingAuthority(), eventManager);
            final CodeValidationController codeValidationController =
                    DefaultCodeValidationController.create(codeChecker,
                            eventManager);
            final SimplePatternValidationController simplePatternValidationController =
                    DefaultSimplePatternValidationController
                            .create(eventManager);
            final MixedPatternValidationController mixedPatternValidationController =
                    DefaultMixedPatternValidationController
                            .create(codeChecker, eventManager);
            final NormalWordValidationController predicateValidationController =
                    DefaultNormalWordValidationController
                            .create(system.getPredicatesNamingAuthority(),
                                    eventManager);

            final ArcPropertiesDisplayController arcPropertiesController =
                    DefaultArcPropertiesDisplayController.create(system, eventManager,
                            botSettings, languageSettings,
                            namespacesToPrefixes,
                            availableReferencesController,
                            nameValidationController, codeValidationController,
                            simplePatternValidationController,
                            mixedPatternValidationController,
                            predicateValidationController);
            final SystemController systemController =
                    DefaultSystemController.create(system, eventManager);
            final NetworkDisplayController networkPropertiesController =
                    DefaultNetworkDisplayController.create(system,
                            arcPropertiesController, getEventManager());
            final Set<CheckController> checkControllers = ImmutableSet.of(codeValidationController, simplePatternValidationController, mixedPatternValidationController, predicateValidationController, nameValidationController);
            
            callViews(new Callback<ProjectView>() {

                @Override
                public void call(final ProjectView view) {
                    Preconditions.checkNotNull(view);

                    view.open(systemController, networkPropertiesController,
                            arcPropertiesController, checkControllers);
                }

            });
        }

    }

    private final class DefaultProjectClosedListener implements
            ProjectClosedListener {

        /**
         * {@inheritDoc}
         * 
         * <p>Odstraní posluchače a dá vědět pohledům, že byl projekt uzavřen.</p>
         */
        @Override
        public void closed(final Project closed) {
            removeAllListeners(ProjectClosedEvent.class, closed);
            removeAllListeners(RuntimeRunEvent.class, closed);
            removeAllListeners(SettingsOpenedEvent.class, closed);
            removeAllListeners(BotSettingsOpenedEvent.class, closed);
            removeAllListeners(LanguageSettingsOpenedEvent.class, closed);
            removeAllListeners(ConversationSettingsOpenedEvent.class, closed);

            callViews(new Callback<ProjectView>() {

                @Override
                public void call(final ProjectView view) {
                    Preconditions.checkNotNull(view);

                    view.close();
                }
            });
        }
    }

    private final class DefaultRuntimeRunListener implements RuntimeRunListener {

        /**
         * {@inheritDoc}
         * 
         * <p>Vytvoří řadič pro ovládání a předá pohledům.</p>
         */
        @Override
        public void run(final Run run) {
            final RunController runController =
                    DefaultRunController.create(run, getEventManager());

            callViews(new Callback<ProjectView>() {

                @Override
                public void call(final ProjectView view) {
                    Preconditions.checkNotNull(view);

                    view.run(runController);
                }

            });
        }

    }

    private final class DefaultSettingsOpenedListener implements
            SettingsOpenedListener {

        /**
         * {@inheritDoc}
         * 
         * <p>Vytvoří řadič pro nastavení a předá pohledům.</p>
         */
        @Override
        public void settingsOpenedTo(final Project project) {
            Preconditions.checkNotNull(project);

            final SettingsController settingsController =
                    DefaultSettingsController
                            .create(project, getEventManager());

            callViews(new Callback<ProjectView>() {

                @Override
                public void call(final ProjectView view) {
                    Preconditions.checkNotNull(view);

                    view.settingsOpened(settingsController);
                }

            });
        }

    }

    private final class DefaultBotSettingsOpenedListener implements
            BotSettingsOpenedListener {

        /**
         * {@inheritDoc}
         * 
         * <p>Vytvoří řadič pro nastavení a předá pohledům.</p>
         */
        @Override
        public void settingsOpenedTo(final Project project) {
            Preconditions.checkNotNull(project);

            final BotSettingsController botSettingsController =
                    DefaultBotSettingsController.create(project,
                            getEventManager());

            callViews(new Callback<ProjectView>() {

                @Override
                public void call(final ProjectView view) {
                    Preconditions.checkNotNull(view);

                    view.botSettingsOpened(botSettingsController);
                }

            });
        }

    }

    private final class DefaultLanguageSettingsOpenedListener implements
            LanguageSettingsOpenedListener {

        /**
         * {@inheritDoc}
         * 
         * <p>Vytvoří řadič pro nastavení a předá pohledům.</p>
         */
        @Override
        public void settingsOpenedTo(final Project project) {
            Preconditions.checkNotNull(project);

            callViews(new Callback<ProjectView>() {

                @Override
                public void call(final ProjectView view) {
                    Preconditions.checkNotNull(view);

                    final LanguageSettingsController languageSettingsController =
                            DefaultLanguageSettingsController.create(project,
                                    getEventManager());

                    view.languageSettingsOpened(languageSettingsController);
                }

            });
        }

    }

    private final class DefaultConversationSettingsOpenedListener implements
            ConversationSettingsOpenedListener {

        /**
         * {@inheritDoc}
         * 
         * <p>Vytvoří řadič pro nastavení a předá pohledům.</p>
         */
        @Override
        public void settingsOpenedTo(final Project project) {
            Preconditions.checkNotNull(project);

            final ConversationSettingsController conversationSettingsController =
                    DefaultConversationSettingsController.create(project,
                            getEventManager());

            callViews(new Callback<ProjectView>() {

                @Override
                public void call(final ProjectView view) {
                    Preconditions.checkNotNull(view);

                    view.conversationSettingsOpened(conversationSettingsController);
                }

            });
        }

    }

    private Optional<Project> currentProject;

    /**
     * Vytvoří řadič bez otevřeného projektu.
     * 
     * @param eventManager správce událostí
     * @return řadič
     */
    public static DefaultProjectController create(
            final EventManager eventManager) {
        Preconditions.checkNotNull(eventManager);

        return create(Optional.<Project> absent(), eventManager);
    }

    /**
     * Vytvoří řadič spravující daný projekt.
     * 
     * @param project otevřený projekt
     * @param eventManager správce událostí
     * @return řadič
     */
    public static DefaultProjectController create(final Project project,
            final EventManager eventManager) {
        Preconditions.checkNotNull(project);
        Preconditions.checkNotNull(eventManager);

        return create(Optional.of(project), eventManager);
    }

    private static DefaultProjectController create(
            final Optional<Project> project, final EventManager eventManager) {
        final DefaultProjectController newInstance =
                new DefaultProjectController(project, eventManager);

        newInstance.addListener(ProjectOpenedEvent.class,
                newInstance.new DefaultProjectOpenedListener());

        return newInstance;
    }

    private DefaultProjectController(final Optional<Project> project,
            final EventManager eventManager) {
        super(eventManager);

        Preconditions.checkNotNull(project);

        this.currentProject = project;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController
     * #save(java.io.File)
     */
    @Override
    public void save(final Path projectPath) throws IOException {
        Preconditions.checkNotNull(projectPath);
        Preconditions.checkState(this.currentProject.isPresent());

        this.currentProject.get().save(projectPath);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController
     * #open(java.io.File)
     */
    @Override
    public void open(final Path projectPath) throws FileNotFoundException,
            ClassNotFoundException, IOException {
        Preconditions.checkNotNull(projectPath);

        this.currentProject = Optional.of(Project.open(projectPath, getEventManager()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController
     * #close()
     */
    @Override
    public void close() {
        Preconditions.checkState(this.currentProject.isPresent());

        final Project closed = this.currentProject.get();
        this.currentProject = Optional.absent();

        fire(SystemClosedEvent.create(closed.getSystem()));
        fire(ProjectClosedEvent.create(closed));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController
     * #export(java.io.File)
     */
    @Override
    public void export(final Path location) throws IOException {
        Preconditions.checkNotNull(location);
        Preconditions.checkState(this.currentProject.isPresent());

        this.currentProject.get().export(location);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController
     * #create(java.lang.String)
     */
    @Override
    public void createNew(final String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(name.isEmpty());

        final Project newProject = Project.createAndOpen(name, getEventManager());
        this.currentProject = Optional.of(newProject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController
     * #isOpen()
     */
    @Override
    public boolean isOpen() {
        return this.currentProject.isPresent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController
     * #openSettings()
     */
    @Override
    public void openSettings() {
        Preconditions.checkState(this.currentProject.isPresent());

        fire(SettingsOpenedEvent.create(this.currentProject.get()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController
     * #openBotSettings()
     */
    @Override
    public void openBotSettings() {
        Preconditions.checkState(this.currentProject.isPresent());

        fire(BotSettingsOpenedEvent.create(this.currentProject.get()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController
     * #openLanguageSettings()
     */
    @Override
    public void openLanguageSettings() {
        Preconditions.checkState(this.currentProject.isPresent());

        fire(LanguageSettingsOpenedEvent.create(this.currentProject.get()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController
     * #openConversationSettings()
     */
    @Override
    public void openConversationSettings() {
        Preconditions.checkState(this.currentProject.isPresent());

        fire(ConversationSettingsOpenedEvent.create(this.currentProject.get()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.ProjectController
     * #run()
     */
    @Override
    public void test() throws RunException {
        Preconditions.checkState(this.currentProject.isPresent());

        this.currentProject.get().test();
    }
}
