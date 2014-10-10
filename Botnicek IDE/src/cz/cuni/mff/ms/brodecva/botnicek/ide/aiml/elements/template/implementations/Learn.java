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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations;

import java.net.URI;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.CovertElement;

/**
 * Při vyhodnocení interpret načte specifikovaný zdroj a v něm obsažené objekty
 * jazyka AIML. Ty se pak účastní vyhodnocování jako kdyby byly načteny při
 * startu interpretu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-learn">http://www.alicebot.org/TR/2011/#section-learn</a>
 */
public final class Learn extends AbstractProperElement implements CovertElement {
    private static final String NAME = "learn";

    /**
     * Vytvoří prvek.
     * 
     * @param sourceUri
     *            URI zdroje pro načtení
     * @return prvek
     */
    public static Learn create(final URI sourceUri) {
        return new Learn(sourceUri);
    }

    private final URI sourceUri;

    private Learn(final URI sourceUri) {
        super();

        Preconditions.checkNotNull(sourceUri);

        this.sourceUri = sourceUri;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.
     * AbstractElement#getName()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }

    /**
     * Vrátí URI zdroje pro načtení nového obsahu.
     * 
     * @return URI zdroje pro načtení nového obsahu
     */
    public URI getSourceUri() {
        return this.sourceUri;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement#getText
     * ()
     */
    @Override
    public String getText() {
        return this.sourceUri.toString();
    }
}
