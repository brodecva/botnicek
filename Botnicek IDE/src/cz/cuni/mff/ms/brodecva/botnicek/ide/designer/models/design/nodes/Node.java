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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes;

import java.util.List;
import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NetworkInfo;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.DispatchProcessible;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.ProceedProcessible;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.StackProcessible;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.Visitable;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.Arc;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public interface Node extends Visitable, DispatchProcessible, ProceedProcessible, StackProcessible {
    String getName();
    Network getParent();
    
    int getX();
    int getY();
    
    Set<Arc> getConnections(Direction direction);
    Set<Arc> getOuts();
    Set<Arc> getIns();
    
    int getDegree(Direction direction);
    int getOutDegree();
    int getInDegree();
    
    NetworkInfo getNetwork();
}
