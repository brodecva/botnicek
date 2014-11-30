/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.components.UnobscuredInternalFrame;

/**
 * Vnitřní rám, na který je možno odkazovat jako na zdroj dat pro validaci.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class ReferrableInternalFrame extends UnobscuredInternalFrame implements
        Source {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří rám.
     * 
     * @return rám
     */
    public static ReferrableInternalFrame create() {
        return new ReferrableInternalFrame();
    }

    private ReferrableInternalFrame() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getTitle();
    }

}
