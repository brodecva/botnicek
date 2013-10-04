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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage.map;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Jednoduchá mapa která mění způsob své implementace dle počtu uložených prvků.
 * Následná změna implementace je určena současným implementujícím prvkem. Počet
 * sousledných změn implementace při vkládání je na ochranu před zacyklením
 * omezen {@value #MAXIMUM_RESIZE_ATTEMPTS_LIMIT}, poté je vyhozena výjimka
 * {@link UnsupportedOperationException}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * 
 * @param <K>
 *            klíč
 * @param <V>
 *            hodnota příslušící danému klíči
 */
public final class AdaptiveMapper<K, V> implements Mapper<K, V>, Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -5053080114843132229L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AdaptiveMapper.class);

    /**
     * Limit pro počet změn po vložení.
     */
    public static final int MAXIMUM_RESIZE_ATTEMPTS_LIMIT = 8;

    /**
     * Jádro mapperu.
     */
    private MapperCore<K, V> core;

    /**
     * Vytvoří nový mapper s daným jádrem.
     * 
     * @param core
     *            počáteční jádro, nesmí být null
     */
    public AdaptiveMapper(final MapperCore<K, V> core) {
        if (core == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("storage.map.NullCoreNotAccepted"));
        }

        this.core = core;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.Mapper#get(java
     * .lang.Object)
     */
    @Override
    public V get(final K key) {
        return core.get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.Mapper#put(java
     * .lang.Object, java.lang.Object)
     */
    @Override
    public void put(final K key, final V value) {
        boolean resizeNeeded = core.put(key, value);

        int resizeAttempts = 0;

        while (resizeNeeded && resizeAttempts < MAXIMUM_RESIZE_ATTEMPTS_LIMIT) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "storage.map.MapperResizeAttempt", new Object[] { key,
                        value, resizeAttempts + 1 });
            }
            
            resize();
            resizeAttempts++;
            resizeNeeded = core.put(key, value);
        }

        if (resizeNeeded && resizeAttempts >= MAXIMUM_RESIZE_ATTEMPTS_LIMIT) {
            throw new UnsupportedOperationException(
                    MESSAGE_LOCALIZER.getMessage("storage.map.ResizeAttemptsLimitReached"));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.Mapper#
     * getEntries()
     */
    @Override
    public Set<Entry<K, V>> getEntries() {
        return core.getEntries();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.Mapper#resize()
     */
    @Override
    public void resize() {
        core = core.resize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.Mapper#getSize()
     */
    @Override
    public int getSize() {
        return core.getSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.Mapper#getCapacity()
     */
    @Override
    public int getCapacity() {
        return core.getCapacity();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.Mapper#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((core == null) ? 0 : core.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.Mapper#equals
     * (java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        final AdaptiveMapper other = (AdaptiveMapper) obj;
        if (core == null) {
            if (other.core != null) {
                return false;
            }
        } else if (!core.equals(other.core)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AdaptiveMapper [core=" + core + "]";
    }
}
