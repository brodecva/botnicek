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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.CodeTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PatternArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc;

/**
 * Procesor hrany.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <T>
 *            typ výsledku zpracování
 */
public interface Processor<T> {
    /**
     * Zpracuje hranu testující shodu výstupu zadaného kódu oproti zadané
     * hodnotě.
     * 
     * @param arc
     *            hrana
     * @return výsledek zpracování
     */
    T process(CodeTestArc arc);

    /**
     * Zpracuje hranu testující shodu na vzor.
     * 
     * @param arc
     *            hrana
     * @return výsledek zpracování
     */
    T process(PatternArc arc);

    /**
     * Zpracuje hranu testující shodu hodnoty predikátu se zadanou hodnotou.
     * 
     * @param arc
     *            hrana
     * @return výsledek zpracování
     */
    T process(PredicateTestArc arc);

    /**
     * Zpracuje hranu testující shodu na výstup ze zanořeného výpočtu.
     * 
     * @param arc
     *            hrana
     * @return výsledek zpracování
     */
    T process(RecurentArc arc);

    /**
     * Zpracuje hranu, která přesouvá do koncového stavu bez podmínky.
     * 
     * @param arc
     *            hrana
     * @return výsledek zpracování
     */
    T process(TransitionArc arc);
}
