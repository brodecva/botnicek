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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types;

import com.google.common.base.Preconditions;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class Attribute {
    private final String name;
    private final String value;
    
    public static Attribute create(final String name, final String value) {
        return new Attribute(name, value);
    }
    
    /**
     * @param name
     * @param value
     */
    private Attribute(final String name, final String value) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(!name.isEmpty());
        
        this.name = name;
        this.value = value;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Attribute other = (Attribute) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
