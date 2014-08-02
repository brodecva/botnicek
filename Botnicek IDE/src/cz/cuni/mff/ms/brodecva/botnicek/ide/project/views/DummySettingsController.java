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

import cz.cuni.mff.ms.brodecva.botnicek.ide.project.controllers.SettingsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings;

/**
 * Atrapa řadiče.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class DummySettingsController implements SettingsController {

    /**
     * Vytvoří atrapu.
     * 
     * @return atrapa
     */
    public static DummySettingsController create() {
        return new DummySettingsController();
    }
    
    private DummySettingsController() {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#addView(java.lang.Object)
     */
    @Override
    public void addView(SettingsView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#removeView(java.lang.Object)
     */
    @Override
    public void removeView(SettingsView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.SettingsController#set(cz.cuni.mff.ms.brodecva.botnicek.ide.projects.model.Settings)
     */
    @Override
    public void set(Settings create) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.projects.controllers.SettingsController#fill(cz.cuni.mff.ms.brodecva.botnicek.ide.projects.views.SettingsView)
     */
    @Override
    public void fill(SettingsView view) {
    }
}
