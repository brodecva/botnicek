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

import java.util.HashSet;
import java.util.Set;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.PredicateName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.AbstractArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.ArcView;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DummyArcController implements ArcController {

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#addView(java
     * .lang.Object)
     */
    @Override
    public void addView(ArcView view) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#removeView(
     * java.lang.Object)
     */
    @Override
    public void removeView(ArcView view) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController
     * #updatePattern(java.lang.String, java.lang.String, int,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code,
     * cz
     * .cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern
     * ,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern
     * )
     */
    @Override
    public void updatePattern(String arcName, String newName, int priority,
            Code code, MixedPattern pattern, MixedPattern that) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController
     * #updatePattern(java.lang.String, java.lang.String, int, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void updatePattern(String arcName, String newName, int priority,
            String pattern, String that, String code) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController
     * #updateCodeTest(java.lang.String, java.lang.String, int,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code,
     * cz
     * .cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern
     * , cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code)
     */
    @Override
    public void updateCodeTest(String arcName, String newName, int priority,
            Code code, SimplePattern expectedValue, Code tested) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController
     * #updateCodeTest(java.lang.String, java.lang.String, int,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateCodeTest(String arcName, String newName, int priority,
            String code, String testedCode, String value) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController
     * #updatePredicateTest(java.lang.String, java.lang.String, int,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code,
     * cz
     * .cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern
     * , cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code,
     * cz
     * .cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.PredicateName
     * )
     */
    @Override
    public void updatePredicateTest(String arcName, String newName,
            int priority, Code code, SimplePattern expectedValue,
            Code prepareCode, PredicateName predicateName) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController
     * #updatePredicateTest(java.lang.String, java.lang.String, int,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updatePredicateTest(String arcName, String newName,
            int priority, String code, String prepareCode, String name,
            String value) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController
     * #updateRecurent(java.lang.String, java.lang.String, int,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code,
     * cz
     * .cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.SimplePattern
     * , java.lang.String)
     */
    @Override
    public void updateRecurent(String arcName, String newName, int priority,
            Code code, SimplePattern expectedValue, String targetName) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController
     * #updateRecurent(java.lang.String, java.lang.String, int,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateRecurent(String arcName, String newName, int priority,
            String code, String targetName, String value) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController
     * #updateTransition(java.lang.String, java.lang.String, int,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types.Code)
     */
    @Override
    public void updateTransition(String arcName, String newName, int priority,
            Code code) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController
     * #updateTransition(java.lang.String, java.lang.String, int,
     * java.lang.String)
     */
    @Override
    public void updateTransition(String arcName, String newName, int priority,
            String code) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController
     * #changeType(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.
     * AbstractArcInternalFrame, java.lang.Class)
     */
    @Override
    public void changeType(AbstractArcInternalFrame old,
            Class<? extends AbstractArcInternalFrame> newType) {

    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcController#getAvailableTargets()
     */
    @Override
    public Set<String> getAvailableTargets() {
        return new HashSet<>();
    }
}
