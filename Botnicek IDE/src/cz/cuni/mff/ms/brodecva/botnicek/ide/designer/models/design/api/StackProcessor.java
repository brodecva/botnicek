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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api;

import java.util.List;
import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.OrderedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.RandomNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations.InnerOrderedInputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations.InnerOrderedProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations.InnerRandomInputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations.InnerRandomProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations.IsolatedProcessingNode;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public interface StackProcessor {
    /**
     * @param deterministicInputNode
     */
    void process(InnerNode node);

    /**
     * @param node
     */
    void process(ExitNode node);

    /**
     * @param node
     */
    void process(EnterNode node);
    
    void process(IsolatedNode node);
}
