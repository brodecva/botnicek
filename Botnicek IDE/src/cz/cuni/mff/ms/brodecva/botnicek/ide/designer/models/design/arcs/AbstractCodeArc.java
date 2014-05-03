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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.RawContentElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.implementations.RawContent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public abstract class AbstractCodeArc extends AbstractArc implements CodeArc {

    private final Code code;

    /**
     * @param from
     * @param to
     */
    protected AbstractCodeArc(final Network parent, final String name, final int priority, final Code code) {
        super(parent, name, priority);
        
        Preconditions.checkNotNull(code);
        
        this.code = code;
    }
    
    @Override
    public final Code getCode() {
        return this.code;
    }
}
