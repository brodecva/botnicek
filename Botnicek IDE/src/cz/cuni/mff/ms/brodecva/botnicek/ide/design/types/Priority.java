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
 * <p>
 * Priorita hrany určuje pořadí, v jakém je vyzkoušena hrana při průchodu sítí.
 * </p>
 * <p>
 * Podle typu výchozího uzlu je tomu tak přímo, či je při náhodném výběru
 * následující hrany v poměru priorit znásobena pravděpodobnost výběru.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class Priority implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Minimální hodnota priority.
     */
    public static final int MIN_VALUE = 0;

    /**
     * Maximální hodnota priority.
     */
    public static final int MAX_VALUE = 5;

    /**
     * Výchozí hodnota priority.
     */
    public static final int DEFAULT = 1;

    /**
     * Vytvoří míru priority.
     * 
     * @param value
     *            hodnota priority
     * @return priorita
     */
    public static Priority of(final int value) {
        return new Priority(value);
    }

    private final int value;

    private Priority(final int value) {
        Preconditions.checkArgument(value >= 0);

        this.value = value;
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
        final Priority other = (Priority) obj;
        if (this.value != other.value) {
            return false;
        }
        return true;
    }

    /**
     * Vrátí hodnotu priority.
     * 
     * @return hodnota
     */
    public int getValue() {
        return this.value;
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
        result = prime * result + this.value;
        return result;
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        Preconditions.checkArgument(this.value >= 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Priority [value=" + this.value + "]";
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
