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
import java.util.Map;
import java.util.Set;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.DefaultUpdateBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.Update;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.UpdateBuilder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates.UpdateBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Function;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DefaultLabeledDirectedGraph;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.LabeledDirectedGraph;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;

/**
 * Implementace {@link SystemGraph}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultSystemGraph implements SystemGraph, Serializable {

    private static final long serialVersionUID = 1L;
    
    private final LabeledDirectedGraph<Node, NormalWord, Arc, NormalWord> baseGraph;
    private final UpdateBuilderFactory updateBuilderFactory;

    /**
     * Vytvoří graf.
     * 
     * @return prázdný graf
     */
    public static DefaultSystemGraph create() {
        return of(
                DefaultLabeledDirectedGraph
                        .<Node, NormalWord, Arc, NormalWord> create(),
                DefaultUpdateBuilderFactory.create());
    }

    /**
     * Dekoruje základní graf.
     * 
     * @param baseGraph
     *            základní graf
     * @param updateBuilderFactory
     *            továrna na stavitele aktualizací
     * 
     * @return obohacený základní graf
     */
    public static
            DefaultSystemGraph
            of(final LabeledDirectedGraph<Node, NormalWord, Arc, NormalWord> baseGraph,
                    final UpdateBuilderFactory updateBuilderFactory) {
        Preconditions.checkNotNull(baseGraph);
        Preconditions.checkNotNull(updateBuilderFactory);

        return new DefaultSystemGraph(baseGraph, updateBuilderFactory);
    }

    private DefaultSystemGraph(
            final LabeledDirectedGraph<Node, NormalWord, Arc, NormalWord> baseGraph,
            final UpdateBuilderFactory updateBuilderFactory) {
        this.baseGraph = baseGraph;
        this.updateBuilderFactory = updateBuilderFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#
     * getVertex(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public Node getVertex(final NormalWord name) {
        Preconditions.checkNotNull(name);

        return this.baseGraph.getVertex(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#
     * getEdge(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public Arc getEdge(final NormalWord name) {
        Preconditions.checkNotNull(name);

        return this.baseGraph.getEdge(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#
     * removeAndRealign
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff
     * .ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor,
     * java.util.Map, java.util.Set)
     */
    @Override
    public
            Update
            removeAndRealign(
                    final Node removed,
                    final RealignmentProcessor processor,
                    final Map<? extends EnterNode, ? extends Set<? extends RecurentArc>> references,
                    final Set<? extends EnterNode> initialNodes)
                    throws IllegalArgumentException {
        Preconditions.checkNotNull(removed);
        Preconditions.checkNotNull(references);
        Preconditions.checkNotNull(initialNodes);

        final Map<EnterNode, Set<RecurentArc>> referencesCopy =
                getReferencesCopy(references);
        final Set<EnterNode> initialsCopy = getInitialsCopy(initialNodes);

        final UpdateBuilder updateBuilder = this.updateBuilderFactory.produce();

        this.baseGraph.extractVertex(removed, new Function<Node, Node>() {
            @Override
            public Node apply(final Node input) {
                return processor.realign(input);
            }
        }, new Callback<Node>() {
            @Override
            public void call(final Node input) {
                addRealignmentUpdates(updateBuilder, processor, input, removed,
                        referencesCopy, initialsCopy);
            }
        }, new Callback<Arc>() {
            @Override
            public void call(final Arc parameter) {
                addArcRemovalUpdates(updateBuilder, parameter);
            }
        });

        return updateBuilder.build();
    }

    private static void addRealignmentUpdates(
            final UpdateBuilder updateBuilder,
            final RealignmentProcessor processor, final Node input,
            final Node removed,
            final Map<EnterNode, Set<RecurentArc>> references,
            final Set<EnterNode> initials) {
        final Node realigned = processor.realign(input);
        if (realigned.equals(input)) {
            return;
        }
        
        checkForDependingArcs(removed, references, input);

        if (initials.contains(input)) {
            updateBuilder.addRemovedInitial((EnterNode) input);
        }

        if (realigned instanceof EnterNode) {
            updateBuilder.addNewInitial((EnterNode) realigned);
        }

        updateBuilder.addSwitched(input, realigned);
    }

    private static void
            checkForDependingArcs(final Node removed,
                    final Map<EnterNode, Set<RecurentArc>> references,
                    final Node input) {
        final Set<RecurentArc> referring = references.get(input);
        if (Presence.isAbsent(referring)) {
            return;
        }

        final Network networkOfRemoved = removed.getNetwork();

        final RecurentArc firstReferring = referring.iterator().next();
        final Network networkOfFirst = firstReferring.getNetwork();

        throw new IllegalArgumentException(ExceptionLocalizer.print(
                "NodeRemovalForbidden", input.getName(), removed.getName(),
                networkOfRemoved.getName(), firstReferring.getName(),
                networkOfFirst.getName()));
    }

    private static void addArcRemovalUpdates(final UpdateBuilder updateBuilder,
            final Arc parameter) {
        if (parameter instanceof RecurentArc) {
            final RecurentArc reference = (RecurentArc) parameter;
            
            updateBuilder.addRemovedReference(reference);
        }

        updateBuilder.addRemovedEdge(parameter);
    }

    private static Set<EnterNode> getInitialsCopy(
            final Set<? extends EnterNode> initialNodes) {
        return ImmutableSet.copyOf(initialNodes);
    }

    private static
            Map<EnterNode, Set<RecurentArc>>
            getReferencesCopy(
                    Map<? extends EnterNode, ? extends Set<? extends RecurentArc>> references) {
        final ImmutableMap.Builder<EnterNode, Set<RecurentArc>> copyBuilder =
                ImmutableMap.builder();

        for (final Map.Entry<? extends EnterNode, ? extends Set<? extends RecurentArc>> entry : references
                .entrySet()) {
            final EnterNode key = entry.getKey();
            Preconditions.checkNotNull(key);

            final Set<? extends RecurentArc> value = entry.getValue();
            Preconditions.checkNotNull(value);

            copyBuilder.put(key, ImmutableSet.copyOf(value));
        }

        return copyBuilder.build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#
     * removeAndRealign
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc,
     * cz.cuni.mff.ms
     * .brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor,
     * java.util.Map, java.util.Set)
     */
    @Override
    public
            Update
            removeAndRealign(
                    final Arc removed,
                    final RealignmentProcessor processor,
                    final Map<? extends EnterNode, ? extends Set<? extends RecurentArc>> references,
                    Set<? extends EnterNode> initials)
                    throws IllegalArgumentException {
        Preconditions.checkNotNull(removed);
        Preconditions.checkNotNull(processor);
        Preconditions.checkNotNull(references);
        Preconditions.checkNotNull(initials);

        final Map<EnterNode, Set<RecurentArc>> referencesCopy =
                getReferencesCopy(references);
        final Set<EnterNode> initialsCopy = getInitialsCopy(initials);

        this.baseGraph.removeEdge(removed);

        final Node from = removed.getFrom();
        final Node to = removed.getTo();
        final Node newFrom = processor.realign(from);
        final Node newTo = processor.realign(to);

        abortOnDependingArcs(removed, referencesCopy, from, newFrom, to);

        replaceVertex(from, newFrom);
        replaceVertex(to, newTo);

        return createArcRemovalUpdate(removed, initialsCopy, from, newFrom, to,
                newTo);
    }

    private Update createArcRemovalUpdate(final Arc removed,
            final Set<EnterNode> initials, final Node from, final Node newFrom,
            final Node to, final Node newTo) {
        final UpdateBuilder updateBuilder = this.updateBuilderFactory.produce();

        updateBuilder.addSwitched(from, newFrom);
        updateBuilder.addSwitched(to, newTo);

        if (removed instanceof RecurentArc) {
            final RecurentArc reference = (RecurentArc) removed;
            
            updateBuilder.addRemovedReference(reference);
        }
        if (!from.equals(newFrom)) {
            if (initials.contains(from)) {
                updateBuilder.addRemovedInitial((EnterNode) from);
            }
        }
        if (!to.equals(newTo)) {
            if (newTo instanceof EnterNode) {
                updateBuilder.addNewInitial((EnterNode) newTo);
            }
        }

        return updateBuilder.build();
    }

    private void abortOnDependingArcs(final Arc removed,
            final Map<EnterNode, Set<RecurentArc>> references, final Node from,
            final Node newFrom, final Node to) throws IllegalArgumentException {
        try {
            checkForDependingArcs(removed, references, from, newFrom);
        } catch (final IllegalArgumentException e) {
            this.baseGraph.add(removed, removed.getName(), from, to);

            throw e;
        }
    }

    private static void checkForDependingArcs(final Arc removed,
            final Map<EnterNode, Set<RecurentArc>> references, final Node from,
            final Node newFrom) throws IllegalArgumentException {
        final Set<RecurentArc> referring = references.get(from);
        
        if (Presence.isAbsent(referring) || newFrom.equals(from)) {
            return;
        }
        
        final Set<RecurentArc> referringWithoutRemoved =
                Sets.difference(referring, ImmutableSet.of(removed));
        
        final boolean refersOnlyToItself =
                referringWithoutRemoved.isEmpty();
        if (refersOnlyToItself) {
            return;
        }
        
        final Network fromNetwork = from.getNetwork();
        final RecurentArc firstReferring = referringWithoutRemoved.iterator().next();
        final Network firstReferringNetwork =
                firstReferring.getNetwork();

        throw new IllegalArgumentException(ExceptionLocalizer.print(
                "ArcRemovalForbidden", from.getName(),
                removed.getName(), fromNetwork.getName(),
                firstReferring.getName(),
                firstReferringNetwork.getName()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#
     * addAndRealign(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.
     * ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor,
     * java.util.Set)
     */
    @Override
    public Update addAndRealign(final Arc added, final Node from,
            final Node to, final RealignmentProcessor processor,
            final Set<? extends EnterNode> initials) {
        Preconditions.checkNotNull(added);
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        Preconditions.checkNotNull(processor);
        Preconditions.checkNotNull(initials);

        final Set<EnterNode> initialsCopy = getInitialsCopy(initials);

        this.baseGraph.add(added, added.getName(), from, to);

        final Node newFrom = processor.realign(from);
        final Node newTo = processor.realign(to);

        replaceVertex(from, newFrom);
        replaceVertex(to, newTo);

        final UpdateBuilder updateBuilder = this.updateBuilderFactory.produce();

        if (!from.equals(newFrom) && (newFrom instanceof EnterNode)) {
            updateBuilder.addNewInitial((EnterNode) newFrom);
        }

        if (!to.equals(newTo) && initialsCopy.contains(to)) {
            updateBuilder.addRemovedInitial((EnterNode) to);
        }

        updateBuilder.addSwitched(from, newFrom);
        updateBuilder.addSwitched(to, newTo);

        return updateBuilder.build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#
     * containsVertex(java.lang.Object)
     */
    @Override
    public boolean containsVertex(final Node vertex) {
        Preconditions.checkNotNull(vertex);

        return this.baseGraph.containsVertex(vertex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#
     * containsEdge(java.lang.Object)
     */
    @Override
    public boolean containsEdge(final Arc edge) {
        Preconditions.checkNotNull(edge);

        return this.baseGraph.containsEdge(edge);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#
     * add(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode)
     */
    @Override
    public void add(final IsolatedNode node) {
        Preconditions.checkNotNull(node);

        this.baseGraph.add(node, node.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#
     * replaceVertex(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceVertex(final Node old, final Node fresh) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);

        this.baseGraph.replaceVertex(old, fresh, fresh.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.DirectedGraph#
     * replaceEdge(java.lang.Object, java.lang.Object)
     */
    @Override
    public void replaceEdge(final Arc old, final Arc fresh) {
        Preconditions.checkNotNull(fresh);
        Preconditions.checkNotNull(old);

        this.baseGraph.replaceEdge(old, fresh, fresh.getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#
     * connections(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
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
            throw new AssertionError();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#
     * attached(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
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
            throw new AssertionError();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#
     * adjoins(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.graphs.Direction)
     */
    @Override
    public boolean adjoins(final Node first, final Node second,
            final Direction direction) {
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.SystemGraph#
     * purge(java.util.Set)
     */
    @Override
    public void purge(final Set<? extends Node> purged) {
        Preconditions.checkNotNull(purged);

        final Set<Node> purgedCopy = ImmutableSet.copyOf(purged);
        for (final Node node : purgedCopy) {
            this.baseGraph.removeVertex(node);
        }
    }
    
    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        
        Preconditions.checkNotNull(this.baseGraph);
        Preconditions.checkNotNull(this.updateBuilderFactory);
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
