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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Codes;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;

/**
 * Hrana, jež neprovádí žádný test a v případě, že přijde na řadu, výpočet po ní projde dál.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class TransitionArc extends AbstractCodeArc {
    
    /**
     * Vrátí výchozí hranu sítě.
     * 
     * @param parent rodičovská síť
     * @param name název hrany
     * @return hrana
     */
    public static TransitionArc getInitial(final Network parent, final NormalWord name) {
        return new TransitionArc(parent, name);
    }
    
    /**
     * Vytvoří hranu.
     * 
     * @param parent rodičovská síť
     * @param name název hrany
     * @param priority priorita
     * @param code kód, který se vždy provede
     * @return hrana
     */
    public static TransitionArc create(final Network parent, final NormalWord name, final Priority priority, final Code code) {
        return new TransitionArc(parent, name, priority, code);
    }
    
    private TransitionArc(final Network parent, final NormalWord name) {
        this(parent, name, DEFAULT_PRIORITY, Codes.createEmpty());
    }
    
    private TransitionArc(final Network parent, final NormalWord name, final Priority priority, final Code code) {
        super(parent, name, priority, code);
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
        final int result = super.hashCode();
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
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TransitionArc [getName()=" + getName() + ", getNetwork()="
                + getNetwork().getName() + ", getFrom()=" + getFrom().getName() + ", getTo()="
                + getTo().getName() + ", getPriority()=" + getPriority().getValue() + "]";
    }
}
