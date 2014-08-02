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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.api.Visitor;

/**
 * Abstraktní třída pro prvek jež není interpretován a je za něj dosazena pouze jeho textová hodnota.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractRawElement extends AbstractElement {
    
    @Override
    public String getLocalName() {
        return "";
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#visitEnter(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.api.Visitor)
     */
    @Override
    protected void visitEnter(Visitor visitor) {
        super.visitEnter(visitor);
        
        visitor.visitEnter(this);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.AbstractElement#visitExit(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.api.Visitor)
     */
    @Override
    protected void visitExit(Visitor visitor) {
        super.visitExit(visitor);
        
        visitor.visitExit(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AbstractRawElement [getText()=" + getText() + ", getClass()="
                + getClass() + "]";
    }
}
