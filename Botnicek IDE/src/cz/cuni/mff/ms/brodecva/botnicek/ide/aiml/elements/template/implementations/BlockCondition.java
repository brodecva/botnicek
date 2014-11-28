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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations;

import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractCompoundElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.ConditionElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;

/**
 * Podmínka, která testuje hodnotu predikátu oproti vzoru. V případě úspěchu
 * vrátí výstup potomků.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-conditional-elements">http://www.alicebot.org/TR/2011/#section-conditional-elements</a>
 */
public final class BlockCondition extends AbstractCompoundElement implements
        ConditionElement {
    private static final String NAME = "condition";
    private static final String ATT_NAME = "name";
    private static final String ATT_VALUE = "value";

    /**
     * Vytvoří prvek.
     * 
     * @param name
     *            název testovaného predikátu
     * @param value
     *            vzor hodnoty predikátu
     * @param content
     *            potomci
     * @return prvek
     */
    public static BlockCondition create(final NormalWord name,
            final SimplePattern value, final List<? extends TemplateElement> content) {
        return new BlockCondition(name, value, content);
    }

    /**
     * Vytvoří prvek.
     * 
     * @param name
     *            název testovaného predikátu
     * @param value
     *            vzor hodnoty predikátu
     * @param content
     *            potomci
     * @return prvek
     */
    public static BlockCondition create(final NormalWord name,
            final SimplePattern value, final TemplateElement... content) {
        return new BlockCondition(name, value, ImmutableList.copyOf(content));
    }

    private final NormalWord name;

    private final SimplePattern value;

    private BlockCondition(final NormalWord name, final SimplePattern value,
            final List<? extends TemplateElement> content) {
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
     * @return the name
     */
    @Override
    public String getLocalName() {
        return NAME;
    }
}
