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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Vytváří pomocné objekty k testování.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ObjectGenerator {
    /**
     * Skrytý konstruktor.
     */
    private ObjectGenerator() {
    }

    /**
     * Vrátí daný počet různých objektů.
     * 
     * @param count
     *            počet
     * @return různé objekty
     */
    public static Object[] getUniqueObjects(final int count) {
        final Object[] result = new Object[count];

        for (int i = 0; i < count; i++) {
            result[i] = new Object();
        }

        return result;
    }

    /**
     * Vrátí daný počet referencí na stejný objekt.
     * 
     * @param count
     *            počet
     * @return reference na stejný objekt
     */
    public static Object[] getEqualObjects(final int count) {
        final Object[] result = new Object[count];

        final Object prototype = new Object();

        for (int i = 0; i < count; i++) {
            result[i] = prototype;
        }

        return result;
    }

    /**
     * Vrátí daný počet různých řetězců.
     * 
     * @param count
     *            počet
     * @return různé řetězce
     */
    public static String[] getUniqueStrings(final int count) {
        final String[] result = new String[count];

        for (int i = 0; i < count; i++) {
            result[i] = "string" + i;
        }

        return result;
    }

    /**
     * Vrátí daný počet referencí na stejný řetězec.
     * 
     * @param count
     *            počet
     * @return reference na stejný řetězec
     */
    public static String[] getEqualStrings(final int count) {
        final String[] result = new String[count];

        final String prototype = "string";

        for (int i = 0; i < count; i++) {
            result[i] = prototype;
        }

        return result;
    }

    /**
     * Vrátí zkopírované pole tolikrát, kolik má položek. V každé kopii bude
     * jedna položka nahrazena, postupně směrem od nejnižšího indexu.
     * 
     * @param array
     *            původní pole
     * @param replacement
     *            nahrazení
     * @return daný počet specifikovaných kopií
     */
    public static Object[][] copyAndReplaceOneInEvery(final Object[] array,
            final Object replacement) {
        final List<Object[]> results = new ArrayList<Object[]>();

        for (int i = 0; i < array.length; i++) {
            final Object[] newResult = Arrays.copyOf(array, array.length);
            newResult[i] = replacement;
            results.add(newResult);
        }

        return results.toArray(new Object[results.size()][]);
    }

}
