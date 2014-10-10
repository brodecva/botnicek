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

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.SystemName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller;

/**
 * Řadič pro modifikaci systému sítí. Umožňuje kromě nastavení systému přidávat
 * a odebírat sítě, přejmenovávat je, vybírat k zobrazení.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface SystemController extends Controller<SystemView> {
    /**
     * Přidá novou síť.
     * 
     * @param name
     *            název nové sítě
     */
    void addNetwork(String name);

    /**
     * Odstraní síť.
     * 
     * @param name
     *            název sítě k odstranění
     */
    void removeNetwork(SystemName name);
    
    /**
     * Odstraní síť.
     * 
     * @param removed
     *            síť k odstranění
     */
    void removeNetwork(Network removed);

    /**
     * Přejmenuje síť.
     * 
     * @param network
     *            síť
     * @param newName
     *            nový název
     */
    void renameNetwork(Network network, String newName);

    /**
     * Přejmenuje systém sítí.
     * 
     * @param newName
     *            nový název
     */
    void renameSystem(String newName);

    /**
     * Dá pokyn k výběru sítě.
     * 
     * @param selected
     *            vybraná síť.
     */
    void selectNetwork(Network selected);
}
