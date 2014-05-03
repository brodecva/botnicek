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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.checking;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.networks.NetworkInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.code.CodeCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Event;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class PreparedCodeCheckEvent implements Event<PreparedCodeCheckListener> {
    
    private final PredicateTestArc arc;
    private final CodeCheckResult result;
    
    public static PreparedCodeCheckEvent create(final PredicateTestArc arc, final CodeCheckResult result) {
        return new PreparedCodeCheckEvent(arc, result);
    }
    
    private PreparedCodeCheckEvent(final PredicateTestArc arc, final CodeCheckResult result) {
        Preconditions.checkNotNull(arc);
        Preconditions.checkNotNull(result);
        
        this.arc = arc;
        this.result = result;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang.Object)
     */
    @Override
    public void dispatchTo(final PreparedCodeCheckListener listener) {
        listener.checked(this.arc, this.result);
    }

}
 