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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.arcs.ArcChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.ArcAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeMovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesExtendedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.AvailableReferencesReducedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.Visitable;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.ArcModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.DefaultArcModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.NodeModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.RealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations.DefaultNodeModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations.DefaultRealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations.IsolatedProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.SystemGraph;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public class System implements Visitable {
    
    private static final String NODE_FACTORY_METHOD_NAME = "create";
    private static final String ARC_FACTORY_METHOD_NAME = "create";

    private final Class<? extends Node> defaultNodeType;
    private final Class<? extends Arc> defaultArcType;
    
    private final String name;
    private final NamingAuthority statesNamingAuthority;
    private final NamingAuthority variablesNamingAuthority;
    private final NodeModifier nodeModifier;
    private final ArcModifier arcModifier;
    private final RealignmentProcessor realignmentProcessor;
    
    private final Multimap<EnterNode, RecurentArc> references = HashMultimap.create();
    private final Map<Network, Set<EnterNode>> initialNodes = new HashMap<>();
    private final SystemGraph graph;
    
    private final Dispatcher dispatcher;
    
    public static System create(final String name, final Dispatcher dispatcher) {
        final NamingAuthority statesNamingAuthority = NormalizedNamingAuthority.create();
        final NodeModifier nodeModifier = DefaultNodeModifier.create(statesNamingAuthority);
        
        return new System(name, statesNamingAuthority, NormalizedNamingAuthority.create(), nodeModifier, DefaultArcModifier.create(statesNamingAuthority), DefaultRealignmentProcessor.create(nodeModifier), IsolatedProcessingNode.class, TransitionArc.class, dispatcher);
    }

    private System(final String name, final NamingAuthority statesNamingAuthority,
            final NamingAuthority variablesNamingAuthority, final NodeModifier nodeModifier,
            final ArcModifier arcModifier,
            final RealignmentProcessor realignmentProcessor,
            final Class<? extends Node> defaultNodeType,
            final Class<? extends Arc> defaultArcType,
            final Dispatcher dispatcher) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(statesNamingAuthority);
        Preconditions.checkNotNull(variablesNamingAuthority);
        Preconditions.checkNotNull(nodeModifier);
        Preconditions.checkNotNull(arcModifier);
        Preconditions.checkNotNull(realignmentProcessor);
        Preconditions.checkNotNull(defaultNodeType);
        Preconditions.checkNotNull(defaultArcType);
        Preconditions.checkNotNull(dispatcher);
        Preconditions.checkArgument(!name.isEmpty());

        this.name = name;
        this.statesNamingAuthority = statesNamingAuthority;
        this.variablesNamingAuthority = variablesNamingAuthority;
        this.nodeModifier = nodeModifier;
        this.arcModifier = arcModifier;
        this.realignmentProcessor = realignmentProcessor;
        this.defaultNodeType = defaultNodeType;
        this.defaultArcType = defaultArcType;
        this.dispatcher = dispatcher;
        this.graph = SystemGraph.create(dispatcher);
    }
    
    private Set<Network> getNetworks() {
        return this.initialNodes.keySet();
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitable#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitor)
     */
    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
        
        final Set<Network> networks = getNetworks();
        for (final Network network : networks) {
            network.accept(visitor);
        }
    }
    
    private void insertNetwork(final Network network) {
        this.initialNodes.put(network, new HashSet<EnterNode>());
    }
    
    public Network addNetwork(final String name) {
        final Network added = Network.create(name,  this);
        
        Preconditions.checkArgument(!getNetworks().contains(added));
        
        insertNetwork(added);
        
        return added;
    }
    
    private boolean deleteNetwork(final Network network) {
        return this.initialNodes.keySet().remove(network);
    }
    
    public void removeNetwork(final String name) {
        Preconditions.checkNotNull(name);
        
        final Network removed = Network.create(name, this);
        
        final Set<EnterNode> initials = this.initialNodes.get(removed);
        for (final EnterNode initial : initials) {
            final Collection<RecurentArc> referring = this.references.get(initial);
            if (referring == null) {
                continue;
            }
            
            for (final RecurentArc inidividualReferring : referring) {                
                Preconditions.checkArgument(inidividualReferring.getNetwork().equals(removed), "The network " + removed + " is referred by arc " + inidividualReferring + " from other network " + inidividualReferring.getNetwork() + ".");            
            }
        }
        
        removeReferences(removed);
        
        final boolean contained = deleteNetwork(removed);
        Preconditions.checkArgument(contained);
        
        this.dispatcher.fire(AvailableReferencesReducedEvent.create(initials));
    }
    
    private void removeReferences(final Network removed) {
        final Collection<Entry<EnterNode, RecurentArc>> entries = this.references.entries();
        Iterables.removeIf(entries, new Predicate<Entry<EnterNode, RecurentArc>>() {

            @Override
            public boolean apply(final Entry<EnterNode, RecurentArc> input) {
                final RecurentArc referring = input.getValue();
                return referring.getNetwork().equals(removed);
            }
            
        });
    }
    
    public Node addNode(final Network network, final int x, final int y) {
        Preconditions.checkNotNull(network);
        
        return addNode(network, Optional.<String>absent(), x, y);
    }
    
    public Node addNode(final Network network, final String name, final int x, final int y) {
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(name);
        
        return addNode(network, Optional.of(name), x, y);
    }
    
    private void insertNode(final Node node, final Network network) {
        this.graph.add(node);
        
        if (node instanceof EnterNode) {
            final Set<EnterNode> present = this.initialNodes.get(network);
            assert present != null;
            
            final EnterNode enterNode = (EnterNode) node;
            present.add(enterNode);
            this.dispatcher.fire(AvailableReferencesExtendedEvent.create(ImmutableSet.of(enterNode)));
        }
    }
    
    private Node addNode(final Network network, final Optional<String> name, final int x, final int y) {
        Preconditions.checkArgument(x >= 0);
        Preconditions.checkArgument(y >= 0);
        Preconditions.checkArgument(getNetworks().contains(network));
        
        final String generated;
        if (name.isPresent()) {
            generated = this.statesNamingAuthority.use(name.get());
        } else {
            generated = this.statesNamingAuthority.generate();
        }
        
        final Node freshDefault = instantiateInitialNode(generated, network, x, y);
        
        insertNode(freshDefault, network);
        
        this.dispatcher.fire(NodeAddedEvent.create(freshDefault));
        
        return freshDefault;
    }
    
    private Node instantiateInitialNode(final String name, final Network parent, final int x, final int y) {
        final Method factoryMethod;
        try {
            factoryMethod = this.defaultNodeType.getMethod(NODE_FACTORY_METHOD_NAME, parent.getClass(), Integer.TYPE, Integer.TYPE);
            if (!factoryMethod.getReturnType().equals(Node.class)) {
                throw new NoSuchMethodException();
            }
        } catch (final NoSuchMethodException | SecurityException e1) {
            return IsolatedProcessingNode.create(name, parent, x, y);
        }
        
        try {
            return (Node) factoryMethod.invoke(null, name, parent, x, y);
        } catch (final IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            return IsolatedProcessingNode.create(name, parent, x, y);
        }       
    }
    
    private Arc instantiateInitialArc(final String name, final Network parent) {
        final Method factoryMethod;
        try {
            factoryMethod = this.defaultArcType.getMethod(ARC_FACTORY_METHOD_NAME, parent.getClass());
            if (!factoryMethod.getReturnType().equals(Arc.class)) {
                throw new NoSuchMethodException();
            }
        } catch (final NoSuchMethodException | SecurityException e1) {
            return TransitionArc.create(parent, name);
        }
        
        try {
            return (Arc) factoryMethod.invoke(null, name, parent);
        } catch (final IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            return TransitionArc.create(parent, name);
        }       
    }
    
    public Set<EnterNode> getInitialNodes(final Network network) {
        Preconditions.checkNotNull(network);
        
        final Set<EnterNode> present = this.initialNodes.get(network);
        Preconditions.checkNotNull(present);
        
        return new HashSet<>(present);
    }
    
    /**
     * @param network
     * @param name2
     */
    public void removeNode(final String name) {
        Preconditions.checkNotNull(name);
        
        final Node removed = this.graph.getVertex(name);
        Preconditions.checkArgument(removed != null);
        
        if (this.references.containsKey(removed)) {
            final Collection<RecurentArc> referring = this.references.get((EnterNode) removed);
            throw new IllegalArgumentException("The node " + removed + " is referred by as entry point to the network " + removed.getNetwork() + " by " + referring + " as entry.");
        }
        
        final Set<EnterNode> presentInitials = this.initialNodes.get(removed.getNetwork());
        Preconditions.checkArgument(presentInitials != null);        
        
        this.graph.removeAndRealign(removed, this.realignmentProcessor, Collections.unmodifiableMap(this.references.asMap()), presentInitials);
        final boolean initial = presentInitials.remove(removed);
        
        if (initial) {
            this.dispatcher.fire(AvailableReferencesReducedEvent.create(Sets.newHashSet((EnterNode) removed)));
        }
    }

    /**
     * @param network
     * @param fromNodeName
     * @param toNodeName
     */
    public void addArc(final Network network, final String fromNodeName, final String toNodeName) {
        addArc(network, Optional.<String>absent(), fromNodeName, toNodeName);
    }
    
    public void addArc(final Network network, final String name, final String fromNodeName, final String toNodeName) {
        addArc(network, Optional.of(name), fromNodeName, toNodeName);
    }

    /**
     * @param network
     * @param name
     * @param fromNodeName
     * @param toNodeName
     */
    private void addArc(final Network network, final Optional<String> name, final String fromNodeName, final String toNodeName) {
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(fromNodeName);
        Preconditions.checkNotNull(toNodeName);
        Preconditions.checkArgument(getNetworks().contains(network));
        
        final Node from = this.graph.getVertex(fromNodeName);
        Preconditions.checkArgument(from != null);
                
        final Node to = this.graph.getVertex(toNodeName);
        Preconditions.checkArgument(to != null);
        
        Preconditions.checkArgument(from.getNetwork().equals(network));
        Preconditions.checkArgument(to.getNetwork().equals(network));        
        
        if (this.references.containsKey(to)) {
            final Collection<RecurentArc> referring = this.references.get((EnterNode) to);
            throw new IllegalArgumentException("The end point " + to + " is referenced as entry point to the network " + to.getNetwork() + " by " + referring); 
        }
        
        final String generated;
        if (name.isPresent()) {
            generated = this.statesNamingAuthority.use(name.get());
        } else {
            generated = this.statesNamingAuthority.generate();
        }
        
        final Arc added = instantiateInitialArc(generated, network);
        this.graph.addAndRealign(added, from, to, this.realignmentProcessor);
        this.dispatcher.fire(ArcAddedEvent.create(added));
    }
    
    /**
     * @param name2
     */
    public void removeArc(final String name) {
        Preconditions.checkNotNull(name);
        
        final Arc removed = this.graph.getEdge(name);
        Preconditions.checkArgument(removed != null);
        
        this.graph.removeAndRealign(removed, this.realignmentProcessor, new HashMap<>(this.references.asMap()), this.initialNodes.get(removed.getNetwork()));
        
        if (removed instanceof RecurentArc) {
            final RecurentArc referring = (RecurentArc) removed;
            final EnterNode target = referring.getTarget();
            
            this.references.get(target).remove(referring);
        }
    }

    /**
     * @param network
     * @param node
     * @return
     */
    public Set<Arc> getIns(final Node node) {
        return getConnections(node, Direction.IN);
    }

    /**
     * @param network
     * @param node
     * @return
     */
    public Set<Arc> getOuts(final Node node) {
        return getConnections(node, Direction.OUT);
    }

    /**
     * @param network
     * @param name
     * @param type
     */
    public void changeNode(final String name, final String newName, final int x, final int y, final Class<? extends Node> type) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(type);
        
        Preconditions.checkArgument(x > 0);
        Preconditions.checkArgument(y > 0);
        Preconditions.checkArgument(!newName.isEmpty());
        
        final Node changed = this.graph.getVertex(name);
        Preconditions.checkArgument(changed != null);
        
        if (changed.getClass() == type) {
            if (name.equals(newName) && changed.getX() == x && changed.getY() == y) {
                return;                
            }
        } else {
            if (this.references.containsKey(changed)) {
                final Collection<RecurentArc> referring = this.references.get((EnterNode) changed);
                throw new IllegalArgumentException("Cannot change the type. The node " + changed + " is referenced as entry point to the network " + changed.getNetwork() + " by " + referring);
            }
            
            final Node replacement = this.nodeModifier.change(changed, type);
            if (this.initialNodes.get(changed.getNetwork()).contains(changed) && !replacement.equals(changed)) {
                this.dispatcher.fire(AvailableReferencesReducedEvent.create(ImmutableSet.of((EnterNode) changed))); 
            } else if (replacement instanceof EnterNode) {
                this.dispatcher.fire(AvailableReferencesExtendedEvent.create(ImmutableSet.of((EnterNode) replacement)));
            }
        }
        
        this.graph.replaceVertex(this.nodeModifier.change(changed, newName, x, y, type), changed);
        this.dispatcher.fire(NodeRenamedEvent.create(changed.getName(), newName));
        this.dispatcher.fire(NodeMovedEvent.create(changed.getName(), x, y));
        this.dispatcher.fire(NodeMovedEvent.create(changed.getName(), x, y));
    }
    
    /**
     * @param network
     * @param name
     * @param newName
     * @param type
     */
    public void changeNode(String name, Class<? extends Node> type) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);
        
        final Node changed = this.graph.getVertex(name);
        Preconditions.checkArgument(changed != null);
        
        if (changed.getClass().equals(type)) {
            return;
        }
        
        if (this.references.containsKey(changed)) {
            final Collection<RecurentArc> referring = this.references.get((EnterNode) changed);
            throw new IllegalArgumentException("Change of type forbidden. The node " + changed + " is referenced as entry point to the network " + changed.getNetwork() + " by " + referring);
        }
        
        final Node replacement = this.nodeModifier.change(changed, type);
        if (this.initialNodes.get(changed.getNetwork()).contains(changed) && !replacement.equals(changed)) {
            this.dispatcher.fire(AvailableReferencesReducedEvent.create(ImmutableSet.of((EnterNode) changed))); 
        } else if (replacement instanceof EnterNode) {
            this.dispatcher.fire(AvailableReferencesExtendedEvent.create(ImmutableSet.of((EnterNode) replacement)));
        }
        
        this.graph.replaceVertex(replacement, changed);
    }
    
    public void changeNode(final String name, String newName) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(newName);
        
        Preconditions.checkArgument(!newName.isEmpty());
        
        if (name.equals(newName)) {
            return;
        }
        
        final Node changed = this.graph.getVertex(name);
        Preconditions.checkArgument(changed != null);
        
        this.graph.replaceVertex(this.nodeModifier.change(changed, newName), changed);
        this.dispatcher.fire(NodeRenamedEvent.create(changed.getName(), newName));
    }
    
    /**
     * @param network
     * @param name2
     * @param x
     * @param y
     */
    public void changeNode(String name, int x, int y) {
        Preconditions.checkNotNull(name);
        
        Preconditions.checkArgument(x > 0);
        Preconditions.checkArgument(y > 0);
        
        final Node changed = this.graph.getVertex(name);
        Preconditions.checkArgument(changed != null);
        
        if (changed.getX() == x && changed.getY() == y) {
            return;
        }
        
        this.graph.replaceVertex(this.nodeModifier.change(changed, x, y), changed);
        this.dispatcher.fire(NodeMovedEvent.create(changed.getName(), x, y));
    }

    /**
     * @param name
     * @param newName
     * @param priority
     * @param arguments
     */
    public void changeArc(final String name, final String newName, final int priority, final Class<? extends Arc> type, final Object... arguments) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(arguments);
        
        final Arc changed = this.graph.getEdge(name);
        Preconditions.checkArgument(changed != null);
        
        if (changed instanceof RecurentArc) {
            final RecurentArc referring = (RecurentArc) changed;
            final EnterNode target = referring.getTarget();
            
            references.get(target).remove(referring);
        }
        
        final Arc replacement = this.arcModifier.change(changed, newName, priority, type, arguments);
        if (replacement instanceof RecurentArc) {
            final RecurentArc reffering = (RecurentArc) replacement;
            
            registerReference(reffering, reffering.getTarget());
        }
        
        this.graph.replaceEdge(replacement, changed);
        this.dispatcher.fire(ArcChangedEvent.create(changed));
    }

    /**
     * @param direction
     * @return
     */
    public Set<Arc> getConnections(final Node node, final Direction direction) {
        Preconditions.checkNotNull(node);
        Preconditions.checkNotNull(direction);
        
        return this.graph.connections(node, direction);
    }

    /**
     * @param network
     * @param arc
     * @param direction
     * @return
     */
    public Node getAttached(final Arc arc, final Direction direction) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkNotNull(direction);
        
        return this.graph.attached(arc, direction);
    }

    /**
     * @param name2
     * @return
     */
    public Node getNode(final String name) {
        Preconditions.checkNotNull(name);
        
        return this.graph.getVertex(name);
    }

    /**
     * @param network
     * @param name
     * @return
     */
    public Arc getArc(final String name) {
        Preconditions.checkNotNull(name);
        
        return this.graph.getEdge(name);
    }

    /**
     * @return
     */
    public NamingAuthority getPredicatesNamingAuthority() {
        return this.variablesNamingAuthority;
    }

    /**
     * @param referrencing
     * @param target
     */
    public void registerReference(final RecurentArc referrencing, final EnterNode target) {
        Preconditions.checkNotNull(referrencing);
        Preconditions.checkNotNull(target);
        Preconditions.checkArgument(this.graph.containsEdge(referrencing));
        Preconditions.checkArgument(this.graph.containsVertex(target));
        
        this.references.put(target, referrencing);
    }

    /**
     * @return
     */
    public Set<EnterNode> getAvailableReferences() {
        final Set<EnterNode> allNetworksInitials = new HashSet<>();
        for (final Set<EnterNode> initials: this.initialNodes.values()) {
            Iterables.addAll(allNetworksInitials, initials);
        }
        
        return Collections.unmodifiableSet(allNetworksInitials);
    }
}
