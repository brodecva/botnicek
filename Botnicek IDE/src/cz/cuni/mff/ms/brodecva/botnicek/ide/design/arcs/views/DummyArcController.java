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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code;

/**
 * @author Václav Brodec
 * @version 1.0
 */
final class DummyArcController implements ArcController {

    public static DummyArcController create() {
        return new DummyArcController();
    }
    
    private DummyArcController() {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#addView(java.lang.Object)
     */
    @Override
    public void addView(ArcView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#removeView(java.lang.Object)
     */
    @Override
    public void removeView(ArcView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#fill(java.lang.Object)
     */
    @Override
    public void fill(ArcView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController#updatePattern(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, int, cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern)
     */
    @Override
    public void updatePattern(NormalWord newName, int priority,
            Code code, MixedPattern pattern, MixedPattern that) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController#updatePattern(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updatePattern(String newName, int priority, String pattern,
            String that, String code) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController#updateCodeTest(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, int, cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern, cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code)
     */
    @Override
    public void updateCodeTest(NormalWord newName, int priority,
            Code code, SimplePattern expectedValue, Code tested) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController#updateCodeTest(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateCodeTest(String newName, int priority,
            String code, String testedCode, String value) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController#updatePredicateTest(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, int, cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern, cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void updatePredicateTest(NormalWord newName, int priority,
            Code code, SimplePattern expectedValue, Code prepareCode,
            NormalWord predicateName) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController#updatePredicateTest(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updatePredicateTest(String newName, int priority,
            String code, String prepareCode, String name, String value) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController#updateRecurent(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, int, cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode)
     */
    @Override
    public void updateRecurent(NormalWord newName, int priority,
            Code code, SimplePattern expectedValue, EnterNode target) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController#updateRecurent(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, java.lang.String, int, java.lang.String, cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode, java.lang.String)
     */
    @Override
    public void updateRecurent(String newName, int priority,
            String code, EnterNode targetName, String value) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController#updateTransition(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, int, cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Code)
     */
    @Override
    public void updateTransition(NormalWord newName, int priority,
            Code code) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController#updateTransition(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, java.lang.String, int, java.lang.String)
     */
    @Override
    public void updateTransition(String newName, int priority,
            String code) {
    }
}
