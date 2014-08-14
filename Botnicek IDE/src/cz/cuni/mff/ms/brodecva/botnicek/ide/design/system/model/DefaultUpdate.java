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

/**
 * Implemenace {@link Update}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultUpdate implements Update {
    
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
            final Set<? extends DefaultNodeSwitch> affected,
            final Set<? extends Arc> edgesRemoved) {
        return new DefaultUpdate(referencesRemoved, initialsAdded, initialsRemoved, affected, edgesRemoved);
    }
    
    private DefaultUpdate(final Map<? extends EnterNode, ? extends RecurentArc> referencesRemoved,
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

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.Update#getReferencesRemoved()
     */
    @Override
    public Map<EnterNode, RecurentArc> getReferencesRemoved() {
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
    public Set<NodeSwitch> getAffected() {
        return affected;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.Update#getEdgesRemoved()
     */
    @Override
    public Set<Arc> getEdgesRemoved() {
        return this.edgesRemoved;
    }
}