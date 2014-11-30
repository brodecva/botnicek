/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events.NodeMovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events.NodeMovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events.NodeRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events.NodeRenamedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events.NodeTypeChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.events.NodeTypeChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.DispatchNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.FunctionalNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.OrderedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ProceedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RandomNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views.NodesView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.ExceptionLocalizer;

/**
 * Výchozí implementace řadiče.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultNodesController extends AbstractController<NodesView>
        implements NodesController {

    private class DefaultNodeMovedListener implements NodeMovedListener {

        @Override
        public void nodeMoved(final Node oldVersion, final Node newVersion) {
            Preconditions.checkNotNull(oldVersion);
            Preconditions.checkNotNull(newVersion);

            callViews(new Callback<NodesView>() {
                @Override
                public void call(final NodesView view) {
                    Preconditions.checkNotNull(view);

                    view.nodeMoved(oldVersion, newVersion);
                }
            });
        }

    }

    private class DefaultNodeRemovedEventListener implements
            NodeRemovedListener {

        @Override
        public void nodeRemoved(final Node node) {
            Preconditions.checkNotNull(node);

            callViews(new Callback<NodesView>() {
                @Override
                public void call(final NodesView view) {
                    view.nodeRemoved(node);
                }
            });
        }

    }

    private class DefaultNodeRenamedListener implements NodeRenamedListener {

        @Override
        public void nodeRenamed(final Node oldVersion, final Node newVersion) {
            Preconditions.checkNotNull(oldVersion);
            Preconditions.checkNotNull(newVersion);

            callViews(new Callback<NodesView>() {
                @Override
                public void call(final NodesView view) {
                    Preconditions.checkNotNull(view);

                    view.nodeRenamed(oldVersion, newVersion);
                }
            });
        }

    }

    private class DefaultNodeTypeChangedListener implements
            NodeTypeChangedListener {

        @Override
        public void
                nodeTypeChanged(final Node oldVersion, final Node newVersion) {
            Preconditions.checkNotNull(oldVersion);
            Preconditions.checkNotNull(newVersion);

            callViews(new Callback<NodesView>() {
                @Override
                public void call(final NodesView view) {
                    Preconditions.checkNotNull(view);

                    view.nodeRetyped(oldVersion, newVersion);
                }
            });
        }

    }

    private final static Iterable<Class<? extends ProceedNode>> CYCLIC_NODE_TYPES =
            Iterables.cycle(ImmutableList.of(InputNode.class,
                    ProcessingNode.class));
    private static final Iterable<Class<? extends DispatchNode>> CYCLIC_NODE_DISPATCH_TYPES =
            Iterables.cycle(ImmutableList.of(OrderedNode.class,
                    RandomNode.class));

    /**
     * Vytvoří řadič a zaregistruje posluchače na změny.
     * 
     * @param system
     *            systém sítí
     * @param network
     *            rodičovská síť
     * @param eventManager
     *            správce událostí
     * @return řadič
     */
    public static DefaultNodesController create(final System system,
            final Network network, final EventManager eventManager) {
        Preconditions.checkNotNull(system);
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(eventManager);

        final DefaultNodesController newInstance =
                new DefaultNodesController(system, eventManager);

        newInstance.addListener(NodeMovedEvent.class, network,
                newInstance.new DefaultNodeMovedListener());
        newInstance.addListener(NodeRenamedEvent.class, network,
                newInstance.new DefaultNodeRenamedListener());
        newInstance.addListener(NodeTypeChangedEvent.class, network,
                newInstance.new DefaultNodeTypeChangedListener());
        newInstance.addListener(NodeRemovedEvent.class, network,
                newInstance.new DefaultNodeRemovedEventListener());

        return newInstance;
    }

    private final System system;

    private DefaultNodesController(final System system,
            final EventManager eventManager) {
        super(eventManager);

        Preconditions.checkNotNull(system);

        this.system = system;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #changeNodeLocation(java.lang.String, int, int)
     */
    @Override
    public void changeNode(final NormalWord nodeName, final int x, final int y) {
        this.system.changeNode(nodeName, x, y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController
     * #removeNode(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void removeNode(final NormalWord name) {
        this.system.removeNode(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController
     * #rename(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * java.lang.String)
     */
    @Override
    public void rename(final NormalWord nodeName, final String proposedName) {
        Preconditions.checkNotNull(nodeName);
        Preconditions.checkNotNull(proposedName);
        Preconditions.checkArgument(!proposedName.isEmpty(),
                ExceptionLocalizer.print("ProposedNodeNameEmpty"));

        final NormalWord normalName = NormalWords.from(proposedName);

        this.system.changeNode(nodeName, normalName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #toggleNodeDispatchType(java.lang.String)
     */
    @Override
    public void toggleNodeDispatchType(final NormalWord nodeName) {
        toggleNodeType(nodeName, CYCLIC_NODE_DISPATCH_TYPES);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController
     * #toggleNodeProceedType(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.
     * NormalWord)
     */
    @Override
    public void toggleNodeProceedType(final NormalWord nodeName) {
        toggleNodeType(nodeName, CYCLIC_NODE_TYPES);
    }

    private void toggleNodeType(final NormalWord name,
            final Iterable<? extends Class<? extends FunctionalNode>> types) {
        final Node node = this.system.getNode(name);

        final UnmodifiableIterator<? extends Class<? extends FunctionalNode>> cyclicUnmodifiableIterator =
                Iterators.unmodifiableIterator(types.iterator());
        Optional<?> visited = Optional.absent();
        while (cyclicUnmodifiableIterator.hasNext()) {
            final Class<? extends Node> type =
                    cyclicUnmodifiableIterator.next();
            if (type.isAssignableFrom(node.getClass())) {
                this.system.changeNode(name, cyclicUnmodifiableIterator.next());
                return;
            }

            if (visited.isPresent()) {
                return;
            } else {
                visited = Optional.of(type);
            }
        }
    }
}
