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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.controllers;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRenamedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRenamedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcReprioritizedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcReprioritizedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRetypedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.ArcRetypedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.views.graph.ArcsView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.events.ArcRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;

/**
 * Výchozí implementace řadiče hran v zobrazení sítě.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultArcsController extends AbstractController<ArcsView>
        implements ArcsController {

    private class DefaultArcRemovedEventListener implements ArcRemovedListener {

        @Override
        public void arcRemoved(final Arc arc) {
            Preconditions.checkNotNull(arc);

            callViews(new Callback<ArcsView>() {

                @Override
                public void call(final ArcsView view) {
                    view.arcRemoved(arc);
                }
            });
        }

    }

    private final class DefaultArcRenamedListener implements ArcRenamedListener {

        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.
         * ArcRenamedListener
         * #arcRenamed(cz.cuni.mff.ms.brodecva.botnicek.ide.design
         * .arcs.model.Arc,
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
         */
        @Override
        public void arcRenamed(final Arc oldVersion, final Arc newVersion) {
            Preconditions.checkNotNull(oldVersion);
            Preconditions.checkNotNull(newVersion);

            callViews(new Callback<ArcsView>() {

                @Override
                public void call(final ArcsView view) {
                    Preconditions.checkNotNull(view);

                    view.arcRenamed(oldVersion, newVersion);
                }

            });
        }

    }

    private final class DefaultArcReprioritizedListener implements
            ArcReprioritizedListener {

        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.
         * ArcReprioritizedListener
         * #arcReprioritized(cz.cuni.mff.ms.brodecva.botnicek
         * .ide.design.arcs.model.Arc,
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
         */
        @Override
        public void
                arcReprioritized(final Arc oldVersion, final Arc newVersion) {
            Preconditions.checkNotNull(oldVersion);
            Preconditions.checkNotNull(newVersion);

            callViews(new Callback<ArcsView>() {

                @Override
                public void call(final ArcsView view) {
                    Preconditions.checkNotNull(view);

                    view.arcReprioritized(oldVersion, newVersion);
                }

            });
        }

    }

    private final class DefaultArcRetypedListener implements ArcRetypedListener {

        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.events.
         * ArcRetypedListener
         * #arcRetyped(cz.cuni.mff.ms.brodecva.botnicek.ide.design
         * .arcs.model.Arc,
         * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)
         */
        @Override
        public void arcRetyped(final Arc oldVersion, final Arc newVersion) {
            Preconditions.checkNotNull(oldVersion);
            Preconditions.checkNotNull(newVersion);

            callViews(new Callback<ArcsView>() {

                @Override
                public void call(final ArcsView view) {
                    Preconditions.checkNotNull(view);

                    view.arcRetyped(oldVersion, newVersion);
                }

            });
        }

    }

    /**
     * Vytvoří řadič.
     * 
     * @param system
     *            systém všech sítí
     * @param network
     *            síť s hranami
     * @param eventManager
     *            správce událostí
     * 
     * @return řadič
     */
    public static DefaultArcsController create(final System system,
            final Network network, final EventManager eventManager) {
        Preconditions.checkNotNull(system);
        Preconditions.checkNotNull(network);
        Preconditions.checkNotNull(eventManager);

        final DefaultArcsController newInstance =
                new DefaultArcsController(system, eventManager);

        newInstance.addListener(ArcRenamedEvent.class, network,
                newInstance.new DefaultArcRenamedListener());
        newInstance.addListener(ArcReprioritizedEvent.class, network,
                newInstance.new DefaultArcReprioritizedListener());
        newInstance.addListener(ArcRetypedEvent.class, network,
                newInstance.new DefaultArcRetypedListener());
        newInstance.addListener(ArcRemovedEvent.class, network,
                newInstance.new DefaultArcRemovedEventListener());

        return newInstance;
    }

    private final System system;

    private DefaultArcsController(final System system,
            final EventManager eventManager) {
        super(eventManager);

        this.system = system;
    }

    @Override
    public void removeArc(final NormalWord name) {
        this.system.removeArc(name);
    }
}
