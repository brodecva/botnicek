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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;

/**
 * Událost zobrazení sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class NetworkDisplayedEvent extends
        AbstractMappedEvent<System, NetworkDisplayedListener> {

    /**
     * Vytvoří událost.
     * 
     * @param system
     *            rodičovský systém sítí
     * @param network
     *            síť
     * @return událost
     */
    public static NetworkDisplayedEvent create(final System system,
            final Network network) {
        return new NetworkDisplayedEvent(system, network);
    }

    private final Network network;

    private NetworkDisplayedEvent(final System system, final Network network) {
        super(system);

        Preconditions.checkNotNull(network);

        this.network = network;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang
     * .Object)
     */
    @Override
    public void dispatchTo(final NetworkDisplayedListener listener) {
        Preconditions.checkNotNull(listener);

        listener.displayed(this.network);
    }
}
