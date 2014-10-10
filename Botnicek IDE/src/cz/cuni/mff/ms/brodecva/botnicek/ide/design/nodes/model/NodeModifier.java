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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;

/**
 * Modifikátor uzlů. Vytvoří novou verzi na základě původní.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface NodeModifier {
    /**
     * Změní typ uzlu.
     * 
     * @param node
     *            uzel
     * @param type
     *            nový typ
     * @return nová verze uzlu
     */
    Node change(Node node, Class<? extends Node> type);

    /**
     * Změní umístění uzlu.
     * 
     * @param node
     *            uzel
     * @param x
     *            nová poloha uzlu v souřadnici x
     * @param y
     *            nová poloha uzlu v souřadnici y
     * @return nová verze uzlu
     */
    Node change(Node node, int x, int y);

    /**
     * Změní název uzlu.
     * 
     * @param node
     *            uzel
     * @param name
     *            nový název
     * @return nová verze uzlu
     */
    Node change(Node node, NormalWord name);

    /**
     * Změní všechny vlastnosti uzlu.
     * 
     * @param node
     *            původní uzel
     * @param name
     *            nový název
     * @param x
     *            nová poloha v souřadnici x
     * @param y
     *            nová poloha v souřadnici y
     * @param type
     *            typ uzlu
     * @return nová verze uzlu
     */
    Node change(Node node, NormalWord name, int x, int y,
            Class<? extends Node> type);
}
