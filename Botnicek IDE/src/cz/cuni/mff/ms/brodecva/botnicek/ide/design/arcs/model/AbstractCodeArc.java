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
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;

/**
 * Abstraktní hrana s kódem, který má být proveden při posunu po ní.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractCodeArc extends AbstractArc implements CodeArc {

    private static final long serialVersionUID = 1L;

    private final Code code;

    /**
     * Vytvoří hranu.
     * 
     * @param parent
     *            rodičovská síť
     * @param name
     *            název hrany
     * @param priority
     *            priorita
     * @param code
     *            kód k provedení při posunu po hraně
     */
    protected AbstractCodeArc(final Network parent, final NormalWord name,
            final Priority priority, final Code code) {
        super(parent, name, priority);

        Preconditions.checkNotNull(code);

        this.code = code;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractCodeArc other = (AbstractCodeArc) obj;
        if (!this.code.equals(other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public final Code getCode() {
        return this.code;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.code.hashCode();
        return result;
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        Preconditions.checkNotNull(this.code);
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
