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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations;

import com.google.common.base.Preconditions;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Direction;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.NodeModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.RealignmentProcessor;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultRealignmentProcessor implements RealignmentProcessor {
    
    private final NodeModifier changeProcessor;

    public static DefaultRealignmentProcessor create(final NodeModifier changeProcessor) {
        return new DefaultRealignmentProcessor(changeProcessor);
    }
    
    private DefaultRealignmentProcessor(final NodeModifier changeProcessor) {
        Preconditions.checkNotNull(changeProcessor);
        
        this.changeProcessor = changeProcessor;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.RealignmentProcessor#realign(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.Node, cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Direction)
     */
    @Override
    public Node realign(final Node node) {
        Preconditions.checkNotNull(node);
        
        final Class<? extends Node> type = determineType(node);
        return this.changeProcessor.change(node, type);
    }
    
    private Class<? extends Node> determineType(final Node node) {
        if (node.getDegree(Direction.OUT) == 0) {
            if (node.getDegree(Direction.IN) == 0) {
                return IsolatedNode.class;
            } else {
                return ExitNode.class;
            }
        } else {
            if (node.getDegree(Direction.IN) == 0) {
                return EnterNode.class;
            } else {
                return InnerNode.class;
            }
        }
    }
}
