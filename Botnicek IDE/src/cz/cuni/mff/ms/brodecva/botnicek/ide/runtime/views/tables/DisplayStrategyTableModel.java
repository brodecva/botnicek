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
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.utils.DefaultDisplayStrategyFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.utils.DisplayStrategyFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Presence;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.models.AbstractNameValueTableModel;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.NameDisplayStretegy;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.ValueDisplayStrategy;

/**
 * Model tabulky s přiřazením predikátu (jeho názvu) k zobrazovací strategie
 * nastavení jeho hodnoty.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DisplayStrategy
 */
public class DisplayStrategyTableModel extends
        AbstractNameValueTableModel<NormalWord, DisplayStrategy> {

    private static final long serialVersionUID = 1L;

    private static final BiMap<String, DisplayStrategy> SUPPORTED =
            ImmutableBiMap.<String, DisplayStrategy> of("name",
                    new NameDisplayStretegy(), "value",
                    new ValueDisplayStrategy());

    /**
     * Vytvoří prázdný model.
     * 
     * @return model
     */
    public static DisplayStrategyTableModel create() {
        return new DisplayStrategyTableModel(
                DefaultDisplayStrategyFactory.create(SUPPORTED));
    }

    /**
     * Vytvoří vyplněný model.
     * 
     * @param namesToValues
     *            původní nastavení
     * @return model
     */
    public static DisplayStrategyTableModel create(
            final Map<? extends NormalWord, ? extends DisplayStrategy> namesToValues) {
        return new DisplayStrategyTableModel(namesToValues,
                DefaultDisplayStrategyFactory.create());
    }

    /**
     * Vytvoří vyplněný model.
     * 
     * @param namesToValues
     *            původní nastavení
     * @param strategyFactory
     *            továrna na konverzi textových popisů na strategie
     * @return model
     */
    public static DisplayStrategyTableModel create(
            final Map<? extends NormalWord, ? extends DisplayStrategy> namesToValues,
            final DisplayStrategyFactory strategyFactory) {
        return new DisplayStrategyTableModel(namesToValues, strategyFactory);
    }

    private final DisplayStrategyFactory strategyFactory;

    private DisplayStrategyTableModel(
            final DisplayStrategyFactory strategyFactory) {
        super(UiLocalizer.print("PREDICATE_COLUMN_NAME"), UiLocalizer
                .print("STRATEGY_COLUMN_NAME"));

        Preconditions.checkNotNull(strategyFactory);

        this.strategyFactory = strategyFactory;
    }

    private DisplayStrategyTableModel(
            final Map<? extends NormalWord, ? extends DisplayStrategy> namesToValues,
            final DisplayStrategyFactory strategyFactory) {
        super(namesToValues, UiLocalizer.print("PREDICATE_COLUMN_NAME"),
                UiLocalizer.print("STRATEGY_COLUMN_NAME"));

        Preconditions.checkNotNull(strategyFactory);

        this.strategyFactory = strategyFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.NameValueTableModel#
     * emptyValue()
     */
    @Override
    protected DisplayStrategy defaultValue() {
        return new ValueDisplayStrategy();
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
        Preconditions.checkNotNull(name);

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
    protected DisplayStrategy valueOf(final String valueString) {
        Preconditions.checkNotNull(valueString);

        return this.strategyFactory.provide(valueString);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.NameValueTableModel#
     * valueToString(java.lang.Object)
     */
    @Override
    protected String valueToString(final DisplayStrategy value) {
        Preconditions.checkNotNull(value);

        final String result =
                ImmutableBiMap.copyOf(this.strategyFactory.getSupported())
                        .inverse().get(value);
        Preconditions.checkArgument(Presence.isPresent(result));

        return result;
    }
}
