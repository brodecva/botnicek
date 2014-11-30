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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.ConversationSettingsChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.ConversationSettingsChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Project;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.ConversationSettingsView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.ConversationConfiguration;

/**
 * Výchozí implementace řadiče nastavení běhového prostředí pro konverzaci
 * konfiguruje přímo projekt.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultConversationSettingsController extends
        AbstractController<ConversationSettingsView> implements
        ConversationSettingsController {

    private final class DefaultConversationSettingsChangedListener implements
            ConversationSettingsChangedListener {

        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.events.
         * SettingsChangedListener
         * #changed(cz.cuni.mff.ms.brodecva.botnicek.ide.projects
         * .model.Settings)
         */
        @Override
        public void changed(final ConversationConfiguration settings) {
            Preconditions.checkNotNull(settings);

            callViews(new Callback<ConversationSettingsView>() {

                @Override
                public void call(final ConversationSettingsView view) {
                    view.updateConversationConfiguration(settings);
                }

            });
        }

    }

    /**
     * Vytvoří řadič pro projekt a bude mu naslouchat na příslušné změny.
     * 
     * @param project
     *            projekt
     * @param eventManager
     *            správce událostí
     * @return řadič
     */
    public static DefaultConversationSettingsController create(
            final Project project, final EventManager eventManager) {
        Preconditions.checkNotNull(project);
        Preconditions.checkNotNull(eventManager);

        final DefaultConversationSettingsController newInstance =
                new DefaultConversationSettingsController(project, eventManager);

        newInstance.addListener(ConversationSettingsChangedEvent.class,
                newInstance.new DefaultConversationSettingsChangedListener());

        return newInstance;
    }

    private final Project project;

    private DefaultConversationSettingsController(final Project project,
            final EventManager eventManager) {
        super(eventManager);

        this.project = project;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.
     * ConversationSettingsController
     * #fill(cz.cuni.mff.ms.brodecva.botnicek.ide.runtime
     * .views.ConversationSettingsView)
     */
    @Override
    public void fill(final ConversationSettingsView view) {
        Preconditions.checkNotNull(view);

        view.updateConversationConfiguration(this.project
                .getConversationConfiguration());
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.
     * ConversationSettingsController
     * #set(cz.cuni.mff.ms.brodecva.botnicek.library
     * .api.ConversationConfiguration)
     */
    @Override
    public void set(final ConversationConfiguration configuration) {
        Preconditions.checkNotNull(configuration);

        this.project.setConversationConfiguration(configuration);
    }
}
