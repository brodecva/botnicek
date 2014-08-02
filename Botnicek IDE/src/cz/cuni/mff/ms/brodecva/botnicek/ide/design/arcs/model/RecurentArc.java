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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;

/**
 * Hrana, jež pro vyhodnocení testu provede zanoření do jiné (i vlastní) sítě přes její vstupní uzel. V případě, že výpočet projde úspěšně podsítí, test projde.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class RecurentArc extends AbstractCodeArc {
    
    private final EnterNode target;
    
    /**
     * Vytvoří hranu.
     * 
     * @param parent rodičovská síť
     * @param name název hrany
     * @param priority priorita
     * @param code kód k provedení
     * @param target cíl zanoření
     * @return hrana
     */
    public static RecurentArc create(final Network parent, final NormalWord name, final Priority priority,
            final Code code, final EnterNode target) {
        return new RecurentArc(parent, name, priority, code, target);
    }
    
    private RecurentArc(final Network parent, final NormalWord name, final Priority priority,
            final Code code, final EnterNode target) {
        super(parent, name, priority, code);
        
        Preconditions.checkNotNull(target);
        
        this.target = target;
    }

    /**
     * Vstupní uzel sítě, který je cílem zanoření.
     * 
     * @return cíl zanoření
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + target.hashCode();
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
        final RecurentArc other = (RecurentArc) obj;
        if (!target.equals(other.target)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RecurentArc [getName()=" + getName() + ", getNetwork()="
                + getNetwork().getName() + ", getFrom()=" + getFrom().getName() + ", getTo()="
                + getTo().getName() + ", getPriority()=" + getPriority().getValue() + ", target="
                + target.getName() + "(" + target.getNetwork().getName() + ") ]";
    }
}
