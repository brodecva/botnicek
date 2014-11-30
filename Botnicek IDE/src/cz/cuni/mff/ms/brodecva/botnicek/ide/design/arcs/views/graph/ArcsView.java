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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.graph;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;

/**
 * Pohled na hrany sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface ArcsView {
    /**
     * Aktualizuje zobrazení hran tak, aby nezahrnovalo odebranou hranu.
     * 
     * @param arc
     *            odebraná hrana
     */
    void arcRemoved(Arc arc);

    /**
     * Změní zobrazení názvu hrany ze staré verze na novou.
     * 
     * @param oldVersion
     *            stará verze hrany
     * @param newVersion
     *            nová verze hrany
     */
    void arcRenamed(Arc oldVersion, Arc newVersion);

    /**
     * Změní zobrazení priority hrany ze staré verze na novou.
     * 
     * @param oldVersion
     *            stará verze hrany
     * @param newVersion
     *            nová verze hrany
     */
    void arcReprioritized(Arc oldVersion, Arc newVersion);

    /**
     * Změní zobrazení typu hrany ze staré verze na novou.
     * 
     * @param oldVersion
     *            stará verze hrany
     * @param newVersion
     *            nová verze hrany
     */
    void arcRetyped(Arc oldVersion, Arc newVersion);
}
