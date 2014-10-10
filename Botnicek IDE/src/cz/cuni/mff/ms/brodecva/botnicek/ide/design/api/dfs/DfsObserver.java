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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;

/**
 * Pozorovatel průchodu grafem ATN do hloubky.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface DfsObserver {
    /**
     * Zpraví pozorovatele o objevení zpětné hrany sítě.
     * 
     * @param back
     *            zpětná hrana
     */
    void notifyBack(Arc back);

    /**
     * Zpraví pozorovatele o příčné hraně sítě.
     * 
     * @param cross
     *            příčná hrana
     */
    void notifyCross(Arc cross);

    /**
     * Zpraví pozorovatele o objevení uzlu sítě.
     * 
     * @param discovered
     *            objevený uzel
     */
    void notifyDiscovery(Node discovered);

    /**
     * <p>
     * Zpraví pozorovatele o prozkoumání hrany sítě.
     * </p>
     * <p>
     * Implementace návštěvníka musí zavolat nad hranou tuto metodu před voláním
     * metody specifikující typ hrany (stromová, zpětná, příčná).
     * </p>
     * 
     * @param examined
     *            prozkoumaná hrana
     */
    void notifyExamination(Arc examined);

    /**
     * Zpraví pozorovatele o ukončení zpracování uzlu sítě.
     * 
     * @param finished
     *            zpracovaný uzel
     */
    void notifyFinish(Node finished);

    /**
     * Zpraví pozorovatele o navštívení dopředné hrany sítě.
     * 
     * @param forward
     *            dopředná hrana
     */
    void notifyForward(Arc forward);

    /**
     * Zpraví pozorovatele o navštívení stromové hrany sítě.
     * 
     * @param tree
     *            stromová hrana
     */
    void notifyTree(Arc tree);

    /**
     * Zpraví pozorovatele o navštívení sítě.
     * 
     * @param visited
     *            navštívená síť
     */
    void notifyVisit(Network visited);

    /**
     * Zpraví pozorovatele o navštívení kořene systému sítí.
     * 
     * @param visited
     *            navštívený kořen
     */
    void notifyVisit(System visited);
}
