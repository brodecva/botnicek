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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController;

/**
 * Pohled zobrazující sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface NetworkDisplayView {
    /**
     * Zobrazí síť řízenou danými řadiči.
     * 
     * @param networkController
     *            řadič sítě
     * @param arcsController
     *            řadič hran sítě
     * @param nodesController
     *            řadič uzlů sítě
     * @param arcDisplayController
     *            řadič zobrazovače hran
     */
    void networkDisplayed(NetworkController networkController,
            ArcsController arcsController, NodesController nodesController,
            ArcPropertiesDisplayController arcDisplayController);
}
