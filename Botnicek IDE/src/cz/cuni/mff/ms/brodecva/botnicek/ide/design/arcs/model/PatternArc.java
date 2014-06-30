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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model;

import java.util.List;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.TestProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class PatternArc extends AbstractCodeArc {
    
    private final SimplePattern pattern;
    private final SimplePattern that;
    
    public static PatternArc create(final Network parent, final NormalWord name, final int priority, final Code code, final SimplePattern pattern, final SimplePattern that) {
        return new PatternArc(parent, name, priority, code, pattern, that);
    }
    
    /**
     * @param from
     * @param to
     */
    protected PatternArc(final Network parent, final NormalWord name, final int priority, final Code code, final SimplePattern pattern, final SimplePattern that) {
        super(parent, name, priority, code);
        
        Preconditions.checkNotNull(pattern);
        Preconditions.checkNotNull(that);
        
        this.pattern = pattern;
        this.that = that;
    }
    
    /**
     * @return
     */
    public SimplePattern getPattern() {
        return pattern;
    }

    /**
     * @return
     */
    public SimplePattern getThat() {
        return that;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.api.Visitable#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.api.Visitor)
     */
    @Override
    public void accept(final Processor visitor) {
        visitor.process(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + pattern.hashCode();
        result = prime * result + that.hashCode();
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PatternArc other = (PatternArc) obj;
        if (!pattern.equals(other.pattern)) {
            return false;
        }
        if (!that.equals(other.that)) {
            return false;
        }
        return true;
    }
}
