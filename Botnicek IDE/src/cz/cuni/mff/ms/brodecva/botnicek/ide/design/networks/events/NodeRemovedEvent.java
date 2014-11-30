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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;

/**
 * Událost odebrání uzlu ze sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class NodeRemovedEvent extends
        AbstractMappedEvent<Network, NodeRemovedListener> {

    /**
     * Vytvoří událost.
     * 
     * @param network
     *            síť
     * @param node
     *            odebraný uzel
     * @return událost
     */
    public static NodeRemovedEvent
            create(final Network network, final Node node) {
        return new NodeRemovedEvent(network, node);
    }

    private final Node node;

    private NodeRemovedEvent(final Network network, final Node node) {
        super(network);

        Preconditions.checkNotNull(node);

        this.node = node;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang
     * .Object)
     */
    @Override
    public void dispatchTo(final NodeRemovedListener listener) {
        listener.nodeRemoved(this.node);
    }
}
