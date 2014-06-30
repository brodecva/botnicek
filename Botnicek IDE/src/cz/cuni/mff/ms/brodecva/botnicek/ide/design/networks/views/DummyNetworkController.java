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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController;

/**
 * @author Václav Brodec
 * @version 1.0
 */
final class DummyNetworkController implements NetworkController {


    /**
     * @return
     */
    public static DummyNetworkController create() {
        return new DummyNetworkController();
    }
    
    private DummyNetworkController() {
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #addView(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView)
     */
    @Override
    public void addView(NetworkView view) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #
     * removeView(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView
     * )
     */
    @Override
    public void removeView(NetworkView view) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #addNode(int, int)
     */
    @Override
    public void addNode(int x, int y) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #removeNode(java.lang.String)
     */
    @Override
    public void removeNode(NormalWord nodeName) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #addArc(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void addArc(String arcName, NormalWord firstNodeName,
            NormalWord secondNodeName) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #removeArc(java.lang.String)
     */
    @Override
    public void removeArc(NormalWord arcName) {

    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController#fill(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkView)
     */
    @Override
    public void fill(NetworkView view) {
    }

}