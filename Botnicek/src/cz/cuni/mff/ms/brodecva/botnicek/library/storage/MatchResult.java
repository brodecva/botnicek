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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage;

import java.util.List;

/**
 * Výsledek hledání v dotazovací struktuře. Lze testovat na úspěch, v pozitivním
 * případě obsahuje odpovídající nalezenou šablonu s reakcí.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface MatchResult {

    /**
     * Indikuje úspěch hledání.
     * 
     * @return zda-li bylo hledání úspěšné
     */
    boolean isSuccesful();

    /**
     * Vrátí šablonu s nalezenou reakcí.
     * 
     * @return šablona s nalezenou reakcí
     */
    Template getTemplate();

    /**
     * Vrátí části výsledku nahrazené hvězdičkami - normalizované.
     * 
     * @param part
     *            část vstupní cesty, ke které jsou nahrazené části vztaženy
     * @return nahrazené části
     */
    List<String> getStarMatchedParts(PartMarker part);

    /**
     * Přidá nahrazenou část do výsledku.
     * 
     * @param pathPart
     *            část ve které došlo k nahrazení, nesmí být null
     * @param matchedPart
     *            nahrazená část, nesmí být null
     */
    void addStarMatchedPart(PartMarker pathPart, InputPath matchedPart);

}
