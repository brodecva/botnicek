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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers;

import java.util.Set;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkRenamedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.NetworkSelectedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkAddedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkAddedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.NetworkRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.SystemClosedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.SystemClosedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.SystemRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.SystemRenamedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;

/**
 * Výchozí implementace řadiče systému sítí.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultSystemController extends AbstractController<SystemView> implements SystemController {

    private final class DefaultNetworkAddedListener implements NetworkAddedListener {

        @Override
        public void networkAdded(final Network network) {
            Preconditions.checkNotNull(network);
            
            callViews(new Callback<SystemView>() {

                @Override
                public void call(final SystemView view) {
                    Preconditions.checkNotNull(view);
                    
                    view.networkAdded(network);
                }
                
            });
        }
        
    }
    
    private final class DefaultNetworkRemovedListener implements NetworkRemovedListener {

        @Override
        public void removed(final Network network) {
            Preconditions.checkNotNull(network);
            
            callViews(new Callback<SystemView>() {

                @Override
                public void call(final SystemView view) {
                    Preconditions.checkNotNull(view);
                    
                    view.networkRemoved(network);
                }
                
            });
        }
        
    }
    
    private final class DefaultNetworkRenamedListener implements NetworkRenamedListener {

        @Override
        public void renamed(final Network network) {
            Preconditions.checkNotNull(network);
            
            callViews(new Callback<SystemView>() {

                @Override
                public void call(final SystemView view) {
                    Preconditions.checkNotNull(view);
                    
                    view.networkRenamed(network);
                }
            });
        }
        
    }
    
    private final class DefaultSystemRenamedListener implements SystemRenamedListener {

        @Override
        public void systemRenamed(final System system) {
            Preconditions.checkNotNull(system);
            
            callViews(new Callback<SystemView>() {

                @Override
                public void call(final SystemView view) {
                    Preconditions.checkNotNull(view);
                    
                    view.systemNameChanged(system);
                }
                
            });
        }
        
    }
    
    private final class DefaultSystemClosedListener implements SystemClosedListener {

        @Override
        public void closed() {
            Preconditions.checkNotNull(system);
            
            callViews(new Callback<SystemView>() {

                @Override
                public void call(final SystemView view) {
                    Preconditions.checkNotNull(view);
                    
                    view.closed();
                }
                
            });
        }
        
    }
    
    private final System system;
    
    /**
     * Vytvoří řadič systému a zaregistruje posluchače změn.
     * 
     * @param system systém
     * @param eventManager správce událostí
     * @return řadič
     */
    public static DefaultSystemController create(final System system, final EventManager eventManager) {
        final DefaultSystemController newInstance = new DefaultSystemController(system, eventManager); 

        newInstance.addListener(NetworkAddedEvent.class, system, newInstance.new DefaultNetworkAddedListener());
        newInstance.addListener(NetworkRemovedEvent.class, system, newInstance.new DefaultNetworkRemovedListener());
        newInstance.addListener(NetworkRenamedEvent.class, system, newInstance.new DefaultNetworkRenamedListener());
        newInstance.addListener(SystemClosedEvent.class, system, newInstance.new DefaultSystemClosedListener());
        newInstance.addListener(SystemRenamedEvent.class, system, newInstance.new DefaultSystemRenamedListener());
        
        return newInstance;
    }
    
    private DefaultSystemController(final System system, final EventManager eventManager) {
        super(eventManager);
        
        Preconditions.checkNotNull(system);
        
        this.system = system;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController#addNetwork(java.lang.String)
     */
    @Override
    public void addNetwork(final String name) {
        Preconditions.checkNotNull(name);
        
        this.system.addNetwork(name);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController#removeNetwork(java.lang.String)
     */
    @Override
    public void removeNetwork(final String name) {
        Preconditions.checkNotNull(name);
        
        this.system.removeNetwork(name);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController#renameSystem(java.lang.String)
     */
    @Override
    public void renameSystem(final String newName) {
        Preconditions.checkNotNull(newName);
        
        this.system.setName(newName);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController#renameNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.NetworkInfo, java.lang.String)
     */
    @Override
    public void renameNetwork(Network network, String newName) {
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(newName);
        Preconditions.checkArgument(!newName.isEmpty());
        
        this.system.renameNetwork(network, newName);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController#selectNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.NetworkInfo)
     */
    @Override
    public void selectNetwork(final Network selected) {
        Preconditions.checkNotNull(selected);
        
        fire(NetworkSelectedEvent.create(selected));
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController#fill(java.lang.Object)
     */
    @Override
    public void fill(final SystemView view) {
        view.systemSet(this.system);
        
        final Set<Network> networks = this.system.getNetworks();
        for (final Network network : networks) {
            view.networkAdded(network);            
        }
    }

}
