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

import java.io.Serializable;

import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;

/**
 * Implementace továrny na obslužné objekty pro AIML při zpracování SAXem.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLHandlerFactory implements SourceHandlerFactory,
        Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 1089371309288281721L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.loader.SourceHandlerFactory#
     * createContentHandler
     * (cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure,
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot)
     */
    @Override
    public Handler createHandler(
            final MatchingStructure structure, final Bot bot) {
        return new AIMLHandler(structure, bot);
    }

}
