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

import java.util.List;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.TestProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class RecurentArc extends AbstractTestArc {
    
    private final EnterNode target;
    
    public RecurentArc create(final Network parent, final String name, final int priority,
            final Code code, final SimplePattern value, final EnterNode target) {
        final RecurentArc newInstance = new RecurentArc(parent, name, priority, code, value, target);
        
        return newInstance;
    }
    
    protected RecurentArc(final Network parent, final String name, final int priority,
            final Code code, final SimplePattern value, final EnterNode target) {
        super(parent, name, priority, code, value);
        
        Preconditions.checkNotNull(target);
        
        this.target = target;
    }

    /**
     * @return the target
     */
    public EnterNode getTarget() {
        return target;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.api.Visitable#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.api.Visitor)
     */
    @Override
    public void accept(final Processor visitor) {
        visitor.process(this);
    }
}
