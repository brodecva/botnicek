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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources;

/**
 * <p>
 * Překladač řetězců. Dovoluje specifikovat parametry, která se dosadí na
 * vyznačená místa.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Localizer {

    /**
     * Vrátí přeloženou zprávu.
     * 
     * @param key
     *            klíč pro hledaný řetězec, musí pro něj existovat překlad
     * @param params
     *            objekty k substituci do lokalizované zprávy
     * @return řetězec pro daný klíč
     * @see java.util.logging.Logger#log(java.util.logging.Level, String,
     *      Object[]) Obdobně užitá metoda
     */
    String getMessage(final String key, final Object... params);

}