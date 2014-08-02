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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers;

import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.RunView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.Controller;

/**
 * Řadič pro zadávání vstupu do běžící konverzace.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface RunController extends Controller<RunView> {
    /**
     * Předá uživatelský vstup do běžící konverzace.
     * 
     * @param content text promluvy
     */
    void tell(String content);
}
