/**
 * Copyright Václav Brodec 2014.
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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.Loader;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParserFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Splitter;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;

/**
 * Implementace {@link ConversationFactory} vytvářející konverzace s robotem
 * postaveným nad AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AimlConversationFactory implements ConversationFactory {

    /**
     * Vytvoří továrnu.
     * 
     * @return továrna
     */
    public static AimlConversationFactory create() {
        return new AimlConversationFactory();
    }

    private AimlConversationFactory() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.ConversationFactory
     * #produce(cz.cuni.mff.ms.brodecva.botnicek.library.loader.Loader,
     * cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Splitter,
     * cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer,
     * cz.cuni.mff.ms.brodecva.botnicek.library.language.Language,
     * cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParserFactory,
     * java.util.Map, java.util.Map)
     */
    @Override
    public Conversation produce(final Loader loader, final Splitter splitter,
            final Normalizer normalizer, final Language language,
            final TemplateParserFactory parserFactory,
            final Map<? extends String, ? extends String> defaultPredicates,
            final Map<? extends String, ? extends DisplayStrategy> predicatesSetBehavior) {
        Preconditions.checkNotNull(loader);
        Preconditions.checkNotNull(splitter);
        Preconditions.checkNotNull(normalizer);
        Preconditions.checkNotNull(language);
        Preconditions.checkNotNull(parserFactory);
        Preconditions.checkNotNull(defaultPredicates);
        Preconditions.checkNotNull(predicatesSetBehavior);

        return new AIMLConversation(loader, splitter, normalizer, language,
                parserFactory, defaultPredicates, predicatesSetBehavior);
    }

}
