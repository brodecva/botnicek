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
 * Implementace {@link Update}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultUpdate implements Update {
    
    private final Set<RecurentArc> referencesRemoved;
    private final Set<EnterNode> initialsAdded;
    private final Set<EnterNode> initialsRemoved;
    private final Map<Node, Node> switched;
    private final Set<Arc> edgesRemoved;
    
    /**
     * Vytvoří výzvu k provedení aktualizace.
     * 
     * @param referencesRemoved odstraněné odkazující hrany
     * @param initialsAdded nově přidané vstupní uzly
     * @param initialsRemoved nově odebrané vstupní uzly
     * @param swithed náhrady uzlu za uzel
     * @param edgesRemoved odstraněné hrany
     * @return výzva k provedení aktualizace
     */
    public static Update of(final Set<? extends RecurentArc> referencesRemoved,
            final Set<? extends EnterNode> initialsAdded,
            final Set<? extends EnterNode> initialsRemoved,
            final Map<? extends Node, ? extends Node> swithed,
            final Set<? extends Arc> edgesRemoved) {
        return new DefaultUpdate(referencesRemoved, initialsAdded, initialsRemoved, swithed, edgesRemoved);
    }
    
    private DefaultUpdate(final Set<? extends RecurentArc> referencesRemoved,
            final Set<? extends EnterNode> initialsAdded,
            final Set<? extends EnterNode> initialsRemoved,
            final Map<? extends Node, ? extends Node> swithed,
            final Set<? extends Arc> edgesRemoved) {
        Preconditions.checkNotNull(referencesRemoved);
        Preconditions.checkNotNull(initialsAdded);
        Preconditions.checkNotNull(initialsRemoved);
        Preconditions.checkNotNull(swithed);
        Preconditions.checkNotNull(edgesRemoved);
        
        this.referencesRemoved = ImmutableSet.copyOf(referencesRemoved);
        this.initialsAdded = ImmutableSet.copyOf(initialsAdded);
        this.initialsRemoved = ImmutableSet.copyOf(initialsRemoved);
        this.switched = ImmutableMap.copyOf(swithed);
        this.edgesRemoved = ImmutableSet.copyOf(edgesRemoved);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.Update#getReferencesRemoved()
     */
    @Override
    public Set<RecurentArc> getReferencesRemoved() {
        return referencesRemoved;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.Update#getInitialsAdded()
     */
    @Override
    public Set<EnterNode> getInitialsAdded() {
        return initialsAdded;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.Update#getInitialsRemoved()
     */
    @Override
    public Set<EnterNode> getInitialsRemoved() {
        return initialsRemoved;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.Update#getAffected()
     */
    @Override
    public Map<Node, Node> getSwitched() {
        return switched;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.Update#getEdgesRemoved()
     */
    @Override
    public Set<Arc> getEdgesRemoved() {
        return this.edgesRemoved;
    }
}