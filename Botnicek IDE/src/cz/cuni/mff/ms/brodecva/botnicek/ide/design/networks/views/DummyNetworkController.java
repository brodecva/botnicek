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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController;

/**
 * Atrapa řadiče sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class DummyNetworkController implements NetworkController {

    /**
     * Vytvoří atrapu.
     * 
     * @return atrapa
     */
    public static DummyNetworkController create() {
        return new DummyNetworkController();
    }

    private DummyNetworkController() {
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
    public void addArc(final String proposedArcName,
            final NormalWord firstNodeName, final NormalWord secondNodeName) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #addNode(int, int)
     */
    @Override
    public void addNode(final int x, final int y) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController
     * #addView(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.NetworkView)
     */
    @Override
    public void addView(final NetworkView view) {
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
    public void removeView(final NetworkView view) {
    }

}