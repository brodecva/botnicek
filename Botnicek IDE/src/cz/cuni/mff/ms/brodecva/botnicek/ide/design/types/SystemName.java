/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;

/**
 * Validní jméno pro systém či jeho část.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class SystemName implements Serializable, Comparable<SystemName> {
    private static final long serialVersionUID = 1L;

    private static final String VALID_TEXT_PATTERN = "[a-zA-Z0-9_]+";

    /**
     * Vytvoří název.
     * 
     * @param text
     *            text názvu
     * @return priorita
     */
    public static SystemName of(final String text) {
        validate(text);

        return new SystemName(text);
    }

    private static void validate(final String text) {
        Preconditions.checkNotNull(text);
        Preconditions.checkArgument(text.matches(VALID_TEXT_PATTERN));
    }

    private final String text;

    private SystemName(final String text) {
        this.text = text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final SystemName name) {
        Preconditions.checkNotNull(name);

        return this.text.compareTo(name.text);
    }

    /**
     * Porovná prioritu s objektem. Shoduje se pouze s prioritou stejné hodnoty.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (Objects.isNull(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SystemName other = (SystemName) obj;
        if (this.text != other.text) {
            return false;
        }
        return true;
    }

    /**
     * Vrátí text názvu.
     * 
     * @return text
     */
    public String getText() {
        return this.text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.text.hashCode();
        return result;
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        validate(this.text);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SystemName [text=" + this.text + "]";
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
