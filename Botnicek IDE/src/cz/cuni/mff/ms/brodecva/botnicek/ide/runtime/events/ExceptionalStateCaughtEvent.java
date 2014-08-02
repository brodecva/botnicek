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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events;

import com.google.common.base.Preconditions;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.Run;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.ExceptionalState;

/**
 * Událost zachycení výjimečného stavu je generována během vyhodnocování interpretem v případě, že se dostane do neplatného stavu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class ExceptionalStateCaughtEvent extends AbstractMappedEvent<Run, ExceptionalStateCaughtListener> {
    
    private final ExceptionalState exceptionalState;
    
    /**
     * Vytvoří událost.
     * 
     * @param run konverzace, v níž došlo k výjimečnému stavu
     * @param exceptionalState popis výjimečného stavu
     * @return událost
     */
    public static ExceptionalStateCaughtEvent create(final Run run, final ExceptionalState exceptionalState) {
        return new ExceptionalStateCaughtEvent(run, exceptionalState);
    }
    
    private ExceptionalStateCaughtEvent(final Run run, final ExceptionalState exceptionalState) {
        super(run);
        
        Preconditions.checkNotNull(exceptionalState);
        
        this.exceptionalState = exceptionalState;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang.Object)
     */
    @Override
    public void dispatchTo(final ExceptionalStateCaughtListener listener) {
        listener.caught(this.exceptionalState);
    }
}
 