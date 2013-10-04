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

import java.util.Iterator;
import java.util.ListIterator;

/**
 * Vstupní cesta.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface InputPath extends Iterable<Word> {
    /**
     * Hodnota prázdné cesty.
     */
    String EMPTY_STRING = "";

    /**
     * Oddělovač slov.
     */
    String WORD_DELIMITER = " ";

    /**
     * Vrátí obousměrný iterátor neměnného seznamu slov vstupní cesty.
     * 
     * @return obousměrný iterátor neměnného seznamu slov vstupní cesty
     */
    ListIterator<Word> listIterator();
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    Iterator<Word> iterator();
    
    /**
     * Vrátí suffix vstupní cesty podle polohy iterátoru.
     * 
     * @param iterator
     *            iterátor vstupní cesty, nesmí být null či nastaven na vyšším
     *            indexu než je délka vstupní cesty
     * @return suffix vstupní cesty
     */
    InputPath getSuffix(ListIterator<Word> iterator);

    /**
     * Vrátí počet zbývajících slov v sekvenci vzhledem k danému iterátoru.
     * 
     * @param pathIterator
     *            iterátor neměnného seznamu slov vstupní cesty
     * @return počet zbývajících slov v sekvenci vzhledem k danému iterátoru (v
     *         případě iterátoru nad větším slovem může být záporné)
     */
    int getRemainingLength(ListIterator<Word> pathIterator);

    /**
     * Vrátí délku vstupní cesty.
     * 
     * @return počet slov v sekvenci slov vstupní cesty
     */
    int getLength();
    
    /**
     * Vrací true pro cestu délky 0, tedy prázdnou cestu.
     * 
     * @return true, pokud je cesta prázdná, jinak false
     */
    boolean isEmpty();

    /**
     * Vrátí úsek cesty.
     * 
     * @param fromIndex
     *            počáteční index (včetně)
     * @param toIndex
     *            koncový index (mimo)
     * @return úsek cesty
     */
    InputPath subPath(int fromIndex, int toIndex);

    /**
     * Vrátí počáteční slovo neprázdné cesty. Pokud je cesta prázdná, vrací
     * {@code null}.
     * 
     * @return počáteční slovo neprázdné cesty
     */
    Word head();

    /**
     * Vrátí ocas cesty. Ocase je zbytek cesty mimo její hlavu. V případě
     * jednoslovné cesty vrací prázdnou cestu. V případě prázdné
     * {@code null}.
     * 
     * @return ocas cesty
     */
    InputPath tail();

    /**
     * Vrátí seznam slov vstupní cesty oddělených mezerami.
     * 
     * @return seznam slov oddělených mezerami
     */
    @Override
    String toString();

    /**
     * Vrátí hash ze seznamu slov.
     * 
     * @return hash spočítanou na základě seznamu slov
     */
    @Override
    int hashCode();

    /**
     * Porovná objekt se vstupní cestou.
     * 
     * @param object
     *            objekt
     * @return true, pokud je objekt vstupní cesta a obsahuje stejný seznam slov
     */
    @Override
    boolean equals(Object object);
}
