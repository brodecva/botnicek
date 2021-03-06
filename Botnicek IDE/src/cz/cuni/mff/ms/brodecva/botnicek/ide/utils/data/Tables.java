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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ImmutableTable.Builder;
import com.google.common.collect.Table;

/**
 * Pomocné metody pro práci s tabulkami.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class Tables {
    /**
     * Převede reprezentace tabulky.
     * 
     * @param tableMap
     *            tabulka ve formě dvojitého zobrazení
     * @return tabulka
     */
    public static <R, C, V> Table<R, C, V> toImmutableTable(
            final Map<? extends R, ? extends Map<? extends C, ? extends V>> tableMap) {
        Preconditions.checkNotNull(tableMap);

        final Builder<R, C, V> builder = ImmutableTable.builder();

        final Set<? extends Entry<? extends R, ? extends Map<? extends C, ? extends V>>> rowEntries = tableMap.entrySet();
        for (final Entry<? extends R, ? extends Map<? extends C, ? extends V>> rowEntry : rowEntries) {
            final R rowKey = rowEntry.getKey();

            final Map<? extends C, ? extends V> row = rowEntry.getValue();
            final Set<? extends Entry<? extends C, ? extends V>> cellEntries = row.entrySet();
            for (final Entry<? extends C, ? extends V> cellEntry : cellEntries) {
                final C columnKey = cellEntry.getKey();
                final V value = cellEntry.getValue();

                builder.put(rowKey, columnKey, value);
            }
        }

        return builder.build();
    }
}
