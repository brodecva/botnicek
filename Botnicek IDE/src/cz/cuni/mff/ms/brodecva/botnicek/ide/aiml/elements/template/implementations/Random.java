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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.CompoundElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.DefaultListItem;

/**
 * Vybírá s rovnoměrným rozdělením jednu z obsažených položek, jejíž výstup
 * vrátí jako svůj.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-random">http://www.alicebot.org/TR/2011/#section-random</a>
 */
public class Random extends AbstractProperElement implements CompoundElement {
    private static final String NAME = "random";

    /**
     * Vytvoří prvek.
     * 
     * @param choices
     *            možnosti
     * @return prvek
     */
    public static Random create(final List<? extends List<? extends TemplateElement>> choices) {
        return new Random(choices);
    }

    private final List<List<TemplateElement>> choices;

    private Random(final List<? extends List<? extends TemplateElement>> choices) {
        Preconditions.checkNotNull(choices);

        final Builder<List<TemplateElement>> listBuilder =
                ImmutableList.builder();
        for (final List<? extends TemplateElement> choice : choices) {
            Preconditions.checkNotNull(choice);

            listBuilder.add(ImmutableList.copyOf(choice));
        }

        this.choices = listBuilder.build();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement#
     * getChildren()
     */
    @Override
    public List<Element> getChildren() {
        final ImmutableList.Builder<Element> listBuilder =
                ImmutableList.builder();
        for (final List<TemplateElement> choice : this.choices) {
            listBuilder.add(DefaultListItem.create(choice));
        }

        return listBuilder.build();
    }

    /**
     * Vrátí možnosti.
     * 
     * @return možnosti
     */
    public List<List<TemplateElement>> getChoices() {
        return this.choices;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.
     * AbstractElement#getName()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }
}
