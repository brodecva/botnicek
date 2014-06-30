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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types;

import java.net.URI;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class AttributeImplementation implements Attribute {
    private final String name;
    private final String value;
    private final URI namespace;
    
    public static AttributeImplementation create(final String name, final String value, final URI namespace) {
        return new AttributeImplementation(name, value, namespace);
    }
    
    public static AttributeImplementation create(final String name, final String value) {
        return new AttributeImplementation(name, value, URI.create(AIML.NAMESPACE_URI.getValue()));
    }
    
    /**
     * @param name
     * @param value
     */
    private AttributeImplementation(final String name, final String value, final URI namespace) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(value);
        Preconditions.checkNotNull(namespace);
        Preconditions.checkArgument(!name.isEmpty());
        
        this.name = name;
        this.value = value;
        this.namespace = namespace;
    }
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute#getName()
     */
    @Override
    public String getName() {
        return name;
    }
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute#getValue()
     */
    @Override
    public String getValue() {
        return value;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute#getNamespace()
     */
    @Override
    public URI getNamespace() {
        return this.namespace;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + namespace.hashCode();
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AttributeImplementation other = (AttributeImplementation) obj;
        if (!name.equals(other.name)) {
            return false;
        }
        if (!namespace.equals(other.namespace)) {
            return false;
        }
        return true;
    }
}
