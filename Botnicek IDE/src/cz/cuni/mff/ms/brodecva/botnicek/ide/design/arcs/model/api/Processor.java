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
 */
public interface Processor {
    /**
     * Zpracuje hranu testující shodu na vzor.
     * 
     * @param arc hrana
     */
    void process(PatternArc arc);
    
    /**
     * Zpracuje hranu testující shodu na výstup ze zanořeného výpočtu.
     * 
     * @param arc hrana
     */
    void process(RecurentArc arc);
    
    /**
     * Zpracuje hranu testující shodu výstupu zadaného kódu oproti zadané hodnotě.
     * 
     * @param arc hrana
     */
    void process(CodeTestArc arc);
    
    /**
     * Zpracuje hranu testující shodu hodnoty predikátu se zadanou hodnotou.
     * 
     * @param arc hrana
     */
    void process(PredicateTestArc arc);
    
    /**
     * Zpracuje hranu, která přesouvá do koncového stavu bez podmínky.
     * 
     * @param arc hrana
     */
    void process(TransitionArc arc);
}
