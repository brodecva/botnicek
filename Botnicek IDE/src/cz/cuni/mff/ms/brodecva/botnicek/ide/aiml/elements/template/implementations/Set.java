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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractCompoundElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.CaptureElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.AttributeImplementation;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;

/**
 * <p>Nastaví predikát daného názvu hodnotu určenou výstupem z vyhodnocení potomků. Samotný výstup toho predikátu je právě nastavená hodnota, za předpokladu, že není pro daný predikát specifikováno v konfiguraci jinak.</p>
 * <p>Interpretu by totiž mělo být možné zadat, u kterých predikátů se po nastavení bude vracet nastavená hodnota, a u kterých místo ní název predikátu.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a href="http://www.alicebot.org/TR/2011/#section-set">http://www.alicebot.org/TR/2011/#section-set</a>
 */
public final class Set extends AbstractCompoundElement implements CaptureElement {
    private static final String NAME = "set";
    
    private static final String ATT_NAME = "name";
    
    private final NormalWord name;
    
    /**
     * Vytvoří prvek.
     * 
     * @param name název nastavovaného predikátu
     * @param content potomci (jejichž výstup bude uložen do predikátu)
     * @return prvek
     */
    public static Set create(final NormalWord name, final TemplateElement... content) {
        Preconditions.checkNotNull(content);
        
        return create(name, ImmutableList.copyOf(content));
    }
    
    /**
     * Vytvoří prvek.
     * 
     * @param name název nastavovaného predikátu
     * @param content potomci (jejichž výstup bude uložen do predikátu)
     * @return prvek
     */
    public static Set create(final NormalWord name, final List<TemplateElement> content) {
        return new Set(name, content);
    }
    
    private Set(final NormalWord name, final List<TemplateElement> content) {
        super(content);
        
        Preconditions.checkNotNull(name);
        
        this.name = name;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getName()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#getAttributes()
     */
    @Override
    public java.util.Set<Attribute> getAttributes() {
        return ImmutableSet.<Attribute>of(AttributeImplementation.create(ATT_NAME, name.getText()));
    }
}
