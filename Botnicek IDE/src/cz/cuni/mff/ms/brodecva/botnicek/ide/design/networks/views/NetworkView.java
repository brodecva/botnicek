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

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;

/**
 * Pohled na síť.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface NetworkView {
    
    /**
     * Aktualizuje zobrazení sítě tak, aby zahrnovalo přidaný uzel.
     * 
     * @param node přidaný uzel
     */
    void nodeAdded(Node node);
    
    /**
     * Aktualizuje zobrazení sítě tak, aby zahrnovalo přidanou hranu.
     * 
     * @param arc přidaná hrana
     */
    void arcAdded(Arc arc);
        
    /**
     * Aktualizuje zobrazení názvu sítě.
     * 
     * @param network nová verze sítě
     */
    void renamed(Network network);
    
    /**
     * Zpraví pohled o odebrání sítě.s
     */
    void removed();
    
    /**
     * Vybere síť pro uživatele.
     */
    void selected();
}
