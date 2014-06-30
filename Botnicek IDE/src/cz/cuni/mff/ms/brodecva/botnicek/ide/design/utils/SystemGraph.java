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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesReducedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.SystemGraph.Update;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class SystemGraph implements DirectedMultigraph<Node, Arc> {
    
    public final static class Update {
        private final Map<EnterNode, RecurentArc> referencesRemoved;
        private final Set<EnterNode> initialsAdded;
        private final Set<EnterNode> initialsRemoved;
        private final Set<IsolatedNode> isolatedAdded;
        private final Set<IsolatedNode> isolatedRemoved;
        
        public static Update of(final Map<? extends EnterNode, ? extends RecurentArc> referencesRemoved,
                final Set<? extends EnterNode> initialsAdded,
                final Set<? extends EnterNode> initialsRemoved,
                final Set<? extends IsolatedNode> isolatedAdded,
                final Set<? extends IsolatedNode> isolatedRemoved) {
            return new Update(referencesRemoved, initialsAdded, initialsRemoved, isolatedAdded, isolatedRemoved);
        }
        
        private Update(final Map<? extends EnterNode, ? extends RecurentArc> referencesRemoved,
                final Set<? extends EnterNode> initialsAdded,
                final Set<? extends EnterNode> initialsRemoved,
                final Set<? extends IsolatedNode> isolatedAdded,
                final Set<? extends IsolatedNode> isolatedRemoved) {
            Preconditions.checkNotNull(referencesRemoved);
            Preconditions.checkNotNull(initialsAdded);
            Preconditions.checkNotNull(initialsRemoved);
            Preconditions.checkNotNull(isolatedAdded);
            Preconditions.checkNotNull(isolatedRemoved);
            
            this.referencesRemoved = ImmutableMap.copyOf(referencesRemoved);
            this.initialsAdded = ImmutableSet.copyOf(initialsAdded);
            this.initialsRemoved = ImmutableSet.copyOf(initialsRemoved);
            this.isolatedAdded = ImmutableSet.copyOf(isolatedAdded);
            this.isolatedRemoved = ImmutableSet.copyOf(isolatedRemoved);
        }

        /**
         * @return the referencesRemoved
         */
        public Map<EnterNode, RecurentArc> getReferencesRemoved() {
            return referencesRemoved;
        }

        /**
         * @return the initialsAdded
         */
        public Set<EnterNode> getInitialsAdded() {
            return initialsAdded;
        }

        /**
         * @return the initialsRemoved
         */
        public Set<EnterNode> getInitialsRemoved() {
            return initialsRemoved;
        }

        /**
         * @return the isolatedAdded
         */
        public Set<IsolatedNode> getIsolatedAdded() {
            return isolatedAdded;
        }

        /**
         * @return the isolatedRemoved
         */
        public Set<IsolatedNode> getIsolatedRemoved() {
            return isolatedRemoved;
        }
    }
    
    private final DirectedMultigraph<Node, Arc> innerGraph = DefaultDirectedMultigraph.create();
    private final Map<NormalWord, Node> namesToNodes = new HashMap<>();
    private final Map<NormalWord, Arc> namesToArcs = new HashMap<>();
    
    private final Dispatcher dispatcher;
    
    public static SystemGraph create(final Dispatcher dispatcher) {
        return new SystemGraph(dispatcher);
    }
    
    private SystemGraph(final Dispatcher dispatcher) {
        Preconditions.checkNotNull(dispatcher);
        
        this.dispatcher = dispatcher;
    }
    
    public Node getVertex(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        return this.namesToNodes.get(name);
    }
    
    public Arc getEdge(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        return this.namesToArcs.get(name);
    }
    
    public Update removeAndRealign(final Node removed, final RealignmentProcessor processor, final Map<? extends EnterNode, ? extends Collection<? extends RecurentArc>> references, final Set<? extends EnterNode> initialNodes, final Set<? extends IsolatedNode> isolatedNodes) throws IllegalArgumentException {
        Preconditions.checkNotNull(removed);
        Preconditions.checkNotNull(references);
        Preconditions.checkNotNull(initialNodes);
        
        final ImmutableMap.Builder<EnterNode, RecurentArc> referencesRemovedBuilder = ImmutableMap.builder();
        final ImmutableSet.Builder<EnterNode> initialsAddedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<EnterNode> initialsRemovedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<IsolatedNode> isolatedAddedBuilder = ImmutableSet.builder();
        
        this.innerGraph.extractVertex(removed, new Function<Node, Node>() {
            @Override
            public Node apply(final Node input) {
                return processor.realign(input);
            }
        }, new Callback<Node>() {
            @Override
            public void call(final Node input) {
                final Node realigned = processor.realign(input);
                
                final Collection<? extends RecurentArc> referring = references.get(input);
                Preconditions.checkArgument(referring == null || realigned.equals(input), "Removal forbidden. The adjcent node " + input + " of the node " + removed + " cannot be reduced to isolated point as it is referred as entry point to network " + removed.getNetwork() + "by " + referring + ".");
                
                if (initialNodes.contains(input) && !input.equals(realigned)) {
                    initialsRemovedBuilder.add((EnterNode) input);
                }
                
                if (!input.equals(input) && (realigned instanceof EnterNode)) {
                    initialsAddedBuilder.add((EnterNode) input);
                }
                
                if (!input.equals(input) && (realigned instanceof IsolatedNode)) {
                    isolatedAddedBuilder.add((IsolatedNode) input);
                }
            }
        }, new Callback<Arc>() {
            @Override
            public void call(final Arc parameter) {
                if (parameter instanceof RecurentArc) {
                    final RecurentArc reference = (RecurentArc) parameter;
                    final EnterNode target = reference.getTarget();
                    
                    referencesRemovedBuilder.put(target, reference);
                }
            }
        });
        
        return Update.of(referencesRemovedBuilder.build(), initialsAddedBuilder.build(), initialsRemovedBuilder.build(), isolatedAddedBuilder.build(), ImmutableSet.<IsolatedNode>of());
    }
    
    public Update removeAndRealign(final Arc removed, final RealignmentProcessor processor, final Map<? extends EnterNode, ? extends Collection<? extends RecurentArc>> references, Set<? extends EnterNode> initials, Set<? extends IsolatedNode> isolated) throws IllegalArgumentException {
        Preconditions.checkNotNull(removed);
        Preconditions.checkNotNull(processor);
        Preconditions.checkNotNull(references);
        
        this.innerGraph.removeEdge(removed);
        
        final Node from = removed.getFrom();
        final Node to = removed.getTo();
        final Node newFrom = processor.realign(from);
        final Node newTo = processor.realign(to);
        final Collection<? extends RecurentArc> referring = references.get(from);
        try {            
            Preconditions.checkArgument(referring == null || newFrom.equals(from) || (referring.size() == 1 && referring.contains(removed)), "Removal forbidden. The node " + from + " the removed arc " + removed + " comes from cannot be reduced to an isolated point as it is referred as entry point to network " + from.getNetwork() + "by " + referring + ".");
        } catch (final Exception e) {
            this.innerGraph.add(removed, from, to);
            
            throw e;
        }
        
        this.innerGraph.replaceVertex(newFrom, from);
        this.innerGraph.replaceVertex(newTo, to);
        
        final ImmutableMap.Builder<EnterNode, RecurentArc> referencesRemovedBuilder = ImmutableMap.builder();
        final ImmutableSet.Builder<EnterNode> initialsAddedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<EnterNode> initialsRemovedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<IsolatedNode> isolatedAddedBuilder = ImmutableSet.builder();
        
        if (removed instanceof RecurentArc) {
            final RecurentArc reference = (RecurentArc) removed;
            final EnterNode target = reference.getTarget();
            
            references.get(target).remove(reference);
            referencesRemovedBuilder.put(target, reference);
        }
        
        if (!from.equals(newFrom)) {
            if (initials.contains(from)) {
                initialsRemovedBuilder.add((EnterNode) from);                    
            }
            
            if (newFrom instanceof IsolatedNode) {
                isolatedAddedBuilder.add((IsolatedNode) newFrom);
            }
        }
        
        if (!to.equals(newTo)) {
            if (newTo instanceof EnterNode) {
                initialsAddedBuilder.add((EnterNode) newTo);
            } else if (newTo instanceof IsolatedNode) {
                isolatedAddedBuilder.add((IsolatedNode) newTo);
            }
        }
        
        return Update.of(referencesRemovedBuilder.build(), initialsAddedBuilder.build(), initialsRemovedBuilder.build(), isolatedAddedBuilder.build(), ImmutableSet.<IsolatedNode>of());
    }
    
    public Update addAndRealign(final Arc added, final Node from, final Node to,
            final RealignmentProcessor processor, final Set<? extends EnterNode> initials, final Set<? extends IsolatedNode> isolated) {
        Preconditions.checkNotNull(added);
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkNotNull(processor);
        Preconditions.checkNotNull(initials);
        Preconditions.checkNotNull(isolated);
                
        this.innerGraph.add(added, from, to);
        
        final Node newFrom = processor.realign(from);
        final Node newTo = processor.realign(to);
        
        this.innerGraph.replaceVertex(newFrom, from);
        this.innerGraph.replaceVertex(newTo, to);
        
        final ImmutableSet.Builder<EnterNode> initialsAddedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<EnterNode> initialsRemovedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<IsolatedNode> isolatedRemovedBuilder = ImmutableSet.builder();
        
        if (!from.equals(newFrom)) {
            if (isolated.contains(from) ) {
                isolatedRemovedBuilder.add((IsolatedNode) from);
            } else if (initials.contains(from)) {
                assert false;
            }
            
            if (newFrom instanceof EnterNode) {
                initialsAddedBuilder.add((EnterNode) newFrom);
            }
        }
        
        if (!to.equals(newTo)) {
            if (isolated.contains(to) ) {
                isolatedRemovedBuilder.add((IsolatedNode) to);
            } else if (initials.contains(to)) {
                initialsRemovedBuilder.add((EnterNode) to);
            }
        }
        
        return Update.of(ImmutableMap.<EnterNode, RecurentArc>of(), initialsAddedBuilder.build(), initialsRemovedBuilder.build(), ImmutableSet.<IsolatedNode>of(), isolatedRemovedBuilder.build());
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#containsVertex(java.lang.Object)
     */
    @Override
    public boolean containsVertex(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        return this.innerGraph.containsVertex(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#containsEdge(java.lang.Object)
     */
    @Override
    public boolean containsEdge(final Arc edge) {
        Preconditions.checkNotNull(edge);
        
        return this.innerGraph.containsEdge(edge);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#add(java.lang.Object)
     */
    @Override
    public void add(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        this.innerGraph.add(vertex);
        this.namesToNodes.put(vertex.getName(), vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#add(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public void add(final Arc edge, final Node from, final Node to) {
        Preconditions.checkNotNull(edge);
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        
        this.innerGraph.add(edge, from, to);
        this.namesToArcs.put(edge.getName(), edge);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#removeVertex(java.lang.Object)
     */
    @Override
    public void removeVertex(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        this.innerGraph.removeVertex(vertex);
        this.namesToNodes.remove(vertex.getName());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#extractVertex(java.lang.Object, com.google.common.base.Function)
     */
    @Override
    public void
            extractVertex(final Node vertex, final Function<Node, Node> neighboursRepair, final Callback<Node> neighboursCall, final Callback<Arc> connectionsCall) {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(neighboursRepair);
        Preconditions.checkNotNull(neighboursCall);
        Preconditions.checkNotNull(connectionsCall);
        
        this.innerGraph.extractVertex(vertex, neighboursRepair, neighboursCall, connectionsCall);
        this.namesToNodes.remove(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#removeEdge(java.lang.Object)
     */
    @Override
    public void removeEdge(final Arc edge) {
        Preconditions.checkNotNull(edge);
        
        this.innerGraph.removeEdge(edge);
        this.namesToArcs.remove(edge.getName());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#replaceVertex(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceVertex(final Node fresh, final Node old) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);
        
        this.innerGraph.replaceVertex(fresh, old);
        this.namesToNodes.remove(old.getName());
        this.namesToNodes.put(fresh.getName(), fresh);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#replaceEdge(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceEdge(final Arc fresh, final Arc old) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);
        
        this.innerGraph.replaceEdge(fresh, old);
        this.namesToArcs.remove(old.getName());
        this.namesToArcs.put(fresh.getName(), fresh);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#vertices()
     */
    @Override
    public Set<Node> vertices() {
        return this.innerGraph.vertices();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#edges()
     */
    @Override
    public Set<Arc> edges() {
        return this.innerGraph.edges();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#ins(java.lang.Object)
     */
    @Override
    public Set<Arc> ins(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        return this.innerGraph.ins(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#outs(java.lang.Object)
     */
    @Override
    public Set<Arc> outs(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        return this.innerGraph.outs(vertex);
    }
    
    public Set<Arc> connections(final Node vertex, final Direction direction) {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(direction);
        
        switch (direction) {
            case IN:
                    return this.innerGraph.ins(vertex);
            case OUT:
                    return this.innerGraph.outs(vertex);
                default:
                    throw new UnsupportedOperationException();
        }
    }

    /**
     * @param arc
     * @param direction
     */
    public Node attached(final Arc arc, final Direction direction) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkNotNull(direction);
        
        switch (direction) {
        case IN:
                return this.innerGraph.from(arc);
        case OUT:
                return this.innerGraph.to(arc);
            default:
                throw new UnsupportedOperationException();
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#from(java.lang.Object)
     */
    @Override
    public Node from(final Arc arc) {
        Preconditions.checkNotNull(arc);
        
        return this.innerGraph.from(arc);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#to(java.lang.Object)
     */
    @Override
    public Node to(final Arc arc) {
        Preconditions.checkNotNull(arc);
        
        return this.innerGraph.to(arc);
    }

    /**
     * @param first
     * @param second
     * @param direction
     * @return
     */
    public boolean adjoins(final Node first, final Node second, final Direction direction) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);
        Preconditions.checkNotNull(direction);
        
        final Set<Arc> connections = connections(first, direction);
        
        for (final Arc connection : connections) {
            if (connection.isAttached(second, direction.getOpposite())) {
                return true;
            }
        }
        
        return false;
    }
}
