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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events;

import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;

/**
 * Posluchač události zmenšení dostupných míst zanoření.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface AvailableReferencesExtendedListener {

    /**
     * Reaguje na rozšíření.
     * 
     * @param additionalReferences
     *            uzly navíc
     */
    void referencesExtended(Set<? extends EnterNode> additionalReferences);

}
