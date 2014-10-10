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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.DefaultArcsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkDisplayedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkDisplayedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkDisplayView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.DefaultNodesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;

/**
 * Zobrazovač grafů sítí.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultNetworkDisplayController extends
        AbstractController<NetworkDisplayView> implements
        NetworkDisplayController {

    private final class DefaultNetworkPropertiesDisplayedListener implements
            NetworkDisplayedListener {

        @Override
        public void displayed(final Network network) {
            Preconditions.checkNotNull(network);

            final NetworkController networkController =
                    DefaultNetworkController.create(network, getEventManager());
            final NodesController nodesController =
                    DefaultNodesController.create(
                            DefaultNetworkDisplayController.this.system,
                            network, getEventManager());
            final ArcsController arcsController =
                    DefaultArcsController.create(
                            DefaultNetworkDisplayController.this.system,
                            network, getEventManager());

            callViews(new Callback<NetworkDisplayView>() {

                @Override
                public void call(final NetworkDisplayView view) {
                    Preconditions.checkNotNull(view);

                    view.displayNetwork(
                            networkController,
                            arcsController,
                            nodesController,
                            DefaultNetworkDisplayController.this.arcPropertiesController);
                }

            });
        }

    }

    /**
     * Vytvoří řadič a zaregistruje posluchače pro zobrazení sítí.
     * 
     * @param system
     *            model rodičovského systému sítí
     * @param arcPropertiesController
     *            řadič zobrazovače vlastností hran
     * @param eventManager
     *            správce událostí
     * @return řadič
     */
    public static DefaultNetworkDisplayController create(final System system,
            final ArcPropertiesDisplayController arcPropertiesController,
            final EventManager eventManager) {
        final DefaultNetworkDisplayController newInstance =
                new DefaultNetworkDisplayController(system,
                        arcPropertiesController, eventManager);

        newInstance.addListener(NetworkDisplayedEvent.class, system,
                newInstance.new DefaultNetworkPropertiesDisplayedListener());

        return newInstance;
    }

    private final System system;

    private final ArcPropertiesDisplayController arcPropertiesController;

    private DefaultNetworkDisplayController(final System system,
            final ArcPropertiesDisplayController arcPropertiesContorller,
            final EventManager eventManager) {
        super(eventManager);

        Preconditions.checkNotNull(system);
        Preconditions.checkNotNull(arcPropertiesContorller);

        this.system = system;
        this.arcPropertiesController = arcPropertiesContorller;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.
     * NetworkPropertiesController
     * #displayNetwork(cz.cuni.mff.ms.brodecva.botnicek
     * .ide.design.networks.model.NetworkInfo)
     */
    @Override
    public void displayNetwork(final Network network) {
        Preconditions.checkNotNull(network);

        fire(NetworkDisplayedEvent.create(this.system, network));
    }
}
