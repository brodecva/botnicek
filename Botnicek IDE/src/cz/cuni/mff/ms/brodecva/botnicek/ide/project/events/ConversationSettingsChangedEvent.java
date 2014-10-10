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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Project;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.ConversationConfiguration;

/**
 * Událost změny nastavení konverzace.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ConversationSettingsChangedEvent extends
        AbstractMappedEvent<Project, ConversationSettingsChangedListener> {

    /**
     * Vytvoří událost
     * 
     * @param project
     *            projekt, kterému náleží konverzace
     * @param settings
     *            nová nastavení konverzace
     * @return událost
     */
    public static ConversationSettingsChangedEvent create(
            final Project project, final ConversationConfiguration settings) {
        return new ConversationSettingsChangedEvent(project, settings);
    }

    private final ConversationConfiguration settings;

    private ConversationSettingsChangedEvent(final Project project,
            final ConversationConfiguration settings) {
        super(project);

        Preconditions.checkNotNull(settings);

        this.settings = settings;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang
     * .Object)
     */
    @Override
    public void dispatchTo(final ConversationSettingsChangedListener listener) {
        listener.changed(this.settings);
    }
}
