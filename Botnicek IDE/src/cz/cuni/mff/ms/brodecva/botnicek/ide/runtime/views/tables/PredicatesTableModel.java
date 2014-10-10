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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables;

import java.util.Map;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.models.AbstractNameValueTableModel;

/**
 * Model tabulky s dvěma sloupci. První obsahuje název predikátu, druhý pak jeho
 * hodnotu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class PredicatesTableModel extends
        AbstractNameValueTableModel<NormalWord, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří prázdný model.
     * 
     * @return model
     */
    public static PredicatesTableModel create() {
        return new PredicatesTableModel();
    }

    /**
     * Vytvoří vyplněný model.
     * 
     * @param namesToValues
     *            původní obsah
     * @return model
     */
    public static PredicatesTableModel create(
            final Map<NormalWord, String> namesToValues) {
        return new PredicatesTableModel(namesToValues);
    }

    private PredicatesTableModel() {
        super(UiLocalizer.print("NAME_COLUMN_NAME"), UiLocalizer
                .print("VALUE_COLUMN_NAME"));
    }

    private PredicatesTableModel(final Map<NormalWord, String> namesToValues) {
        super(namesToValues, UiLocalizer.print("NAME_COLUMN_NAME"), UiLocalizer
                .print("VALUE_COLUMN_NAME"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.NameValueTableModel#
     * emptyValue()
     */
    @Override
    protected String defaultValue() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.NameValueTableModel#
     * nameOf(String)
     */
    @Override
    protected NormalWord nameOf(final String nameString) {
        Preconditions.checkNotNull(nameString);
        Preconditions.checkArgument(!nameString.isEmpty());

        return NormalWords.from(nameString);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.models.NameValueTableModel
     * #nameToString(java.lang.Object)
     */
    @Override
    protected String nameToString(final NormalWord name) {
        return name.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.NameValueTableModel#
     * valueOf(String)
     */
    @Override
    protected String valueOf(final String valueString) {
        Preconditions.checkNotNull(valueString);

        return valueString;
    }
}
