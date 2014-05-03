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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers;

import java.net.URI;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.ArcRemovedEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.events.network.ArcRemovedListener;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.CodeTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.PatternArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.utils.Callback;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.ArcPropertiesView;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.AbstractArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.CodeTestArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.PatternArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.PredicateTestArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.RecurentArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.views.arcs.TransitionArcInternalFrame;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.EventManager;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.mvc.AbstractController;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultArcPropertiesController extends AbstractController<ArcPropertiesView>
        implements ArcPropertiesController {
    
    private static final class ArcToFrameConverter implements cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.api.Processor {
        
        private final System system;
        private final Bot bot;
        private final Map<URI, String> namespacesToPrefixes;
        private final EventManager eventManager;
        
        private Optional<AbstractArcInternalFrame> correspondingFrame = Optional.<AbstractArcInternalFrame>absent();

        public static ArcToFrameConverter create(final System system, final EventManager eventManager, final Bot bot, final Map<URI, String> namespacesToPrefixes) {
            return new ArcToFrameConverter(system, eventManager, bot, namespacesToPrefixes);
        }
        
        private ArcToFrameConverter(final System system, final EventManager eventManager, final Bot bot, final Map<URI, String> namespacesToPrefixes) {
            Preconditions.checkNotNull(system);
            Preconditions.checkNotNull(eventManager);
            Preconditions.checkNotNull(bot);
            Preconditions.checkNotNull(namespacesToPrefixes);
            
            this.system = system;
            this.bot = bot;
            this.namespacesToPrefixes = namespacesToPrefixes;
            this.eventManager = eventManager;
        }

        @Override
        public void process(final TransitionArc arc) {
            this.correspondingFrame = Optional.<AbstractArcInternalFrame>of(new TransitionArcInternalFrame(DefaultArcController.create(system, eventManager, bot, namespacesToPrefixes), arc.getName(), arc.getFrom().getName(),arc.getTo().getName(), arc.getPriority(), arc.getCode().getText()));
        }

        @Override
        public void process(final PredicateTestArc arc) {
            this.correspondingFrame = Optional.<AbstractArcInternalFrame>of(new PredicateTestArcInternalFrame(DefaultArcController.create(system, eventManager, bot, namespacesToPrefixes), arc.getName(), arc.getFrom().getName(), arc.getTo().getName(), arc.getPriority(), arc.getCode().getText(), arc.getPrepareCode().getText(), arc.getPredicateName().getValue(), arc.getValue().getText()));
        }

        @Override
        public void process(final CodeTestArc arc) {
            this.correspondingFrame = Optional.<AbstractArcInternalFrame>of(new CodeTestArcInternalFrame(DefaultArcController.create(system, eventManager, bot, namespacesToPrefixes), arc.getName(), arc.getFrom().getName(),  arc.getTo().getName(), arc.getPriority(), arc.getCode().getText(), arc.getTested().getText(), arc.getValue().getText()));
        }

        @Override
        public void process(final RecurentArc arc) {
            this.correspondingFrame = Optional.<AbstractArcInternalFrame>of(new RecurentArcInternalFrame(DefaultArcController.create(system, eventManager, bot, namespacesToPrefixes), arc.getName(), arc.getFrom().getName(),  arc.getTo().getName(), arc.getPriority(), arc.getCode().getText(), arc.getTarget().getName(), null, arc.getValue().getText()));
        }

        @Override
        public void process(PatternArc arc) {
            this.correspondingFrame = Optional.<AbstractArcInternalFrame>of(new PatternArcInternalFrame(DefaultArcController.create(system, eventManager, bot, namespacesToPrefixes), arc.getName(), arc.getFrom().getName(),  arc.getTo().getName(), arc.getPriority(), arc.getCode().getText(), arc.getPattern().getText(), arc.getThat().getText()));
        }

        public AbstractArcInternalFrame getCorrespondingFrame() {
            return this.correspondingFrame.get();
        }
    }
    
    private class DefaultArcRemovedEventListener implements ArcRemovedListener {
        
        @Override
        public void arcRemoved(final String name) {
            callViews(new Callback<ArcPropertiesView>() {

                @Override
                public void call(final ArcPropertiesView view) {
                    view.removeProperties(name);
                }
                
            });
        }
        
    }
    
    private final System system;
    private final Bot bot;
    private final Map<URI, String> namespacesToPrefixes;
    
    public DefaultArcPropertiesController create(final System system, final EventManager eventManager) {
        final DefaultArcPropertiesController newInstance = new DefaultArcPropertiesController(system, eventManager, bot, namespacesToPrefixes);
        
        newInstance.addEventListener(ArcRemovedEvent.class, newInstance.new DefaultArcRemovedEventListener());
        
        return newInstance;
    }
    
    /**
     * @param eventManager
     */
    private DefaultArcPropertiesController(final System system, final EventManager eventManager, final Bot bot, final Map<URI, String> namespacesToPrefixes) {
        super(eventManager);
        
        Preconditions.checkNotNull(system);
        Preconditions.checkNotNull(bot);
        Preconditions.checkNotNull(namespacesToPrefixes);
        
        this.system = system;
        this.bot = bot;
        this.namespacesToPrefixes = namespacesToPrefixes;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.controllers.ArcPropertiesController#displayProperties(java.lang.String)
     */
    @Override
    public void displayProperties(final String name) {
        Preconditions.checkNotNull(name);
        
        final Arc arc = this.system.getArc(name);
        
        final ArcToFrameConverter processor = ArcToFrameConverter.create(this.system, getEventManager(), bot, namespacesToPrefixes);
        arc.accept(processor);
        final AbstractArcInternalFrame corresponding = processor.getCorrespondingFrame();
        
        this.callViews(new Callback<ArcPropertiesView>() {
            
            @Override
            public void call(final ArcPropertiesView view) {
                view.displayProperties(corresponding);
            }
        });
    }    
}
