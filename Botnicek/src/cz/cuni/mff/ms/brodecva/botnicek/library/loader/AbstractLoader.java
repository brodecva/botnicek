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
package cz.cuni.mff.ms.brodecva.botnicek.library.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Abstraktní verze načítače zdrojových souborů pro konverzaci. Zajišťuje
 * korektní zpracování předané lokace, ať už se jedná o adresář či přímo soubor.
 * Vlastní načítací metodu nespecifikuje.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractLoader implements Loader {

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AbstractLoader.class);

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Konstruktor abstraktní verze parseru zdrojových souborů.
     */
    protected AbstractLoader() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.loader.Loader#loadFromLocation
     * (java.nio.file.Path, java.util.List, java.util.List)
     */
    @Override
    public final void loadFromLocation(final Path location,
            final List<String> beforeLoadingOrder,
            final List<String> afterLoadingOrder) throws LoaderException {
        LOGGER.log(Level.INFO, "loader.LocationLoadAttempt", location);

        final File fileLocation = location.toFile();

        try {
            if (!fileLocation.exists()) {
                throw new FileNotFoundException(MESSAGE_LOCALIZER.getMessage(
                        "loader.FileNotFound", fileLocation));
            }

            if (fileLocation.isDirectory()) {
                LOGGER.log(Level.INFO, "loader.LocationIsDir", fileLocation);
                
                LOGGER.log(Level.INFO, "loader.BeforeLoadingAttempts", beforeLoadingOrder);

                for (final String fileName : beforeLoadingOrder) {
                    loadIndividualFile(location.resolve(fileName));
                }

                LOGGER.log(Level.INFO, "loader.StandardLoadingAttempts", fileLocation);

                final File dir = fileLocation;
                final Set<String> before = new HashSet<String>(beforeLoadingOrder);
                final Set<String> after = new HashSet<String>(afterLoadingOrder);
                for (final File child : dir.listFiles()) {
                    final String childName = child.getName();
                    
                    if (".".equals(childName)
                            || "..".equals(childName)) {
                        continue;
                    }

                    if (before.contains(childName)
                            || after.contains(childName)) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.log(Level.FINE, "loader.SkippingExtra", childName);
                        }
                        continue;
                    }

                    loadIndividualFile(child.toPath());
                }

                LOGGER.log(Level.INFO, "loader.AfterLoadingAttempts", afterLoadingOrder);

                for (final String fileName : afterLoadingOrder) {
                    loadIndividualFile(location.resolve(fileName));
                }
            } else {
                LOGGER.log(Level.INFO, "loader.LocationIsNotDir", fileLocation);
                
                loadIndividualFile(location);
            }
        } catch (final FileNotFoundException e) {
            throw new LoaderException(e);
        }
    }
}
