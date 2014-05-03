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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.TestProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Codes;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class TransitionArc extends AbstractCodeArc {
    
    public static TransitionArc create(final Network parent, final String name) {
        return new TransitionArc(parent, name);
    }
    
    public static TransitionArc create(final Network parent, final String name, final int priority, final Code code) {
        return new TransitionArc(parent, name, DEFAULT_PRIORITY, code);
    }
    
    protected TransitionArc(final Network parent, final String name) {
        this(parent, name, DEFAULT_PRIORITY, Codes.createEmpty());
    }
    
    protected TransitionArc(final Network parent, final String name, final int priority, final Code code) {
        super(parent, name, priority, code);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.api.Visitable#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.api.Visitor)
     */
    @Override
    public void accept(final Processor visitor) {
        visitor.process(this);
    }
}
