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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.validator;

import java.net.URI;
import java.util.Map;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.validator.Validator;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;

/**
 * Validátor řetězce s předpokládaným složeným vzorem dle specifikace jazyka
 * AIML, který vysílá po kontrole událost s výsledkem.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface MixedPatternValidator extends Validator<MixedPattern> {
    /**
     * Nastavení robota užitá pro validaci.
     * 
     * @return nastavení robota
     */
    BotConfiguration getBotSettings();
    
    /**
     * Nastavení jazyka užitá pro validaci.
     * 
     * @return nastavení jazyka
     */
    LanguageConfiguration getLanguageSettings();

    /**
     * Vrátí vzájemně jednoznačné zobrazení jmenných prostorů a jejich výchozích prefixů.
     * 
     * @return vzájemně jednoznačné zobrazení jmenných prostorů a jejich výchozích prefixů
     */
    Map<URI, String> getNamespacesToPrefixes();
}
