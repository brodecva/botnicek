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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultUpdateBuilder implements UpdateBuilder {

    private final ImmutableSet.Builder<RecurentArc> referencesRemovedBuilder = ImmutableSet.builder();
    private final ImmutableSet.Builder<EnterNode> initialsAddedBuilder = ImmutableSet.builder();
    private final ImmutableSet.Builder<EnterNode> initialsRemovedBuilder = ImmutableSet.builder();
    private final ImmutableMap.Builder<Node, Node> switchedBuilder = ImmutableMap.builder();
    private final ImmutableSet.Builder<Arc> removedEdgesBuilder = ImmutableSet.builder();
    
    /**
     * Vytvoří stavitele.
     * 
     * @return stavitel
     */
    public static DefaultUpdateBuilder create() {
        return new DefaultUpdateBuilder();
    }
    
    private DefaultUpdateBuilder() {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.UpdateBuilder#addRemovedReference(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode)
     */
    @Override
    public void addRemovedReference(final RecurentArc referring) {
        Preconditions.checkNotNull(referring);
        
        this.referencesRemovedBuilder.add(referring);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.UpdateBuilder#addNewInitial(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode)
     */
    @Override
    public void addNewInitial(final EnterNode newInitial) {
        Preconditions.checkNotNull(newInitial);
        
        this.initialsAddedBuilder.add(newInitial);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.UpdateBuilder#addRemovedInitial(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode)
     */
    @Override
    public void addRemovedInitial(final EnterNode removedInitial) {
        Preconditions.checkNotNull(removedInitial);

        this.initialsRemovedBuilder.add(removedInitial);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.UpdateBuilder#addSwitched(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public void addSwitched(final Node from, final Node to) {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        
        this.switchedBuilder.put(from, to);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.UpdateBuilder#addRemovedEdge(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
     */
    @Override
    public void addRemovedEdge(final Arc removedEdge) {
        Preconditions.checkNotNull(removedEdge);
        
        this.removedEdgesBuilder.add(removedEdge);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.UpdateBuilder#build()
     */
    @Override
    public Update build() {
        return DefaultUpdate.of(referencesRemovedBuilder.build(), initialsAddedBuilder.build(), initialsRemovedBuilder.build(), switchedBuilder.build(), removedEdgesBuilder.build());
    }

}
