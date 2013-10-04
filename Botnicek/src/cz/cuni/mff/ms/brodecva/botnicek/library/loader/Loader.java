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
package cz.cuni.mff.ms.brodecva.botnicek.library.loader;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;

/**
 * Načítač definicí mozku robota.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Loader {

    /**
     * Načte zdrojový kód z daného umístění (ať už jde o adresář či soubor).
     * 
     * @param path
     *            načítané umístění
     * @param beforeLoadingOrder
     *            specifikované pořadí načítaných souborů v umístění načítaných
     *            přednostně (pro ostatní libovolné)
     * @param afterLoadingOrder
     *            specifikované pořadí načítaných souborů v umístění načítaných
     *            na konec (pro ostatní libovolné)
     * @throws LoaderException
     *             chyba při načítání
     */
    void loadFromLocation(Path path, List<String> beforeLoadingOrder,
            List<String> afterLoadingOrder) throws LoaderException;

    /**
     * Načte zdrojový kód z daného souboru.
     * 
     * @param source
     *            načítaný soubor
     * @throws LoaderException
     *             chyba při načítání
     */
    void loadIndividualFile(Path source) throws LoaderException;

    /**
     * Načte zdrojový kód z proudu.
     * 
     * @param inputStream
     *            vstupní proud
     * @param systemId
     *            systémové ID
     * @throws LoaderException
     *             chyba při načítání
     */
    void loadFromStream(InputStream inputStream, String systemId)
            throws LoaderException;

    /**
     * Načte data dle výchozího nastavení.
     * 
     * @throws LoaderException
     *             chyba při načítání
     */
    void load() throws LoaderException;

    /**
     * Vrátí strukturu naplněnou načítačem.
     * 
     * @return plněná prohledávací struktura
     */
    MatchingStructure getFilledStructure();

    /**
     * Vrátí bota určujícího kontext plnění.
     * 
     * @return bot, který ovlivňuje plnění
     */
    Bot getBot();
}
