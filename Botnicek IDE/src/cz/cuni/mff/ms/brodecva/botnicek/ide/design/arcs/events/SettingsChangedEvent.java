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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.SettingsChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;

/**
 * Událost změny nastavení projektu, který vlastní daný systém.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class SettingsChangedEvent extends
        AbstractMappedEvent<System, SettingsChangedListener> {

    /**
     * Vytvoří událost.
     * 
     * @param System
     *            system
     * @param settings
     *            nastavení
     * @return událost
     */
    public static SettingsChangedEvent create(final System System,
            final Settings settings) {
        return new SettingsChangedEvent(System, settings);
    }

    private final Settings settings;

    private SettingsChangedEvent(final System system, final Settings settings) {
        super(system);

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
    public void dispatchTo(final SettingsChangedListener listener) {
        listener.changed(this.settings);
    }
}
