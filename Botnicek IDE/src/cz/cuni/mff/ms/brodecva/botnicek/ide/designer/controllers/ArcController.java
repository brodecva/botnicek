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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers;

import java.util.Map.Entry;
import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.PredicateName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.AbstractArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public interface ArcController extends Controller<ArcView> {
    void updatePattern(String arcName, String newName, int priority, Code code, MixedPattern pattern, MixedPattern that);
    void updatePattern(String arcName, String newName, int priority, String pattern,
            String that, String code);
    
    void updateCodeTest(String arcName, String newName, int priority, Code code, SimplePattern expectedValue, Code tested);
    void updateCodeTest(String arcName, String newName, int priority,
            String code, String testedCode, String value);
    
    void updatePredicateTest(String arcName, String newName, int priority, Code code, SimplePattern expectedValue, Code prepareCode, PredicateName predicateName);
    void updatePredicateTest(String arcName, String newName, int priority,
            String code, String prepareCode, String name, String value);
    
    void updateRecurent(String arcName, String newName, int priority, Code code, SimplePattern expectedValue, String targetName);
    void updateRecurent(String arcName, String newName, int priority,
            String code, String targetName, String value);
    
    void updateTransition(String arcName, String newName, int priority, Code code);
    void updateTransition(String arcName, String newName, int priority, String code);
    
    void changeType(AbstractArcInternalFrame old, Class<? extends AbstractArcInternalFrame> newType);
    
    Set<String> getAvailableTargets();
}
