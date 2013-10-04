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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * <p>
 * Stromová struktura uchovávající v komprimované podobě normalizovaná vstupní
 * data (čtou se směrem od kořene do listů) a reakce na ně (šablony uložené v
 * koncových uzlech).
 * <p>
 * 
 * <p>
 * Tato implementace není bezpečná pro vícevláknové využití.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class WordTree implements MatchingStructure, Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 2709310057638062838L;

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(WordTree.class);

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Kořenový uzel stromu.
     */
    private final WordNode root;

    /**
     * Počet kategorií.
     */
    private int categoryCount;

    /**
     * Továrna na uzly stromu.
     */
    private final MapperFactory mapperFactory;

    /**
     * Indikuje přítomnost prvků vyžadujících dopředné zpracování.
     */
    private boolean forwardCompatible;

    /**
     * Konstruktor stromové struktury.
     * 
     * @param mapperFactory
     *            továrna na větve uzlů stromu
     */
    public WordTree(final MapperFactory mapperFactory) {
        if (mapperFactory == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("storage.NullFactory"));
        }

        this.mapperFactory = mapperFactory;
        root = new WordNode(0, null, AIMLPartMarker.PATTERN, mapperFactory);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure#find
     * (cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath)
     */
    @Override
    public MatchResult find(final InputPath path) {
        LOGGER.log(Level.INFO, "storage.FindingMatchForPath", path);

        final MatchResult match =
                root.find(path, AIMLPartMarker.PATTERN);

        return match;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure#add
     * (cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath,
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.Template)
     */
    @Override
    public void add(final InputPath path, final Template answer) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "storage.InsertingPathAndTemplate", new Object[] { path,
                    answer });
        }

        root.add(path, answer, AIMLPartMarker.PATTERN,
                mapperFactory);
        categoryCount++;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure#
     * getCategoryCount()
     */
    @Override
    public int getCategoryCount() {
        return categoryCount;
    }

    /**
     * Vrátí používanou továrnu na větve.
     * 
     * @return používaná továrna na větve
     */
    public MapperFactory getMapperFactory() {
        return mapperFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure#
     * isForwardCompatible()
     */
    @Override
    public boolean isForwardCompatible() {
        return forwardCompatible;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure#
     * setForwardCompatible(boolean)
     */
    @Override
    public void setForwardCompatible(final boolean forwardCompatible) {
        this.forwardCompatible = forwardCompatible;
    }
}
