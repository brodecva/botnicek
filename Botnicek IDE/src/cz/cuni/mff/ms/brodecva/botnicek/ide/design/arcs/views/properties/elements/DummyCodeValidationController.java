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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.elements;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.controllers.CodeValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views.CheckView;

/**
 * Atrapa řadiče validace kódu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
class DummyCodeValidationController implements CodeValidationController {

    
    /**
     * Vytvoří atrapu.
     * 
     * @return atrapa
     */
    public static CodeValidationController create() {
        return new DummyCodeValidationController();
    }
    
    private DummyCodeValidationController() {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController#check(java.lang.Object, java.lang.String)
     */
    @Override
    public void check(Source client, Object subject, String value) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#addView(java.lang.Object)
     */
    @Override
    public void addView(CheckView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#removeView(java.lang.Object)
     */
    @Override
    public void removeView(CheckView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller#fill(java.lang.Object)
     */
    @Override
    public void fill(CheckView view) {
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController#clear(java.lang.Object)
     */
    @Override
    public void clear(Object subject) {
    }
}
