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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;

/**
 * Událost přejmenování uzlu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class NodeRenamedEvent extends
        AbstractMappedEvent<Network, NodeRenamedListener> {

    /**
     * Vytvoří událost.
     * 
     * @param network
     *            rodičovská síť uzlu
     * @param oldVersion
     *            původní verze
     * @param newVersion
     *            nová verze
     * @return událost
     */
    public static NodeRenamedEvent create(final Network network,
            final Node oldVersion, final Node newVersion) {
        return new NodeRenamedEvent(network, oldVersion, newVersion);
    }

    private final Node oldVersion;

    private final Node newVersion;

    private NodeRenamedEvent(final Network network, final Node oldVersion,
            final Node newVersion) {
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
    public void dispatchTo(final NodeRenamedListener listener) {
        listener.nodeRenamed(this.oldVersion, this.newVersion);
    }
}
