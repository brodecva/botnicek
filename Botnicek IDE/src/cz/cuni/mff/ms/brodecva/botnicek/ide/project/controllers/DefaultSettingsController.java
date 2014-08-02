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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.SettingsChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.SettingsChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Project;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.views.SettingsView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;

/**
 * Výchozí implementace řadiče nastavení projektu. Pracuje přímo s modelem projektu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultSettingsController extends AbstractController<SettingsView> implements SettingsController {

    private final class DefaultSettingsChangedListener implements SettingsChangedListener {

        /**
         * @{inheritDoc}
         * 
         * <p>Předá pohledům novou verzi nastavení.</p>
         */
        @Override
        public void changed(final Settings settings) {
            Preconditions.checkNotNull(settings);
            
            callViews(new Callback<SettingsView>() {

                @Override
                public void call(final SettingsView view) {
                    view.updateSettings(settings);
                }
                
            });
        }
        
    }
    
    private final Project project;
    
    /**
     * Vytvoří řadič nastavení pro daný projekt.
     * 
     * @param project konfigurovaný projekt
     * @param eventManager správce událostí
     * @return řadič
     */
    public static DefaultSettingsController create(final Project project, final EventManager eventManager) {
        final DefaultSettingsController newInstance = new DefaultSettingsController(project, eventManager);
        
        newInstance.addListener(SettingsChangedEvent.class, newInstance.new DefaultSettingsChangedListener());
        
        return newInstance;
    }
    
    private DefaultSettingsController(final Project project, final EventManager eventManager) {
        super(eventManager);
        
        Preconditions.checkNotNull(project);
        
        this.project = project;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.SettingsController#setRandomizeState(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void set(final Settings settings) {
        Preconditions.checkNotNull(settings);
        
        this.project.set(settings);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.SettingsController#fill(cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.SettingsView)
     */
    @Override
    public void fill(final SettingsView view) {
        Preconditions.checkNotNull(view);
        
        view.updateSettings(this.project.getSettings());
    }
}
