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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * AIML šablona s reakcí na dotaz.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLTemplate implements Template, Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 6588376045943793757L;
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLTemplate.class);
    
    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Kód šablony.
     */
    private final String value;

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.Template#getValue()
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Konstruktor šablony s reakcí na dotaz.
     * 
     * @param value
     *            vlastní kód reakce
     */
    public AIMLTemplate(final String value) {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "storage.TemplateCreating", value);
        }
        
        if (value == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("storage.NullArgument"));
        }

        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * jcz.cuni.mff.ms.brodecva.botnicek.library.storage.Template#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime * result
                        + value.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.Template#equals(java
     * .lang.Object)
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
        final AIMLTemplate other = (AIMLTemplate) obj;
        if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.Template#toString()
     */
    @Override
    public String toString() {
        return value;
    }
}
