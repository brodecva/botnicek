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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Jednoduchá implementace {@link ClassMap}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * 
 * @param <K>
 *            klíč
 * @param <T>
 *            nadtyp tříd k uložení
 */
public final class SimpleClassMap<K, T> implements ClassMap<K, T>, Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 5221832949428746175L;

    /**
     * Mapa tříd.
     */
    private final Map<K, Class<? extends T>> classMap;

    /**
     * Výchozí konstruktor.
     */
    public SimpleClassMap() {
        classMap = new HashMap<K, Class<? extends T>>();
    }

    /**
     * Vytvoří kopii mapy tříd.
     * 
     * @param original
     *            původní mapa tříd
     */
    public SimpleClassMap(final SimpleClassMap<K, T> original) {
        classMap = new HashMap<K, Class<? extends T>>(original.classMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.utils.ClassMap#get(java.lang
     * .Object)
     */
    @Override
    public Class<? extends T> get(final K key) {
        return classMap.get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.utils.ClassMap#put(java.lang
     * .Object, java.lang.Class)
     */
    @Override
    public void put(final K key, final Class<? extends T> klass) {
        classMap.put(key, klass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.utils.ClassMap#put(java.lang
     * .Object, java.lang.String)
     */
    @Override
    public void put(final K key, final String fullName)
            throws ClassNotFoundException {
        final Class<?> klass = Class.forName(fullName);

        @SuppressWarnings("unchecked")
        final Class<? extends T> castClass = (Class<? extends T>) klass;

        classMap.put(key, castClass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.utils.ClassMap#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + classMap.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.utils.ClassMap#equals(java.lang
     * .Object)
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
        @SuppressWarnings("rawtypes")
        final SimpleClassMap other = (SimpleClassMap) obj;
        if (!classMap.equals(other.classMap)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SimpleClassMap [classMap=" + classMap + "]";
    }
}
