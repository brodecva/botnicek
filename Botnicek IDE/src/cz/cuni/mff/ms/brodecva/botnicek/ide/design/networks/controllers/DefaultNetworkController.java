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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.AbstractDfsObserver;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitorFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsVisitorFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcAddedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRenamedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkSelectedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkSelectedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NodeAddedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;

/**
 * Výchozí implementace řadiče sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultNetworkController extends AbstractController<NetworkView>
        implements NetworkController {

    private class DefaultArcAddedEventListener implements ArcAddedListener {

        @Override
        public void arcAdded(final Arc arc) {
            Preconditions.checkNotNull(arc);

            callViews(new Callback<NetworkView>() {
                @Override
                public void call(final NetworkView view) {
                    Preconditions.checkNotNull(view);

                    view.arcAdded(arc);
                }
            });
        }

    }

    private final class DefaultNetworkRemovedListener implements
            NetworkRemovedListener {

        @Override
        public void removed(final Network network) {
            Preconditions.checkNotNull(network);

            callViews(new Callback<NetworkView>() {

                @Override
                public void call(final NetworkView view) {
                    view.removed();
                }
            });
        }

    }

    private final class DefaultNetworkRenamedListener implements
            NetworkRenamedListener {

        @Override
        public void renamed(final Network network) {
            Preconditions.checkNotNull(network);

            callViews(new Callback<NetworkView>() {

                @Override
                public void call(final NetworkView view) {
                    view.renamed(network);
                }
            });
        }

    }

    private final class DefaultNetworkSelectedListener implements
            NetworkSelectedListener {

        @Override
        public void selected(final Network network) {
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

    /**
     * Vytvoří řadič a zaregistruje posluchače událostí změn v síti.
     * 
     * @param network
     *            model sítě
     * @param eventManager
     *            správce událostí
     * @return řadič
     */
    public static DefaultNetworkController create(final Network network,
            final EventManager eventManager) {
        return create(network, eventManager, DefaultDfsVisitorFactory.create());
    }

    /**
     * Vytvoří řadič a zaregistruje posluchače událostí změn v síti.
     * 
     * @param network
     *            model sítě
     * @param eventManager
     *            správce událostí
     * @param networkVisitorFactory
     *            továrna na návštěvníky sítě
     * @return řadič
     */
    public static DefaultNetworkController create(final Network network,
            final EventManager eventManager,
            final DfsVisitorFactory networkVisitorFactory) {
        final DefaultNetworkController newInstance =
                new DefaultNetworkController(network, eventManager,
                        networkVisitorFactory);

        newInstance.addListener(NodeAddedEvent.class, network,
                newInstance.new DefaultNodeAddedEventListener());
        newInstance.addListener(ArcAddedEvent.class, network,
                newInstance.new DefaultArcAddedEventListener());
        newInstance.addListener(NetworkRemovedEvent.class, network,
                newInstance.new DefaultNetworkRemovedListener());
        newInstance.addListener(NetworkRenamedEvent.class, network,
                newInstance.new DefaultNetworkRenamedListener());
        newInstance.addListener(NetworkSelectedEvent.class, network,
                newInstance.new DefaultNetworkSelectedListener());

        return newInstance;
    }

    private final Network network;

    private final DfsVisitorFactory networkVisitorFactory;

    private DefaultNetworkController(final Network network,
            final EventManager eventManager,
            final DfsVisitorFactory networkVisitorFactory) {
        super(eventManager);

        Preconditions.checkNotNull(network);

        this.network = network;
        this.networkVisitorFactory = networkVisitorFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.
     * NetworkController#addArc(java.lang.String,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void addArc(final String proposedName, final NormalWord firstName,
            final NormalWord secondName) {
        Preconditions.checkNotNull(proposedName);
        Preconditions.checkNotNull(firstName);
        Preconditions.checkNotNull(secondName);

        final NormalWord normalName = NormalWords.from(proposedName);

        this.network.addArc(normalName, firstName, secondName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.
     * NetworkController#addNode(int, int)
     */
    @Override
    public void addNode(final int x, final int y) {
        this.network.addNode(x, y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.
     * NetworkController
     * #fill(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks
     * .views.NetworkView)
     */
    @Override
    public void fill(final NetworkView view) {
        Preconditions.checkNotNull(view);

        view.renamed(this.network);

        final Collection<Node> nodes = new HashSet<>();
        final Collection<Arc> arcs = new HashSet<>();
        this.network.accept(this.networkVisitorFactory
                .produce(new AbstractDfsObserver() {

                    @Override
                    public void notifyDiscovery(final Node discovered) {
                        final boolean fresh = nodes.add(discovered);
                        assert fresh;
                    }

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
