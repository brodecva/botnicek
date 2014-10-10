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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.BotSettingsChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.BotSettingsChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Project;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.BotSettingsView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;

/**
 * Výchozí implementace řadič nastavení robota konfiguruje přímo projekt.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultBotSettingsController extends
        AbstractController<BotSettingsView> implements BotSettingsController {

    private final class DefaultBotSettingsChangedListener implements
            BotSettingsChangedListener {

        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.events.
         * SettingsChangedListener
         * #changed(cz.cuni.mff.ms.brodecva.botnicek.ide.projects
         * .model.Settings)
         */
        @Override
        public void changed(final BotConfiguration settings) {
            Preconditions.checkNotNull(settings);

            callViews(new Callback<BotSettingsView>() {

                @Override
                public void call(final BotSettingsView view) {
                    view.updateBotConfiguration(settings);
                }

            });
        }

    }

    /**
     * Vytvoří řadič pro daný projekt a bude naslouchat změnám na něm.
     * 
     * @param project
     *            projekt
     * @param eventManager
     *            správce událostí
     * @return řadič
     */
    public static DefaultBotSettingsController create(final Project project,
            final EventManager eventManager) {
        Preconditions.checkNotNull(project);
        Preconditions.checkNotNull(eventManager);

        final DefaultBotSettingsController newInstance =
                new DefaultBotSettingsController(project, eventManager);

        newInstance.addListener(BotSettingsChangedEvent.class,
                newInstance.new DefaultBotSettingsChangedListener());

        return newInstance;
    }

    private final Project project;

    private DefaultBotSettingsController(final Project project,
            final EventManager eventManager) {
        super(eventManager);

        this.project = project;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.
     * BotSettingsController
     * #fill(cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.BotSettingsView)
     */
    @Override
    public void fill(final BotSettingsView view) {
        Preconditions.checkNotNull(view);

        view.updateBotConfiguration(this.project.getBotConfiguration());
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.
     * BotSettingsController
     * #setBotConfiguration(cz.cuni.mff.ms.brodecva.botnicek
     * .library.api.BotConfiguration)
     */
    @Override
    public void set(final BotConfiguration configuration) {
        Preconditions.checkNotNull(configuration);

        this.project.setBotConfiguration(configuration);
    }
}
