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
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;

/**
 * Položka s názvem i hodnotou obsahuje dva významné atributy: název a hodnota.
 * Nachází se výlučně ve vícepredikátovém podmínkovém bloku, kde je jsou
 * procházeny jedna takováto položka po druhé a výstupem je první, u které
 * odpovídá hodnota predikátu (uvedeného v atributu název) vzoru v atributu
 * hodnota.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-condition">http://www.alicebot.org/TR/2011/#section-condition</a>
 */
public final class NameAndValueListItem extends AbstractListItem {
    private static final String ATT_NAME = "name";
    private static final String ATT_VALUE = "value";

    /**
     * Vytvoří položku.
     * 
     * @param name
     *            název testovaného predikátu
     * @param value
     *            vzor očekávané hodnoty
     * @param content
     *            potomci položky
     * @return položka
     */
    public static NameAndValueListItem create(final NormalWord name,
            final SimplePattern value, final List<? extends TemplateElement> content) {
        return new NameAndValueListItem(name, value,
                ImmutableList.copyOf(content));
    }

    /**
     * Vytvoří položku.
     * 
     * @param name
     *            název testovaného predikátu
     * @param value
     *            vzor očekávané hodnoty
     * @param content
     *            potomci položky
     * @return položka
     */
    public static NameAndValueListItem create(final NormalWord name,
            final SimplePattern value, final TemplateElement... content) {
        return new NameAndValueListItem(name, value,
                ImmutableList.copyOf(content));
    }

    private final NormalWord name;

    private final SimplePattern value;

    private NameAndValueListItem(final NormalWord name,
            final SimplePattern value, final List<? extends TemplateElement> content) {
        super(content);

        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(value);

        this.name = name;
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.
     * AbstractElement#getAttributes()
     */
    @Override
    public Set<Attribute> getAttributes() {
        return ImmutableSet
                .<Attribute> of(
                        AttributeImplementation.create(ATT_NAME,
                                this.name.getText()),
                        AttributeImplementation.create(ATT_VALUE,
                                this.value.getText()));
    }

    /**
     * Vrátí název testovaného predikátu.
     * 
     * @return název testovaného predikátu
     */
    public NormalWord getName() {
        return this.name;
    }

    /**
     * Vrátí vzor testující hodnotu predikátu.
     * 
     * @return vzor testující hodnotu predikátu
     */
    public SimplePattern getValue() {
        return this.value;
    }
}
