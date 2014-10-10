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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.AbstractDfsObserver;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitorFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsVisitorFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcReprioritizedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRetypedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.FromRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ToRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.ArcModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.DefaultArcModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.DefaultNetwork;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events.NodeMovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events.NodeRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events.NodeTypeChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.FunctionalNode;
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
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesExtendedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesReducedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.SystemRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.Update;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.SystemName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;

/**
 * <p>
 * Výchozí implementace systému ukládá všechny uzly a hrany sítí do jednoho
 * grafu. Jejich rozložení do sítí či přítomnost ve speciálních kategoriích
 * (vstupní uzly, referované uzly z jiných sítí) je uloženo zvlášť a udržováno v
 * synchronizaci.
 * </p>
 * <p>
 * Pro přehlednost rozhraní jsou operace nad jednou sítí proveditelné
 * zprostředkovaně i přes fasádu v podobě sítě, který ovšem deleguje všechny
 * metody na rodičovský systém.
 * </p>
 * <p>
 * Model systému vysílá pomocí vloženého vysílače zpráv veškeré změny ve formě
 * událostí. Rozesílač není uchováván při serializaci a je nutné ho po
 * deserializaci inicializovat, jinak nebudou zasílány žádné zprávy.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultSystem implements System, Serializable {

    private static final long serialVersionUID = 1L;

    private static final int MAX_BRANCHING_FACTOR = 17;

    /**
     * Vytvoří prázdný systém sítí.
     * 
     * @param name
     *            název systému
     * @param dispatcher
     *            vysílač událostí
     * @param statesNamingAuthority
     *            autorita přidělující názvy stavů tak, aby nekolidovaly
     * @param predicatesNamingAuthority
     *            autorita přidělující názvy predikátů tak, aby nekolidovaly
     * @param reservedNetworkNames
     *            rezervované názvy sítí
     * @return systém
     */
    public static System create(final SystemName name,
            final Dispatcher dispatcher,
            final NamingAuthority statesNamingAuthority,
            final NamingAuthority predicatesNamingAuthority,
            final Set<? extends SystemName> reservedNetworkNames) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(dispatcher);
        Preconditions.checkNotNull(statesNamingAuthority);
        Preconditions.checkNotNull(predicatesNamingAuthority);

        final NodeModifier nodeModifier = DefaultNodeModifier.create();

        return create(name, DefaultSystemGraph.create(), statesNamingAuthority,
                NormalizedNamingAuthority.create(), nodeModifier,
                DefaultArcModifier.create(),
                DefaultRealignmentProcessor.create(nodeModifier),
                reservedNetworkNames, DefaultDfsVisitorFactory.create(),
                DefaultInitialNodeFactory.create(),
                DefaultInitialArcFactory.create(), dispatcher);
    }

    /**
     * Vytvoří prázdný systém sítí.
     * 
     * @param name
     *            název systému
     * @param graph
     *            uložiště uzlů a hran
     * @param statesNamingAuthority
     *            autorita přidělující názvy stavů tak, aby nekolidovaly
     * @param predicatesNamingAuthority
     *            autorita přidělující názvy predikátů tak, aby nekolidovaly
     * @param nodeModifier
     *            modifikátor uzlů
     * @param arcModifier
     *            modifikátor hran
     * @param realignmentProcessor
     *            opravný procesor uzlů po strukturálních změnách grafu
     * @param reservedNetworkNames
     *            rezervované názvy sítí
     * @param systemVisitorFactory
     *            užitá továrna na návštěvníky systému
     * @param initialNodeFactory
     *            užitá továrna na čerstvě přidané uzly
     * @param initialArcFactory
     *            užitá továrna na čerstvě přidané hrany
     * @param dispatcher
     *            vysílač událostí
     * @return systém
     */
    public static System create(final SystemName name, final SystemGraph graph,
            final NamingAuthority statesNamingAuthority,
            final NamingAuthority predicatesNamingAuthority,
            final NodeModifier nodeModifier, final ArcModifier arcModifier,
            final RealignmentProcessor realignmentProcessor,
            final Set<? extends SystemName> reservedNetworkNames,
            final DfsVisitorFactory systemVisitorFactory,
            final InitialNodeFactory initialNodeFactory,
            final InitialArcFactory initialArcFactory,
            final Dispatcher dispatcher) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(statesNamingAuthority);
        Preconditions.checkNotNull(predicatesNamingAuthority);
        Preconditions.checkNotNull(nodeModifier);
        Preconditions.checkNotNull(arcModifier);
        Preconditions.checkNotNull(realignmentProcessor);
        Preconditions.checkNotNull(systemVisitorFactory);
        Preconditions.checkNotNull(initialNodeFactory);
        Preconditions.checkNotNull(initialArcFactory);
        Preconditions.checkNotNull(dispatcher);
        Preconditions.checkNotNull(graph);
        Preconditions.checkNotNull(reservedNetworkNames);

        return new DefaultSystem(name, graph, statesNamingAuthority,
                predicatesNamingAuthority, nodeModifier, arcModifier,
                realignmentProcessor,
                ImmutableSet.copyOf(reservedNetworkNames),
                systemVisitorFactory, initialNodeFactory, initialArcFactory,
                Optional.of(dispatcher),
                HashMultimap.<EnterNode, RecurentArc> create(),
                HashMultimap.<Network, EnterNode> create(),
                HashMultimap.<Network, Node> create(),
                HashBiMap.<Network, SystemName> create());
    }

    private final NamingAuthority statesNamingAuthority;
    private final NamingAuthority predicatesNamingAuthority;
    private final NodeModifier nodeModifier;

    private final ArcModifier arcModifier;
    private final RealignmentProcessor realignmentProcessor;
    private final SetMultimap<EnterNode, RecurentArc> references;

    private final SetMultimap<Network, EnterNode> initialNodes;
    private final SetMultimap<Network, Node> networksNodes;

    private final SystemGraph graph;
    private final BiMap<Network, SystemName> networksNames;
    private final DfsVisitorFactory systemVisitorFactory;

    private final InitialArcFactory initialArcFactory;

    private final InitialNodeFactory initialNodeFactory;
    private final Set<SystemName> reservedNetworkNames;

    private transient Optional<Dispatcher> dispatcher;

    private SystemName name;

    private DefaultSystem(final SystemName name, final SystemGraph graph,
            final NamingAuthority statesNamingAuthority,
            final NamingAuthority predicatesNamingAuthority,
            final NodeModifier nodeModifier, final ArcModifier arcModifier,
            final RealignmentProcessor realignmentProcessor,
            final Set<SystemName> reservedNetworkNames,
            final DfsVisitorFactory systemVisitorFactory,
            final InitialNodeFactory initialNodeFactory,
            final InitialArcFactory initialArcFactory,
            final Optional<Dispatcher> dispatcher,
            final SetMultimap<EnterNode, RecurentArc> references,
            final SetMultimap<Network, EnterNode> initialNodes,
            final SetMultimap<Network, Node> networksNodes,
            final BiMap<Network, SystemName> networkNames) {
        this.name = name;
        this.statesNamingAuthority = statesNamingAuthority;
        this.predicatesNamingAuthority = predicatesNamingAuthority;
        this.nodeModifier = nodeModifier;
        this.arcModifier = arcModifier;
        this.realignmentProcessor = realignmentProcessor;
        this.dispatcher = dispatcher;
        this.graph = graph;
        this.systemVisitorFactory = systemVisitorFactory;
        this.initialNodeFactory = initialNodeFactory;
        this.initialArcFactory = initialArcFactory;
        this.reservedNetworkNames = reservedNetworkNames;
        this.references = references;
        this.initialNodes = initialNodes;
        this.networksNodes = networksNodes;
        this.networksNames = networkNames;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.Visitable#accept(cz.cuni
     * .mff.ms.brodecva.botnicek.ide.design.api.Visitor)
     */
    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);

        final Set<Network> networks = networks();
        for (final Network network : networks) {
            network.accept(visitor);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#addArc
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public void addArc(final Network network, final NormalWord name,
            final Node from, final Node to) {
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkArgument(!from.equals(to));
        Preconditions.checkArgument(contains(network));

        Preconditions.checkArgument(from.getNetwork().equals(network));
        Preconditions.checkArgument(to.getNetwork().equals(network));

        Preconditions.checkArgument(!from.pointsTo(to),
                ExceptionLocalizer.print("ArcExists"));
        Preconditions.checkArgument(
                from.getOutDegree() + 1 <= MAX_BRANCHING_FACTOR,
                ExceptionLocalizer.print("MaxBranches"));
        checkForReferences(to, network, "ChangeForbidden");

        final String generatedName =
                this.statesNamingAuthority.use(name.getText());

        final Arc added;
        try {
            added =
                    instantiateInitialArc(NormalWords.of(generatedName),
                            network);
        } catch (final Exception e) {
            this.statesNamingAuthority.release(generatedName);
            throw e;
        }

        final Set<EnterNode> initials = initial(network);

        final Update addUpdate =
                this.graph.addAndRealign(added, from, to,
                        this.realignmentProcessor,
                        ImmutableSet.copyOf(initials));

        updateInitials(initials, addUpdate);
        updateAffected(addUpdate);

        fire(ArcAddedEvent.create(network, added));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#addArc
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void addArc(final Network network, final NormalWord name,
            final NormalWord fromNodeName, final NormalWord toNodeName) {
        Preconditions.checkNotNull(fromNodeName);
        Preconditions.checkNotNull(toNodeName);

        final Node from = this.graph.getVertex(fromNodeName);
        final Node to = this.graph.getVertex(toNodeName);

        addArc(network, name, from, to);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#addNetwork
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network,
     * java.lang.String)
     */
    @Override
    public void addNetwork(final Network added, final SystemName name) {
        Preconditions.checkNotNull(added);
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(added.getSystem().equals(this));

        Preconditions.checkArgument(!contains(added));
        Preconditions.checkArgument(!this.networksNames.inverse().containsKey(
                name));
        Preconditions.checkArgument(!this.reservedNetworkNames.contains(name));

        insertNetwork(added, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#addNetwork
     * (java.lang.String)
     */
    @Override
    public void addNetwork(final SystemName name) {
        addNetwork(DefaultNetwork.create(this), name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#addNode
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network, int,
     * int)
     */
    @Override
    public void addNode(final Network network, final int x, final int y) {
        Preconditions.checkNotNull(network);
        Preconditions.checkArgument(x >= 0);
        Preconditions.checkArgument(y >= 0);
        Preconditions.checkArgument(contains(network));

        final String generatedName = this.statesNamingAuthority.generate();

        final IsolatedNode freshDefault =
                instantiateInitialNode(NormalWords.of(generatedName), network,
                        x, y);
        insertNode(freshDefault, network);
    }

    private void addNodeToNetwork(final Network network, final Node node) {
        final boolean contained = this.networksNodes.put(network, node);
        assert !contained;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#adjoins
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Direction)
     */
    @Override
    public boolean adjoins(final Node first, final Node second,
            final Direction direction) {
        Preconditions.checkNotNull(first);
        Preconditions.checkNotNull(second);
        Preconditions.checkNotNull(direction);

        return this.graph.adjoins(first, second, direction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#contains
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.NetworkInfo)
     */
    @Override
    public boolean contains(final Network network) {
        Preconditions.checkNotNull(network);

        return networks().contains(network);
    }

    private void deleteNetwork(final Network network) {
        this.networksNames.remove(network);
        this.initialNodes.removeAll(network);
        this.networksNodes.removeAll(network);

        fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRemovedEvent
                .create(this, network));
        fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedEvent
                .create(network));
    }

    private <L> void fire(final Event<L> event) {
        if (!this.dispatcher.isPresent()) {
            return;
        }

        this.dispatcher.get().fire(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getArc
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public Arc getArc(final NormalWord name) {
        Preconditions.checkNotNull(name);

        return this.graph.getEdge(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getAttached
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Direction)
     */
    @Override
    public Node getAttached(final Arc arc, final Direction direction) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkNotNull(direction);

        return this.graph.attached(arc, direction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#
     * getAvailableReferences()
     */
    @Override
    public Set<EnterNode> getAvailableReferences() {
        return ImmutableSet.copyOf(this.initialNodes.values());
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#
     * getConnections
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Direction)
     */
    @Override
    public Set<Arc> getConnections(final Node node, final Direction direction) {
        Preconditions.checkNotNull(node);
        Preconditions.checkNotNull(direction);

        return this.graph.connections(node, direction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getIns
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public Set<Arc> getIns(final Node node) {
        return getConnections(node, Direction.IN);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#
     * getMaxBranchingFactor()
     */
    @Override
    public int getMaxBranchingFactor() {
        return MAX_BRANCHING_FACTOR;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getName()
     */
    @Override
    public SystemName getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getNetwork
     * (java.lang.String)
     */
    @Override
    public Network getNetwork(final SystemName name) {
        Preconditions.checkNotNull(name);

        final Network result = this.networksNames.inverse().get(name);
        Preconditions.checkNotNull(result);

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#
     * getNetworkName
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.NetworkInfo)
     */
    @Override
    public SystemName getNetworkName(final Network network) {
        Preconditions.checkNotNull(network);

        final SystemName name = this.networksNames.get(network);
        Preconditions.checkArgument(Presence.isPresent(name));

        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getNetworks
     * ()
     */
    @Override
    public Set<Network> getNetworks() {
        return ImmutableSet.copyOf(networks());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getNode
     * (java.lang.String)
     */
    @Override
    public Node getNode(final NormalWord name) {
        Preconditions.checkNotNull(name);

        return this.graph.getVertex(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getNodes
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public Set<Node> getNodes(final Network network) {
        Preconditions.checkNotNull(network);
        Preconditions.checkArgument(contains(network));

        final Set<Node> nodes = this.networksNodes.get(network);

        return ImmutableSet.copyOf(nodes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#getOuts
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
     */
    @Override
    public Set<Arc> getOuts(final Node node) {
        return getConnections(node, Direction.OUT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#
     * getPredicatesNamingAuthority()
     */
    @Override
    public NamingAuthority getPredicatesNamingAuthority() {
        return this.predicatesNamingAuthority;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#
     * getStatesNamingAuthority()
     */
    @Override
    public NamingAuthority getStatesNamingAuthority() {
        return this.statesNamingAuthority;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#changeArc
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority,
     * java.lang.Class, java.lang.Object[])
     */
    @Override
    public void changeArc(final Arc changed, final NormalWord newName,
            final Priority priority, final Class<? extends Arc> type,
            final Object... arguments) {
        Preconditions.checkNotNull(changed);
        Preconditions.checkNotNull(newName);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(arguments);
        Preconditions.checkArgument(this.graph.containsEdge(changed));

        final String generatedName =
                this.statesNamingAuthority.replace(changed.getName().getText(),
                        newName.getText());

        final Arc replacement;
        try {
            replacement =
                    this.arcModifier.change(changed,
                            NormalWords.of(generatedName), priority, type,
                            arguments);

            if (changed instanceof RecurentArc) {
                final RecurentArc referring = (RecurentArc) changed;

                removeReference(referring);
            }

            if (replacement instanceof RecurentArc) {
                final RecurentArc reffering = (RecurentArc) replacement;

                registerReference(reffering);
            }
        } catch (final Exception e) {
            this.statesNamingAuthority.release(generatedName);
            throw e;
        }

        this.graph.replaceEdge(changed, replacement);
        fire(ArcChangedEvent.create(changed, replacement));

        final Network network = changed.getNetwork();
        if (!replacement.getName().equals(changed.getName())) {
            fire(ArcRenamedEvent.create(network, changed, replacement));
        }
        if (!replacement.getPriority().equals(changed.getPriority())) {
            fire(ArcReprioritizedEvent.create(network, changed, replacement));
        }
        if (!replacement.getClass().equals(changed.getClass())) {
            fire(ArcRetypedEvent.create(network, changed, replacement));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#changeNode
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * java.lang.Class)
     */
    @Override
    public void changeNode(final Node node,
            final Class<? extends FunctionalNode> type) {
        Preconditions.checkNotNull(node);
        Preconditions.checkNotNull(type);
        Preconditions.checkArgument(this.graph.containsVertex(node));
        Preconditions.checkArgument(type.equals(InputNode.class)
                || type.equals(ProcessingNode.class)
                || type.equals(RandomNode.class)
                || type.equals(OrderedNode.class));

        final Node oldVersion = node;

        final Node newVersion = this.nodeModifier.change(oldVersion, type);
        this.graph.replaceVertex(oldVersion, newVersion);

        replaceInInitials(oldVersion, newVersion);

        final Network network = oldVersion.getNetwork();
        removeNodeFromNetwork(oldVersion, network);
        addNodeToNetwork(network, newVersion);

        updateReferring(oldVersion, newVersion);

        fire(NodeTypeChangedEvent.create(network, oldVersion, newVersion));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#changeNode
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, int, int)
     */
    @Override
    public void changeNode(final Node node, final int x, final int y) {
        Preconditions.checkNotNull(node);
        Preconditions.checkArgument(x >= 0);
        Preconditions.checkArgument(y >= 0);
        Preconditions.checkArgument(this.graph.containsVertex(node));

        final Node oldVersion = node;
        final Node newVersion = this.nodeModifier.change(oldVersion, x, y);
        this.graph.replaceVertex(oldVersion, newVersion);

        replaceInInitials(oldVersion, newVersion);

        updateReferring(oldVersion, newVersion);

        final Network network = oldVersion.getNetwork();
        removeNodeFromNetwork(oldVersion, network);
        addNodeToNetwork(network, newVersion);

        fire(NodeMovedEvent.create(network, oldVersion, newVersion));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#changeNode
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void changeNode(final Node node, final NormalWord newName) {
        Preconditions.checkNotNull(node);
        Preconditions.checkNotNull(newName);
        Preconditions.checkArgument(this.graph.containsVertex(node));

        final Node oldVersion = node;
        final NormalWord name = oldVersion.getName();

        final String generatedName =
                this.statesNamingAuthority.replace(name.getText(),
                        newName.getText());

        final Node newVersion;
        try {
            newVersion =
                    this.nodeModifier.change(oldVersion,
                            NormalWords.of(generatedName));
            this.graph.replaceVertex(oldVersion, newVersion);
        } catch (final Exception e) {
            this.statesNamingAuthority.release(generatedName);
            throw e;
        }

        replaceInInitials(oldVersion, newVersion);

        final Network network = oldVersion.getNetwork();
        removeNodeFromNetwork(oldVersion, network);
        addNodeToNetwork(network, newVersion);

        updateReferring(oldVersion, newVersion);

        fire(NodeRenamedEvent.create(network, oldVersion, newVersion));

        final Set<Arc> outs = newVersion.getOuts();
        for (final Arc out : outs) {
            fire(FromRenamedEvent.create(out, oldVersion, newVersion));
        }

        final Set<Arc> ins = newVersion.getIns();
        for (final Arc in : ins) {
            fire(ToRenamedEvent.create(in, oldVersion, newVersion));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#changeNode
     * (java.lang.String, java.lang.Class)
     */
    @Override
    public void changeNode(final NormalWord name,
            final Class<? extends FunctionalNode> type) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);
        Preconditions.checkArgument(type.equals(InputNode.class)
                || type.equals(ProcessingNode.class)
                || type.equals(RandomNode.class)
                || type.equals(OrderedNode.class));

        final Node node = this.graph.getVertex(name);

        changeNode(node, type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#changeNode
     * (java.lang.String, int, int)
     */
    @Override
    public void changeNode(final NormalWord name, final int x, final int y) {
        Preconditions.checkNotNull(name);
        Preconditions.checkArgument(x >= 0);
        Preconditions.checkArgument(y >= 0);

        final Node oldVersion = this.graph.getVertex(name);

        changeNode(oldVersion, x, y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#changeNode
     * (java.lang.String, java.lang.String)
     */
    @Override
    public void changeNode(final NormalWord name, final NormalWord newName) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(newName);

        final Node oldVersion = this.graph.getVertex(name);

        changeNode(oldVersion, newName);
    }

    private void checkCrossNetworkDependingArcs(final Network removed) {
        final Set<EnterNode> initials = initial(removed);
        for (final EnterNode initial : initials) {
            final Collection<RecurentArc> referring =
                    this.references.get(initial);
            if (referring.isEmpty()) {
                continue;
            }

            for (final RecurentArc inidividualReferring : referring) {
                final Network individualreferringNetwork =
                        inidividualReferring.getNetwork();

                Preconditions.checkArgument(inidividualReferring.getNetwork()
                        .equals(removed), ExceptionLocalizer.print(
                        "NetworkReferredElsewhere", removed.getName().getText(),
                        inidividualReferring.getName().getText(),
                        individualreferringNetwork.getName().getText()));
            }
        }
    }

    private void checkForReferences(final Node affected, final Network network,
            final String messageKey) {
        if (this.references.containsKey(affected)) {
            final EnterNode castAffected = (EnterNode) affected;

            final Set<RecurentArc> referring =
                    this.references.get(castAffected);
            final RecurentArc firstReferring = referring.iterator().next();

            throw new IllegalArgumentException(ExceptionLocalizer.print(
                    messageKey, affected.getName(), network.getName().getText(),
                    firstReferring.getName(), firstReferring.getNetwork()
                            .getName().getText()));
        }
    }

    private Set<EnterNode> initial(final Network network) {
        return this.initialNodes.get(network);
    }

    private void insertNetwork(final Network network, final SystemName name) {
        this.networksNames.put(network, name);

        fire(NetworkAddedEvent.create(this, network));
    }

    private void insertNode(final IsolatedNode node, final Network network) {
        this.graph.add(node);
        addNodeToNetwork(network, node);

        fire(NodeAddedEvent.create(network, node));
    }

    private Arc instantiateInitialArc(final NormalWord name,
            final Network parent) {
        return this.initialArcFactory.produce(parent, name);
    }

    private IsolatedNode instantiateInitialNode(final NormalWord name,
            final Network parent, final int x, final int y) {
        return this.initialNodeFactory.produce(name, parent, x, y);
    }

    private Set<Network> networks() {
        return this.networksNames.keySet();
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        this.dispatcher = Optional.absent();

        Preconditions.checkNotNull(this.name);
        Preconditions.checkNotNull(this.statesNamingAuthority);
        Preconditions.checkNotNull(this.predicatesNamingAuthority);
        Preconditions.checkNotNull(this.nodeModifier);
        Preconditions.checkNotNull(this.arcModifier);
        Preconditions.checkNotNull(this.realignmentProcessor);
        Preconditions.checkNotNull(this.systemVisitorFactory);
        Preconditions.checkNotNull(this.initialNodeFactory);
        Preconditions.checkNotNull(this.initialArcFactory);
        Preconditions.checkNotNull(this.dispatcher);
        Preconditions.checkNotNull(this.graph);
        Preconditions.checkNotNull(this.reservedNetworkNames);
        Preconditions.checkNotNull(this.references);
        Preconditions.checkNotNull(this.initialNodes);
        Preconditions.checkNotNull(this.networksNodes);
    }

    private void registerReference(final RecurentArc referring) {
        final EnterNode target = referring.getTarget();

        Preconditions.checkArgument(this.graph.containsVertex(target));

        final boolean contained = this.references.put(target, referring);
        Preconditions.checkArgument(contained);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#removeArc
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
     */
    @Override
    public void removeArc(final Arc removed) {
        Preconditions.checkNotNull(removed);
        Preconditions.checkArgument(this.graph.containsEdge(removed));

        final Network network = removed.getNetwork();

        final Set<EnterNode> initials = initial(network);

        final Update removeUpdate =
                this.graph.removeAndRealign(removed, this.realignmentProcessor,
                        Multimaps.asMap(ImmutableSetMultimap
                                .copyOf(this.references)), ImmutableSet
                                .copyOf(initials));

        removeReferences(removeUpdate.getReferencesRemoved());
        this.statesNamingAuthority.release(removed.getName().getText());
        updateInitials(initials, removeUpdate);
        updateAffected(removeUpdate);

        fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.ArcRemovedEvent
                .create(this, removed));
        fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcRemovedEvent
                .create(network, removed));
        fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedEvent
                .create(removed));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#removeArc
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void removeArc(final NormalWord name) {
        Preconditions.checkNotNull(name);

        final Arc removed = this.graph.getEdge(name);
        removeArc(removed);
    }

    private void removeFromInitial(final Node removed,
            final Set<? extends EnterNode> initials) {
        final boolean isInitial = initials.remove(removed);
        if (isInitial) {
            fire(AvailableReferencesReducedEvent.create(this,
                    Sets.newHashSet((EnterNode) removed)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#removeNetwork
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.NetworkInfo)
     */
    @Override
    public void removeNetwork(final Network removed) {
        Preconditions.checkNotNull(removed);
        Preconditions.checkArgument(contains(removed));

        checkCrossNetworkDependingArcs(removed);

        removeReferences(removed);
        removeNodes(removed);
        deleteNetwork(removed);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#removeNetwork
     * (java.lang.String)
     */
    @Override
    public void removeNetwork(final SystemName name) {
        Preconditions.checkNotNull(name);

        final Network removed = this.networksNames.inverse().get(name);
        removeNetwork(removed);
    }

    @Override
    public void removeNode(final Node removed) {
        Preconditions.checkNotNull(removed);
        Preconditions.checkArgument(this.graph.containsVertex(removed));

        final Network network = removed.getNetwork();
        checkForReferences(removed, network, "RemovalForbidden");

        final Set<EnterNode> initials = initial(network);
        assert Presence.isPresent(initials);
        final Update removeUpdate =
                this.graph.removeAndRealign(removed, this.realignmentProcessor,
                        Multimaps.asMap(ImmutableSetMultimap
                                .copyOf(this.references)), ImmutableSet
                                .copyOf(initials));

        this.statesNamingAuthority.release(removed.getName().getText());
        removeReferences(removeUpdate.getReferencesRemoved());
        updateInitials(initials, removeUpdate);
        updateAffected(removeUpdate);
        removeFromInitial(removed, initials);
        removeNodeFromNetwork(removed, network);
        updateArcs(removeUpdate);

        fire(NodeRemovedEvent.create(network, removed));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#removeNode
     * (java.lang.String)
     */
    @Override
    public void removeNode(final NormalWord name) {
        Preconditions.checkNotNull(name);

        final Node removed = this.graph.getVertex(name);
        removeNode(removed);
    }

    private void removeNodeFromNetwork(final Node node, final Network network) {
        this.networksNodes.remove(network, node);
    }

    private void removeNodes(final Network removed) {
        final ImmutableSet.Builder<Node> removedNodesBuilder =
                ImmutableSet.builder();

        removed.accept(this.systemVisitorFactory
                .produce(new AbstractDfsObserver() {

                    /*
                     * (non-Javadoc)
                     * 
                     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.
                     * AbstractDfsObserver
                     * #notifyDiscovery(cz.cuni.mff.ms.brodecva
                     * .botnicek.ide.design.nodes.model.Node)
                     */
                    @Override
                    public void notifyDiscovery(final Node discovered) {
                        removedNodesBuilder.add(discovered);

                        final NormalWord discoveredName = discovered.getName();
                        final String discoveredNameText =
                                discoveredName.getText();
                        DefaultSystem.this.statesNamingAuthority
                                .release(discoveredNameText);
                    }

                    /*
                     * (non-Javadoc)
                     * 
                     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.
                     * AbstractDfsObserver
                     * #notifyExamination(cz.cuni.mff.ms.brodecva
                     * .botnicek.ide.design.arcs.model.Arc)
                     */
                    @Override
                    public void notifyExamination(final Arc examined) {
                        DefaultSystem.this.statesNamingAuthority
                                .release(examined.getName().getText());
                    }
                }));

        this.graph.purge(removedNodesBuilder.build());

        fire(AvailableReferencesReducedEvent.create(this, initial(removed)));
    }

    private void removeReference(final RecurentArc referring) {
        final EnterNode target = referring.getTarget();

        final boolean contained = this.references.remove(target, referring);
        assert contained;
    }

    private void removeReferences(final Network removed) {
        final Collection<Entry<EnterNode, RecurentArc>> entries =
                this.references.entries();
        Iterables.removeIf(entries,
                new Predicate<Entry<EnterNode, RecurentArc>>() {

                    @Override
                    public boolean apply(
                            final Entry<EnterNode, RecurentArc> input) {
                        final RecurentArc referring = input.getValue();
                        return referring.getNetwork().equals(removed);
                    }

                });
    }

    private void removeReferences(final Set<? extends RecurentArc> removed) {
        final Collection<Entry<EnterNode, RecurentArc>> entries =
                this.references.entries();
        Iterables.removeIf(entries,
                new Predicate<Entry<EnterNode, RecurentArc>>() {

                    @Override
                    public boolean apply(
                            final Entry<EnterNode, RecurentArc> input) {
                        final RecurentArc referring = input.getValue();
                        return removed.contains(referring);
                    }

                });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#renameNetwork
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.NetworkInfo,
     * java.lang.String)
     */
    @Override
    public void renameNetwork(final Network network, final SystemName newName) {
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(newName);
        Preconditions.checkArgument(contains(network));

        this.networksNames.forcePut(network, newName);

        fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRenamedEvent
                .create(this, network));
        fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRenamedEvent
                .create(network));
    }

    private void
            replaceInInitials(final Node oldVersion, final Node newVersion) {
        final Set<EnterNode> initials =
                this.initialNodes.get(oldVersion.getNetwork());
        final boolean initial = initials.remove(oldVersion);

        if (initial && newVersion instanceof EnterNode) {
            final EnterNode castNewVersion = (EnterNode) newVersion;
            initials.add(castNewVersion);

            fire(AvailableReferencesReducedEvent.create(this,
                    ImmutableSet.of((EnterNode) oldVersion)));
            fire(AvailableReferencesExtendedEvent.create(this,
                    ImmutableSet.of(castNewVersion)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#setDispatcher
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher)
     */
    @Override
    public void setDispatcher(final Dispatcher dispatcher) {
        Preconditions.checkNotNull(dispatcher);

        this.dispatcher = Optional.of(dispatcher);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System#setName
     * (java.lang.String)
     */
    @Override
    public void setName(final SystemName newName) {
        Preconditions.checkNotNull(newName);

        this.name = newName;
        fire(SystemRenamedEvent.create(this));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DefaultSystem [name=" + this.name + "]";
    }

    private void updateAffected(final Update update) {
        final Map<Node, Node> switched = update.getSwitched();

        for (final Map.Entry<Node, Node> entry : switched.entrySet()) {
            final Node from = entry.getKey();
            final Node to = entry.getValue();

            final Network network = from.getNetwork();
            assert network.equals(to.getNetwork());

            removeNodeFromNetwork(from, network);
            addNodeToNetwork(network, to);

            fire(NodeTypeChangedEvent.create(network, from, to));
        }
    }

    private void updateArcs(final Update removeUpdate) {
        for (final Arc removed : removeUpdate.getEdgesRemoved()) {
            this.statesNamingAuthority.release(removed.getName().getText());

            fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedEvent
                    .create(removed));
            fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcRemovedEvent
                    .create(removed.getNetwork(), removed));
            fire(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.ArcRemovedEvent
                    .create(this, removed));
        }
    }

    private void updateInitials(final Set<EnterNode> initials,
            final Update update) {
        final Set<EnterNode> initialsRemoved = update.getInitialsRemoved();
        final Set<EnterNode> initialsAdded = update.getInitialsAdded();
        initials.removeAll(initialsRemoved);
        initials.addAll(initialsAdded);

        if (!initialsAdded.isEmpty()) {
            fire(AvailableReferencesExtendedEvent.create(this, initialsAdded));
        }
        if (!initialsRemoved.isEmpty()) {
            fire(AvailableReferencesReducedEvent.create(this, initialsRemoved));
        }
    }

    private void updateReferring(final Node oldVersion, final Node newVersion) {
        if (!this.references.containsKey(oldVersion)) {
            return;
        }

        assert oldVersion instanceof EnterNode;
        assert newVersion instanceof EnterNode;

        final EnterNode referred = (EnterNode) oldVersion;
        final EnterNode newReferred = (EnterNode) newVersion;

        final Set<RecurentArc> referringArcs = this.references.get(referred);
        for (final RecurentArc referringArc : referringArcs) {
            changeArc(referringArc, referringArc.getName(),
                    referringArc.getPriority(), RecurentArc.class,
                    referringArc.getCode(), newReferred);
        }
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
