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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.DispatchProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.ProceedProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.StackProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.AbstractNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.OrderedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ProcessingNode;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class EnterOrderedProcessingNode extends AbstractNode implements OrderedNode, ProcessingNode, EnterNode {

    public static EnterOrderedProcessingNode create(final NormalWord name, final Network parent, final int x, final int y) {
        return new EnterOrderedProcessingNode(name, parent, x, y);
    }
    
    public static EnterOrderedProcessingNode create(final Node original) {
        return new EnterOrderedProcessingNode(original);
    }
    
    /**
     * @param name
     */
    protected EnterOrderedProcessingNode(final NormalWord name, final Network parent, final int x, final int y) {
        super(name, parent, x, y);
    }
    
    protected EnterOrderedProcessingNode(final Node original) {
        super(original);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.DispatchProcessible#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.DispatchProcessor)
     */
    @Override
    public void accept(final DispatchProcessor processor) {
        processor.process(this);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.ProceedProcessible#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.ProceedProcessor)
     */
    @Override
    public void accept(ProceedProcessor processor) {
        processor.process(this);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.StackProcessible#accept(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.StackProcessor)
     */
    @Override
    public void accept(StackProcessor processor) {
        processor.process(this);
    }
}
