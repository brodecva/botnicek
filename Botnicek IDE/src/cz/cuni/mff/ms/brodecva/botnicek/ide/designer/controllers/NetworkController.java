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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.networks.NetworkView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public interface NetworkController extends Controller<NetworkView> {
    void addNode(int x, int y);
    void removeNode(String nodeName);
    
    void changeNode(String nodeName, String newName);
    void changeNode(String nodeName, int x, int y);
    void toggleNodeProceedType(String nodeName);
    void toggleNodeDispatchType(String nodeName);
    
    void addArc(String arcName, String firstNodeName, String secondNodeName);
    void removeArc(String arcName);
}
