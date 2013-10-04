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
package cz.cuni.mff.ms.brodecva.botnicek.library.processor;

import java.nio.file.Path;
import java.util.logging.Level;

import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.loader.LoaderException;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;

/**
 * Načte a zpracuje dodatečný zdrojový soubor.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class LearnProcessor extends AbstractProcessor {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -4433396958364492485L;

    @Override
    public String process(final Element element, final TemplateParser parser)
            throws ProcessorException {
        final Bot bot = parser.getBot();

        final Path defaultPath = bot.getFilesPath();

        final Path path =
                defaultPath.resolve(parser.evaluate(element.getChildNodes()));

        final Conversation conversation = parser.getConversation();

        try {
            LOGGER.log(Level.INFO, "processor.LearnAttempt", new Object[] { path, bot,
                    conversation });

            conversation.learn(path);

            LOGGER.log(Level.INFO, "processor.LearnFinished", new Object[] { path, bot,
                    conversation });
        } catch (final LoaderException e) {
            throw new ProcessorException(e);
        }

        return EMPTY_RESPONSE;
    }

}
