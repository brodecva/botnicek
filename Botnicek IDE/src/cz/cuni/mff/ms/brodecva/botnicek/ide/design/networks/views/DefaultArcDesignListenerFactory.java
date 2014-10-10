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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views;

import java.util.Set;

import javax.swing.JPanel;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.graph.ArcUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views.NodeUI;

/**
 * Továrna na výchozí implementace posluchače návrhu hrany.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultArcDesignListenerFactory implements
        ArcDesignListenerFactory {

    /**
     * Vytvoří továrnu.
     * 
     * @return továrna
     */
    public static DefaultArcDesignListenerFactory create() {
        return new DefaultArcDesignListenerFactory();
    }

    private DefaultArcDesignListenerFactory() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.
     * ArcDesignListenerFactory#produce()
     */
    @Override
    public ArcDesignListener produce(final JPanel designPanel,
            final Set<NodeUI> nodes, final Set<ArcUI> arcs,
            final NetworkController networkController) {
        Preconditions.checkNotNull(designPanel);
        Preconditions.checkNotNull(nodes);
        Preconditions.checkNotNull(arcs);
        Preconditions.checkNotNull(networkController);

        return DefaultArcDesignListener.create(designPanel, nodes, arcs,
                networkController);
    }

}
