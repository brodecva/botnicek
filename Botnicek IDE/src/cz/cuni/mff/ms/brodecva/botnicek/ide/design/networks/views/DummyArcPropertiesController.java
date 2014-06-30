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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcPropertiesView;


/**
 * @author Václav Brodec
 * @version 1.0
 */
final class DummyArcPropertiesController implements
        ArcPropertiesController {

    public static DummyArcPropertiesController create() {
        return new DummyArcPropertiesController();
    }
    
    private DummyArcPropertiesController() {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#addView(java.lang.Object)
     */
    @Override
    public void addView(ArcPropertiesView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#removeView(java.lang.Object)
     */
    @Override
    public void removeView(ArcPropertiesView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#fill(java.lang.Object)
     */
    @Override
    public void fill(ArcPropertiesView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesController#displayProperties(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)
     */
    @Override
    public void displayProperties(NormalWord name) {
    }

}
