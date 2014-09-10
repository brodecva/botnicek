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
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;

/**
 * Implementace hrany, jejíž test projde pouze tehdy, pokud zadaný testovací kód má výstup odpovídající požadované hodnotě vzoru.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class CodeTestArc extends AbstractTestArc {
    
    private static final long serialVersionUID = 1L;
    
    private final Code tested;
    
    /**
     * Vytvoří hranu.
     * 
     * @param parent rodičovská síť
     * @param name název hrany
     * @param priority priorita
     * @param code kód k provedení v případě splnění testu
     * @param value očekávaná hodnota testu
     * @param tested kód generující testovanou hodnotu
     * @return hrana
     */
    public static CodeTestArc create(
            final Network parent,
            final NormalWord name,
            final Priority priority,
            final Code code,
            final SimplePattern value,
            final Code tested) {
        return new CodeTestArc(parent, name, priority, code, value, tested);
    }
    
    private CodeTestArc(
               final Network parent,
               final NormalWord name,
               final Priority priority,
               final Code code,
               final SimplePattern value,
               final Code tested) {
        super(parent, name, priority, code, value);
        
        Preconditions.checkNotNull(tested);
        
        this.tested = tested;
    }
    
    /**
     * Vrátí testovací kód.
     * 
     * @return kód jehož výstup je porovnáván s očekávanou hodnotou
     */
    public Code getTested() {
        return this.tested;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processible#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processor)
     */
    @Override
    public <T> T accept(final Processor<T> processor) {
        return processor.process(this);
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CodeTestArc [getName()=" + getName() + ", getNetwork()="
                + getNetwork() + ", getFrom()=" + getFrom() + ", getTo()="
                + getTo() + ", getPriority()=" + getPriority()
                + ", getValue()=" + getValue() + ", tested=" + tested + "]";
    }
    
    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        
        Preconditions.checkNotNull(this.tested);
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
