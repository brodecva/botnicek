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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;

/**
 * Modifikátor hran. Hrany jsou v dané doméně neměnné objekty. V případě
 * nutnosti úpravy hrany je tedy nutné vytvořit novou a tou nahradit výskyt
 * hrany původní.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface ArcModifier {
    /**
     * Vrátí novou hranu na základě původní.
     * 
     * @param arc
     *            původní hrana
     * @param newName
     *            nový název
     * @param priority
     *            nová priorita
     * @param type
     *            nový typ
     * @param arguments
     *            argumenty typu
     * @return nová hrana
     */
    Arc change(final Arc arc, final NormalWord newName,
            final Priority priority, final Class<? extends Arc> type,
            final Object... arguments);
}
