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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.AvailableReferencesController;

/**
 * Zobrazovač vlastností hran.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface ArcPropertiesDisplayView {
    /**
     * Zobrazí vlastnosti hrany obsluhované danými řadiči.
     * 
     * @param arcController
     *            řadič hrany
     * @param availableReferencesController
     *            řadič pro dostupné reference
     * @param nameValidationController
     *            řadič validace názvů stavů
     * @param codeValidationController
     *            řadič validace zdrojových kódu
     * @param simplePatternValidationController
     *            řadič validace prostých vzorů
     * @param mixedPatternValidationController
     *            řadič validace složených vzorů
     * @param predicateNameValidationController
     *            řadič validace názvů predikátů
     */
            void
            arcDisplayed(
                    ArcController arcController,
                    AvailableReferencesController availableReferencesController,
                    CheckController<? extends NormalWord> nameValidationController,
                    CheckController<? extends Code> codeValidationController,
                    CheckController<? extends SimplePattern> simplePatternValidationController,
                    CheckController<? extends MixedPattern> mixedPatternValidationController,
                    CheckController<? extends NormalWord> predicateNameValidationController);
}
