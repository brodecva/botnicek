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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage.map;

import cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode;

/**
 * Tovární rozhraní pro {@link Mapper} využitý v rozhodovací struktuře.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface MapperFactory {

    /**
     * Metoda konstruující mapu synovských uzlů s přihlédnutím k zadaným
     * parametrům, jež ovlivňují její vnitřní realizaci tak, aby byla paměťově
     * co nejúspornější bez přílišné újmy na efektivitě prohledávání.
     * 
     * @param depth
     *            hloubka, ve které se nachází uzel, pro který se mapa
     *            konstruuje
     * @param parentalWord
     *            slovo, které bezprostředně odkazuje na uzel, pro který se mapa
     *            konstruuje
     * @param part
     *            část vstupní cesty, ve které se právě při konstrukci nacházíme
     * @return optimalizovaná mapa
     */
    Mapper<Word, WordNode> getMapper(int depth, Word parentalWord,
            PartMarker part);

}
