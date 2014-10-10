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
package cz.cuni.mff.ms.brodecva.botnicek.ide.compile;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;

/**
 * Továrna na kompilátory systému sítí do AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface CompilerFactory {
    /**
     * Vytvoří kompilátor.
     * 
     * @param pullState
     *            název stavu začínajícího vyhození nezpracovaných stavů ze
     *            zásobníku
     * @param pullStopState
     *            název stavu ukončujícího vyhození nezpracovaných stavů ze
     *            zásobníku
     * @param successState
     *            název stavu pro úspěšný průchod podsítí
     * @param returnState
     *            název stavu návratu z podsítě
     * @param randomizeState
     *            název stavu pro zamíchání stavy
     * @param testingPredicate
     *            název testovacího predikátu
     * @return kompilátor
     */
    Compiler produce(NormalWord pullState, NormalWord pullStopState,
            NormalWord randomizeState, NormalWord successState,
            NormalWord returnState, NormalWord testingPredicate);
}
