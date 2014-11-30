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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.views;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;

/**
 * Pohled na výsledky kontroly.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface CheckView {
    /**
     * Zpraví o ukončení systému pro validaci.
     */
    void closed();

    /**
     * Aktualizuje zobrazení výsledku stejného předmětu.
     * 
     * @param result
     *            nový výsledek
     */
    void updateResult(CheckResult result);
    
    /**
     * <p>
     * Upozorní pohled na to, že došlo od poslední validace ke změně podmínek.
     * </p>
     * </p>
     * Toto obvykle vyžaduje opětovnou validaci zdrojového obsahu a aktualizaci pohledu.
     * </p>
     */
    void repeal();
}
