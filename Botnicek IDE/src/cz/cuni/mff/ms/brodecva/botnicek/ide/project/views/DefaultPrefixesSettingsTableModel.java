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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.views;

import java.net.URI;
import java.util.Map;
import java.util.TreeMap;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.models.AbstractNameValueTableModel;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;

/**
 * <p>Model tabulky, která obsahuje v prvním sloupci URI prostoru jmen XML a v druhém pak výchozí prefix, které mu budou přiřazeny v dalším zpracování.</p>
 * <p>Je vždy zajištěno, že tabulka obsahuje nějaký prefix pro prostor jmen AIML a nějaký prefix pro jeho schéma.</p>
 * <p>Model nekontroluje unikátnost prefixů. V takové situaci je lepší užít prefixu explicitně ve zdrojovém kódu. Též se nelze spolehnout na validitu samotných řetězců prefixů podle pravidel XML.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class DefaultPrefixesSettingsTableModel extends AbstractNameValueTableModel<URI, String> implements PrefixesSettingsTableModel {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Vytvoří prázdný model (až na výchozí hodnoty).
     * 
     * @return model s výchozími hodnotami
     */
    public static DefaultPrefixesSettingsTableModel create() {
        final TreeMap<URI, String> namespacesToPrefixes = new TreeMap<>();
        
        namespacesToPrefixes.put(URI.create(AIML.NAMESPACE_URI.getValue()), AIML.DEFAULT_PREFIX.getValue());
        namespacesToPrefixes.put(URI.create(XML.SCHEMA_NAMESPACE_URI.getValue()), XML.DEFAULT_SCHEMA_PREFIX.getValue());        
        
        return create(namespacesToPrefixes);
    }
    
    /**
     * Vytvoří model, který nastavuje pro dané prostory jmen dodané prefixy.
     * 
     * @param namespacesToPrefixes zobrazení URI prostorů jmen na prefixy
     * @return model
     * @throws IllegalArgumentException pokud zobrazení neobsahuje položky pro povinné prostory jmen
     */
    public static DefaultPrefixesSettingsTableModel create(final Map<URI, String> namespacesToPrefixes) {
        final ImmutableMap<URI, String> copy = ImmutableMap.copyOf(namespacesToPrefixes);
        
        Preconditions.checkArgument(copy.containsKey(URI.create(AIML.NAMESPACE_URI.getValue())));
        Preconditions.checkArgument(copy.containsKey(URI.create(XML.SCHEMA_NAMESPACE_URI.getValue())));        
        
        return new DefaultPrefixesSettingsTableModel(namespacesToPrefixes);
    }
    
    private DefaultPrefixesSettingsTableModel(final Map<URI, String> namespacesToPrefixes) {
        super(namespacesToPrefixes, UiLocalizer.print("NAMESPACE_COLUMN_NAME"), UiLocalizer.print("PREFIX_COLUMN_NAME"));
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            final Object value = getValueAt(rowIndex, 0);
            if (value.equals(AIML.NAMESPACE_URI.getValue()) || value.equals(XML.SCHEMA_NAMESPACE_URI.getValue())) {
                return false;
            }
        }
        
        return super.isCellEditable(rowIndex, columnIndex);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.models.AbstractNameValueTableModel#update(java.util.Map)
     */
    @Override
    public void update(final Map<URI, String> namespacesToPrefixes) {
        final ImmutableMap<URI, String> copy = ImmutableMap.copyOf(namespacesToPrefixes);
        
        Preconditions.checkArgument(copy.containsKey(URI.create(AIML.NAMESPACE_URI.getValue())));
        Preconditions.checkArgument(copy.containsKey(URI.create(XML.SCHEMA_NAMESPACE_URI.getValue())));        
        
        super.update(copy);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.NameValueTableModel#emptyValue()
     */
    @Override
    protected String defaultValue() {
        return "";
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.NameValueTableModel#nameOf(java.lang.String)
     */
    @Override
    protected URI nameOf(final String nameString) {
        return URI.create(nameString);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.NameValueTableModel#valueOf(java.lang.String)
     */
    @Override
    protected String valueOf(String valueString) {
        return valueString;
    }
}
