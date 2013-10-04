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

import java.util.List;

import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.IndexFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer;

/**
 * Poskytuje prostředky pro zpracování prvků využívajících reference na řetězce
 * zachycené žolíky.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class StarReferenceProcessor extends AbstractProcessor {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -6292833878395072717L;

    /**
     * Továrna na indexy.
     */
    private final IndexFactory indexFactory;

    /**
     * Normalizér.
     */
    private Normalizer normalizer;

    /**
     * Výchozí konstruktor.
     */
    protected StarReferenceProcessor() {
        indexFactory = new DefaultIndexFactory();
    }

    /**
     * Vytvoří procesor s dodanými závislostmi.
     * 
     * @param indexFactory
     *            továrna na indexy
     * @param normalizer
     *            normalizér pro denormalizaci
     */
    protected StarReferenceProcessor(final IndexFactory indexFactory,
            final Normalizer normalizer) {
        this.indexFactory = indexFactory;
        this.normalizer = normalizer;
    }

    /**
     * Vybere dle jednorozměrného indexu příslušnou zachycenou část a předá na
     * výstup.
     * 
     * @param element
     *            prvek ke zpracování
     * @param parser
     *            parser
     * @param references
     *            reference na zachycenou část
     * @return výstup zpracování, prázdný v případě chybného indexu
     * @throws ProcessorException
     *             chyba při zpracování
     */
    protected final String process(final Element element,
            final TemplateParser parser, final List<String> references)
            throws ProcessorException {
        if (references.isEmpty()) {
            return EMPTY_RESPONSE;
        }

        final Index index = indexFactory.createIndex(element);

        final int zeroBasedIndex = index.getValue() - 1;

        if (zeroBasedIndex >= references.size()) {
            throw new ProcessorException(MESSAGE_LOCALIZER.getMessage(
                    "processor.ReferenceOutOfBounds", new Object[] { element, index,
                            references, parser }));
        }

        if (normalizer == null) {
            normalizer = new SimpleNormalizer();
        }

        return normalizer.deconvertFromNormalChars(references
                .get(zeroBasedIndex));
    }
}
