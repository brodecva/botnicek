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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;

/**
 * <p>Model tabulky pro zobrazení seřazených výsledků neúspěšných kontrol zadaných řetězců oproti datovým typům.</p>
 * <p>Umožňuje aktualizovat výsledky tak, že nahrazuje ty, které mají stejný předmět. Výsledek úspěšné kontroly nebude přidán, pouze smaže související negativní.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ResultsTableModel extends AbstractTableModel {
    
    /**
     * Komparátor pro přehledné řazení výsledků (není slučitelný s ekvivalencí výsledků).
     */
    private static final class SortingComparator implements
            Comparator<CheckResult> {
        @Override
        public int compare(final CheckResult first, final CheckResult second) {
            Preconditions.checkNotNull(first);
            Preconditions.checkNotNull(second);
            
            final int sourceComparation = first.getSource().toString().compareTo(second.toString());
            if (sourceComparation != 0) {
                return sourceComparation;
            }
            
            final int lineComparison = Integer.compare(first.getErrorLineNumber(), second.getErrorLineNumber());
            if (lineComparison != 0) {
                return lineComparison;
            }
            
            final int columnComparison = Integer.compare(first.getErrorColumnNumber(), second.getErrorColumnNumber());
            if (columnComparison != 0) {
                return columnComparison;
            }
            
            final int messageComparison = first.getMessage().compareTo(second.getMessage());
            if (messageComparison != 0) {
                return messageComparison;
            }
            
            return 0;
        }
    }

    private static final long serialVersionUID = 1L;
    
    private static final Comparator<CheckResult> SORTING_COMPARATOR = new SortingComparator();
    private static final int COLUMNS_COUNT = 4;
    private static final List<String> COLUMNS_NAMES = ImmutableList.of(UiLocalizer.print("Description"), UiLocalizer.print("Source"), UiLocalizer.print("Line"), UiLocalizer.print("Column"));
    private static final List<Class<?>> COLUMNS_CLASSES = ImmutableList.<Class<?>>of(String.class, String.class, Integer.class, Integer.class);
    
    static {
        assert COLUMNS_NAMES.size() == COLUMNS_COUNT;
        assert COLUMNS_CLASSES.size() == COLUMNS_COUNT;
    }
    
    /**
     * Seznam, který je udržován seřazený v pořadí: podle zdroje, čísla řádky, sloupce a zprávy.
     */
    private final List<CheckResult> results = new LinkedList<>();
    
    /**
     * Vytvoří model.
     * 
     * @return model
     */
    public static ResultsTableModel create() {
        return new ResultsTableModel();
    }
    
    private ResultsTableModel() {
    }
    
    
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(final int column) {
        Preconditions.checkPositionIndex(column, COLUMNS_CLASSES.size());
        
        return COLUMNS_CLASSES.get(column);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return COLUMNS_COUNT;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(final int column) {
        Preconditions.checkPositionIndex(column, COLUMNS_NAMES.size());
        
        return COLUMNS_NAMES.get(column);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return this.results.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final List<CheckResult> rows = new ArrayList<>(this.results);
        final CheckResult row = rows.get(rowIndex);
        
        final List<Object> columns = Lists.<Object>newArrayList(row.getMessage(), row.getSource().toString(), row.getErrorLineNumber(), row.getErrorColumnNumber());
        return columns.get(columnIndex);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    /**
     * Aktualizuje výsledek.
     * 
     * @param result nový výsledek
     */
    public void updateResult(final CheckResult result) {
        Preconditions.checkNotNull(result);
        
        final boolean removed = this.results.remove(result);
        
        final boolean added;
        if (!result.isValid()) {
            added = this.results.add(result);
        } else {
            added = false;
        }
        
        if (removed || added) {
            Collections.sort(this.results, SORTING_COMPARATOR);
            
            fireTableDataChanged();
        }
    }
}
