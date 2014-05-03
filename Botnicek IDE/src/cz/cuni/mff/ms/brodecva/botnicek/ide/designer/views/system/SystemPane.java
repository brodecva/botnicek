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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.system;

import java.util.Collection;

import javax.swing.JDesktopPane;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.SystemController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.ArcPropertiesView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.AbstractArcInternalFrame;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class SystemPane extends JDesktopPane implements ArcPropertiesView {
    
    private final SystemController systemController;
    
    private final Multimap<String, AbstractArcInternalFrame> namesToProperties = HashMultimap.create();
    
    private SystemPane(final SystemController systemController) {
        Preconditions.checkNotNull(systemController);
        
        this.systemController = systemController;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.ArcPropertiesView#displayPropertiesFrame(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.AbstractArcInternalFrame)
     */
    @Override
    public void displayProperties(final AbstractArcInternalFrame frame) {
        Preconditions.checkNotNull(frame);
        
        final String name = frame.getName();
        Preconditions.checkNotNull(name);
        
        this.namesToProperties.put(name, frame);
        this.add(frame);
        frame.show();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.ArcPropertiesView#removeProperties(java.lang.String)
     */
    @Override
    public void removeProperties(final String name) {
        final Collection<AbstractArcInternalFrame> present = this.namesToProperties.removeAll(name);
        
        for (final AbstractArcInternalFrame removed : present) {
            this.remove(removed);
        }
    }
}
