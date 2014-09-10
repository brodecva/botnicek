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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;

/**
 * Abstraktní hrana, která obsahuje hodnotu ve formě prostého vzoru jazyka AIML, které musí specifikovaný test dosáhnout.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractTestArc extends AbstractCodeArc implements TestArc {
    
    private static final long serialVersionUID = 1L;
    
    private final SimplePattern value;
    
    /**
     * Vytvoří hranu.
     * 
     * @param parent rodičovská síť
     * @param name název hrany
     * @param priority priorita
     * @param code kód k provedení při posunu po hraně
     * @param value vzor, vůči kterému je porovnáván test hrany
     */
    protected AbstractTestArc(final Network parent, final NormalWord name,
            final Priority priority, final Code code, final SimplePattern value) {
        super(parent, name, priority, code);
        
        Preconditions.checkNotNull(value);
        
        this.value = value;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TestArc#getValue()
     */
    public SimplePattern getValue() {
        return this.value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + value.hashCode();
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
        final AbstractTestArc other = (AbstractTestArc) obj;
        if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }
    
    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        
        Preconditions.checkNotNull(this.value);
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
