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

import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.ConversationException;

/**
 * Provede rekurzivní pokus o vyhodnocení svého obsahu jako nového vstupu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class SRAIProcessor extends AbstractProcessor {
    
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 7951094403821228758L;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.processor.Processor#process(
     * org.w3c.dom.Element,
     * cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser)
     */
    @Override
    public String process(final Element element, final TemplateParser parser)
            throws ProcessorException {
        final String innerResult = parser.evaluate(element.getChildNodes());
        
        final Conversation currentConversation = parser.getConversation();
        
        try {
            return currentConversation.attemptTalk(innerResult);
        } catch (final ConversationException e) {
            throw new ProcessorException(e);
        }
    }
    
}
