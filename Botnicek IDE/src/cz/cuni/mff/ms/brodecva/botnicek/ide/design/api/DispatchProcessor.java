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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.api;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.OrderedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RandomNode;

/**
 * Procesor uzlů, který je analyzuje kvůli způsobu přechodu na odchozí hranu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface DispatchProcessor {
    /**
     * Zpracuje uzel náhodného výběru.
     * 
     * @param node uzel náhodného výběru
     */
    void process(RandomNode node);
    
    /**
     * Zpracuje uzel uspořádaného výběru.
     * 
     * @param node uzel uspořádaného výběru
     */
    void process(OrderedNode node);

    /**
     * Zpracuje výstupní uzel.
     * 
     * @param node výstupní uzel
     */
    void process(ExitNode node);
    
    /**
     * Zpracuje izolovaný uzel.
     * 
     * @param node izolovaný uzel
     */
    void process(IsolatedNode node);
}
