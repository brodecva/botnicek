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

import java.util.List;

import com.google.common.base.Preconditions;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractCompoundElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.ExternalProcessorElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;

/**
 * Prvek systém dle podpory interpreta předzpracuje výstup potomků (kvůli dosazení proměnných), a poté jej pošle externímu interpretu JavaScriptu na daném stroji.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a href="http://www.alicebot.org/TR/2011/#section-javascript">http://www.alicebot.org/TR/2011/#section-javascript</a>
 */
public final class Javascript extends AbstractCompoundElement implements ExternalProcessorElement {
    private static final String NAME = "javascript";
    
    /**
     * Vytvoří prvek.
     * 
     * @param content potomci
     * @return prvek
     */
    public static Javascript create(final TemplateElement... content) {
        Preconditions.checkNotNull(content);
        
        return new Javascript(content);
    }
    
    /**
     * Vytvoří prvek.
     * 
     * @param content potomci
     * @return prvek
     */
    public static Javascript create(final List<TemplateElement> content) {
        return new Javascript(content);
    }
    
    private Javascript(final TemplateElement... content) {
        super(content);
    }
    
    private Javascript(final List<TemplateElement> content) {
        super(content);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getName()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }
}
