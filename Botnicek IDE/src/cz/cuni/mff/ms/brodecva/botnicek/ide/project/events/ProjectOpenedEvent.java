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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.events;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Project;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractEvent;

/**
 * Událost otevření projektu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ProjectOpenedEvent extends
        AbstractEvent<ProjectOpenedListener> {

    /**
     * Vytvoří událost.
     * 
     * @param project
     *            otevřený projekt
     * @return událost
     */
    public static ProjectOpenedEvent create(final Project project) {
        return new ProjectOpenedEvent(project);
    }

    private final Project project;

    private ProjectOpenedEvent(final Project project) {
        Preconditions.checkNotNull(project);

        this.project = project;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang
     * .Object)
     */
    @Override
    public void dispatchTo(final ProjectOpenedListener listener) {
        listener.opened(this.project);
    }
}
