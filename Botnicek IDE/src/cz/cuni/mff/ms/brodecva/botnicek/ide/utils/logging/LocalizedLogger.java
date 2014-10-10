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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.logging;

import java.util.logging.Logger;

/**
 * Pomocná třída pro lokalizované logování.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class LocalizedLogger {

    private static final String ROOT_PACKAGE =
            "cz.cuni.mff.ms.brodecva.botnicek.ide";

    /**
     * Lokální umístění překladu logů.
     */
    public static final String BUNDLE_PACKAGE_NAME = ".logging";

    private static final String DEFAULT_LOCALE_NAME = ROOT_PACKAGE
            + BUNDLE_PACKAGE_NAME;

    /**
     * Vrací lokalizovaný logger.
     * 
     * @param loggedClass
     *            logovaná třída
     * @return lokalizovaný logger
     */
    public static Logger getLogger(final Class<?> loggedClass) {
        final Logger provided =
                Logger.getLogger(loggedClass.getName(), DEFAULT_LOCALE_NAME);
        return provided;
    }

    /**
     * Vrací lokalizovaný logger.
     * 
     * @param loggedPackage
     *            název logovaného balíku
     * @return lokalizovaný logger
     */
    public static Logger getLogger(final Package loggedPackage) {
        final Logger provided =
                Logger.getLogger(loggedPackage.getName(), DEFAULT_LOCALE_NAME);
        return provided;
    }

    private LocalizedLogger() {
    }
}
