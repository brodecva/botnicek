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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.collect.ImmutableTable.Builder;

/**
 * Tabulky.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class Tables {
    /**
     * Převede reprezentace tabulky.
     * 
     * @param tableMap tabulka ve formě dvojitého zobrazení
     * @return tabulka
     */
    public static <R, C, V> Table<R, C, V> toImmutableTable(final Map<R, Map<C, V>> tableMap) {
        Preconditions.checkNotNull(tableMap);
        
        final Builder<R, C, V> builder = ImmutableTable.builder();
        
        final Set<Entry<R, Map<C, V>>> rowEntries = tableMap.entrySet();
        for (final Entry<R, Map<C, V>> rowEntry : rowEntries) {
            final R rowKey = rowEntry.getKey();
            
            final Map<C, V> row = rowEntry.getValue();
            final Set<Entry<C, V>> cellEntries = row.entrySet(); 
            for (final Entry<C, V> cellEntry : cellEntries) {
                final C columnKey = cellEntry.getKey();
                final V value = cellEntry.getValue();
                
                builder.put(rowKey, columnKey, value);
            }
        }
        
        return builder.build();
    }
}
