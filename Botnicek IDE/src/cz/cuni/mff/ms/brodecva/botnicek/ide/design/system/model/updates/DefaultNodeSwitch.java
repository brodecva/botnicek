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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.updates;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;

/**
 * Implementace {@link NodeSwitch}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultNodeSwitch implements NodeSwitch {
    private final Node from;
    private final Node to;
    
    /**
     * Vytvoří záznam o náhradě.
     * 
     * @param from původní uzel
     * @param to nová uzel
     * @return záznam o náhradě
     */
    public static DefaultNodeSwitch of(final Node from, final Node to) {
        return new DefaultNodeSwitch(from, to);
    }
        
    private DefaultNodeSwitch(final Node from, final Node to) {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        
        this.from = from;
        this.to = to;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NodeSwitch#getFrom()
     */
    @Override
    public Node getFrom() {
        return from;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NodeSwitch#getTo()
     */
    @Override
    public Node getTo() {
        return to;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NodeSwitch [from=" + from + ", to=" + to + "]";
    }
}