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
package cz.cuni.mff.ms.brodecva.botnicek.library.loader;

/**
 * Spojení strategie pro bílé znaky a prvku.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ElementSpacing {

    /**
     * URI prvku.
     */
    private final String uri;

    /**
     * Jméno prvku.
     */
    private final String name;

    /**
     * Strategie bílých znaků.
     */
    private final SpaceStrategy spaceStrategy;

    /**
     * Vytvoří záznam o strategii pro podstrom s kořenem v prvku.
     * 
     * @param uri
     *            URI prostoru jmen prvku
     * @param name
     *            jméno prvku
     * @param spaceStrategy
     *            strategie pro nakládání s bílými znaky
     */
    public ElementSpacing(final String uri, final String name,
            final SpaceStrategy spaceStrategy) {
        if (name == null) {
            throw new NullPointerException();
        }

        if (spaceStrategy == null) {
            throw new NullPointerException();
        }

        this.uri = uri;
        this.name = name;
        this.spaceStrategy = spaceStrategy;
    }

    /**
     * Vrátí URI prostoru jmen prvku.
     * 
     * @return URI prostoru jmen prvku
     */
    public String getUri() {
        return uri;
    }

    /**
     * Vrátí jméno prvku.
     * 
     * @return jméno prvku
     */
    public String getName() {
        return name;
    }

    /**
     * Vrátí strategii pro bílé znaky.
     * 
     * @return strategie pro bílé znaky
     */
    public SpaceStrategy getSpaceStrategy() {
        return spaceStrategy;
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
        result = prime * result + name.hashCode();
        result = prime * result + spaceStrategy.hashCode();
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        return result;
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
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ElementSpacing other = (ElementSpacing) obj;
        if (!name.equals(other.name)) {
            return false;
        }
        if (!spaceStrategy.equals(other.spaceStrategy)) {
            return false;
        }
        if (uri == null) {
            if (other.uri != null) {
                return false;
            }
        } else if (!uri.equals(other.uri)) {
            return false;
        }
        return true;
    }

}
