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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller;

/**
 * Řadič pro správu sítě, především přidávání prvků.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface NetworkController extends Controller<NetworkView> {
    
    /**
     * Přidá izolovaný uzel do sítě.
     * 
     * @param x souřadnice nového uzlu na ose x
     * @param y souřadnice nového uzlu na ose y
     */
    void addNode(int x, int y);
    
    
    /**
     * Přidá výchozí typ hrany mezi uzly sítě.
     * 
     * @param name název nové hrany
     * @param fromName výchozí uzel hrany
     * @param toName cílový uzel hrany
     */
    void addArc(String name, NormalWord fromName, NormalWord toName);

}
