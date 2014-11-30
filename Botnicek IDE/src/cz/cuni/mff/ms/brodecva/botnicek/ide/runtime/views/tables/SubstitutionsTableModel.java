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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.tables;

import java.util.Comparator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.models.AbstractNameValueTableModel;

/**
 * Model tabulky se dvěma sloupci. První obsahuje regulární výrazy, jimiž
 * zachycené úseky textu budou nahrazeny odpovídajícím textem v druhém.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class SubstitutionsTableModel extends
        AbstractNameValueTableModel<Pattern, String> {

    /**
     * Porovnává regulární výrazy podle výchozího porovnání řetězců, jimiž jsou
     * zapsány.
     */
    private static final class PatternComparator implements Comparator<Pattern> {
        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(final Pattern first, final Pattern second) {
            Preconditions.checkNotNull(first);
            Preconditions.checkNotNull(second);

            return first.toString().compareTo(second.toString());
        }
    }

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří prázdný model.
     * 
     * @return model
     */
    public static SubstitutionsTableModel create() {
        return new SubstitutionsTableModel();
    }

    /**
     * Vytvoří předvyplněný model.
     * 
     * @param patternsToSubs
     *            původní obsah
     * @return model
     */
    public static SubstitutionsTableModel create(
            final Map<? extends Pattern, ? extends String> patternsToSubs) {
        return new SubstitutionsTableModel(patternsToSubs);
    }

    private SubstitutionsTableModel() {
        super(new PatternComparator(),
                UiLocalizer.print("PATTERN_COLUMN_NAME"), UiLocalizer
                        .print("VALUE_COLUMN_NAME"));
    }

    private SubstitutionsTableModel(final Map<? extends Pattern, ? extends String> patternsToSubs) {
        super(patternsToSubs, new PatternComparator(), UiLocalizer
                .print("PATTERN_COLUMN_NAME"), UiLocalizer
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
    protected Pattern nameOf(final String nameString) {
        Preconditions.checkNotNull(nameString);

        try {
            return Pattern.compile(nameString);
        } catch (final PatternSyntaxException e) {
            throw new IllegalArgumentException(e);
        }
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
