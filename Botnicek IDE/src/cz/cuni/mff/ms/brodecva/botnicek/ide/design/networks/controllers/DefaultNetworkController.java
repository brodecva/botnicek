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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.AbstractDfsObserver;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcAddedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRenamedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkSelectedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkSelectedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeAddedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.utils.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultNetworkController extends AbstractController<NetworkView> implements NetworkController {
    
    private class DefaultArcAddedEventListener implements ArcAddedListener {
        
        @Override
        public void arcAdded(final Arc arc) {
            Preconditions.checkNotNull(arc);
            
            callViews(new Callback<NetworkView>() {
                @Override
                public void call(final NetworkView view) {
                    view.arcAdded(arc);
                }
            });
        }
        
    }
    
    private class DefaultArcRemovedEventListener implements ArcRemovedListener {
        
        @Override
        public void arcRemoved(final Arc arc) {
            Preconditions.checkNotNull(arc);
            
            callViews(new Callback<NetworkView>() {

                @Override
                public void call(final NetworkView view) {
                    view.arcRemoved(arc);
                }                
            });
        }
        
    }
    
    private class DefaultNodeAddedEventListener implements NodeAddedListener {
        
        @Override
        public void nodeAdded(final Node node) {
            Preconditions.checkNotNull(node);
            
            callViews(new Callback<NetworkView>() {
                @Override
                public void call(final NetworkView view) {
                    view.nodeAdded(node);
                }                
            });
        }
        
    }
    
    private class DefaultNodeRemovedEventListener implements NodeRemovedListener {
        
        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeRemovedListener#nodeRemoved(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
         */
        @Override
        public void nodeRemoved(final Node node) {
            Preconditions.checkNotNull(node);
            
            callViews(new Callback<NetworkView>() {
                @Override
                public void call(final NetworkView view) {
                    view.nodeRemoved(node);
                }                
            });
        }
        
    }
        
    private final class DefaultNetworkRemovedListener implements NetworkRemovedListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRemovedListener#networkRemoved(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
         */
        @Override
        public void networkRemoved(final Network network) {
            Preconditions.checkNotNull(network);
            
            callViews(new Callback<NetworkView>() {

                @Override
                public void call(final NetworkView view) {
                    view.removed();
                }                
            });
        }
        
    }
    
    private final class DefaultNetworkRenamedListener implements NetworkRenamedListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRemovedListener#networkRemoved(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
         */
        @Override
        public void networkRenamed(final Network network) {
            Preconditions.checkNotNull(network);
            
            callViews(new Callback<NetworkView>() {

                @Override
                public void call(final NetworkView view) {
                    view.renamed(network);
                }               
            });
        }
        
    }
    
    private final class DefaultNetworkSelectedListener implements NetworkSelectedListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkSelectedListener#networkSelected(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.NetworkInfo)
         */
        @Override
        public void networkSelected(final Network network) {
            Preconditions.checkNotNull(network);
            
            callViews(new Callback<NetworkView>() {

                @Override
                public void call(final NetworkView view) {
                    Preconditions.checkNotNull(view);
                    
                    view.selected();
                }
                
            });
        }
        
    }
    
    private final Network network;
    
    public static DefaultNetworkController create(final Network network, final EventManager eventManager) {
        final DefaultNetworkController newInstance = new DefaultNetworkController(network, eventManager);
        
        newInstance.addListener(NodeAddedEvent.class, network, newInstance.new DefaultNodeAddedEventListener());
        newInstance.addListener(NodeRemovedEvent.class, network, newInstance.new DefaultNodeRemovedEventListener());
        newInstance.addListener(ArcAddedEvent.class, network, newInstance.new DefaultArcAddedEventListener());
        newInstance.addListener(ArcRemovedEvent.class, network, newInstance.new DefaultArcRemovedEventListener());
        newInstance.addListener(NetworkRemovedEvent.class, network, newInstance.new DefaultNetworkRemovedListener());
        newInstance.addListener(NetworkRenamedEvent.class, network, newInstance.new DefaultNetworkRenamedListener());
        newInstance.addListener(NetworkSelectedEvent.class, network, newInstance.new DefaultNetworkSelectedListener());
        
        return newInstance;
    }
    
    private DefaultNetworkController(final Network network,
            final EventManager eventManager) {
        super(eventManager);
        
        Preconditions.checkNotNull(network);
        
        this.network = network;
    }
    
    @Override
    public void addNode(final int x, final int y) {
        this.network.addNode(x, y);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#removeNode(java.lang.String)
     */
    @Override
    public void removeNode(final NormalWord name) {
        this.network.removeNode(name);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController#removeArc(java.lang.String)
     */
    @Override
    public void removeArc(final NormalWord name) {
        this.network.removeArc(name);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController#addArc(java.lang.String, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void addArc(final String proposedName, final NormalWord firstName, final NormalWord secondName) {
        Preconditions.checkNotNull(proposedName);
        Preconditions.checkNotNull(firstName);
        Preconditions.checkNotNull(secondName);
        
        final NormalWord normalName = NormalWords.from(proposedName);
        
        this.network.addArc(normalName, firstName, secondName);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController#fill(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkView)
     */
    @Override
    public void fill(final NetworkView view) {
        Preconditions.checkNotNull(view);
                
        view.renamed(this.network);
        
        final Collection<Node> nodes = new HashSet<>();
        final Collection<Arc> arcs = new HashSet<>();
        this.network.accept(DefaultDfsVisitor.create(new AbstractDfsObserver() {
            
            /* (non-Javadoc)
             * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.AbstractDfsObserver#notifyDiscovery(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)
             */
            @Override
            public void notifyDiscovery(final Node discovered) {
                final boolean fresh = nodes.add(discovered);
                assert fresh;
            }
            
            /* (non-Javadoc)
             * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.AbstractDfsObserver#notifyExamination(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
             */
            @Override
            public void notifyExamination(final Arc examined) {
                final boolean fresh = arcs.add(examined);
                assert fresh;
            }
        }));
        
        for (final Node node : nodes) {
            view.nodeAdded(node);
        }
        for (final Arc arc : arcs) {
            view.arcAdded(arc);
        }
    }
}
