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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.api;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.CodeTestArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.PatternArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.PredicateTestArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.RecurentArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.TransitionArcInternalFrame;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public interface Processor {
    void process(PatternArcInternalFrame frame);
    void process(PredicateTestArcInternalFrame frame);
    void process(RecurentArcInternalFrame frame);
    void process(TransitionArcInternalFrame frame);
    void process(CodeTestArcInternalFrame frame);
}
