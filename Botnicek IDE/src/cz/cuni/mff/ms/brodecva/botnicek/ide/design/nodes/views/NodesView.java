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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;

/**
 * Pohled na uzly v grafu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface NodesView {
    /**
     * Aktualizuje zobrazení polohy uzlu.
     * 
     * @param original
     *            původní verze uzlu
     * @param changed
     *            nová verze uzlu
     */
    void nodeMoved(Node original, Node changed);

    /**
     * Aktualizuje zobrazení vrcholů tak, aby nezahrnovalo odebraný uzel.
     * 
     * @param node
     *            odebraný uzel
     */
    void nodeRemoved(Node node);

    /**
     * Aktualizuje zobrazení názvu uzlu.
     * 
     * @param original
     *            původní verze uzlu
     * @param changed
     *            nová verze uzlu
     */
    void nodeRenamed(Node original, Node changed);

    /**
     * Aktualizuje zobrazení typu uzlu.
     * 
     * @param original
     *            původní verze uzlu
     * @param changed
     *            nová verze uzlu
     */
    void nodeRetyped(Node original, Node changed);
}
