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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.events;

import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Project;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;

/**
 * Událost otevření nastavení projektu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class SettingsOpenedEvent extends
        AbstractMappedEvent<Project, SettingsOpenedListener> {

    /**
     * Vytvoří událost.
     * 
     * @param project
     *            cílený projekt
     * @return událost
     */
    public static SettingsOpenedEvent create(final Project project) {
        return new SettingsOpenedEvent(project);
    }

    private SettingsOpenedEvent(final Project project) {
        super(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang
     * .Object)
     */
    @Override
    public void dispatchTo(final SettingsOpenedListener listener) {
        listener.settingsOpenedTo(getKey());
    }
}
