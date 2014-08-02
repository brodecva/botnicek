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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.graph.ArcsView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller;

/**
 * <p>Řadič poskytující dostupné metody pro ovládání hran sítě pomocí jejich grafické reprezentace.</p>
 * <p>V této verzi neposkytuje žádné metody modifikace, neboť nelze vlastnosti hran editovat jinak než pomocí dedikovaného okna.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see ArcController řadič pro změnu vlastností hrany
 */
public interface ArcsController extends Controller<ArcsView> {
    
    /**
     * Odebere hranu sítě.
     * 
     * @param name název odebírané hrany
     */
    void removeArc(NormalWord name);
}
