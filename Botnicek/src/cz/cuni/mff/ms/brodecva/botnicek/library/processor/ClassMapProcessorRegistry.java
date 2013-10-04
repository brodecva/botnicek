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

import java.io.Serializable;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassControlError;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassMap;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.SimpleClassMap;

/**
 * Registr procesorů (implementace) pro {@link Processor} na zpracování XML
 * elementů jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ClassMapProcessorRegistry implements ProcessorRegistry,
        Serializable {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 3022954843234122865L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Logger. Společný pro všechny procesory.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(ClassMapProcessorRegistry.class);

    /**
     * URI jmenného prostoru.
     */
    private final URI namespace;

    /**
     * Zobrazení tagů na implementace procesorů.
     */
    private final ClassMap<String, Processor> processors;

    /**
     * Vytvoří registr procesorů.
     * 
     * @param namespace
     *            použité URI jmenného prostoru
     * @param tagToProcessorsConfig
     *            přiřazení tagů k procesorům
     * @return nová instance {@link ClassMapProcessorRegistry}
     */
    public static ClassMapProcessorRegistry create(final URI namespace,
            final Map<String, String> tagToProcessorsConfig) {
        return create(namespace, tagToProcessorsConfig,
                new SimpleClassMap<String, Processor>());
    }

    /**
     * Vytvoří registr procesorů s danou mapou tříd.
     * 
     * @param namespace
     *            použité URI jmenného prostoru
     * @param tagToProcessorsConfig
     *            přiřazení tagů k procesorům
     * @param classMap
     *            použitá mapa tříd. Její manipulací lze měnit chování registru!
     * @return nová instance {@link ClassMapProcessorRegistry}
     */
    public static ClassMapProcessorRegistry create(final URI namespace,
            final Map<String, String> tagToProcessorsConfig,
            final ClassMap<String, Processor> classMap) {
        for (final Entry<String, String> entry : tagToProcessorsConfig
                .entrySet()) {
            final String key = entry.getKey();
            final String className = entry.getValue();

            try {
                LOGGER.log(Level.INFO, "processor.ProcessorClassRegistration",
                        new Object[] { key, className });
                classMap.put(key, className);
            } catch (final ClassNotFoundException e) {
                throw new ClassControlError(MESSAGE_LOCALIZER.getMessage(
                        "processor.ProcessorNotFound", key, className), e);
            }
        }

        return new ClassMapProcessorRegistry(namespace, classMap);
    }

    /**
     * Vytvoří registr procesorů.
     * 
     * @param namespace
     *            použité URI jmenného prostoru
     * @param processors
     *            použitá mapa tříd
     */
    private ClassMapProcessorRegistry(final URI namespace,
            final ClassMap<String, Processor> processors) {
        if (namespace == null || processors == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("processor.NullArgument"));
        }

        this.namespace = namespace;
        this.processors = processors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorRegistry#
     * getNamespace()
     */
    @Override
    public URI getNamespace() {
        return namespace;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorRegistry#
     * get(java.lang.String, java.net.URI)
     */
    @Override
    public Class<? extends Processor>
            get(final String key, final URI namespace)
                    throws ClassNotFoundException {
        if (namespace != null && !this.namespace.equals(namespace)) {
            throw new ClassNotFoundException(MESSAGE_LOCALIZER.getMessage(
                    "processor.ProcessorNotFound", this, key, namespace));
        }

        final Class<? extends Processor> result = processors.get(key);

        if (result == null) {
            throw new ClassNotFoundException(MESSAGE_LOCALIZER.getMessage(
                    "processor.ProcessorNotFound", this, key, namespace));
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorRegistry#
     * hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
                prime
                        * result
                        + ((namespace == null) ? 0 : namespace
                                .hashCode());
        result =
                prime * result
                        + ((processors == null) ? 0 : processors.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorRegistry#
     * equals(java.lang.Object)
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
        final ClassMapProcessorRegistry other = (ClassMapProcessorRegistry) obj;
        if (getNamespace() == null) {
            if (other.getNamespace() != null) {
                return false;
            }
        } else if (!getNamespace().equals(other.getNamespace())) {
            return false;
        }
        if (processors == null) {
            if (other.processors != null) {
                return false;
            }
        } else if (!processors.equals(other.processors)) {
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
        return "ProcessorRegistry [namespace=" + getNamespace()
                + ", processors=" + processors + "]";
    }

}
