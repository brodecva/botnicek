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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.views.NodesView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller;

/**
 * Řadič uzlů grafu, které díky němu mohou měnit podobu svého modelu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface NodesController extends Controller<NodesView> {
    /**
     * Změní umístění uzlu v rovině grafu.
     * 
     * @param nodeName
     *            název
     * @param x
     *            souřadnice nové polohy na ose x
     * @param y
     *            souřadnice nové polohy na ose y
     */
    void changeNode(NormalWord nodeName, int x, int y);

    /**
     * Odebere uzel sítě.
     * 
     * @param name
     *            název odebraného uzlu
     */
    void removeNode(NormalWord name);

    /**
     * Přejmenuje uzel (pokud nový název vyhovuje požadavkům na název uzlu).
     * 
     * @param nodeName
     *            původní název
     * @param proposedName
     *            navržený název
     */
    void rename(NormalWord nodeName, String proposedName);

    /**
     * Přepne typy ovlivňující míru determinismu uzlu. Rotuje se mezi postupem
     * přes odchozí hrany v pořadí dle klesající priority a náhodným pořadím s
     * prioritou jako vahou.
     * 
     * @param nodeName
     *            název uzlu
     */
    void toggleNodeDispatchType(NormalWord nodeName);

    /**
     * Přepne typ interaktivity uzlu. Rotuje se mezi zastavením výpočtu pro
     * uživatelský vstup a postupem bez přerušení.
     * 
     * @param nodeName
     *            název uzlu
     */
    void toggleNodeProceedType(NormalWord nodeName);
}
