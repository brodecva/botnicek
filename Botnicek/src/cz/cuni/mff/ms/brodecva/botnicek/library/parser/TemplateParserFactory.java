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
package cz.cuni.mff.ms.brodecva.botnicek.library.parser;

import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult;

/**
 * Továrna na parsery šablon.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see TemplateParser
 */
public interface TemplateParserFactory {

    /**
     * Vrátí implementaci parseru šablony.
     * 
     * @param conversation
     *            konverzace
     * @param result
     *            výsledek
     * @param forwardProcessingEnabled
     *            dopředné zpracování povoleno
     * @return parser šablony
     */
    TemplateParser getParser(Conversation conversation, MatchResult result,
            boolean forwardProcessingEnabled);
}
