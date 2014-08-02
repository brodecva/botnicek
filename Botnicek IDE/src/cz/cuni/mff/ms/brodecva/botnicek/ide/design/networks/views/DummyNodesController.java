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
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views.NodesView;

/**
 * Atrapa řadiče uzlů grafu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
class DummyNodesController implements NodesController {

    /**
     * Vytvoří atrapu.
     * 
     * @return atrapa
     */
    public static DummyNodesController create() {
        return new DummyNodesController();
    }
    
    private DummyNodesController() {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#addView(java.lang.Object)
     */
    @Override
    public void addView(NodesView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#removeView(java.lang.Object)
     */
    @Override
    public void removeView(NodesView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#fill(java.lang.Object)
     */
    @Override
    public void fill(NodesView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController#rename(java.lang.String, java.lang.String)
     */
    @Override
    public void rename(NormalWord nodeName, String proposedName) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController#changeNode(java.lang.String, int, int)
     */
    @Override
    public void changeNode(NormalWord nodeName, int x, int y) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController#toggleNodeProceedType(java.lang.String)
     */
    @Override
    public void toggleNodeProceedType(NormalWord nodeName) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController#toggleNodeDispatchType(java.lang.String)
     */
    @Override
    public void toggleNodeDispatchType(NormalWord nodeName) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController#removeNode(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void removeNode(NormalWord name) {
    }

}
