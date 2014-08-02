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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties;

import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;

/**
 * Zobrazovač dostupných referencí (míst do kterých lze zanořit výpočet) v systému. 
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface AvailableReferencesView {
    /**
     * Aktualizuje množinu zobrazených dostupných referencí.
     * 
     * @param references množina dostupných referencí
     */
    void updateAvailableReferences(Set<EnterNode> references);
    
    /**
     * Rozšíří množinu zobrazených dostupných referencí.
     * 
     * @param extension rozšiřující množina dostupných referencí
     */
    void extendAvailableReferences(Set<EnterNode> extension);
    
    /**
     * Zmenší množinu dostupných referencí.
     * 
     * @param removed odstraněná množina dostupných referencí
     */
    void removeAvailableReferences(Set<EnterNode> removed);
}
