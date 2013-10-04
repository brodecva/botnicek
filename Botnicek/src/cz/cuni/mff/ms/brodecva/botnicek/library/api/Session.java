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
package cz.cuni.mff.ms.brodecva.botnicek.library.api;

import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;

/**
 * Rozhraní vyšší úrovně pro vedení konverzace s roboty.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface Session {
    
    /**
     * Poskytuje přístup k definici robota, se kterým je vedena konverzace.
     * 
     * @return zúčastněný konverzační robot
     */
    Bot getBot();

    /**
     * Vrací instanci probíhající konverzace, která poskytuje metody pro
     * interakci.
     * 
     * @return konverzace
     */
    Conversation getConversation();

    /**
     * Poskytuje přístup k definici aspektů jazyka, které jsou třeba pro vedení
     * konverzace.
     * 
     * @return definice jazyka
     */
    Language getLangugage();
}
