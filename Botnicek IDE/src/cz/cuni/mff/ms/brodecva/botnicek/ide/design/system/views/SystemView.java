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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;

/**
 * Pohled na systém.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface SystemView {
    
    /**
     * Nastaví užitý systém.
     * 
     * @param system systém
     */
    void systemSet(System system);
    
    /**
     * Aktualizuje zobrazený název systému.
     * 
     * @param system systém
     */
    void systemNameChanged(System system);
    
    /**
     * Zobrazí přidanou síť.
     * 
     * @param added nová síť
     */
    void networkAdded(Network added);
    
    /**
     * Aktualizuje zobrazení tak, aby neobsahovalo odstraněnou síť.
     * 
     * @param network síť
     */
    void networkRemoved(Network network);
    
    /**
     * Aktualizuje zobrazený název sítě.
     * 
     * @param network síť
     */
    void networkRenamed(Network network);
    
    /**
     * Vybere pro uživatele danou síť.
     * 
     * @param network síť
     */
    void networkSelected(Network network);
    
    /**
     * Signalizuje uzavření systému.s
     */
    void closed();
}
