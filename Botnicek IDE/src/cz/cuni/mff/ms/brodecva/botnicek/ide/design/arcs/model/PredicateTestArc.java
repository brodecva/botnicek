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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.TestProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class PredicateTestArc extends AbstractTestArc {
    
    private final Code prepareCode;
    private final NormalWord predicateName;
    
    public static PredicateTestArc create(
            final Network parent,
            final NormalWord name,
            final int priority,
            final Code code,
            final SimplePattern value,
            final Code prepareCode,
            final NormalWord predicateName) {
        return new PredicateTestArc(parent, name, priority, code, value, prepareCode, predicateName);
    }
    
    /**
     * @param from
     * @param to
     */
    private PredicateTestArc(
               final Network parent,
               final NormalWord name,
               final int priority,
               final Code code,
               final SimplePattern value,
               final Code prepareCode,
               final NormalWord predicateName) {
        super(parent, name, priority, code, value);
        
        if (predicateName == null) {
            throw new NullPointerException();
        }
        
        if (prepareCode == null) {
            throw new NullPointerException();
        }
        
        this.prepareCode = prepareCode;
        this.predicateName = predicateName;
    }
    /**
     * @return the prepareCode
     */
    public Code getPrepareCode() {
        return this.prepareCode;
    }

    /**
     * @return the predicateName
     */
    public NormalWord getPredicateName() {
        return this.predicateName;
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
        result =
                prime
                        * result
                        + predicateName
                                .hashCode();
        result =
                prime * result
                        + prepareCode.hashCode();
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
        PredicateTestArc other = (PredicateTestArc) obj;
        if (!predicateName.equals(other.predicateName)) {
            return false;
        }
        if (!prepareCode.equals(other.prepareCode)) {
            return false;
        }
        return true;
    }
}
