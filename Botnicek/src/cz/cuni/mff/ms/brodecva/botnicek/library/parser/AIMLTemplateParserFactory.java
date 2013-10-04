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
package cz.cuni.mff.ms.brodecva.botnicek.library.parser;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.ClassMapProcessorRegistry;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorRegistry;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property;

/**
 * Továrna na parsery AIML šablon.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLTemplateParserFactory implements TemplateParserFactory,
        Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -899206538589132662L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    protected static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Umístění procesorů pro tagy.
     */
    private static final String PROCESSORS_TO_TAGS =
            "/cz/cuni/mff/ms/brodecva/botnicek/library/processor/"
                    + "processors.properties";

    /**
     * Registr procesorů šablony jazyka AIML.
     */
    private final ProcessorRegistry processorRegistry;

    /**
     * Vytvoří továrnu na parsery šablony jazyka AIML s výchozími procesory.
     * 
     * @throws IOException
     *             I/O chyba
     */
    public AIMLTemplateParserFactory() throws IOException {
        processorRegistry =
                ClassMapProcessorRegistry.create(URI.create(AIML.NAMESPACE_URI
                        .getValue()), Property.toMap(Property.load(getClass(),
                        PROCESSORS_TO_TAGS)));
    }

    /**
     * Vytvoří továrnu na parsery šablony jazyka AIML s danými procesory.
     * 
     * @param registry
     *            registr procesorů
     */
    public AIMLTemplateParserFactory(final ProcessorRegistry registry) {
        if (registry == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("parser.NullArgument"));
        }

        processorRegistry = registry;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParserFactory
     * #getParser
     * (cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation,
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult, boolean)
     */
    @Override
    public TemplateParser getParser(final Conversation conversation,
            final MatchResult result, final boolean forwardProcessingEnabled) {
        return new AIMLTemplateParser(conversation, result, processorRegistry,
                forwardProcessingEnabled);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + processorRegistry.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AIMLTemplateParserFactory other = (AIMLTemplateParserFactory) obj;
        if (!processorRegistry.equals(other.processorRegistry)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AIMLTemplateParserFactory [processorRegistry="
                + processorRegistry + "]";
    }

}
