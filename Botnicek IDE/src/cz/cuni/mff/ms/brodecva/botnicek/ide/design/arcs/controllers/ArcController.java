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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.ArcView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller;

/**
 * Řadič vlastností hrany.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface ArcController extends Controller<ArcView> {
    /**
     * Aktualizuje hranu testující kód.
     * 
     * @param newName
     *            nové jméno hrany
     * @param priority
     *            nová priorita hrany
     * @param code
     *            nový kód
     * @param testedCode
     *            kód generující testovanou hodnotu
     * @param value
     *            očekávaná hodnota
     */
    void updateCodeTest(String newName, int priority, String code,
            String testedCode, String value);

    /**
     * Aktualizuje hranu testující vzor.
     * 
     * @param newName
     *            nové jméno hrany
     * @param priority
     *            nová priorita hrany
     * @param pattern
     *            nový vzor
     * @param that
     *            nový vzor zmínky
     * @param code
     *            nový kód
     */
    void updatePattern(String newName, int priority, String pattern,
            String that, String code);

    /**
     * Aktualizuje hranu testující predikát.
     * 
     * @param newName
     *            nové jméno hrany
     * @param priority
     *            nová priorita hrany
     * @param code
     *            nový kód
     * @param prepareCode
     *            přípravný kód
     * @param predicateName
     *            název testovaného predikátu
     * @param value
     *            očekávaná hodnota
     */
    void updatePredicateTest(String newName, int priority, String code,
            String prepareCode, String predicateName, String value);

    /**
     * Aktualizuje hranu testující vzor.
     * 
     * @param newName
     *            nové jméno hrany
     * @param priority
     *            nová priorita hrany
     * @param code
     *            nový kód
     * @param target
     *            uzel zanoření
     */
    void updateRecurent(String newName, int priority, String code,
            EnterNode target);

    /**
     * Aktualizuje vždy průchozí hranu.
     * 
     * @param newName
     *            nové jméno hrany
     * @param priority
     *            nová priorita hrany
     * @param code
     *            nový kód
     */
    void updateTransition(String newName, int priority, String code);
}
