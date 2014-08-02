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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.SessionException;

/**
 * Výchozí továrna pro inicializaci běhového prostředí.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultRuntimeFactory implements RuntimeFactory {

    /**
     * Vytvoří instanci.
     * 
     * @return výchozí továrna na běhové prostředí
     */
    public static DefaultRuntimeFactory create() {
        return new DefaultRuntimeFactory();
    }
    
    private DefaultRuntimeFactory() {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.RuntimeFactory#produce(cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.RuntimeSettings, cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher)
     */
    @Override
    public Runtime produce(final RuntimeSettings settings, final Dispatcher dispatcher) throws SessionException {
        Preconditions.checkNotNull(settings);
        Preconditions.checkNotNull(dispatcher);
        
        return DefaultRuntime.create(settings, dispatcher);
    }

}
