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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesReducedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.RealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class SystemGraph implements DirectedMultigraph<Node, Arc> {
    
    private final DirectedMultigraph<Node, Arc> innerGraph = DefaultDirectedMultigraph.create();
    private final Map<String, Node> namesToNodes = new HashMap<>();
    private final Map<String, Arc> namesToArcs = new HashMap<>();
    
    private final Dispatcher dispatcher;
    
    public static SystemGraph create(final Dispatcher dispatcher) {
        return new SystemGraph(dispatcher);
    }
    
    private SystemGraph(final Dispatcher dispatcher) {
        Preconditions.checkNotNull(dispatcher);
        
        this.dispatcher = dispatcher;
    }
    
    public Node getVertex(final String name) {
        Preconditions.checkNotNull(name);
        
        return this.namesToNodes.get(name);
    }
    
    public Arc getEdge(final String name) {
        Preconditions.checkNotNull(name);
        
        return this.namesToArcs.get(name);
    }
    
    public void removeAndRealign(final Node removed, final RealignmentProcessor processor, final Map<? extends EnterNode, ? extends Collection<? extends RecurentArc>> references, final Collection<? extends EnterNode> initialNodes) throws IllegalArgumentException {
        this.innerGraph.extractVertex(removed, new Function<Node, Node>() {
            @Override
            public Node apply(final Node input) {
                final Node realigned = processor.realign(input);
                final Collection<? extends RecurentArc> referring = references.get(realigned);
                Preconditions.checkArgument(referring == null || realigned.equals(input), "Removal forbidden. The adjcent node " + input + " of the node " + removed + " cannot be reduced to isolated point as it is referred as entry point to network " + removed.getNetwork() + "by " + referring + ".");
                
                if (initialNodes.contains(input) && !input.equals(realigned)) {
                    dispatcher.fire(AvailableReferencesReducedEvent.create(ImmutableSet.of((EnterNode) input)));
                }
                
                return realigned;
            }
        }, new Callback<Node>() {
            @Override
            public void call(final Node parameter) {
                initialNodes.remove(parameter);
            }
        }, new Callback<Arc>() {
            @Override
            public void call(final Arc parameter) {
                if (parameter instanceof RecurentArc) {
                    final RecurentArc referring = (RecurentArc) parameter;
                    final EnterNode target = referring.getTarget();
                    
                    references.get(target).remove(referring);
                }
            }
        });
    }
    
    public void removeAndRealign(final Arc removed, final RealignmentProcessor processor, final Map<? extends EnterNode, ? extends Collection<? extends RecurentArc>> references, Set<EnterNode> initials) throws IllegalArgumentException {
        this.innerGraph.removeEdge(removed);
        
        final Node from = removed.getFrom();
        final Node to = removed.getTo();
        
        final Node realignedFrom = processor.realign(from);
        final Collection<? extends RecurentArc> referring = references.get(from);
        Preconditions.checkArgument(referring == null || realignedFrom.equals(from) || (referring.size() == 1 && referring.contains(removed)), "Removal forbidden. The node " + from + " the removed arc " + removed + " comes from cannot be reduced to an isolated point as it is referred as entry point to network " + from.getNetwork() + "by " + referring + ".");
        
        this.innerGraph.replaceVertex(realignedFrom, from);
        this.innerGraph.replaceVertex(processor.realign(to), to);
        
        if (initials.contains(from) && !from.equals(realignedFrom)) {
            initials.remove(from);
            this.dispatcher.fire(AvailableReferencesReducedEvent.create(ImmutableSet.of((EnterNode) from)));
        }
    }
    
    public void addAndRealign(final Arc added, final Node from, final Node to,
            final RealignmentProcessor processor) {
        this.innerGraph.add(added, from, to);
        
        this.innerGraph.replaceVertex(processor.realign(from), from);
        this.innerGraph.replaceVertex(processor.realign(to), to);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#containsVertex(java.lang.Object)
     */
    @Override
    public boolean containsVertex(final Node vertex) {
        return this.innerGraph.containsVertex(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#containsEdge(java.lang.Object)
     */
    @Override
    public boolean containsEdge(Arc edge) {
        return this.innerGraph.containsEdge(edge);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#add(java.lang.Object)
     */
    @Override
    public void add(Node vertex) {
        this.innerGraph.add(vertex);
        this.namesToNodes.put(vertex.getName(), vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#add(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public void add(Arc edge, Node from, Node to) {
        this.innerGraph.add(edge, from, to);
        this.namesToArcs.put(edge.getName(), edge);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#removeVertex(java.lang.Object)
     */
    @Override
    public void removeVertex(Node vertex) {
        this.innerGraph.removeVertex(vertex);
        this.namesToNodes.remove(vertex.getName());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#extractVertex(java.lang.Object, com.google.common.base.Function)
     */
    @Override
    public void
            extractVertex(Node vertex, Function<Node, Node> neighboursRepair, final Callback<Node> neighboursCall, Callback<Arc> connectionsCall) {
        this.innerGraph.extractVertex(vertex, neighboursRepair, neighboursCall, connectionsCall);
        this.namesToNodes.remove(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#removeEdge(java.lang.Object)
     */
    @Override
    public void removeEdge(Arc edge) {
        this.innerGraph.removeEdge(edge);
        this.namesToArcs.remove(edge.getName());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#replaceVertex(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceVertex(Node fresh, Node old) {
        this.innerGraph.replaceVertex(fresh, old);
        this.namesToNodes.remove(old.getName());
        this.namesToNodes.put(fresh.getName(), fresh);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#replaceEdge(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceEdge(Arc fresh, Arc old) {
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
    public Set<Arc> ins(Node vertex) {
        return this.innerGraph.ins(vertex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#outs(java.lang.Object)
     */
    @Override
    public Set<Arc> outs(Node vertex) {
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
    public Node from(Arc arc) {
        return this.innerGraph.from(arc);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.DirectedMultigraph#to(java.lang.Object)
     */
    @Override
    public Node to(Arc arc) {
        return this.innerGraph.to(arc);
    }
}
