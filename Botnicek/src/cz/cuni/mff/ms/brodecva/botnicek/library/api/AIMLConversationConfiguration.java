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

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.NameDisplayStretegy;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.ValueDisplayStrategy;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassMap;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap;

/**
 * Konfigurace konverzace.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation
 */
public final class AIMLConversationConfiguration implements
        ConversationConfiguration, Serializable {
    
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -4286965766803256340L;
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLConversationConfiguration.class);
    
    /**
     * Název strategie pro zobrazení názvu predikátu při nastavení.
     */
    private static final String NAME_DISPLAY_STRATEGY_NAME = "name";
    
    /**
     * Název strategie pro zobrazení hodnoty predikátu při nastavení.
     */
    private static final String VALUE_DISPLAY_STRATEGY_NAME = "value";
    
    /**
     * Zobrazení názvu strategií na implementace.
     */
    private static final ClassMap<String, DisplayStrategy> STRATEGIES_MAP = new SimpleClassMap<String, DisplayStrategy>();
    
    static {
        STRATEGIES_MAP.put(NAME_DISPLAY_STRATEGY_NAME, NameDisplayStretegy.class);
        STRATEGIES_MAP.put(VALUE_DISPLAY_STRATEGY_NAME, ValueDisplayStrategy.class);
    }
    
    /**
     * Výchozí predikáty.
     */
    private final Map<String, String> defaultPredicates;
    
    /**
     * Strategie vracení výstupu při nastavení predikátu daného jména.
     */
    private final Map<String, DisplayStrategy> displayStrategies;
    
    /**
     * Načte konfiguraci konverzace z {@link Properties}.
     * 
     * @param defaultPredicates
     *            výchozí názvy a hodnoty predikátů
     * @param displayStrategies
     *            strategie vracení výstupu při nastavení predikátu daného jména (páry název predikátu a název strategie - name či value)
     * @return načtená konfigurace
     * @throws ConfigurationException
     *             chyba ve formátu konfigurace
     */
    public static ConversationConfiguration create(final Properties defaultPredicates,
            final Properties displayStrategies) throws ConfigurationException {
        return create(Property.toMap(defaultPredicates),
                Property.toMap(displayStrategies));
    }

    /**
     * Načte konfiguraci konverzace z {@link Properties}.
     * 
     * @param defaultPredicates
     *            výchozí názvy a hodnoty predikátů
     * @param displayStrategies
     *            strategie vracení výstupu při nastavení predikátu daného jména (páry název predikátu a název strategie - name či value)
     * @return načtená konfigurace
     * @throws ConfigurationException
     *             chyba ve formátu konfigurace
     */
    public static ConversationConfiguration create(
            final Map<String, String> defaultPredicates,
            final Map<String, String> displayStrategies) throws ConfigurationException {
        final Map<String, String> validDefaultPredicates =
                Configuration.readValidEntries(defaultPredicates);
        LOGGER.log(Level.INFO, "api.ConversationPredicatesRead", new Object[] { validDefaultPredicates });
        
        final Map<String, DisplayStrategy> validDisplayStrategies = Configuration.readValidStrategies(displayStrategies, STRATEGIES_MAP);
        LOGGER.log(Level.INFO, "api.ConversationStrategiesRead", new Object[] { validDisplayStrategies });
        
        return new AIMLConversationConfiguration(validDefaultPredicates, validDisplayStrategies);
    }
    
    /**
     * Vytvoří konfiguraci.
     * 
     * @param defaultPredicates výchozí predikáty
     * @param displayStrategies strategie zobrazení při nastavení predikátu
     */
    private AIMLConversationConfiguration(
            final Map<String, String> defaultPredicates,
            final Map<String, DisplayStrategy> displayStrategies) {
        this.defaultPredicates = defaultPredicates;
        this.displayStrategies = displayStrategies;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.ConversationConfiguration#getDefaultPredicates()
     */
    @Override
    public Map<String, String> getDefaultPredicates() {
        return Collections.unmodifiableMap(defaultPredicates);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.ConversationConfiguration#getDisplayStrategies()
     */
    @Override
    public Map<String, DisplayStrategy> getDisplayStrategies() {
        return Collections.unmodifiableMap(displayStrategies);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final int maxLen = 10;
        StringBuilder builder = new StringBuilder();
        builder.append("AIMLConversationConfiguration [defaultPredicates=");
        builder.append(Text.toString(
                defaultPredicates.entrySet(), maxLen));
        builder.append(", displayStrategies=");
        builder.append(Text.toString(
                displayStrategies.entrySet(), maxLen));
        builder.append("]");
        return builder.toString();
    }
}
