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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;

/**
 * Posluchač událost změny názvu cílového uzlu hrany.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface ToRenamedListener {

    /**
     * Reaguje na změnu názvu cílového uzlu hrany.
     * 
     * @param oldVersion
     *            původní verze cílového uzlu
     * @param newVersion
     *            nová verze cílového uzlu
     */
    void toRenamed(Node oldVersion, Node newVersion);

}
