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

import com.google.common.base.Preconditions;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class CodeTestArc extends AbstractTestArc {
    
    private final Code tested;
    
    public static CodeTestArc create(
            final Network parent,
            final NormalWord name,
            final int priority,
            final Code code,
            final SimplePattern value,
            final Code tested) {
        return new CodeTestArc(parent, name, priority, code, value, tested);
    }
    
    /**
     * @param from
     * @param to
     */
    private CodeTestArc(
               final Network parent,
               final NormalWord name,
               final int priority,
               final Code code,
               final SimplePattern value,
               final Code tested) {
        super(parent, name, priority, code, value);
        
        Preconditions.checkNotNull(tested);
        
        this.tested = tested;
    }
    
    /**
     * @return the tested
     */
    public Code getTested() {
        return this.tested;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.api.Visitable#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.api.Visitor)
     */
    @Override
    public void accept(final Processor processor) {
        processor.process(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + tested.hashCode();
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
        final CodeTestArc other = (CodeTestArc) obj;
        if (!tested.equals(other.tested)) {
            return false;
        }
        return true;
    }
}
