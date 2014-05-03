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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.toplevel;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.Toplevel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.category.Pattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.category.Template;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.category.That;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class Category extends AbstractProperElement implements Toplevel {
    private static final String NAME = "category";
    
    private final MixedPattern pattern;
    private final SimplePattern that;
    private final Template template;
    
    public static Category create(final MixedPattern pattern, SimplePattern that, Template template) {
        return new Category(pattern, that, template);
    }
    
    private Category(final MixedPattern pattern, final SimplePattern that, final Template template) {
        Preconditions.checkNotNull(pattern);
        Preconditions.checkNotNull(that);
        Preconditions.checkNotNull(template);
        
        this.pattern = pattern;
        this.that = that;
        this.template = template;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.AbstractElement#getName()
     */
    @Override
    public String getName() {
        return NAME;
    }
    
    public List<Element> getChildren() {
        return ImmutableList.<Element>of(Pattern.create(this.pattern), That.create(this.that), this.template);
    }
}
