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

import java.util.Map;

import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;

/**
 * Konfigurace konverzace.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation
 */
public interface ConversationConfiguration {
    
    /**
     * Vrátí výchozí uživatelské predikáty.
     * 
     * @return výchozí uživatelské predikáty
     */
    Map<String, String> getDefaultPredicates();

    /**
     * Vrátí strategie pro zobrazení výstupu při nastavování uživatelských
     * predikátů daného jména.
     * 
     * @return strategie pro zobrazení
     */
    Map<String, DisplayStrategy> getDisplayStrategies();
}

