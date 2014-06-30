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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public interface ArcController extends Controller<ArcView> {
    void updatePattern(NormalWord newName, int priority, Code code, MixedPattern pattern, MixedPattern that);
    void updatePattern(String newName, int priority, String pattern, String that,
            String code);
    
    void updateCodeTest(NormalWord newName, int priority, Code code, SimplePattern expectedValue, Code tested);
    void updateCodeTest(String newName, int priority, String code,
            String testedCode, String value);
    
    void updatePredicateTest(NormalWord newName, int priority, Code code, SimplePattern expectedValue, Code prepareCode, NormalWord predicateName);
    void updatePredicateTest(String newName, int priority, String code,
            String prepareCode, String name, String value);
    
    void updateRecurent(NormalWord newName, int priority, Code code, SimplePattern expectedValue, EnterNode target);
    void updateRecurent(String newName, int priority, String code,
            EnterNode targetName, String value);
    
    void updateTransition(NormalWord newName, int priority, Code code);
    void updateTransition(String newName, int priority, String code);
}
