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
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.AbstractDfsObserver;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.ArcModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.DefaultArcModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeMovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeTypeChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.DefaultNetwork;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.NodeModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.OrderedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RandomNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultNodeModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.DefaultRealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations.IsolatedProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesExtendedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesReducedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.SystemRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.Update.NodeSwitch;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultSystem implements System {
    
    private final NamingAuthority statesNamingAuthority;
    private final NamingAuthority predicatesNamingAuthority;
    
    private final NodeModifier nodeModifier;
    private final ArcModifier arcModifier;
    private final RealignmentProcessor realignmentProcessor;
    
    private final SetMultimap<EnterNode, RecurentArc> references = HashMultimap.create();
    private final SetMultimap<Network, EnterNode> initialNodes = HashMultimap.create();
    private final Map<Network, Set<IsolatedNode>> isolatedNodes = new HashMap<>();
    private final SetMultimap<Network, Node> networksNodes = HashMultimap.create();
    private final Map<Node, Network> nodesNetworks = new HashMap<>();
    
    private final SystemGraph graph;
    private final BiMap<Network, String> networksNames = HashBiMap.create();
    
    private final Dispatcher dispatcher;
    
    private String name;
    
    public static DefaultSystem create(final String name, final Dispatcher dispatcher, final NamingAuthority statesNamingAuthority, final NamingAuthority predicatesNamingAuthority) {
        Preconditions.checkNotNull(statesNamingAuthority);
        Preconditions.checkNotNull(predicatesNamingAuthority);
        
        final NodeModifier nodeModifier = DefaultNodeModifier.create();
        
        return new DefaultSystem(name, SystemGraph.create(), statesNamingAuthority, NormalizedNamingAuthority.create(), nodeModifier, DefaultArcModifier.create(statesNamingAuthority), DefaultRealignmentProcessor.create(nodeModifier), dispatcher);
    }

    private DefaultSystem(final String name, final SystemGraph graph,
            final NamingAuthority statesNamingAuthority, final NamingAuthority variablesNamingAuthority,
            final NodeModifier nodeModifier,
            final ArcModifier arcModifier,
            final RealignmentProcessor realignmentProcessor,
            final Dispatcher dispatcher) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(statesNamingAuthority);
        Preconditions.checkNotNull(variablesNamingAuthority);
        Preconditions.checkNotNull(nodeModifier);
        Preconditions.checkNotNull(arcModifier);
        Preconditions.checkNotNull(realignmentProcessor);
        Preconditions.checkNotNull(dispatcher);
        Preconditions.checkNotNull(graph);
        Preconditions.checkArgument(!name.isEmpty());

        this.name = name;
        this.statesNamingAuthority = statesNamingAuthority;
        this.predicatesNamingAuthority = variablesNamingAuthority;
        this.nodeModifier = nodeModifier;
        this.arcModifier = arcModifier;
        this.realignmentProcessor = realignmentProcessor;
        this.dispatcher = dispatcher;
        this.graph = graph;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitable#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Visitor)
     */
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor)
     */
    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
        
        final Set<Network> networks = networks();
        for (final Network network : networks) {
            network.accept(visitor);
        }
    }
    
    public Set<Network> getNetworks() {
        return ImmutableSet.copyOf(networks());
    }
    
    private Set<Network> networks() {
        return this.networksNames.keySet();
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#addNetwork(java.lang.String)
     */
    @Override
    public void addNetwork(final String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        
        final Network added = DefaultNetwork.create(this);
        Preconditions.checkArgument(!contains(added));
        Preconditions.checkArgument(!this.networksNames.inverse().containsKey(name));
        
        insertNetwork(added, name);
        
        this.dispatcher.fire(NetworkAddedEvent.create(this, added));
    }
    
    private void insertNetwork(final Network network, final String name) {
        this.isolatedNodes.put(network, new HashSet<IsolatedNode>());
        this.networksNames.put(network, name);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#removeNetwork(java.lang.String)
     */
    @Override
    public void removeNetwork(final String name) {
        Preconditions.checkNotNull(name);
        
        final Network removed = (Network) this.networksNames.inverse().get(name);
        removeNetwork(removed);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#removeNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.NetworkInfo)
     */
    @Override
    public void removeNetwork(final Network removed) {
        Preconditions.checkNotNull(removed);
        Preconditions.checkArgument(contains(removed));
        
        final Set<EnterNode> initials = initial(removed);
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
        removeNodes(removed);
        deleteNetwork(removed);
        
        this.dispatcher.fire(AvailableReferencesReducedEvent.create(this, initials));
        this.dispatcher.fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRemovedEvent.create(this, removed));
        this.dispatcher.fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedEvent.create(removed));
    }
    
    private void deleteNetwork(final Network network) {
        this.networksNames.remove(network);
        this.initialNodes.removeAll(network);
        this.isolatedNodes.remove(network);
    }
    
    /**
     * @param removed
     */
    private void removeNodes(final Network removed) {
        removed.accept(DefaultDfsVisitor.create(new AbstractDfsObserver() {
            
            /* (non-Javadoc)
             * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.AbstractDfsObserver#notifyDiscovery(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
             */
            @Override
            public void notifyDiscovery(final Node discovered) {
                graph.removeVertex(discovered);
                statesNamingAuthority.release(discovered.getName().getText());
            }
            
            /* (non-Javadoc)
             * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.AbstractDfsObserver#notifyExamination(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
             */
            @Override
            public void notifyExamination(Arc examined) {
                graph.removeEdge(examined);
                statesNamingAuthority.release(examined.getName().getText());
            }
        }));
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
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#addNode(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network, int, int)
     */
    @Override
    public Node addNode(final Network network, final int x, final int y) {
        Preconditions.checkNotNull(network);
        Preconditions.checkArgument(x >= 0);
        Preconditions.checkArgument(y >= 0);
        Preconditions.checkArgument(contains(network));
        
        final String generatedName = this.statesNamingAuthority.generate();
        
        final IsolatedNode freshDefault = instantiateInitialNode(NormalWords.of(generatedName), network, x, y);
        insertNode(freshDefault, network);        
        isolated(network).add(freshDefault);
        
        this.dispatcher.fire(NodeAddedEvent.create(network, freshDefault));
        
        return freshDefault;
    }
    
    private void insertNode(final Node node, final Network network) {
        this.graph.add(node);
    }
    
    private IsolatedNode instantiateInitialNode(final NormalWord name, final Network parent, final int x, final int y) {
        return IsolatedProcessingNode.create(name, parent, x, y);
    }
    
    private Arc instantiateInitialArc(final NormalWord name, final Network parent) {
        return TransitionArc.getInitial(parent, name);
    }
    
    private Set<EnterNode> initial(final Network network) {
        return this.initialNodes.get(network);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getInitialNodes(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public Set<EnterNode> getInitialNodes(final Network network) {
        Preconditions.checkNotNull(network);
        
        final Set<EnterNode> present = initial(network);
        Preconditions.checkNotNull(present);
        
        return new HashSet<>(present);
    }
    
    private Set<IsolatedNode> isolated(final Network network) {
        return this.isolatedNodes.get(network);
    }
    
    @Override
    public Set<IsolatedNode> getIsolatedNodes(final Network network) {
        Preconditions.checkNotNull(network);
        
        final Set<IsolatedNode> present = isolated(network);
        Preconditions.checkNotNull(present);
        
        return new HashSet<>(present);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#removeNode(java.lang.String)
     */
    @Override
    public void removeNode(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        final Node removed = this.graph.getVertex(name);
        Preconditions.checkArgument(removed != null);
        
        removeNode(removed);
    }
    
    public void removeNode(final Node removed) {
        Preconditions.checkNotNull(removed);
        Preconditions.checkArgument(this.graph.containsVertex(removed));
        checkReferences(removed);        
        
        final Network network = removed.getNetwork();
        
        final Set<EnterNode> initials = initial(network);
        final Set<IsolatedNode> isolated = isolated(network);
        assert initials != null;
        assert isolated != null;
        
        final Update removeUpdate = this.graph.removeAndRealign(removed, this.realignmentProcessor, ImmutableMap.copyOf(this.references.asMap()), ImmutableSet.copyOf(initials), ImmutableSet.copyOf(isolated));
        
        this.statesNamingAuthority.release(removed.getName().getText());
        this.references.entries().removeAll(removeUpdate.getReferencesRemoved().entrySet());
        updateIsolated(isolated, removeUpdate);
        updateInitials(initials, removeUpdate);
        updateAffected(removeUpdate);
        removeFromSpecial(removed, isolated, initials);
        
        this.dispatcher.fire(NodeRemovedEvent.create(network, removed));
    }

    /**
     * @param removeUpdate
     */
    private void updateAffected(final Update update) {
        final Set<NodeSwitch> affected = update.getAffected();
        
        for (final NodeSwitch switeched : affected) {
            final Node from = switeched.getFrom();
            final Node to = switeched.getTo();
            
            final Network network = from.getNetwork();
            assert network.equals(to.getNetwork());
            
            this.dispatcher.fire(NodeTypeChangedEvent.create(network, from, to));
        }
    }

    /**
     * @param initials
     * @param update
     */
    private void updateInitials(final Set<EnterNode> initials,
            final Update update) {
        final Set<EnterNode> initialsRemoved = update.getInitialsRemoved();
        final Set<EnterNode> initialsAdded = update.getInitialsAdded();
        initials.removeAll(initialsRemoved);
        initials.addAll(initialsAdded);
        
        if (!initialsAdded.isEmpty()) {
            this.dispatcher.fire(AvailableReferencesExtendedEvent.create(this, initialsAdded));
        }
        if (!initialsRemoved.isEmpty()) {
            this.dispatcher.fire(AvailableReferencesReducedEvent.create(this, initialsRemoved));
        }
    }

    /**
     * @param removed
     * @param network
     * @param presentInitials
     */
    private void removeFromSpecial(final Node removed, final Set<? extends IsolatedNode> isolated,
            final Set<? extends EnterNode> initials) {
        isolated.remove(removed);
        
        final boolean isInitial = initials.remove(removed);
        if (isInitial) {
            this.dispatcher.fire(AvailableReferencesReducedEvent.create(this, Sets.newHashSet((EnterNode) removed)));
        }
    }

    private void checkReferences(final Node removed) {
        if (this.references.containsKey(removed)) {
            final Collection<RecurentArc> referring = this.references.get((EnterNode) removed);
            throw new IllegalArgumentException("The node " + removed + " is referred by as entry point to the network " + removed.getNetwork() + " by " + referring + " as entry.");
        }
    }

    public void addArc(final Network network, final NormalWord name, final NormalWord fromNodeName, final NormalWord toNodeName) {
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(fromNodeName);
        Preconditions.checkNotNull(toNodeName);
        Preconditions.checkArgument(contains(network));
        
        final Node from = this.graph.getVertex(fromNodeName);
        Preconditions.checkArgument(from != null);
                
        final Node to = this.graph.getVertex(toNodeName);
        Preconditions.checkArgument(to != null);
        
        Preconditions.checkArgument(from.getNetwork().equals(network));
        Preconditions.checkArgument(to.getNetwork().equals(network));        
        
        Preconditions.checkArgument(!from.pointsTo(to), "The arc already exists.");
        
        if (this.references.containsKey(to)) {
            final Collection<RecurentArc> referring = this.references.get((EnterNode) to);
            throw new IllegalArgumentException("The end point " + to + " is referenced as entry point to the network " + to.getNetwork() + " by " + referring); 
        }
        
        final String generatedName = this.statesNamingAuthority.use(name.getText());
        
        final Arc added;
        try {
            added = instantiateInitialArc(NormalWords.of(generatedName), network);
        } catch (final Exception e) {
            this.statesNamingAuthority.release(generatedName);
            throw e;
        }
        
        final Set<EnterNode> initials = initial(network);
        final Set<IsolatedNode> isolated = isolated(network);
        
        final Update addUpdate = this.graph.addAndRealign(added, from, to, this.realignmentProcessor, ImmutableSet.copyOf(initials), ImmutableSet.copyOf(isolated));
        
        updateInitials(initials, addUpdate);
        updateIsolated(isolated, addUpdate);
        updateAffected(addUpdate);
        
        this.dispatcher.fire(ArcAddedEvent.create(network, added));
    }
    
    private void updateIsolated(final Set<IsolatedNode> isolated,
            final Update update) {
        isolated.removeAll(update.getIsolatedRemoved());
        isolated.addAll(update.getIsolatedAdded());
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#removeArc(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void removeArc(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        final Arc removed = this.graph.getEdge(name);
        Preconditions.checkArgument(removed != null);
    
        removeArc(removed);
    }
    
    @Override
    public void removeArc(final Arc removed) {
        Preconditions.checkNotNull(removed);
        Preconditions.checkArgument(this.graph.containsEdge(removed));
        
        final Network network = removed.getNetwork();
        
        final Set<EnterNode> initials = initial(network);
        final Set<IsolatedNode> isolated = isolated(network);
        
        final Update removeUpdate = this.graph.removeAndRealign(removed, this.realignmentProcessor, ImmutableMap.copyOf(this.references.asMap()), ImmutableSet.copyOf(initials), ImmutableSet.copyOf(isolated));
        
        this.references.entries().removeAll(removeUpdate.getReferencesRemoved().entrySet());
        this.statesNamingAuthority.release(removed.getName().getText());
        updateIsolated(isolated, removeUpdate);
        updateInitials(initials, removeUpdate);
        updateAffected(removeUpdate);
        
        this.dispatcher.fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.ArcRemovedEvent.create(this, removed));
        this.dispatcher.fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcRemovedEvent.create(network, removed));
        this.dispatcher.fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedEvent.create(removed));
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getIns(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public Set<Arc> getIns(final Node node) {
        return getConnections(node, Direction.IN);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getOuts(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public Set<Arc> getOuts(final Node node) {
        return getConnections(node, Direction.OUT);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#changeNode(java.lang.String, java.lang.Class)
     */
    @Override
    public void changeNode(final NormalWord name, final Class<? extends Node> type) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);
        Preconditions.checkArgument(type.equals(InputNode.class) || type.equals(ProcessingNode.class) || type.equals(RandomNode.class) || type.equals(OrderedNode.class));
        
        final Node oldVersion = this.graph.getVertex(name);
        Preconditions.checkArgument(oldVersion != null);
        
        if (type.isAssignableFrom(oldVersion.getClass())) {
            return;
        }
        
        if (this.references.containsKey(oldVersion)) {
            final Collection<RecurentArc> referring = this.references.get((EnterNode) oldVersion);
            throw new IllegalArgumentException("Change of type forbidden. The node " + oldVersion + " is referenced as entry point to the network " + oldVersion.getNetwork() + " by " + referring);
        }
        
        final Node newVersion = this.nodeModifier.change(oldVersion, type);
        this.graph.replaceVertex(newVersion, oldVersion);
        
        replaceInSpecials(oldVersion, newVersion);
        
        final Network network = oldVersion.getNetwork();
        this.dispatcher.fire(NodeTypeChangedEvent.create(network, oldVersion, newVersion));
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#changeNode(java.lang.String, java.lang.String)
     */
    @Override
    public void changeNode(final NormalWord name, final NormalWord newName) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(newName);        
        
        final Node oldVersion = this.graph.getVertex(name);
        Preconditions.checkArgument(oldVersion != null);
        
        if (name.equals(newName)) {
            return;
        }        
        
        final String generatedName = this.statesNamingAuthority.replace(name.getText(), newName.getText());
                
        final Node newVersion;
        try {
            newVersion = this.nodeModifier.change(oldVersion, NormalWords.of(generatedName));
            this.graph.replaceVertex(newVersion, oldVersion);
        } catch (final Exception e) {
            this.statesNamingAuthority.release(generatedName);
            throw e;
        }
        
        replaceInSpecials(oldVersion, newVersion);
        
        final Network network = oldVersion.getNetwork();
        this.dispatcher.fire(NodeRenamedEvent.create(network, oldVersion, newVersion));
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#changeNode(java.lang.String, int, int)
     */
    @Override
    public void changeNode(NormalWord name, final int x, final int y) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(x >= 0);
        Preconditions.checkArgument(y >= 0);
        
        final Node oldVersion = this.graph.getVertex(name);
        Preconditions.checkArgument(oldVersion != null);
        
        if (oldVersion.getX() == x && oldVersion.getY() == y) {
            return;
        }
        
        final Node newVersion = this.nodeModifier.change(oldVersion, x, y);
        this.graph.replaceVertex(newVersion, oldVersion);
        
        replaceInSpecials(oldVersion, newVersion);
        
        final Network network = oldVersion.getNetwork();
        this.dispatcher.fire(NodeMovedEvent.create(network, oldVersion, newVersion));
    }
        
    /**
     * @param oldVersion
     * @param newVersion
     */
    private void replaceInSpecials(final Node oldVersion, final Node newVersion) {
        replaceInIsolated(oldVersion, newVersion);
        repalceInInitials(oldVersion, newVersion);
    }

    /**
     * @param oldVersion
     * @param newVersion
     */
    private void
            repalceInInitials(final Node oldVersion, final Node newVersion) {
        final Set<EnterNode> initials = this.initialNodes.get(oldVersion.getNetwork());
        final boolean initial = initials.remove(oldVersion);
        
        if (initial && newVersion instanceof EnterNode) {
            final EnterNode castNewVersion = (EnterNode) newVersion; 
            initials.add(castNewVersion);
            this.dispatcher.fire(AvailableReferencesReducedEvent.create(this, ImmutableSet.of((EnterNode) oldVersion)));
            this.dispatcher.fire(AvailableReferencesExtendedEvent.create(this, ImmutableSet.of(castNewVersion)));
        }
    }

    /**
     * @param oldVersion
     * @param newVersion
     */
    private void
            replaceInIsolated(final Node oldVersion, final Node newVersion) {
        final Set<IsolatedNode> isolatedNodes = this.isolatedNodes.get(oldVersion.getNetwork());
        final boolean isolated = isolatedNodes.remove(oldVersion);
        
        if (isolated && newVersion instanceof IsolatedNode) {
            isolatedNodes.add((IsolatedNode) newVersion);
        }
    }

    @Override
    public void changeArc(final Arc changed, final NormalWord newName, final int priority, final Class<? extends Arc> type, final Object... arguments) {
        Preconditions.checkNotNull(changed);
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(arguments);
        Preconditions.checkArgument(this.graph.containsEdge(changed));
        
        if (changed instanceof RecurentArc) {
            final RecurentArc referring = (RecurentArc) changed;
            final EnterNode target = referring.getTarget();
            
            references.get(target).remove(referring);
        }
        
        final String generatedName = this.statesNamingAuthority.replace(changed.getName().getText(), newName.getText());
        
        final Arc replacement;
        try {
            replacement = this.arcModifier.change(changed, generatedName, priority, type, arguments);
        } catch (final Exception e) {
            this.statesNamingAuthority.release(generatedName);
            throw e;
        }
        
        if (replacement instanceof RecurentArc) {
            final RecurentArc reffering = (RecurentArc) replacement;
            
            registerReference(reffering, reffering.getTarget());
        }
        
        this.graph.replaceEdge(replacement, changed);
        this.dispatcher.fire(ArcChangedEvent.create(changed, replacement));
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getConnections(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Direction)
     */
    @Override
    public Set<Arc> getConnections(final Node node, final Direction direction) {
        Preconditions.checkNotNull(node);
        Preconditions.checkNotNull(direction);
        
        return this.graph.connections(node, direction);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getAttached(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Direction)
     */
    @Override
    public Node getAttached(final Arc arc, final Direction direction) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkNotNull(direction);
        
        return this.graph.attached(arc, direction);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getNode(java.lang.String)
     */
    @Override
    public Node getNode(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        return this.graph.getVertex(name);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getArc(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public Arc getArc(final NormalWord name) {
        Preconditions.checkNotNull(name);
        
        return this.graph.getEdge(name);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getPredicatesNamingAuthority()
     */
    @Override
    public NamingAuthority getPredicatesNamingAuthority() {
        return this.predicatesNamingAuthority;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getStatesNamingAuthority()
     */
    @Override
    public NamingAuthority getStatesNamingAuthority() {
        return this.statesNamingAuthority;
    }

    /**
     * @param referrencing
     * @param target
     */
    private void registerReference(final RecurentArc referrencing, final EnterNode target) {
        assert this.graph.containsEdge(referrencing);
        assert this.graph.containsVertex(target);
        
        this.references.put(target, referrencing);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getAvailableReferences()
     */
    @Override
    public Set<EnterNode> getAvailableReferences() {
        return ImmutableSet.copyOf(this.initialNodes.values());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#setName(java.lang.String)
     */
    @Override
    public void setName(final String newName) {
        Preconditions.checkNotNull(newName);
        Preconditions.checkArgument(!newName.isEmpty());
        
        this.name = newName;
        this.dispatcher.fire(SystemRenamedEvent.create(this));
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#renameNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.NetworkInfo, java.lang.String)
     */
    @Override
    public void renameNetwork(final Network network, final String newName) {
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(newName);
        Preconditions.checkArgument(!newName.isEmpty());
        Preconditions.checkArgument(contains(network));
        
        this.networksNames.forcePut(network, newName);
        
        this.dispatcher.fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRenamedEvent.create(this, network));
        this.dispatcher.fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRenamedEvent.create(network));
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getNetworkName(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.NetworkInfo)
     */
    @Override
    public String getNetworkName(final Network network) {
        Preconditions.checkNotNull(network);
        
        final String name = this.networksNames.get(network);
        Preconditions.checkArgument(name != null);
        
        return name;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#contains(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.NetworkInfo)
     */
    @Override
    public boolean contains(final Network network) {
        Preconditions.checkNotNull(network);
        
        return networks().contains(network);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getNetwork(java.lang.String)
     */
    @Override
    public Network getNetwork(final String name) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(!name.isEmpty());
        
        final Network result = this.networksNames.inverse().get(name);
        Preconditions.checkNotNull(result);
        
        return result;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#adjoins(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Direction)
     */
    @Override
    public boolean adjoins(final Node first, final Node second, final Direction direction) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);
        Preconditions.checkNotNull(direction);
        
        return this.graph.adjoins(first, second, direction);
    }
    
    private void addNodeToNetwork(final Network network, final Node node) {
        this.networksNodes.put(network, node);
        this.nodesNetworks.put(node, network);
    }
    
    private void removeNodeFromNetwork(final Node node, final Network network) {
        this.networksNodes.remove(network, node);
        this.nodesNetworks.remove(node);
    }
}
