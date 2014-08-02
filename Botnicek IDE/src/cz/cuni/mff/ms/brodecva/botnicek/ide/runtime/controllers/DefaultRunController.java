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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.ExceptionalStateCaughtEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.ExceptionalStateCaughtListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.RunsTerminatedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.RunsTerminatedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.SpokenEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.SpokenListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.Run;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.views.RunView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.ExceptionalState;

/**
 * Výchozí implementace řadiče pro zadávání a zobrazení vstupu do konverzace.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultRunController extends AbstractController<RunView> implements
        RunController {

    private final class DefaultSpokenListener implements SpokenListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.SpokenListener#spoken(java.lang.String)
         */
        @Override
        public void spoken(final String author, final String content) {
            Preconditions.checkNotNull(author);
            Preconditions.checkNotNull(content);
            
            callViews(new Callback<RunView>() {

                @Override
                public void call(final RunView view) {
                    view.receive(author, content);
                }

            });
        }
        
    }
    
    private final class DefaultExceptionStateCaughtListener implements ExceptionalStateCaughtListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.ExceptionalStateCaughtListener#caught(cz.cuni.mff.ms.brodecva.botnicek.library.responder.ExceptionalState)
         */
        @Override
        public void caught(final ExceptionalState exceptionalState) {
            Preconditions.checkNotNull(exceptionalState);
            
            callViews(new Callback<RunView>() {

                @Override
                public void call(final RunView view) {
                    view.exceptionalStateCaught(exceptionalState);
                }
                
            });
        }
        
    }
    
    private final class DefaultRunsTerminatedListener implements RunsTerminatedListener {

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.events.RunsTerminatedListener#terminated()
         */
        @Override
        public void terminated() {
            callViews(new Callback<RunView>() {

                @Override
                public void call(final RunView view) {
                    view.terminate();
                }
            });
        }
        
    }
    
    private final Run run;
    
    /**
     * Vytvoří řadič, který bude též naslouchat vyřčeným promluvám a aktualizovat pohledy na konverzaci, případě reagovat na ukočnení konverzací.
     * 
     * @param run běžící konverzace
     * @param eventManager správce událostí
     * @return řadič
     */
    public static DefaultRunController create(final Run run, final EventManager eventManager) {
        Preconditions.checkNotNull(run);
        Preconditions.checkNotNull(eventManager);
        
        final DefaultRunController newInstance = new DefaultRunController(run, eventManager);
        
        newInstance.addListener(SpokenEvent.class, newInstance.new DefaultSpokenListener());
        newInstance.addListener(RunsTerminatedEvent.class, newInstance.new DefaultRunsTerminatedListener());
        newInstance.addListener(ExceptionalStateCaughtEvent.class, run, newInstance.new DefaultExceptionStateCaughtListener());
        
        return newInstance;
    }
    
    private DefaultRunController(final Run run, final EventManager eventManager) {
        super(eventManager);
        
        Preconditions.checkNotNull(run);
        
        this.run = run;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.controllers.RunController#tell(java.lang.String)
     */
    @Override
    public void tell(final String content) {
        Preconditions.checkNotNull(content);
        
        this.run.tell(content);
    }

}
