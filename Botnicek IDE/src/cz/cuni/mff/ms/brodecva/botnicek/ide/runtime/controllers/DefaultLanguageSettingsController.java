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

import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.LanguageSettingsChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.LanguageSettingsChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Project;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.LanguageSettingsView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;

/**
 * Výchozí implementace řadiče nastavení jazyka robota konfiguruje přímo projekt.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultLanguageSettingsController extends AbstractController<LanguageSettingsView> implements LanguageSettingsController {

    private final class DefaultLanguageSettingsChangedListener implements LanguageSettingsChangedListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.events.SettingsChangedListener#changed(cz.cuni.mff.ms.brodecva.botnicek.ide.projects.model.Settings)
         */
        @Override
        public void changed(final LanguageConfiguration settings) {
            Preconditions.checkNotNull(settings);
            
            callViews(new Callback<LanguageSettingsView>() {

                @Override
                public void call(final LanguageSettingsView view) {
                    view.updateLanguageConfiguration(settings);
                }
                
            });
        }
        
    }
    
    private final Project project;
    
    /**
     * Vytvoří řadič pro nastavování a bude naslouchat změnám na projektu.
     * 
     * @param project projekt
     * @param eventManager správce událost
     * @return řadič
     */
    public static DefaultLanguageSettingsController create(final Project project, final EventManager eventManager) {
        Preconditions.checkNotNull(project);
        Preconditions.checkNotNull(eventManager);
        
        final DefaultLanguageSettingsController newInstance = new DefaultLanguageSettingsController(project, eventManager);
        
        newInstance.addListener(LanguageSettingsChangedEvent.class, newInstance.new DefaultLanguageSettingsChangedListener());
        
        return newInstance;
    }
    
    private DefaultLanguageSettingsController(final Project project, final EventManager eventManager) {
        super(eventManager);
        
        this.project = project;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.LanguageSettingsController#setLanguageConfiguration(cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration)
     */
    @Override
    public void set(final LanguageConfiguration configuration) {
        Preconditions.checkNotNull(configuration);
        
        this.project.setLanguageConfiguration(configuration);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.LanguageSettingsController#fill(cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.LanguageSettingsView)
     */
    @Override
    public void fill(final LanguageSettingsView view) {
        Preconditions.checkNotNull(view);
        
        view.updateLanguageConfiguration(project.getLanguageConfiguration());
    }
}
