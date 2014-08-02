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

import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Project;
import cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.Run;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.AbstractMappedEvent;

/**
 * Událost zahájení konverzace s robotem.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class RuntimeRunEvent extends AbstractMappedEvent<Project, RuntimeRunListener> {
    
    private final Run run;
    
    /**
     * Vytvoří událost.
     * 
     * @param project projekt, v rámci něhož testovací konverzace probíhá
     * @param run testovací konverzace
     * @return událost
     */
    public static RuntimeRunEvent create(final Project project, final Run run) {
        return new RuntimeRunEvent(project, run);
    }
    
    private RuntimeRunEvent(final Project project, final Run run) {
        super(project);
        
        Preconditions.checkNotNull(run);
        
        this.run = run;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.utils.Event#dispatchTo(java.lang.Object)
     */
    @Override
    public void dispatchTo(final RuntimeRunListener listener) {
        listener.run(this.run);
    }
}
 