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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;

/**
 * Stavitel reprezentace aktualizace systémového grafu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Update
 */
public interface UpdateBuilder {
    /**
     * Přidá hranu, která je odstraňována, a tak je možné uvolnit příslušnou referenci jejího cílového uzlu.
     * 
     * @param referring odstraněná odkazující hrana
     */
    void addRemovedReference(RecurentArc referring);

    /**
     * Přidá nový vstupní uzel.
     * 
     * @param newInitial nový vstupní uzel
     */
    void addNewInitial(EnterNode newInitial);

    /**
     * Přidá odstraněný vstupní uzel.
     * 
     * @param removedInitial odstraněný vstupní uzel
     */
    void addRemovedInitial(EnterNode removedInitial);

    /**
     * Přidá uzel, který bude nahrazen a jeho náhradu.
     * 
     * @param from nahrazený
     * @param to náhrada
     */
    void addSwitched(Node from, Node to);

    /**
     * Přidá hranu k odstranění.
     * 
     * @param removedEdge hrana k odstranění
     */
    void addRemovedEdge(Arc removedEdge);
    
    /**
     * Sestaví aktualizaci.
     * 
     * @return aktualizace
     */
    Update build();
}
