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

import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;

/**
 * Parser zdrojového kódu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface SourceParser {

    /**
     * Parsuje vstupní proud s daným ID a plní danou strukturu. Pokud je třeba,
     * využívá při tom specifika bota.
     * 
     * @param inputStream
     *            vstupní proud s kódem
     * @param systemId
     *            systémové ID
     * @param filledStructure
     *            plněná struktura
     * @param bot
     *            bot
     * @throws SourceParserException
     *             pokud dojde k chybě při parsování zdroje
     */
    void parse(InputStream inputStream, String systemId,
            MatchingStructure filledStructure, Bot bot)
            throws SourceParserException;
}
