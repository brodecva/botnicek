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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.logging.Level;

import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;

/**
 * <p>
 * Zapíše informace získané botem o subjektech z rozmluvy do souboru. Tato
 * implementace vzhledem k řídkému užívání této funkce neudržuje otevřený soubor
 * pro zapisování pro více instancí.
 * </p>
 * <p>
 * Ve výchozím nastavení zapisuje v kódování UTF-8.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class GossipProcessor extends AbstractProcessor {

    /**
     * Název kódování.
     */
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -519279909352913908L;

    /**
     * Oddělovač řádek.
     */
    public static final String LINE_SEPARATOR = System.getProperty(
            "line.separator", "\n");

    /**
     * Zapisovač.
     */
    private final Writer writer;

    /**
     * Výchozí konstruktor.
     */
    public GossipProcessor() {
        writer = null; // Je zajištěna dodatečná inicializace.
    }

    /**
     * Vytvoří procesor s daným zapisovačem.
     * 
     * @param writer
     *            zapisovač
     */
    public GossipProcessor(final Writer writer) {
        this.writer = writer;
    }

    @Override
    public String process(final Element element, final TemplateParser parser)
            throws ProcessorException {
        final String response = parser.evaluate(element.getChildNodes());

        final Bot bot = parser.getBot();

        try (final Writer writer = getWriter(bot)) {
            writer.append(response + LINE_SEPARATOR);
            writer.flush();

            LOGGER.log(Level.INFO, "processor.GossipWritten", new Object[] { response,
                    bot, parser.getConversation() });
        } catch (final IOException e) {
            throw new ProcessorException(e);
        }

        return EMPTY_RESPONSE;
    }

    /**
     * Vrátí {@link Writer} pro zápis promluvy bota s uživatelem.
     * 
     * @param bot
     *            bot
     * @return {@link Writer} pro zápis pro mluvy
     * @throws IOException
     *             pokud dojde k chybě při získávání
     */
    private Writer getWriter(final Bot bot) throws IOException {
        if (writer == null) {
            final File file = bot.getGossipPath().toFile();
            file.createNewFile();

            final CharsetEncoder encoder =
                    Charset.forName(CHARSET_NAME).newEncoder();
            encoder.onMalformedInput(CodingErrorAction.REPORT);
            encoder.onUnmappableCharacter(CodingErrorAction.REPORT);

            final Writer newWriter =
                    new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(file, true), encoder));

            return newWriter;
        }

        return writer;
    }

}
