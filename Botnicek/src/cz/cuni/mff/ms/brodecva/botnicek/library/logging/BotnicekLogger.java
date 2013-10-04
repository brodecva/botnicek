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
package cz.cuni.mff.ms.brodecva.botnicek.library.logging;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Pomocná třída pro pohodlné lokalizované logování.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class BotnicekLogger implements Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -949777375940937590L;

    /**
     * Lokální umístění překladu logů.
     */
    public static final String BUNDLE_PACKAGE_NAME = ".logging";

    /**
     * Výchozí umístění překladu výjimek.
     */
    private static final String DEFAULT_LOCALE_NAME =
            "cz.cuni.mff.ms.brodecva.botnicek.library" + BUNDLE_PACKAGE_NAME;

    /**
     * Vrací logger pro knihovnu s výchozím umístěním překladu.
     * 
     * @param loggedClass
     *            logovaná třída
     * @return lokalizovaný logger (pomocí properties souboru uloženém v
     *         defaultním podbalíku)
     */
    public static Logger getLogger(final Class<?> loggedClass) {
        return getLogger(loggedClass, false);
    }

    /**
     * Vrací logger pro knihovnu s výchozím umístěním překladu.
     * 
     * @param loggedPackage
     *            název logovaného balíku
     * @return lokalizovaný logger (pomocí properties souboru uloženém v
     *         defaultním podbalíku)
     */
    public static Logger getLogger(final Package loggedPackage) {
        return getLogger(loggedPackage, false);
    }

    /**
     * Vrací logger pro knihovnu.
     * 
     * @param loggedClass
     *            logovaná třída
     * @param local
     *            true pro umístění zdroje překladu přímo v balíku, false pro
     *            výchozí
     * @return lokalizovaný logger (pomocí properties souboru uloženém v
     *         defaultním podbalíku)
     */
    public static Logger getLogger(final Class<?> loggedClass,
            final boolean local) {
        final String bundleName;
        if (local) {
            bundleName =
                    loggedClass.getPackage().getName() + BUNDLE_PACKAGE_NAME;
        } else {
            bundleName = DEFAULT_LOCALE_NAME;
        }

        final Logger provided =
                Logger.getLogger(loggedClass.getName(), bundleName);
        return provided;
    }

    /**
     * Vrací logger pro knihovnu.
     * 
     * @param loggedPackage
     *            název logovaného balíku
     * @param local
     *            true pro umístění zdroje překladu přímo v balíku, false pro
     *            výchozí
     * @return lokalizovaný logger (pomocí properties souboru uloženém v
     *         defaultním podbalíku)
     */
    public static Logger getLogger(final Package loggedPackage,
            final boolean local) {
        final String bundleName;
        if (local) {
            bundleName = loggedPackage.getName() + BUNDLE_PACKAGE_NAME;
        } else {
            bundleName = DEFAULT_LOCALE_NAME;
        }

        final Logger provided =
                Logger.getLogger(loggedPackage.getName(), bundleName);
        return provided;
    }

    /**
     * Skrytý konstruktor.
     */
    private BotnicekLogger() {
    }
}
