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
package cz.cuni.mff.ms.brodecva.botnicek.library.responder;

import java.util.EventObject;

/**
 * Událost výjimky v reakci.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ExceptionalState extends EventObject {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 3501264891514991381L;

    /**
     * Výjimka.
     */
    private final Throwable throwable;

    /**
     * Vytvoří stav.
     * 
     * @param source
     *            zdrojová konverzace
     * @param throwable
     *            výjimka či chyba
     */
    public ExceptionalState(final Conversation source, final Throwable throwable) {
        super(source);

        this.throwable = throwable;
    }

    /**
     * Vrátí výjimku.
     * 
     * @return výjimka
     */
    public Throwable getThrowable() {
        return throwable;
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
        result = prime * result + ((throwable == null) ? 0 : throwable.hashCode());
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
        final ExceptionalState other = (ExceptionalState) obj;
        if (throwable == null) {
            if (other.throwable != null) {
                return false;
            }
        } else if (!throwable.equals(other.throwable)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.EventObject#toString()
     */
    @Override
    public String toString() {
        return "ExceptionalState [throwable=" + throwable + ", eventObject=" + super.toString()
                + "]";
    }
}
