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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.networks;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.NetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcType;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.DispatchType;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.NodeUI;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.PositionalType;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.nodes.ProceedType;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public interface NetworkView {
    void nodeAdded(String name, PositionalType positional, ProceedType proceed,
            DispatchType dispatch, int x, int y,
            NetworkController defaultNetworkController);
    
    void nodeRemoved(String name);
    
    void nodeRenamed(String name, String newName);
    
    void nodeMoved(String name, int x, int y);
    
    void nodeProceedTypeChanged(String name, ProceedType type);
    void nodeDispatchTypeChanged(String name, DispatchType type);
    void nodePositionalTypeChanged(String name, PositionalType type);
    
    void arcAdded(String name, int priority, ArcType type, String from,
            String to);
    
    void arcRemoved(String name);

}
