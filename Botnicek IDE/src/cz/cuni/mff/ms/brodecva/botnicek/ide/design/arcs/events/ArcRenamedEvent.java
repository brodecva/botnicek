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

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;

/**
 * Událost přejmenování hrany.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ArcRenamedEvent extends
        AbstractMappedEvent<Network, ArcRenamedListener> {

    /**
     * Vytvoří událost.
     * 
     * @param network
     *            síť, do které hrana náleží
     * @param oldVersion
     *            původní verze
     * @param newVersion
     *            přejmenovaná verze
     * @return událost
     */
    public static ArcRenamedEvent create(final Network network,
            final Arc oldVersion, final Arc newVersion) {
        return new ArcRenamedEvent(network, oldVersion, newVersion);
    }

    private final Arc oldVersion;

    private final Arc newVersion;

    private ArcRenamedEvent(final Network network, final Arc oldVersion,
            final Arc newVersion) {
        super(network);

        Preconditions.checkNotNull(oldVersion);
        Preconditions.checkNotNull(newVersion);

        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang
     * .Object)
     */
    @Override
    public void dispatchTo(final ArcRenamedListener listener) {
        listener.arcRenamed(this.oldVersion, this.newVersion);
    }
}
