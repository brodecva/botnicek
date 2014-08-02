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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text;

/**
 * Konfigurace AIML robota.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot
 */
public final class AIMLBotConfiguration implements BotConfiguration,
        Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 7297669968890376999L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLBotConfiguration.class);

    /**
     * Klíč ke jménu.
     */
    public static final String NAME_KEY = "Name";

    /**
     * Klíč umístění souborů.
     */
    public static final String FILES_KEY = "FilesLocation";

    /**
     * Klíč cesty k souboru s promluvami.
     */
    public static final String GOSSIP_PATH_KEY = "GossipPath";

    /**
     * Klíč k přednostnímu načítání.
     */
    public static final String BEFORE_LOADING_ORDER_KEY = "BeforeLoadingOrder";

    /**
     * Klíč k dodatečnému načítání.
     */
    public static final String AFTER_LOADING_ORDER_KEY = "AfterLoadingOrder";

    /**
     * Jméno robota.
     */
    private final String name;

    /**
     * Cesta k souborům robota.
     */
    private final Path filesLocation;

    /**
     * Cesta k souboru pro zápis promluv.
     */
    private final Path gossipPath;

    /**
     * Pevné predikáty robota.
     */
    private final Map<String, String> predicates;

    /**
     * Pořadí přednostně načítaných souborů.
     */
    private final List<String> beforeLoadingOrder;

    /**
     * Pořadí dodatečně načítaných souborů.
     */
    private final List<String> afterLoadingOrder;

    /**
     * Načte konfiguraci robota z {@link Properties}.
     * 
     * @param botSettings
     *            nastavení robota
     * @param botPredicates
     *            predikáty robota
     * @return načtená konfigurace
     * @throws ConfigurationException
     *             chyba ve formátu konfigurace
     */
    public static BotConfiguration create(final Properties botSettings,
            final Properties botPredicates) throws ConfigurationException {
        return create(Property.toMap(botSettings),
                Property.toMap(botPredicates));
    }

    /**
     * Načte konfiguraci robota z {@link Map}.
     * 
     * @param botSettings
     *            nastavení robota
     * @param botPredicates
     *            predikáty robota
     * @return načtená konfigurace
     * @throws ConfigurationException
     *             chyba ve formátu konfigurace
     */
    public static BotConfiguration create(
            final Map<String, String> botSettings,
            final Map<String, String> botPredicates)
            throws ConfigurationException {
        LOGGER.log(Level.INFO, "api.BotSettingsReading",
                new Object[] { botSettings });

        final String name = Configuration.readValue(botSettings, NAME_KEY);
        LOGGER.log(Level.INFO, "api.BotNameRead", new Object[] { name });

        final Path filesLocation =
                Configuration.readPath(botSettings, FILES_KEY);
        LOGGER.log(Level.INFO, "api.BotFilesRead",
                new Object[] { filesLocation });

        final Path gossipPath =
                Configuration.readPath(botSettings, GOSSIP_PATH_KEY);
        LOGGER.log(Level.INFO, "api.BotGossipLocationRead",
                new Object[] { gossipPath });

        final List<String> beforeLoadingOrder =
                Configuration.readLoadingOrder(botSettings,
                        BEFORE_LOADING_ORDER_KEY);
        LOGGER.log(Level.INFO, "api.BotBeforeRead",
                new Object[] { beforeLoadingOrder });

        final List<String> afterLoadingOrder =
                Configuration.readLoadingOrder(botSettings,
                        AFTER_LOADING_ORDER_KEY);
        LOGGER.log(Level.INFO, "api.BotAfterRead",
                new Object[] { afterLoadingOrder });

        final Map<String, String> validBotPredicates =
                Configuration.readValidEntries(botPredicates);
        LOGGER.log(Level.INFO, "api.BotPredicatesRead",
                new Object[] { validBotPredicates });

        return new AIMLBotConfiguration(name, filesLocation, gossipPath,
                validBotPredicates, beforeLoadingOrder, afterLoadingOrder);
    }

    /**
     * Vytvoří konfiguraci.
     * 
     * @param name
     *            jméno robota
     * @param filesLocation
     *            umístění souborů
     * @param gossipPath
     *            cesta k souboru s promluvami
     * @param predicates
     *            predikáty
     * @param beforeLoadingOrder
     *            pořadí souborů k přednostnímu načítání
     * @param afterLoadingOrder
     *            pořadí souborů k dodatečnému načítání
     * @return konfigurace
     */
    public static AIMLBotConfiguration of(final String name,
            final Path filesLocation, final Path gossipPath,
            final Map<String, String> predicates,
            final List<String> beforeLoadingOrder,
            final List<String> afterLoadingOrder) {
        if (name == null || filesLocation == null || gossipPath == null
                || predicates == null || beforeLoadingOrder == null
                || afterLoadingOrder == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("api.NullArgument"));
        }

        for (final Entry<String, String> predicate : predicates.entrySet()) {
            final String key = predicate.getKey();
            final String value = predicate.getValue();

            if (key == null || value == null) {
                throw new NullPointerException(
                        MESSAGE_LOCALIZER.getMessage("api.NullPredicateEntry"));
            }

            if (key.isEmpty()) {
                throw new IllegalArgumentException(
                        MESSAGE_LOCALIZER.getMessage("api.EmptyPredicateEntry"));
            }
        }

        for (final String item : beforeLoadingOrder) {
            if (item == null) {
                throw new NullPointerException(
                        MESSAGE_LOCALIZER.getMessage("api.NullOrderingItem"));
            }

            if (item.isEmpty()) {
                throw new IllegalArgumentException(
                        MESSAGE_LOCALIZER.getMessage("api.EmptyOrderingItem"));
            }
        }

        return new AIMLBotConfiguration(name, filesLocation, gossipPath,
                predicates, beforeLoadingOrder, afterLoadingOrder);
    }

    /**
     * Vytvoří konfiguraci.
     * 
     * @param name
     *            jméno robota
     * @param filesLocation
     *            umístění souborů
     * @param gossipPath
     *            cesta k souboru s promluvami
     * @param predicates
     *            predikáty
     * @param beforeLoadingOrder
     *            pořadí souborů k přednostnímu načítání
     * @param afterLoadingOrder
     *            pořadí souborů k dodatečnému načítání
     */
    private AIMLBotConfiguration(final String name, final Path filesLocation,
            final Path gossipPath, final Map<String, String> predicates,
            final List<String> beforeLoadingOrder,
            final List<String> afterLoadingOrder) {
        this.name = name;
        this.filesLocation = filesLocation;
        this.gossipPath = gossipPath;
        this.predicates = new HashMap<>(predicates);
        this.beforeLoadingOrder = new ArrayList<>(beforeLoadingOrder);
        this.afterLoadingOrder = new ArrayList<>(afterLoadingOrder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration#
     * getFilesLocation()
     */
    @Override
    public Path getFilesLocation() {
        return filesLocation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration#
     * getGossipPath ()
     */
    @Override
    public Path getGossipPath() {
        return gossipPath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration#
     * getPredicates ()
     */
    @Override
    public Map<String, String> getPredicates() {
        return Collections.unmodifiableMap(predicates);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration#
     * getBeforeLoadingOrder()
     */
    @Override
    public List<String> getBeforeLoadingOrder() {
        return Collections.unmodifiableList(beforeLoadingOrder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration#
     * getAfterLoadingOrder()
     */
    @Override
    public List<String> getAfterLoadingOrder() {
        return Collections.unmodifiableList(afterLoadingOrder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final int maxLen = 10;
        final StringBuilder builder = new StringBuilder();
        builder.append("AIMLBotConfiguration [name=");
        builder.append(name);
        builder.append(", filesLocation=");
        builder.append(filesLocation);
        builder.append(", gossipPath=");
        builder.append(gossipPath);
        builder.append(", predicates=");
        builder.append(Text.toString(predicates.entrySet(), maxLen));
        builder.append(", beforeLoadingOrder=");
        builder.append(Text.toString(beforeLoadingOrder, maxLen));
        builder.append(", afterLoadingOrder=");
        builder.append(Text.toString(afterLoadingOrder, maxLen));
        builder.append("]");
        return builder.toString();
    }
}
