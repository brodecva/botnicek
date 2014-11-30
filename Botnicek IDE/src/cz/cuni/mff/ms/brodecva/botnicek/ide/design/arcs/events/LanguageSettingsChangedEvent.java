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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.events.LanguageSettingsChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;

/**
 * Událost změny nastavení jazyka robota užitého pro validaci v systému.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class LanguageSettingsChangedEvent extends
        AbstractMappedEvent<System, LanguageSettingsChangedListener> {

    /**
     * Vytvoří událost.
     * 
     * @param system
     *            systém, který je změnou nastavení ovlivněn
     * @param settings
     *            nastavení jazyka
     * @return událost
     */
    public static LanguageSettingsChangedEvent create(final System system,
            final LanguageConfiguration settings) {
        return new LanguageSettingsChangedEvent(system, settings);
    }

    private final LanguageConfiguration settings;

    private LanguageSettingsChangedEvent(final System system,
            final LanguageConfiguration settings) {
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
    public void dispatchTo(final LanguageSettingsChangedListener listener) {
        listener.changed(this.settings);
    }
}
