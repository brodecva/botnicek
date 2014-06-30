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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractRawElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class Pattern extends AbstractElement {
    private static final String NAME = "pattern";
    
    private final MixedPattern pattern;
    
    public static Pattern create(final MixedPattern pattern) {
        return new Pattern(pattern);
    }
    
    private Pattern(final MixedPattern pattern) {
        Preconditions.checkNotNull(pattern);
        
        this.pattern = pattern;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.AbstractElement#getName()
     */
    @Override
    public String getName() {
        return NAME;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getChildren()
     */
    @Override
    public List<Element> getChildren() {
        return ImmutableList.<Element>of(new AbstractRawElement() {
            
            @Override
            public String getName() {
                return NAME;
            }
            
            /* (non-Javadoc)
             * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getText()
             */
            @Override
            public String getText() {
                return pattern.getText();
            }
        });
    }
}
