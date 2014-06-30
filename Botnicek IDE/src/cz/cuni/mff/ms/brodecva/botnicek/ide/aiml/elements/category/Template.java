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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;


/**
 * @author Václav Brodec
 * @version 1.0
 */
public class Template extends AbstractElement {
    private static final String NAME = "template";
    
    private List<TemplateElement> content;
    
    public static Template create(final TemplateElement... content) {
        return new Template(content);
    }
    
    public static Template create(final List<TemplateElement> content) {
        return new Template(content);
    }
    
    private Template(final TemplateElement... content) {
        this(Arrays.asList(content));
    }
    
    private Template(final List<TemplateElement> content) {
        Preconditions.checkNotNull(content);
        
        this.content = ImmutableList.copyOf(content);
    }
    
    public List<Element> getChildren() {
        return ImmutableList.<Element>copyOf(this.content);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.AbstractElement#getName()
     */
    @Override
    public String getName() {
        return NAME;
    }    
}
