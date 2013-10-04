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

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cz.cuni.mff.ms.brodecva.botnicek.library.api.ConfigurationException;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassMap;

/**
 * Prostředky pro snadnější načítání konfigurace.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class Configuration {

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Načte textovou hodnotu.
     * 
     * @param properties
     *            mapa nastavení
     * @param key
     *            klíč položky
     * @return hodnota položky
     * @throws ConfigurationException
     *             chyba při načítání hodnoty
     */
    public static String readValue(final Map<String, String> properties,
            final String key) throws ConfigurationException {
        final String value = properties.get(key);

        if (value == null) {
            throw new ConfigurationException(MESSAGE_LOCALIZER.getMessage(
                    "utils.ItemMissing", properties, key));
        }

        return value;
    }

    /**
     * Načte cestu.
     * 
     * @param properties
     *            nastavení
     * @param key
     *            klíč cesty
     * @return načtená cesta
     * @throws ConfigurationException
     *             chyba při načítání cesty
     */
    public static Path readPath(final Map<String, String> properties,
            final String key) throws ConfigurationException {
        final String pathString = readValue(properties, key);

        final Path path;
        try {
            path = Paths.get(pathString);
        } catch (final InvalidPathException e) {
            throw new ConfigurationException(MESSAGE_LOCALIZER.getMessage(
                    "utils.InvalidPathFormat", properties, key), e);
        }

        return path;
    }

    /**
     * Načte pořadí načítaných souborů. Očekává pouze názvy souborů oddělené
     * pomocí /, nikoli cesty.
     * 
     * @param properties
     *            nastavení
     * @param key
     *            klíč pořadí
     * @return načtené pořadí
     * @throws ConfigurationException
     *             chyba při načítání pořadí
     */
    public static List<String> readLoadingOrder(
            final Map<String, String> properties, final String key)
            throws ConfigurationException {
        final String loadingOrderJoined = readValue(properties, key);

        final String[] loadingOrderFileNames = loadingOrderJoined.split("/");
        
        final List<String> loadingOrder = new ArrayList<String>();
        for (final String fileName : loadingOrderFileNames) {
            if (fileName.isEmpty()) {
                continue;
            }
            
            loadingOrder.add(fileName);
        }
        

        return loadingOrder;
    }

    /**
     * Načte instanci {@link Pattern}.
     * 
     * @param properties
     *            nastavení
     * @param key
     *            klíč vzoru
     * @return načtený vzor
     * @throws ConfigurationException
     *             chyba při načítání vzoru
     */
    public static Pattern readPattern(final Map<String, String> properties,
            final String key) throws ConfigurationException {
        final String patternString = readValue(properties, key);

        Pattern pattern;
        try {
            pattern = Pattern.compile(patternString);
        } catch (final PatternSyntaxException e) {
            throw new ConfigurationException(MESSAGE_LOCALIZER.getMessage(
                    "utils.InvalidPattern", properties, key), e);
        }

        return pattern;
    }

    /**
     * Kontroluje položky, zda nemají klíč či hodnotu {@code null}.
     * 
     * @param properties
     *            nastavení
     * @return vrátí argument
     * @throws ConfigurationException
     *             chyba při načítání položek
     */
    public static Map<String, String> readValidEntries(
            final Map<String, String> properties)
            throws ConfigurationException {
        final boolean entriesNotNull = Property.checkEntriesNotNull(properties);

        if (!entriesNotNull) {
            throw new ConfigurationException(MESSAGE_LOCALIZER.getMessage(
                    "utils.NullProperty", properties));
        }

        return properties;
    }
    
    /**
     * Načte nahrazení.
     * 
     * @param properties
     *            nastavení
     * @return nahrazení
     * @throws ConfigurationException
     *             chyba při načítání nahrazení
     */
    public static Map<Pattern, String> readReplacements(
            final Map<String, String> properties)
            throws ConfigurationException {
        final Map<Pattern, String> result = new HashMap<Pattern, String>();
        
        for (final Entry<String, String> entry : properties.entrySet()) {
            final String key = entry.getKey();
            
            final String value = entry.getValue();
            if (value == null) {
                throw new ConfigurationException(MESSAGE_LOCALIZER.getMessage(
                        "utils.NullProperty", properties));
            }
            
            Pattern replacementPattern;
            try {
                replacementPattern = Pattern.compile(key);
            } catch (final PatternSyntaxException e) {
                throw new ConfigurationException(MESSAGE_LOCALIZER.getMessage(
                        "utils.InvalidReplacementPattern", properties, key), e);
            }
            
            result.put(replacementPattern, value);
        }

        return result;
    }
    
    /**
     * Vrátí přiřazené implementace k názvům predikátů.
     * 
     * @param displayStrategies nastavení zobrazovací strategie
     * @param strategiesMap  implementace
     * @return zobrazení názvů predikátů na implementace
     * @throws ConfigurationException chyba ve formátu konfigurace 
     */
    public static Map<String, DisplayStrategy> readValidStrategies(
            final Map<String, String> displayStrategies, final ClassMap<String, DisplayStrategy> strategiesMap) throws ConfigurationException {
        final Map<String, DisplayStrategy> result = new HashMap<String, DisplayStrategy>();
        
        for (final Entry<String, String> entry : displayStrategies.entrySet()) {
            final String predicateName = entry.getKey();
            if (predicateName == null) {
                throw new ConfigurationException(MESSAGE_LOCALIZER.getMessage(
                        "utils.NullProperty", displayStrategies));
            }

            final String value = entry.getValue();
            
            DisplayStrategy implementation;
            try {
                implementation = strategiesMap.get(value).newInstance();
            } catch (final InstantiationException | IllegalAccessException e) {
                throw new ConfigurationException(MESSAGE_LOCALIZER.getMessage(
                        "utils.UnavailableStrategy", strategiesMap, value, predicateName), e);
            }
            
            result.put(predicateName, implementation);
        }

        return result;
    }

    /**
     * Skrytý konstruktor.
     */
    private Configuration() {
    }

}
