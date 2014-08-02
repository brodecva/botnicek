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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.NodeModifier;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RealignmentProcessor;

/**
 * Výchozí implementace procesoru pro opravu typu uzlů dle umístění v grafu po změně.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultRealignmentProcessor implements RealignmentProcessor {
    
    private final NodeModifier changeProcessor;

    /**
     * Vytvoří procesor.
     * 
     * @param modifier modifikátor uzlů
     * @return procesor
     */
    public static DefaultRealignmentProcessor create(final NodeModifier modifier) {
        return new DefaultRealignmentProcessor(modifier);
    }
    
    private DefaultRealignmentProcessor(final NodeModifier changeProcessor) {
        Preconditions.checkNotNull(changeProcessor);
        
        this.changeProcessor = changeProcessor;
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>Zkoumá stupně uzlu. Z toho jasně usoudí, nachází-li se po změně uzel uvnitř či na okrajích sítě.</p>
     */
    @Override
    public Node realign(final Node node) {
        Preconditions.checkNotNull(node);
        
        final Class<? extends Node> type = determineType(node);
        return this.changeProcessor.change(node, type);
    }
    
    private Class<? extends Node> determineType(final Node node) {
        final int inDegree =  node.getInDegree();
        
        if (node.getOutDegree() == 0) {
            if (inDegree == 0) {
                return IsolatedNode.class;
            } else {
                return ExitNode.class;
            }
        } else {
            if (inDegree == 0) {
                return EnterNode.class;
            } else {
                return InnerNode.class;
            }
        }
    }
}
