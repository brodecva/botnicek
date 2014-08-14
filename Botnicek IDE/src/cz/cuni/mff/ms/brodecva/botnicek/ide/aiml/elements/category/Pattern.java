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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractRawElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;

/**
 * <p>Prvek popisující vzor svým obsahem.</p>
 * <p>Prvek je vždy obsažen v kategorii, společně se vzorem pro zmínku a šablonou.</p>
 * 
 * @version 1.0
 * @author Václav Brodec
 * @see <a href="http://www.alicebot.org/TR/2011/#section-pattern">http://www.alicebot.org/TR/2011/#section-pattern</a>
 */
public final class Pattern extends AbstractProperElement {
    
    private final class PatternTextElement extends AbstractRawElement {
        
        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getText()
         */
        @Override
        public String getText() {
            return Pattern.this.pattern.getText();
        }
    }

    private static final String NAME = "pattern";
    
    private final MixedPattern pattern;
    
    /**
     * Vytvoří prvek.
     * 
     * @param pattern vzor
     * @return prvek
     */
    public static Pattern create(final MixedPattern pattern) {
        Preconditions.checkNotNull(pattern);
        
        return new Pattern(pattern);
    }
    
    private Pattern(final MixedPattern pattern) {
        this.pattern = pattern;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.AbstractElement#getName()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getChildren()
     */
    @Override
    public List<Element> getChildren() {
        return ImmutableList.<Element>of(new PatternTextElement());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Pattern [pattern=" + pattern + "]";
    }
}
