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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model;

import java.util.Map;
import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.Loader;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParserFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Splitter;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public interface ConversationFactory {
    /**
     * Vytvoří konverzaci. Pro určeného bota načte jeho zdrojové
     * soubory, do kterých dosadí jeho predikáty.
     * 
     * @param loader
     *            načítač zdrojových souborů
     * @param splitter
     *            dělič vět
     * @param normalizer
     *            normalizér
     * @param language
     *            jazyk konverzace
     * @param parserFactory
     *            továrna na parser šablon
     * @param defaultPredicates
     *            výchozí predikáty (názvy a jejich hodnoty)
     * @param predicatesSetBehavior
     *            názvy predikátů zobrazené na strategie pro zobrazení výstupu
     *            při jejich nastavování
     * @return konverzace
     */
    Conversation produce(Loader loader, Splitter splitter,
            Normalizer normalizer, Language language,
            TemplateParserFactory parserFactory,
            Map<String, String> defaultPredicates,
            Map<String, DisplayStrategy> predicatesSetBehavior);
}
