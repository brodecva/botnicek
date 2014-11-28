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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;

/**
 * <p>
 * Model se dvěma sloupci, které mohou mít libovolný typ (ne nutně stejný) -
 * název a hodnota.
 * </p>
 * <p>
 * V případě, že typ názvu neimplementuje rozhraní {@link Comparable} a není sám
 * vůči sobě porovnatelný, musí dědící třída dodat vlastní {@link Comparator} v
 * konstruktoru.
 * </p>
 * <p>
 * Model očekává unikátní názvy a podporuje výskyt jednoho řádku s prázdným
 * názvem, který je ovšem ignorován a slouží dočasně pro vklad nové hodnoty.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <N>
 *            typ názvu
 * @param <V>
 *            typ hodnoty
 */
public abstract class AbstractNameValueTableModel<N, V> extends
        AbstractTableModel implements NameValueTableModel<N, V> {

    /**
     * Komparátor, který jako základ využívá porovnatelný typ.
     * 
     * @param <N>
     *            porovnávaný typ
     */
    private static final class NaturalComparator<N> implements Comparator<N> {
        @Override
        public int compare(final N first, final N second) {
            @SuppressWarnings("unchecked")
            final Comparable<? super N> castFirst =
                    (Comparable<? super N>) first;

            return castFirst.compareTo(second);
        }
    }

    /**
     * Dekorátor komparátoru volitelných hodnot, který zařadí chybějící hodnoty
     * na konec.
     * 
     * @param <N>
     *            porovnávaný typ
     */
    private static final class OptionalLastComparator<N> implements
            Comparator<Optional<N>> {
        private final Comparator<? super N> comparator;

        private OptionalLastComparator(final Comparator<? super N> keyComparator) {
            this.comparator = keyComparator;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(final Optional<N> first, final Optional<N> second) {
            if (first.isPresent()) {
                if (second.isPresent()) {
                    return this.comparator.compare(first.get(), second.get());
                } else {
                    return -1;
                }
            } else {
                if (second.isPresent()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    private static final long serialVersionUID = 1L;

    private static final int COLUMNS_COUNT = 2;
    private static final int NAME_COLUMN_INDEX = 0;
    private static final int VALUE_COLUMN_INDEX = 1;

    private final Map<Optional<N>, V> namesToValues;
    private final List<String> columnNames;

    /**
     * Vytvoří model.
     * 
     * @param nameComparator
     *            komparátor názvů
     * @param nameColumnName
     *            název sloupce s názvy
     * @param valueColumnName
     *            název sloupce s hodnotami
     */
    protected AbstractNameValueTableModel(
            final Comparator<? super N> nameComparator,
            final String nameColumnName, final String valueColumnName) {
        this(ImmutableMap.<N, V> of(), nameComparator, nameColumnName,
                valueColumnName);
    }

    /**
     * Vytvoří model.
     * 
     * @param namesToValues
     *            zobrazení názvů na hodnoty
     * @param nameComparator
     *            komparátor názvů
     * @param nameColumnName
     *            název sloupce s názvy
     * @param valueColumnName
     *            název sloupce s hodnotami
     */
    protected AbstractNameValueTableModel(final Map<? extends N, ? extends V> namesToValues,
            final Comparator<? super N> nameComparator,
            final String nameColumnName, final String valueColumnName) {
        Preconditions.checkNotNull(namesToValues);
        Preconditions.checkNotNull(nameComparator);
        Preconditions.checkNotNull(nameColumnName);
        Preconditions.checkNotNull(valueColumnName);

        final Map<N, V> namesToValuesCopy = ImmutableMap.copyOf(namesToValues);

        this.namesToValues =
                new TreeMap<Optional<N>, V>(new OptionalLastComparator<N>(
                        nameComparator));
        for (final Entry<N, V> entry : namesToValuesCopy.entrySet()) {
            this.namesToValues.put(Optional.of(entry.getKey()),
                    entry.getValue());
        }

        this.columnNames = ImmutableList.of(nameColumnName, valueColumnName);
    }

    /**
     * Vytvoří model, který srovnává názvy podle přirozeného řazení.
     * 
     * @param namesToValues
     *            zobrazení názvů na hodnoty
     * @param nameColumnName
     *            název sloupce názvů
     * @param valueColumnName
     *            název sloupce hodnot
     */
    protected AbstractNameValueTableModel(final Map<? extends N, ? extends V> namesToValues,
            final String nameColumnName, final String valueColumnName) {
        this(namesToValues, new NaturalComparator<N>(), nameColumnName,
                valueColumnName);
    }

    /**
     * Vytvoří model, který srovnává názvy podle přirozeného řazení.
     * 
     * @param nameColumnName
     *            název sloupce názvů
     * @param valueColumnName
     *            název sloupce hodnot
     */
    protected AbstractNameValueTableModel(final String nameColumnName,
            final String valueColumnName) {
        this(new TreeMap<N, V>(), nameColumnName, valueColumnName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.models.NameValueTableModel
     * #addRow()
     */
    @Override
    public final void addRow() {
        this.namesToValues.put(Optional.<N> absent(), defaultValue());
        fireTableDataChanged();
    }

    /**
     * Vrátí výchozí hodnotu.
     * 
     * @return výchozí hodnota hodnota
     */
    protected abstract V defaultValue();

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(final int column) {
        return String.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return COLUMNS_COUNT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(final int column) {
        Preconditions.checkPositionIndex(column, this.columnNames.size());

        return this.columnNames.get(column);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.models.NameValueTableModel
     * #getNamesToValues()
     */
    @Override
    public final Map<N, V> getNamesToValues() {
        final Builder<N, V> resultBuilder = ImmutableMap.builder();
        for (final Entry<Optional<N>, V> entry : this.namesToValues.entrySet()) {
            final Optional<N> possibleKey = entry.getKey();
            if (!possibleKey.isPresent()) {
                continue;
            }

            resultBuilder.put(possibleKey.get(), entry.getValue());
        }

        return resultBuilder.build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return this.namesToValues.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final List<Entry<Optional<N>, V>> rows =
                new ArrayList<>(this.namesToValues.entrySet());
        final Entry<Optional<N>, V> row = rows.get(rowIndex);

        final Optional<N> rowKey = row.getKey();
        final String keyValue =
                rowKey.isPresent() ? nameToString(rowKey.get()) : "";

        final List<Object> columns =
                Lists.<Object> newArrayList(keyValue,
                        valueToString(row.getValue()));
        return columns.get(columnIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return true;
    }

    /**
     * Konverze textu na název.
     * 
     * @param nameString
     *            text s názvem
     * @return název
     */
    protected abstract N nameOf(final String nameString);

    /**
     * Konverze názvu na text.
     * 
     * @param name
     *            název
     * @return textová hodnota
     */
    protected String nameToString(final N name) {
        Preconditions.checkNotNull(name);

        return name.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
     * int, int)
     */
    @Override
    public void setValueAt(final Object aValue, final int rowIndex,
            final int columnIndex) {
        if (Presence.isAbsent(aValue)) {
            return;
        }

        final String currentKeyString =
                (String) getValueAt(rowIndex, NAME_COLUMN_INDEX);
        final Optional<N> currentKey =
                currentKeyString.isEmpty() ? Optional.<N> absent() : Optional
                        .of(nameOf(currentKeyString));

        if (columnIndex == NAME_COLUMN_INDEX) {
            final String newKeyString = (String) aValue;

            if (newKeyString.isEmpty()) {
                this.namesToValues.remove(currentKey);
                fireTableDataChanged();
            } else {
                final Optional<N> newKey;
                try {
                    newKey = Optional.of(nameOf(newKeyString));
                } catch (final IllegalArgumentException e) {
                    return;
                }

                final V currentValue =
                        valueOf((String) getValueAt(rowIndex,
                                VALUE_COLUMN_INDEX));

                this.namesToValues.remove(currentKey);
                this.namesToValues.put(newKey, currentValue);
                fireTableDataChanged();
            }
        } else if (columnIndex == VALUE_COLUMN_INDEX) {
            final V newValue;
            try {
                newValue = valueOf((String) aValue);
            } catch (final IllegalArgumentException e) {
                return;
            }

            this.namesToValues.put(currentKey, newValue);
            fireTableDataChanged();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.models.NameValueTableModel
     * #update(java.util.Map)
     */
    @Override
    public void update(final Map<N, V> namesToValues) {
        Preconditions.checkNotNull(namesToValues);

        final Map<N, V> namesToValuesCopy = ImmutableMap.copyOf(namesToValues);

        this.namesToValues.clear();
        for (final Entry<N, V> entry : namesToValuesCopy.entrySet()) {
            this.namesToValues.put(Optional.of(entry.getKey()),
                    entry.getValue());
        }

        fireTableDataChanged();
    }

    /**
     * Konverze textu na hodnotu.
     * 
     * @param valueString
     *            text s hodnotou
     * @return hodnota
     */
    protected abstract V valueOf(final String valueString);

    /**
     * Konverze hodnoty na text.
     * 
     * @param value
     *            hodnota
     * @return textová hodnota
     */
    protected String valueToString(final V value) {
        Preconditions.checkNotNull(value);

        return value.toString();
    }
}
