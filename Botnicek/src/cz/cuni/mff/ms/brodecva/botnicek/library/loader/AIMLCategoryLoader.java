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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Načítač kategorií z AIML dokumentu do uložiště.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLCategoryLoader extends AbstractLoader implements
        Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -5444092124973952458L;

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLCategoryLoader.class);

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Plněná struktura.
     */
    private final MatchingStructure filledStructure;

    /**
     * Kontext bota při načítání.
     */
    private final Bot bot;

    /**
     * Parser.
     */
    private final SourceParser parser;

    /**
     * Vytvoří parser AIML souborů pro naplnění dané rozhodovací struktury.
     * 
     * @param bot
     *            bot ovlivňující načítání
     * @param filledStructure
     *            rozhodovací struktura
     * @param parser
     *            parser zdrojového kódu
     */
    public AIMLCategoryLoader(final MatchingStructure filledStructure,
            final Bot bot, final SourceParser parser) {
        LOGGER.log(Level.INFO, "loader.AIMLParserInitialization", parser);

        if (filledStructure == null || bot == null || parser == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("loader.NullArgument"));
        }

        this.parser = parser;
        this.filledStructure = filledStructure;
        this.bot = bot;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.loader.Loader#getFilledStructure
     * ()
     */
    @Override
    public MatchingStructure getFilledStructure() {
        return filledStructure;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.loader.Loader#getOwner()
     */
    @Override
    public Bot getBot() {
        return bot;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.loader.Loader#loadIndividualFile
     * (java.nio.file.Path)
     */
    @Override
    public void loadIndividualFile(final Path path) throws LoaderException {
        LOGGER.log(Level.INFO, "loader.FileLoadAttempt", path);

        final String fileName = path.getFileName().toString();

        if (!fileName.endsWith("." + AIML.FILE_SUFFIX.getValue())) {
            LOGGER.log(Level.INFO, "loader.FileLoadSkipped", path);
            return;
        }

        try (final InputStream inputStream = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            loadFromStream(inputStream, path.toUri()
                    .toString());
        } catch (final IOException e) {
            throw new LoaderException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.loader.AbstractLoader#
     * loadFromStream (java.io.InputStream, java.lang.String)
     */
    @Override
    public void loadFromStream(final InputStream inputStream,
            final String systemId) throws LoaderException {
        LOGGER.log(Level.INFO, "loader.StreamLoadAttempt", systemId);
        
        try {
            parser.parse(inputStream, systemId, filledStructure, bot);
        } catch (final SourceParserException e) {
            throw new LoaderException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.loader.Loader#load()
     */
    @Override
    public void load() throws LoaderException {
        LOGGER.log(Level.INFO, "loader.DefaultLoadAttempt", bot);
        
        loadFromLocation(bot.getFilesPath(), bot.getBeforeOrder(),
                bot.getAfterOrder());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AIMLCategoryLoader [matchingStructure=" + filledStructure
                + ", bot=" + bot + ", parser=" + parser + "]";
    }
}
