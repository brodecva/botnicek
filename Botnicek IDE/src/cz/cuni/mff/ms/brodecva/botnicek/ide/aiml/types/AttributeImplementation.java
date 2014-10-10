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

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;

/**
 * Implementace atributu stromu odlehčeného objektového modelu jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AttributeImplementation implements Attribute {
    /**
     * Vytvoří atribut podporované verze jazyka AIML.
     * 
     * @param name
     *            název
     * @param value
     *            hodnota
     * @return atribut
     */
    public static AttributeImplementation create(final String name,
            final String value) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(!name.isEmpty());

        return new AttributeImplementation(name, value, Optional.<URI> absent());
    }

    /**
     * Vytvoří atribut.
     * 
     * @param name
     *            název
     * @param value
     *            hodnota
     * @param namespace
     *            prostor jmen
     * @return atribut
     */
    public static AttributeImplementation create(final String name,
            final String value, final URI namespace) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(value);
        Preconditions.checkNotNull(namespace);
        Preconditions.checkArgument(!name.isEmpty());

        return new AttributeImplementation(name, value, Optional.of(namespace));
    }

    private final String name;

    private final String value;

    private final Optional<URI> namespace;

    private AttributeImplementation(final String name, final String value,
            final Optional<URI> namespace) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(value);
        Preconditions.checkNotNull(namespace);
        Preconditions.checkArgument(!name.isEmpty());

        this.name = name;
        this.value = value;
        this.namespace = namespace;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute#equals(java
     * .lang.Object)
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
        final Attribute other = (Attribute) obj;
        if (!this.name.equals(other.getName())) {
            return false;
        }
        if (!this.namespace.equals(other.getNamespace())) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute#getNamespace()
     */
    @Override
    public URI getNamespace() {
        return this.namespace.orNull();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute#getValue()
     */
    @Override
    public String getValue() {
        return this.value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.name.hashCode();
        result = prime * result + this.namespace.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Attribute [name=" + this.name + ", namespace=" + this.namespace
                + ", value=" + this.value + "]";
    }
}
