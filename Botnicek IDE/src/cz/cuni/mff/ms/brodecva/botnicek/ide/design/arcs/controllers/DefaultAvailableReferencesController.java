/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers;

import java.util.Set;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.properties.AvailableReferencesView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesChangedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesChangedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesExtendedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesExtendedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesReducedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.events.AvailableReferencesReducedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;

/**
 * Výchozí implementace řadiče dostupných míst zanoření.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultAvailableReferencesController extends
        AbstractController<AvailableReferencesView> implements
        AvailableReferencesController {

    private final class DefaultAvailableReferencesExtendedListener implements
            AvailableReferencesExtendedListener {

        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.
         * AvailableReferencesChangedListener#referencesChanged(java.util.Set)
         */
        @Override
        public void referencesExtended(final Set<? extends EnterNode> references) {
            Preconditions.checkNotNull(references);

            callViews(new Callback<AvailableReferencesView>() {

                @Override
                public void call(final AvailableReferencesView view) {
                    view.extendAvailableReferences(references);
                }

            });
        }

    }

    private final class DefaultAvailableReferencesChangedListener implements
            AvailableReferencesChangedListener {

        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.
         * AvailableReferencesChangedListener#referencesChanged(java.util.Set)
         */
        @Override
        public void referencesChanged(final Set<? extends EnterNode> references) {
            Preconditions.checkNotNull(references);

            callViews(new Callback<AvailableReferencesView>() {

                @Override
                public void call(final AvailableReferencesView view) {
                    view.updateAvailableReferences(references);
                }

            });
        }

    }

    private final class DefaultAvailableReferencesReducedListener implements
            AvailableReferencesReducedListener {

        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.system.
         * AvailableReferencesChangedListener#referencesChanged(java.util.Set)
         */
        @Override
        public void referencesReduced(final Set<? extends EnterNode> references) {
            Preconditions.checkNotNull(references);

            callViews(new Callback<AvailableReferencesView>() {

                @Override
                public void call(final AvailableReferencesView view) {
                    view.removeAvailableReferences(references);
                }

            });
        }

    }

    /**
     * Vytvoří řadič
     * 
     * @param system
     *            systém sítí
     * @param eventManager
     *            správce událostí
     * @return řadič
     */
    public static DefaultAvailableReferencesController create(
            final System system, final EventManager eventManager) {
        Preconditions.checkNotNull(system);
        Preconditions.checkNotNull(eventManager);

        final DefaultAvailableReferencesController newInstance =
                new DefaultAvailableReferencesController(system, eventManager);

        newInstance.addListener(AvailableReferencesChangedEvent.class, system,
                newInstance.new DefaultAvailableReferencesChangedListener());
        newInstance.addListener(AvailableReferencesExtendedEvent.class, system,
                newInstance.new DefaultAvailableReferencesExtendedListener());
        newInstance.addListener(AvailableReferencesReducedEvent.class, system,
                newInstance.new DefaultAvailableReferencesReducedListener());

        return newInstance;
    }

    private final System system;

    private DefaultAvailableReferencesController(final System system,
            final EventManager eventManager) {
        super(eventManager);

        Preconditions.checkNotNull(system);

        this.system = system;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController#fill
     * (java.lang.Object)
     */
    @Override
    public void fill(final AvailableReferencesView view) {
        Preconditions.checkNotNull(view);

        view.updateAvailableReferences(this.system.getAvailableReferences());
    }
}
