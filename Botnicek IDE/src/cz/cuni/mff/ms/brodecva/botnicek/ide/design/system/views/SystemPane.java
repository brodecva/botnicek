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
import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.controllers.CheckController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcPropertiesDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.ArcsController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers.AvailableReferencesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.ArcInternalWindow;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.ArcPropertiesDisplayView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.controllers.NetworkDisplayController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkDisplayView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.NetworkInternalWindow;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.controllers.NodesController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.controllers.SystemController;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.Components;

/**
 * Správce plochy s vnitřními okny grafů sítí a podrobností hran.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class SystemPane implements ArcPropertiesDisplayView,
        NetworkDisplayView, SystemView {

    private static SystemPane create(final Container parent) {
        return create(parent, DummySystemController.create(),
                DummyNetworkDisplayController.create(),
                DummyArcPropertiesDisplayController.create());
    }

    /**
     * Vytvoří správce plochy
     * 
     * @param parent
     *            rodičovský kontejner plochy
     * @param systemController
     *            řadič systému sítí
     * @param networkDisplayController
     *            řadič zobrazení grafů sítí
     * @param arcPropertiesDisplayController
     *            řadič zobrazení vlastností sítí
     * @return správce plochy
     */
    public static
            SystemPane
            create(final Container parent,
                    final SystemController systemController,
                    final NetworkDisplayController networkDisplayController,
                    final ArcPropertiesDisplayController arcPropertiesDisplayController) {
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(systemController);
        Preconditions.checkNotNull(networkDisplayController);
        Preconditions.checkNotNull(arcPropertiesDisplayController);

        final SystemPane newInstance =
                new SystemPane(systemController, networkDisplayController,
                        arcPropertiesDisplayController);

        newInstance.subscribe(systemController, networkDisplayController,
                arcPropertiesDisplayController);

        newInstance.addToParent(parent);

        return newInstance;
    }

    /**
     * Spustí testovací verzi.
     * 
     * @param args
     *            argumenty
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    final SystemPane systemPane = SystemPane.create(frame);
                    systemPane.desktopPane.setBackground(Color.BLUE);
                    frame.setContentPane(systemPane.desktopPane);

                    frame.setVisible(true);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final JDesktopPane desktopPane = new JDesktopPane();

    private final SystemController systemController;

    private final NetworkDisplayController networkPropertiesController;

    private final ArcPropertiesDisplayController arcPropertiesController;

    private SystemPane(final SystemController systemController,
            final NetworkDisplayController networkPropertiesController,
            final ArcPropertiesDisplayController arcPropertiesController) {
        Preconditions.checkNotNull(systemController);
        Preconditions.checkNotNull(networkPropertiesController);
        Preconditions.checkNotNull(arcPropertiesController);

        this.systemController = systemController;
        this.networkPropertiesController = networkPropertiesController;
        this.arcPropertiesController = arcPropertiesController;

        this.desktopPane.setBackground(UIManager.getColor("Panel.background"));
    }

    private void addToParent(final Container parent) {
        parent.add(this.desktopPane);
        parent.revalidate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.
     * ArcPropertiesDisplayView
     * #arcDisplayed(cz.cuni.mff.ms.brodecva.botnicek.ide
     * .design.arcs.controllers.ArcController,
     * cz.cuni.mff.ms.brodecva.botnicek.ide
     * .design.arcs.controllers.AvailableReferencesController,
     * cz.cuni.mff.ms.brodecva
     * .botnicek.ide.check.words.controllers.NormalWordValidationController,
     * cz.cuni
     * .mff.ms.brodecva.botnicek.ide.check.code.controllers.CodeValidationController
     * , cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.controllers.
     * SimplePatternValidationController,
     * cz.cuni.mff.ms.brodecva.botnicek.ide.check
     * .mixedpattern.controllers.MixedPatternValidationController,
     * cz.cuni.mff.ms
     * .brodecva.botnicek.ide.check.words.controllers.NormalWordValidationController
     * )
     */
    @Override
    public
            void
            arcDisplayed(
                    final ArcController arcController,
                    final AvailableReferencesController availableReferencesController,
                    final CheckController<? extends NormalWord> nameValidationController,
                    final CheckController<? extends Code> codeValidationController,
                    final CheckController<? extends SimplePattern> simplePatternValidationController,
                    final CheckController<? extends MixedPattern> mixedPatternValidationController,
                    final CheckController<? extends NormalWord> predicateNameValidationController) {
        Preconditions.checkNotNull(arcController);
        Preconditions.checkNotNull(availableReferencesController);
        Preconditions.checkNotNull(nameValidationController);
        Preconditions.checkNotNull(codeValidationController);
        Preconditions.checkNotNull(simplePatternValidationController);
        Preconditions.checkNotNull(mixedPatternValidationController);
        Preconditions.checkNotNull(predicateNameValidationController);

        final ArcInternalWindow arcInternalWindow =
                ArcInternalWindow.create(this.desktopPane, arcController,
                        availableReferencesController,
                        nameValidationController, codeValidationController,
                        simplePatternValidationController,
                        mixedPatternValidationController,
                        predicateNameValidationController);

        arcInternalWindow.show();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#closed
     * ()
     */
    @Override
    public void closed() {
        removeFromParent();
        unsubscribe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.views.
     * NetworkPropertiesView
     * #networkDisplayed(cz.cuni.mff.ms.brodecva.botnicek.ide
     * .design.networks.controllers.NetworkController,
     * cz.cuni.mff.ms.brodecva.botnicek
     * .ide.design.arcs.controllers.ArcPropertiesController)
     */
    @Override
    public void networkDisplayed(final NetworkController networkController,
            final ArcsController arcsController,
            final NodesController nodesController,
            final ArcPropertiesDisplayController arcPropertiesController) {
        Preconditions.checkNotNull(networkController);
        Preconditions.checkNotNull(nodesController);
        Preconditions.checkNotNull(arcsController);
        Preconditions.checkNotNull(arcPropertiesController);

        final NetworkInternalWindow networkWindow =
                NetworkInternalWindow.create(this.desktopPane,
                        networkController, nodesController, arcsController,
                        arcPropertiesController);
        networkWindow.show();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#
     * networkAdded
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void networkAdded(final Network added) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#
     * networkRemoved
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void networkRemoved(final Network network) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#
     * networkRenamed
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void networkRenamed(final Network network) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#
     * networkSelected
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)
     */
    @Override
    public void networkSelected(final Network network) {
    }

    private void removeFromParent() {
        final Container parent = this.desktopPane.getParent();
        Preconditions.checkState(Components.hasParent(parent));

        parent.remove(this.desktopPane);
    }

    private void subscribe(final SystemController systemController,
            final NetworkDisplayController networkPropertiesController,
            final ArcPropertiesDisplayController arcPropertiesController) {
        systemController.addView(this);
        networkPropertiesController.addView(this);
        arcPropertiesController.addView(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#
     * systemNameChanged
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System)
     */
    @Override
    public void systemNameChanged(final System system) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.views.SystemView#systemSet
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System)
     */
    @Override
    public void systemSet(final System system) {
    }

    private void unsubscribe() {
        this.systemController.removeView(this);
        this.arcPropertiesController.removeView(this);
        this.networkPropertiesController.removeView(this);
    }
}
