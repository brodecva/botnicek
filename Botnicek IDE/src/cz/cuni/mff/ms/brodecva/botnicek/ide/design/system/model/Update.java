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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model;

import java.util.Map;
import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;

/**
 * Reprezentace nutné aktualizace prvků systému po strukturální změně (přidání, odebrání uzlu či hrany).
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Update {

    /**
     * Vrátí odblokované uzly, které byly dříve blokované odkazy z odstraněných uzlů.
     * 
     * @return odblokované uzly, které byly dříve blokované odkazy z odstraněných uzlů
     */
    Map<EnterNode, RecurentArc> getReferencesRemoved();

    /**
     * Vrátí nově přidané vstupní uzly.
     * 
     * @return nově přidané vstupní uzly
     */
    Set<EnterNode> getInitialsAdded();

    /**
     * Vrátí nově odebrané vstupní uzly.
     * 
     * @return nově odebrané vstupní uzly
     */
    Set<EnterNode> getInitialsRemoved();

    /**
     * Vrátí náhrady uzlu za uzel.
     * 
     * @return náhrady uzlu za uzel
     */
    Set<NodeSwitch> getAffected();

    /**
     * @return odstranění nepořádku
     */
    Set<Arc> getEdgesRemoved();

}