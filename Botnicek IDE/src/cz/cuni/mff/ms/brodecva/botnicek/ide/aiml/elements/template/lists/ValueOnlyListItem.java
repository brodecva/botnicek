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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists;

import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;

/**
 * Položka pouze s hodnotou obsahuje jediný významný atribut hodnota. Nachází se
 * výlučně v jednopredikátovém podmínkovém bloku, kde je porovnáván vzor hodnoty
 * s hodnotou predikátu společného pro všechny položky bloku.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-condition">http://www.alicebot.org/TR/2011/#section-condition</a>
 */
public final class ValueOnlyListItem extends AbstractListItem {
    private static final String ATT_VALUE = "value";

    /**
     * Vytvoří položku.
     * 
     * @param value
     *            vzor hodnoty
     * @param content
     *            potomci prvku
     * @return položka
     */
    public static ValueOnlyListItem create(final SimplePattern value,
            final List<? extends TemplateElement> content) {
        return new ValueOnlyListItem(value, content);
    }

    /**
     * Vytvoří položku.
     * 
     * @param value
     *            vzor hodnoty
     * @param content
     *            potomci prvku
     * @return položka
     */
    public static ValueOnlyListItem create(final SimplePattern value,
            final TemplateElement... content) {
        return new ValueOnlyListItem(value, content);
    }

    private final SimplePattern value;

    private ValueOnlyListItem(final SimplePattern value,
            final List<? extends TemplateElement> content) {
        super(content);

        Preconditions.checkNotNull(value);

        this.value = value;
    }

    private ValueOnlyListItem(final SimplePattern value,
            final TemplateElement... content) {
        this(value, ImmutableList.copyOf(content));
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.
     * AbstractElement#getAttributes()
     */
    @Override
    public Set<Attribute> getAttributes() {
        return ImmutableSet.<Attribute> of(AttributeImplementation.create(
                ATT_VALUE, this.value.getText()));
    }

    /**
     * Vrátí vzor hodnoty.
     * 
     * @return vzor hodnoty
     */
    public SimplePattern getValue() {
        return this.value;
    }
}
