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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.ArcAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.ArcAddedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.ArcRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.ArcRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeAddedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeMovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeMovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeRenamedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeTypeChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.NodeTypeChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.CodeTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.PatternArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.DispatchNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.InputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.OrderedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.PositionalNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.ProceedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.ProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.RandomNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcType;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.networks.NetworkView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.DispatchType;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.PositionalType;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.ProceedType;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.code.CodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.code.DefaultCodeChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.simplepattern.DefaultSimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.simplepattern.SimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultNetworkController extends AbstractController<NetworkView> implements NetworkController {
    
    private class DefaultArcAddedEventListener implements ArcAddedListener {
        
        @Override
        public void arcAdded(final Arc arc) {
            final Class<? extends Arc> type = arc.getClass();
            
            final ArcType arcType = DefaultNetworkController.this.toArcType.get(type);
            
            Preconditions.checkArgument(arcType != null);
            
            DefaultNetworkController.this.callViews(new Callback<NetworkView>() {
                @Override
                public void call(final NetworkView view) {
                    view.arcAdded(arc.getName(), arc.getPriority(), arcType, arc.getFrom().getName(), arc.getTo().getName());
                }
            });
        }
        
    }
    
    private class DefaultArcRemovedEventListener implements ArcRemovedListener {
        
        @Override
        public void arcRemoved(final String name) {
            callViews(new Callback<NetworkView>() {

                @Override
                public void call(final NetworkView view) {
                    view.arcRemoved(name);
                }                
            });
        }
        
    }
    
    private class DefaultNodeAddedEventListener implements NodeAddedListener {
        
        @Override
        public void nodeAdded(final Node node) {
            final Class<? extends Node> type = node.getClass();
            
            final PositionalType positional = DefaultNetworkController.this.toPositional.get(type);
            final ProceedType proceed = DefaultNetworkController.this.toProceed.get(type);
            final DispatchType dispatch = DefaultNetworkController.this.toDispatch.get(type);
            
            Preconditions.checkArgument(positional != null);
            Preconditions.checkArgument(proceed != null);
            Preconditions.checkArgument(dispatch != null);
            
            callViews(new Callback<NetworkView>() {
                @Override
                public void call(final NetworkView view) {
                    view.nodeAdded(node.getName(), positional, proceed, dispatch, node.getX(), node.getY(), DefaultNetworkController.this);
                }                
            });
        }
        
    }
    
    private class DefaultNodeTypeChangedEventListener implements NodeTypeChangedListener {
        
        @Override
        public void nodeTypeChanged(final String name, final Class<? extends Node> newType) {
            final PositionalType positional = DefaultNetworkController.this.toPositional.get(newType);
            final ProceedType proceed = DefaultNetworkController.this.toProceed.get(newType);
            final DispatchType dispatch = DefaultNetworkController.this.toDispatch.get(newType);
            
            Preconditions.checkArgument(positional != null);
            Preconditions.checkArgument(proceed != null);
            Preconditions.checkArgument(dispatch != null);
            
            callViews(new Callback<NetworkView>() {
                @Override
                public void call(final NetworkView view) {
                    view.nodeDispatchTypeChanged(name, dispatch);
                    view.nodeProceedTypeChanged(name, proceed);
                    view.nodePositionalTypeChanged(name, positional);
                }                
            });
        }
        
    }
    
    private class DefaultNodeRemovedEventListener implements NodeRemovedListener {
        
        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.NodeAddedListener#nodeAdded(java.lang.String, int, int)
         */
        @Override
        public void nodeRemoved(final String name) {
            callViews(new Callback<NetworkView>() {
                @Override
                public void call(final NetworkView view) {
                    view.nodeRemoved(name);
                }                
            });
        }
        
    }
    
    private class DefaultNodeRenamedEventListener implements NodeRenamedListener {
        
        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.NodeAddedListener#nodeAdded(java.lang.String, int, int)
         */
        @Override
        public void nodeRenamed(final String name, final String newName) {
            callViews(new Callback<NetworkView>() {
                @Override
                public void call(final NetworkView view) {
                    view.nodeRenamed(name, newName);
                }                
            });
        }
        
    }
    
    private class DefaultNodeMovedEventListener implements NodeMovedListener {
        
        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.NodeAddedListener#nodeAdded(java.lang.String, int, int)
         */
        @Override
        public void nodeMoved(final String name, final int x, final int y) {
            callViews(new Callback<NetworkView>() {
                @Override
                public void call(final NetworkView view) {
                    view.nodeMoved(name, x, y);
                }                
            });
        }
        
    }
    
    private final static Iterable<Class<? extends ProceedNode>> CYCLIC_NODE_TYPES = Iterables.cycle(ImmutableList.of(InputNode.class, ProcessingNode.class));
    private static final Iterable<Class<? extends DispatchNode>> CYCLIC_NODE_DISPATCH_TYPES = Iterables.cycle(ImmutableList.of(OrderedNode.class, RandomNode.class));

    private static final Map<Class<? extends PositionalNode>, PositionalType> TO_POSITIONAL_DEFAULTS;
    private static final Map<Class<? extends ProceedNode>, ProceedType> TO_PROCEED_DEFAULTS;
    private static final Map<Class<? extends DispatchNode>, DispatchType> TO_DISPATCH_DEFAULTS;
    private static final Map<Class<? extends Arc>, ArcType> TO_ARC_TYPE_DEFAULTS;
    private static final SimplePatternChecker DEFAULT_PATTERN_CHECKER =
            DefaultSimplePatternChecker.create();
    
    static {
        final Builder<Class<? extends PositionalNode>, PositionalType> toPositionalDefaultsBuilder = ImmutableMap.builder();
        toPositionalDefaultsBuilder.put(EnterNode.class, PositionalType.ENTER);
        toPositionalDefaultsBuilder.put(InnerNode.class, PositionalType.INNER);
        toPositionalDefaultsBuilder.put(ExitNode.class, PositionalType.EXIT);
        toPositionalDefaultsBuilder.put(IsolatedNode.class, PositionalType.ISOLATED);
        TO_POSITIONAL_DEFAULTS = toPositionalDefaultsBuilder.build();
        
        final Builder<Class<? extends ProceedNode>, ProceedType> toProceedDefaultsBuilder = ImmutableMap.builder();
        toProceedDefaultsBuilder.put(InputNode.class, ProceedType.INPUT);
        toProceedDefaultsBuilder.put(ProcessingNode.class, ProceedType.PROCESSING);
        TO_PROCEED_DEFAULTS = toProceedDefaultsBuilder.build();
        
        final Builder<Class<? extends DispatchNode>, DispatchType> toDispatchDefaultsBuilder = ImmutableMap.builder();
        toDispatchDefaultsBuilder.put(OrderedNode.class, DispatchType.ORDERED);
        toDispatchDefaultsBuilder.put(RandomNode.class, DispatchType.RANDOM);
        TO_DISPATCH_DEFAULTS = toDispatchDefaultsBuilder.build();
        
        final Builder<Class<? extends Arc>, ArcType> toArcTypeDefaultsBuilder = ImmutableMap.builder();
        toArcTypeDefaultsBuilder.put(PatternArc.class, ArcType.PATTERN);
        toArcTypeDefaultsBuilder.put(PredicateTestArc.class, ArcType.PREDICATE_TEST);
        toArcTypeDefaultsBuilder.put(CodeTestArc.class, ArcType.CODE_TEST);
        toArcTypeDefaultsBuilder.put(TransitionArc.class, ArcType.TRANSITION);
        toArcTypeDefaultsBuilder.put(RecurentArc.class, ArcType.RECURENT);
        TO_ARC_TYPE_DEFAULTS = toArcTypeDefaultsBuilder.build();
    }
    
    private final ImmutableMap<Class<? extends PositionalNode>, PositionalType> toPositional;
    private final ImmutableMap<Class<? extends ProceedNode>, ProceedType> toProceed;
    private final ImmutableMap<Class<? extends DispatchNode>, DispatchType> toDispatch;
    
    public final ImmutableMap<Class<? extends Arc>, ArcType> toArcType;
    
    private final Network network;
    
    public static DefaultNetworkController create(final Network network, final EventManager eventManager) {
        final DefaultNetworkController newInstance = new DefaultNetworkController(network, eventManager);
        
        newInstance.addEventListener(NodeAddedEvent.class, newInstance.new DefaultNodeAddedEventListener());
        newInstance.addEventListener(NodeMovedEvent.class, newInstance.new DefaultNodeMovedEventListener());
        newInstance.addEventListener(NodeRemovedEvent.class, newInstance.new DefaultNodeRemovedEventListener());
        newInstance.addEventListener(NodeRenamedEvent.class, newInstance.new DefaultNodeRenamedEventListener());
        newInstance.addEventListener(NodeTypeChangedEvent.class, newInstance.new DefaultNodeTypeChangedEventListener());
        newInstance.addEventListener(ArcAddedEvent.class, newInstance.new DefaultArcAddedEventListener());
        newInstance.addEventListener(ArcRemovedEvent.class, newInstance.new DefaultArcRemovedEventListener());
        
        return newInstance;
    }
    
    private DefaultNetworkController(final Network network,
            final EventManager eventManager) {
        this(network, eventManager, TO_POSITIONAL_DEFAULTS, TO_PROCEED_DEFAULTS, TO_DISPATCH_DEFAULTS, TO_ARC_TYPE_DEFAULTS);
    }
    
    private DefaultNetworkController(final Network network,
            final EventManager eventManager,
            final Map<Class<? extends PositionalNode>, PositionalType> toPositional,
            final Map<Class<? extends ProceedNode>, ProceedType> toProceed,
            final Map<Class<? extends DispatchNode>, DispatchType> toDispatch,
            final Map<Class<? extends Arc>, ArcType> toArcType) {
        super(eventManager);
        
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(toPositional);
        Preconditions.checkNotNull(toProceed);
        Preconditions.checkNotNull(toDispatch);
        Preconditions.checkNotNull(toArcType);
        
        this.network = network;
        this.toPositional = ImmutableMap.copyOf(toPositional);
        this.toProceed = ImmutableMap.copyOf(toProceed);
        this.toDispatch = ImmutableMap.copyOf(toDispatch);
        this.toArcType = ImmutableMap.copyOf(toArcType);
    }
    
    @Override
    public void addNode(final int x, final int y) {
        this.network.addNode(x, y);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#removeNode(java.lang.String)
     */
    @Override
    public void removeNode(final String name) {
        this.network.removeNode(name);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#removeArc(java.lang.String)
     */
    @Override
    public void removeArc(final String name) {
        this.network.removeArc(name);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#addArc(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void addArc(final String name, final String firstName, final String secondName) {
        this.network.addArc(name, firstName, secondName);
    }

    @Override
    public void toggleNodeProceedType(final String nodeName) {
        toggleNodeType(nodeName, CYCLIC_NODE_TYPES);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#toggleNodeDispatchType(java.lang.String)
     */
    @Override
    public void toggleNodeDispatchType(String nodeName) {
        toggleNodeType(nodeName, CYCLIC_NODE_DISPATCH_TYPES);
    }
    
    private void toggleNodeType(final String name, final Iterable<? extends Class<? extends Node>> types) {
        final Node node = this.network.getNode(name);
        
        final UnmodifiableIterator<? extends Class<? extends Node>> cyclicUnmodifiableIterator = Iterators.unmodifiableIterator(types.iterator());
        Optional<?> current = Optional.absent();
        while (cyclicUnmodifiableIterator.hasNext()) {
            final Class<? extends Node> type = cyclicUnmodifiableIterator.next();
            if (type.equals(node.getClass())) {
                this.network.changeNode(name, cyclicUnmodifiableIterator.next());
                return;
            }
            
            if (current.isPresent()) {
                Preconditions.checkArgument(!current.get().equals(type));
            } else {                
                current = Optional.of(type);
            }
        }
    }

    @Override
    public void changeNode(final String nodeName, final String newName) {
        this.network.changeNode(nodeName, newName);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#changeNodeLocation(java.lang.String, int, int)
     */
    @Override
    public void changeNode(String nodeName, int x, int y) {
        this.network.changeNode(nodeName, x, y);
    }
}
