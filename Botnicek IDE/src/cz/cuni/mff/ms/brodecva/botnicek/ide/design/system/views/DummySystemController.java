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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.SystemName;

/**
 * Atrapa řadiče.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DummySystemController implements SystemController {

    /**
     * Vytvoří atrapu.
     * 
     * @return atrapa
     */
    public static DummySystemController create() {
        return new DummySystemController();
    }

    private DummySystemController() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.
     * SystemController#addNetwork(java.lang.String)
     */
    @Override
    public void addNetwork(final String name) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#addView(java
     * .lang.Object)
     */
    @Override
    public void addView(final SystemView view) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#fill(java.lang
     * .Object)
     */
    @Override
    public void fill(final SystemView view) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.
     * SystemController#removeNetwork(java.lang.String)
     */
    @Override
    public void removeNetwork(final SystemName name) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#removeView(
     * java.lang.Object)
     */
    @Override
    public void removeView(final SystemView view) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.
     * SystemController
     * #renameNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design
     * .networks.model.Network, java.lang.String)
     */
    @Override
    public void renameNetwork(final Network network, final String newName) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.
     * SystemController#renameSystem(java.lang.String)
     */
    @Override
    public void renameSystem(final String newName) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.
     * SystemController
     * #selectNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design
     * .networks.model.Network)
     */
    @Override
    public void selectNetwork(final Network selected) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController#removeNetwork(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void removeNetwork(Network removed) {
    }

}
