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
import java.util.regex.Pattern;

/**
 * Konfigurace jazyka.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface LanguageConfiguration {
    /**
     * Vrátí název jazyka.
     * 
     * @return název jazyka
     */
    String getName();
    
    /**
     * Vrátí oddělovač vět.
     * 
     * @return oddělovač vět
     */
    Pattern getSentenceDelim();
    
    /**
     * Vrátí záměny pro mužský a ženský rod.
     * 
     * @return záměny pro mužský a ženský rod.
     */
    Map<Pattern, String> getGenderSubs();
    
    /**
     * Vrátí záměny pro 1. a 2. osobu.
     * 
     * @return záměny pro 1. a 2. osobu.
     */
    Map<Pattern, String> getPersonSubs();
    
    /**
     * Vrátí záměny pro 1. a 3. osobu.
     * 
     * @return záměny pro 1. a 3. osobu.
     */
    Map<Pattern, String> getPerson2Subs();
    
    /**
     * Vrátí záměny pro rozvinutí zkratek.
     * 
     * @return záměny pro rozvinutí zkratek
     */
    Map<Pattern, String> getAbbreviationsSubs();
    
    /**
     * Vrátí záměny pro opravu pravopisu.
     * 
     * @return záměny pro opravu pravopisu
     */
    Map<Pattern, String> getSpellingSubs();
    
    /**
     * Vrátí záměny emotikon.
     * 
     * @return záměny emotikon
     */
    Map<Pattern, String> getEmoticonsSubs();

    /**
     * Vrátí záměny vnitřní interpunkce (mimo hranice vět).
     * 
     * @return záměny vnitřní interpunkce (mimo hranice vět)
     */
    Map<Pattern, String> getInnerPunctuationSubs();
}
