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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultDirectedGraph;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;

/**
 * Implementace {@link SystemGraph}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultSystemGraph implements SystemGraph {
    
    private final DirectedGraph<Node, Arc> baseGraph;
    private final Map<NormalWord, Node> namesToNodes = new HashMap<>();
    private final Map<NormalWord, Arc> namesToArcs = new HashMap<>();
    
    /**
     * Vytvoří graf.
     * 
     * @return prázdný graf
     */
    public static DefaultSystemGraph create() {
        return of(DefaultDirectedGraph.<Node, Arc>create());
    }
    
    /**
     * Dekoruje základní graf.
     * 
     * @param baseGraph základní graf
     * 
     * @return obohacený základní graf
     */
    public static DefaultSystemGraph of(final DirectedGraph<Node, Arc> baseGraph) {
        Preconditions.checkNotNull(baseGraph);
        
        return new DefaultSystemGraph(baseGraph);
    }
    
    private DefaultSystemGraph(final DirectedGraph<Node, Arc> baseGraph) {
        this.baseGraph = baseGraph;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#getVertex(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public Node getVertex(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        return this.namesToNodes.get(name);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#getEdge(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public Arc getEdge(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        return this.namesToArcs.get(name);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#removeAndRealign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor, java.util.Map, java.util.Set)
     */
    @Override
    public Update removeAndRealign(final Node removed, final RealignmentProcessor processor, final Map<? extends EnterNode, ? extends Collection<? extends RecurentArc>> references, final Set<? extends EnterNode> initialNodes) throws IllegalArgumentException {
        Preconditions.checkNotNull(removed);
        Preconditions.checkNotNull(references);
        Preconditions.checkNotNull(initialNodes);
        
        final ImmutableMap.Builder<EnterNode, RecurentArc> referencesRemovedBuilder = ImmutableMap.builder();
        final ImmutableSet.Builder<EnterNode> initialsAddedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<EnterNode> initialsRemovedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<DefaultNodeSwitch> affectedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<Arc> removedEdgesBuilder = ImmutableSet.builder();
        
        extractVertex(removed, new Function<Node, Node>() {
            @Override
            public Node apply(final Node input) {
                final Node realigned = processor.realign(input);
                return realigned;
            }
        }, new Callback<Node>() {
            @Override
            public void call(final Node input) {
                final Node realigned = processor.realign(input);               
                
                if (!realigned.equals(input)) {
                    final Collection<? extends RecurentArc> referring = references.get(input);
                    if (Presence.isPresent(referring)) {
                        throw new IllegalArgumentException(ExceptionLocalizer.print("NodeRemovalForbidden", input.getName(), removed.getName(), removed.getNetwork(), referring.iterator().next().getName(), referring.iterator().next().getNetwork().getName()));
                    }
                    
                    if (initialNodes.contains(input)) {
                        initialsRemovedBuilder.add((EnterNode) input);
                    }
                    
                    if (realigned instanceof EnterNode) {
                        initialsAddedBuilder.add((EnterNode) input);
                    }
                    
                    affectedBuilder.add(DefaultNodeSwitch.of(input, realigned));
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
                
                removedEdgesBuilder.add(parameter);
            }
        });
        
        return DefaultUpdate.of(referencesRemovedBuilder.build(), initialsAddedBuilder.build(), initialsRemovedBuilder.build(), affectedBuilder.build(), removedEdgesBuilder.build());
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#removeAndRealign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor, java.util.Map, java.util.Set)
     */
    @Override
    public Update removeAndRealign(final Arc removed, final RealignmentProcessor processor, final Map<? extends EnterNode, ? extends Collection<? extends RecurentArc>> references, Set<? extends EnterNode> initials) throws IllegalArgumentException {
        Preconditions.checkNotNull(removed);
        Preconditions.checkNotNull(processor);
        Preconditions.checkNotNull(references);
        
        removeEdge(removed);
        
        final Node from = removed.getFrom();
        final Node to = removed.getTo();
        final Node newFrom = processor.realign(from);
        final Node newTo = processor.realign(to);
        final Collection<? extends RecurentArc> referring = references.get(from);
        try {            
            Preconditions.checkArgument(Presence.isAbsent(referring) || newFrom.equals(from) || (referring.size() == 1 && referring.contains(removed)), ExceptionLocalizer.print("ArcRemovalForbidden", from.getName(), removed.getName(), from.getNetwork().getName(), referring.iterator().next().getName(), referring.iterator().next().getNetwork().getName()));
        } catch (final Exception e) {
            add(removed, from, to);
            
            throw e;
        }
        
        replaceVertex(newFrom, from);
        replaceVertex(newTo, to);
        
        final ImmutableMap.Builder<EnterNode, RecurentArc> referencesRemovedBuilder = ImmutableMap.builder();
        final ImmutableSet.Builder<EnterNode> initialsAddedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<EnterNode> initialsRemovedBuilder = ImmutableSet.builder();
        
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
        }
        
        if (!to.equals(newTo)) {
            if (newTo instanceof EnterNode) {
                initialsAddedBuilder.add((EnterNode) newTo);
            }
        }
        
        final Set<DefaultNodeSwitch> affected = ImmutableSet.of(DefaultNodeSwitch.of(from, newFrom), DefaultNodeSwitch.of(to, newTo));
        return DefaultUpdate.of(referencesRemovedBuilder.build(), initialsAddedBuilder.build(), initialsRemovedBuilder.build(), affected, ImmutableSet.<Arc>of());
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#addAndRealign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor, java.util.Set)
     */
    @Override
    public Update addAndRealign(final Arc added, final Node from, final Node to,
            final RealignmentProcessor processor, final Set<? extends EnterNode> initials) {
        Preconditions.checkNotNull(added);
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkNotNull(processor);
        Preconditions.checkNotNull(initials);
                
        add(added, from, to);
        
        final Node newFrom = processor.realign(from);
        final Node newTo = processor.realign(to);
        
        replaceVertex(newFrom, from);
        replaceVertex(newTo, to);
        
        final ImmutableSet.Builder<EnterNode> initialsAddedBuilder = ImmutableSet.builder();
        final ImmutableSet.Builder<EnterNode> initialsRemovedBuilder = ImmutableSet.builder();
        
        if (!from.equals(newFrom)) {
            if (initials.contains(from)) {
                assert false;
            }
            
            if (newFrom instanceof EnterNode) {
                initialsAddedBuilder.add((EnterNode) newFrom);
            }
        }
        
        if (!to.equals(newTo)) {
            if (initials.contains(to)) {
                initialsRemovedBuilder.add((EnterNode) to);
            }
        }
        
        final Set<DefaultNodeSwitch> affected = ImmutableSet.of(DefaultNodeSwitch.of(from, newFrom), DefaultNodeSwitch.of(to, newTo));
        return DefaultUpdate.of(ImmutableMap.<EnterNode, RecurentArc>of(), initialsAddedBuilder.build(), initialsRemovedBuilder.build(), affected, ImmutableSet.<Arc>of());
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#containsVertex(java.lang.Object)
     */
    @Override
    public boolean containsVertex(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        return this.baseGraph.containsVertex(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#containsEdge(java.lang.Object)
     */
    @Override
    public boolean containsEdge(final Arc edge) {
        Preconditions.checkNotNull(edge);
        
        return this.baseGraph.containsEdge(edge);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#add(java.lang.Object)
     */
    @Override
    public void add(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        this.baseGraph.add(vertex);
        this.namesToNodes.put(vertex.getName(), vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#add(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public void add(final Arc edge, final Node from, final Node to) {
        Preconditions.checkNotNull(edge);
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        
        this.baseGraph.add(edge, from, to);
        this.namesToArcs.put(edge.getName(), edge);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#removeVertex(java.lang.Object)
     */
    @Override
    public boolean removeVertex(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        final boolean contained = this.baseGraph.removeVertex(vertex);
        if (!contained) {
            return false;
        }
        
        this.namesToNodes.remove(vertex.getName());
        
        return true;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#extractVertex(java.lang.Object, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback)
     */
    @Override
    public void
            extractVertex(final Node vertex, final Function<Node, Node> neighboursRepair, final Callback<Node> neighboursCall, final Callback<Arc> connectionsCall) {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(neighboursRepair);
        Preconditions.checkNotNull(neighboursCall);
        Preconditions.checkNotNull(connectionsCall);
        
        this.baseGraph.extractVertex(vertex, neighboursRepair, neighboursCall, connectionsCall);
        this.namesToNodes.remove(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#removeEdge(java.lang.Object)
     */
    @Override
    public boolean removeEdge(final Arc edge) {
        Preconditions.checkNotNull(edge);
        
        final boolean contained = this.baseGraph.removeEdge(edge);
        if (!contained) {
            return false;
        }
        
        this.namesToArcs.remove(edge.getName());
        
        return true;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#replaceVertex(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceVertex(final Node fresh, final Node old) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);
        
        this.baseGraph.replaceVertex(fresh, old);
        this.namesToNodes.remove(old.getName());
        this.namesToNodes.put(fresh.getName(), fresh);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#replaceEdge(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceEdge(final Arc fresh, final Arc old) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);
        
        this.baseGraph.replaceEdge(fresh, old);
        this.namesToArcs.remove(old.getName());
        this.namesToArcs.put(fresh.getName(), fresh);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#vertices()
     */
    @Override
    public Set<Node> vertices() {
        return this.baseGraph.vertices();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#edges()
     */
    @Override
    public Set<Arc> edges() {
        return this.baseGraph.edges();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#ins(java.lang.Object)
     */
    @Override
    public Set<Arc> ins(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        return this.baseGraph.ins(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#outs(java.lang.Object)
     */
    @Override
    public Set<Arc> outs(final Node vertex) {
        Preconditions.checkNotNull(vertex);
        
        return this.baseGraph.outs(vertex);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#connections(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
     */
    @Override
    public Set<Arc> connections(final Node vertex, final Direction direction) {
        Preconditions.checkNotNull(vertex);
        Preconditions.checkNotNull(direction);
        
        switch (direction) {
            case IN:
                    return this.baseGraph.ins(vertex);
            case OUT:
                    return this.baseGraph.outs(vertex);
                default:
                    throw new UnsupportedOperationException();
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#attached(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
     */
    @Override
    public Node attached(final Arc arc, final Direction direction) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkNotNull(direction);
        
        switch (direction) {
        case IN:
                return this.baseGraph.from(arc);
        case OUT:
                return this.baseGraph.to(arc);
            default:
                throw new UnsupportedOperationException();
        }
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#from(java.lang.Object)
     */
    @Override
    public Node from(final Arc arc) {
        Preconditions.checkNotNull(arc);
        
        return this.baseGraph.from(arc);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#to(java.lang.Object)
     */
    @Override
    public Node to(final Arc arc) {
        Preconditions.checkNotNull(arc);
        
        return this.baseGraph.to(arc);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#adjoins(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
     */
    @Override
    public boolean adjoins(final Node first, final Node second, final Direction direction) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);
        Preconditions.checkNotNull(direction);
        
        final Set<Arc> connections = connections(first, direction);
        
        for (final Arc connection : connections) {
            if (connection.isAttached(second, direction)) {
                return true;
            }
        }
        
        return false;
    }
}
