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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLTemplate;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordTree;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory;

/**
 * Testuje vzor pomocí prázdné {@link MatchingStructure}, do které vloží vzor a
 * na takto vzniklé struktuře se pokusí vyhledat daný text.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLStructuralMatcher implements Matcher, Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 6220062835753007619L;

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLStructuralMatcher.class);

    /**
     * Zástupný text šablony. Při porovnání se nevyužívá.
     */
    private static final String DUMMY_TEMPLATE_TEXT = "";

    /**
     * Normalizér textu.
     */
    private Normalizer normalizer = new SimpleNormalizer();

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.utils.Matcher#matches(java.lang
     * .String, java.lang.String,
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot)
     */
    @Override
    public boolean matches(final String text, final String pattern) {
        final MatchingStructure arbitter =
                new WordTree(new FrugalMapperFactory());

        arbitter.add(new AIMLInputPath(pattern, null, null), new AIMLTemplate(
                DUMMY_TEMPLATE_TEXT));

        final MatchResult result =
                arbitter.find(new AIMLInputPath(normalizer
                        .convertToNormalChars(text), null, null));

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "utils.MatcherResult", new Object[] { text,
                    pattern, result });
        }

        return result.isSuccesful();
    }
}
