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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;

/**
 * Pomocná třída pro načítání {@link Properties}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class Property {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(Property.class);
    
    /**
     * Načte {@link Properties} z umístění.
     * 
     * @param path
     *            cesta k souboru
     * @return načtené {@link Properties}
     * @throws IOException
     *             I/O chyba
     */
    public static Properties load(final Path path) throws IOException {
        LOGGER.log(Level.INFO, "utils.PropertyPathLoad", path);
        
        try (final InputStream propertiesStream =
                new BufferedInputStream(new FileInputStream(path.toString()))) {
            return load(propertiesStream);
        } catch (final IllegalArgumentException e) {
            throw new IOException(e);
        }
    }
    
    /**
     * Načte {@link Properties} ze vstupního proudu.
     * 
     * @param inputStream
     *            vstupní proud
     * @return načtené {@link Properties}
     * @throws IOException
     *             I/O chyba
     * @see Properties#load(InputStream)
     */
    public static Properties load(final InputStream inputStream) throws IOException {
        LOGGER.log(Level.INFO, "utils.PropertyStreamLoad");
        
        final Properties result = new Properties();

        result.load(inputStream);

        return result;
    }
    
    /**
     * Načte {@link Properties} ze zdroje.
     * 
     * @param klass třída určující výchozí balík
     * @param resourceName název zdroje
     * @return načtené {@link Properties}
     * @throws IOException
     *             I/O chyba
     * @see Properties#load(InputStream)
     * @see Class#getResourceAsStream(String)
     */
    public static Properties load(final Class<?> klass, final String resourceName) throws IOException {
        LOGGER.log(Level.INFO, "utils.PropertyResourceLoad");
        
        return load(klass.getResourceAsStream(resourceName));
    }

    /**
     * Převede instanci {@link Properties} na {@link Map} položek typu
     * {@link String}. Vyžaduje, aby původní mapa neobsahovala objekty jiného typu.
     * 
     * @param properties
     *            instance {@link Properties}
     * @return mapa s položkami z instance
     */
    public static Map<String, String> toMap(final Properties properties) {
        final Map<String, String> result = new HashMap<String, String>();

        for (final Entry<Object, Object> entry : properties.entrySet()) {
            result.put((String) entry.getKey(), (String) entry.getValue());
        }

        return result;
    }
    
    /**
     * Kontroluje položky na přítomnost {@code null} v klíči či hodnotě.
     * 
     * @param map
     *            instance {@link Map}
     * @return true, pokud jak klíče, tak hodnoty mapy nejsou ani jeden
     *         {@code null}, jinak false
     */
    public static boolean checkEntriesNotNull(
            final Map<String, String> map) {
        for (final Entry<String, String> entry : map.entrySet()) {
            final String key = entry.getKey();
            if (key == null) {
                return false;
            }

            final String value = entry.getValue();
            if (value == null) {
                return false;
            }
        }

        return true;
    }
    
    /**
     * Skrytý konstruktor.
     */
    private Property() {
    }
}
