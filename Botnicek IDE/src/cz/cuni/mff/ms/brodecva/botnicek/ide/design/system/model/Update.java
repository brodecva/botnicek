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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;

/**
 * Reprezentace nutné aktualizace prvků systému po strukturální změně (přidání, odebrání uzlu či hrany).
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class Update {
    
    /**
     * Náhrada uzlu za uzel.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    static final class NodeSwitch {
        private final Node from;
        private final Node to;
        
        /**
         * Vytvoří záznam o náhradě.
         * 
         * @param from původní uzel
         * @param to nová uzel
         * @return záznam o náhradě
         */
        public static NodeSwitch of(final Node from, final Node to) {
            return new NodeSwitch(from, to);
        }
            
        private NodeSwitch(final Node from, final Node to) {
            Preconditions.checkNotNull(from);
            Preconditions.checkNotNull(to);
            
            this.from = from;
            this.to = to;
        }
        
        /**
         * Vrátí výchozí uzel.
         * 
         * @return výchozí uzel
         */
        public Node getFrom() {
            return from;
        }

        /**
         * Vrátí nová uzel.
         * 
         * @return nová uzel
         */
        public Node getTo() {
            return to;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "NodeSwitch [from=" + from + ", to=" + to + "]";
        }
    }
    
    private final Map<EnterNode, RecurentArc> referencesRemoved;
    private final Set<EnterNode> initialsAdded;
    private final Set<EnterNode> initialsRemoved;
    private final Set<NodeSwitch> affected;
    private final Set<Arc> edgesRemoved;
    
    /**
     * Vytvoří výzvu k provedení aktualizace.
     * 
     * @param referencesRemoved odblokované uzly, které byly dříve blokované odkazy z odstraněných uzlů
     * @param initialsAdded nově přidané vstupní uzly
     * @param initialsRemoved nově odebrané vstupní uzly
     * @param affected náhrady uzlu za uzel
     * @param edgesRemoved odstraněné hrany
     * @return výzva k provedení aktualizace
     */
    public static Update of(final Map<? extends EnterNode, ? extends RecurentArc> referencesRemoved,
            final Set<? extends EnterNode> initialsAdded,
            final Set<? extends EnterNode> initialsRemoved,
            final Set<? extends NodeSwitch> affected,
            final Set<? extends Arc> edgesRemoved) {
        return new Update(referencesRemoved, initialsAdded, initialsRemoved, affected, edgesRemoved);
    }
    
    private Update(final Map<? extends EnterNode, ? extends RecurentArc> referencesRemoved,
            final Set<? extends EnterNode> initialsAdded,
            final Set<? extends EnterNode> initialsRemoved,
            final Set<? extends NodeSwitch> affected,
            final Set<? extends Arc> edgesRemoved) {
        Preconditions.checkNotNull(referencesRemoved);
        Preconditions.checkNotNull(initialsAdded);
        Preconditions.checkNotNull(initialsRemoved);
        Preconditions.checkNotNull(affected);
        Preconditions.checkNotNull(edgesRemoved);
        
        this.referencesRemoved = ImmutableMap.copyOf(referencesRemoved);
        this.initialsAdded = ImmutableSet.copyOf(initialsAdded);
        this.initialsRemoved = ImmutableSet.copyOf(initialsRemoved);
        this.affected = ImmutableSet.copyOf(affected);
        this.edgesRemoved = ImmutableSet.copyOf(edgesRemoved);
    }

    /**
     * Vrátí odblokované uzly, které byly dříve blokované odkazy z odstraněných uzlů.
     * 
     * @return odblokované uzly, které byly dříve blokované odkazy z odstraněných uzlů
     */
    public Map<EnterNode, RecurentArc> getReferencesRemoved() {
        return referencesRemoved;
    }

    /**
     * Vrátí nově přidané vstupní uzly.
     * 
     * @return nově přidané vstupní uzly
     */
    public Set<EnterNode> getInitialsAdded() {
        return initialsAdded;
    }

    /**
     * Vrátí nově odebrané vstupní uzly.
     * 
     * @return nově odebrané vstupní uzly
     */
    public Set<EnterNode> getInitialsRemoved() {
        return initialsRemoved;
    }
    
    /**
     * Vrátí náhrady uzlu za uzel.
     * 
     * @return náhrady uzlu za uzel
     */
    public Set<NodeSwitch> getAffected() {
        return affected;
    }

    /**
     * @return odstranění nepořádku
     */
    public Set<Arc> getEdgesRemoved() {
        return this.edgesRemoved;
    }

    
}