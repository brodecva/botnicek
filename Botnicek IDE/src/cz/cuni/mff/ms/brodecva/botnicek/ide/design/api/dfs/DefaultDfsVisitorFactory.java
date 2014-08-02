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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs;

import java.util.Set;

import com.google.common.base.Preconditions;

/**
 * Továrna na produkci výchozí implementace DFS návštěvníka systému sítí.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultDfsVisitorFactory implements DfsVisitorFactory {

    /**
     * Vytvoří továrnu.
     * 
     * @return továrna
     */
    public static DefaultDfsVisitorFactory create() {
        return new DefaultDfsVisitorFactory();
    }
    
    private DefaultDfsVisitorFactory() {
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsVisitorFactory#produce(cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsObserver[])
     */
    @Override
    public DfsVisitor produce(final DfsObserver... observers) {
        Preconditions.checkNotNull(observers);
        
        return DefaultDfsVisitor.create(observers);
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsVisitorFactory#produce(java.util.Set)
     */
    @Override
    public DfsVisitor produce(Set<? extends DfsObserver> observers) {
        Preconditions.checkNotNull(observers);
        
        return DefaultDfsVisitor.create(observers);
    }

}
