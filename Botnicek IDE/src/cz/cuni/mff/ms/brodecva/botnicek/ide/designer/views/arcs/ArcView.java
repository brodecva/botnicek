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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs;

import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CheckResult;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public interface ArcView {
    void updatedName(String name);
    
    void updatedPriority(int priority);
    
    void updatedCode(String code);
    
    void updatedPrepare(String code);
    
    void updatedTested(String code);
    
    void updatedValue(String value);
    
    void updatedPredicate(String name);
    
    void updatedTarget(String targetName);
    
    void updatedPattern(String pattern);
    
    void updatedThat(String that);
    
    void replaced(AbstractArcInternalFrame transformed);
    
    void updatedAvailableReferences(Set<String> references);

    /**
     * @param referenceNames
     */
    void extendedAvailableReferences(Set<String> referenceNames);

    /**
     * @param referenceNames
     */
    void removedAvailableReferences(Set<String> referenceNames);
}
