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

/**
 * Strategie pro zpracování bílých znaků.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface SpaceStrategy {

    /**
     * Provede změny na bufferu znaků před vložením začátku tagu, aby byl v
     * souladu se strategií.
     * 
     * @param chars
     *            buffer znaků
     */
    void transformOpenTag(final StringBuilder chars);

    /**
     * Provede změny na bufferu znaků před vložením konce tagu, aby byl v
     * souladu se strategií.
     * 
     * @param chars
     *            buffer znaků
     */
    void transformCloseTag(final StringBuilder chars);
    

    /**
     * Přidá znaky na buffer tak, aby byl v souladu se strategií.
     * 
     * @param chars buffer znaků
     * @param ch pole se znaky k přidání
     * @param start offset přidávané části
     * @param length délka přidávané části
     */
    void transformChars(StringBuilder chars, char[] ch, int start, int length);

    /**
     * Vrátí hash strategie.
     * 
     * @return hash strategie
     */
    @Override
    int hashCode();

    /**
     * Porovná objekt s touto strategií.
     * 
     * @param object
     *            objekt
     * @return true, pokud je objekt strategie stejného typu
     */
    @Override
    boolean equals(final Object object);
}
