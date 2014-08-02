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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;

/**
 * <p>Abstraktní třída prvku, jehož potomci jsou též prvky.<p>
 * <p>Tato částečná implementace dovoluje jako potomky pouze prvky šablony, neboť pouze jejich zpracování je požadováno.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractCompoundElement extends AbstractProperElement implements TemplateElement {
    
    private final List<TemplateElement> content;
    
    /**
     * Vytvoří složený prvek ze seznamu potomků.
     * 
     * @param children potomci
     */
    protected AbstractCompoundElement(final TemplateElement... children) {
        Preconditions.checkNotNull(children);
        
        this.content = ImmutableList.copyOf(children);
    }
    
    /**
     * Vytvoří složený prvek ze seznamu potomků.
     * 
     * @param children potomci
     */
    protected AbstractCompoundElement(final List<TemplateElement> children) {
        Preconditions.checkNotNull(children);
        
        this.content = ImmutableList.copyOf(children);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement#getChildren()
     */
    public List<Element> getChildren() {
        return ImmutableList.<Element>copyOf(this.content);
    }
}
