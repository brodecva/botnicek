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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views;

import java.awt.Color;
import java.net.NetPermission;
import java.util.Collection;

import javax.swing.JDesktopPane;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.controllers.CodeValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.controllers.MixedPatternValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.controllers.SimplePatternValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.words.controllers.NormalWordValidationController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.AvailableReferencesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcInternalWindow;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.ArcPropertiesView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkPropertiesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkInternalWindow;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkPropertiesView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class SystemPane extends JDesktopPane implements ArcPropertiesView, NetworkPropertiesView {
    
    private final Multimap<Network, NetworkInternalWindow> networksToFrame = HashMultimap.create();
    
    public static SystemPane create(final NetworkPropertiesController networkPropertiesController, final ArcPropertiesController arcPropertiesController) {
        Preconditions.checkNotNull(networkPropertiesController);
        Preconditions.checkNotNull(arcPropertiesController);
        
        final SystemPane newInstance = new SystemPane();
        
        networkPropertiesController.addView(newInstance);
        arcPropertiesController.addView(newInstance);
        
        newInstance.setBackground(Color.GREEN);
        
        return newInstance;
    }

    private SystemPane() {
    }
    
    @Override
    public void arcDisplayed(final ArcController arcController, final AvailableReferencesController availableReferencesController, final NormalWordValidationController nameValidationController, final CodeValidationController codeValidationController, final SimplePatternValidationController simplePatternValidationController, final MixedPatternValidationController mixedPatternValidationController, final NormalWordValidationController predicateNameValidationController) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(availableReferencesController);
        Preconditions.checkNotNull(nameValidationController);
        Preconditions.checkNotNull(codeValidationController);
        Preconditions.checkNotNull(simplePatternValidationController);
        Preconditions.checkNotNull(mixedPatternValidationController);
        Preconditions.checkNotNull(predicateNameValidationController);

        final ArcInternalWindow arcInternalWindow = ArcInternalWindow.create(this, arcController, availableReferencesController, nameValidationController, codeValidationController, simplePatternValidationController, mixedPatternValidationController, predicateNameValidationController);
        
        arcInternalWindow.show();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkPropertiesView#networkDisplayed(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController, cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesController)
     */
    @Override
    public void networkDisplayed(final NetworkController networkController,
            NodesController nodesController, final ArcPropertiesController arcPropertiesController) {
        Preconditions.checkNotNull(networkController);
        Preconditions.checkNotNull(nodesController);
        Preconditions.checkNotNull(arcPropertiesController);
        
        final NetworkInternalWindow networkWindow = NetworkInternalWindow.create(this, networkController, nodesController, arcPropertiesController);
        networkWindow.show();
    }
}
