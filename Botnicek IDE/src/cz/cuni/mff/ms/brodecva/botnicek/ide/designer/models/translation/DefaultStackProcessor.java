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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.translation;

import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.implementations.Topicstar;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.StackProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations.IsolatedProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultStackProcessor implements
        StackProcessor, TemplateElementsGenerator {
    private final String pullState;

    private final String pullStopState;
    
    private List<TemplateElement> code = new LinkedList<>();

    public static DefaultStackProcessor create(final String pullState, final String pullStopState) {
        return new DefaultStackProcessor(pullState, pullStopState);
    }
    
    /**
     * @param code
     */
    private DefaultStackProcessor(final String pullState, final String pullStopState) {
        Preconditions.checkNotNull(pullState);
        Preconditions.checkNotNull(pullStopState);
        Preconditions.checkArgument(!pullState.isEmpty());
        Preconditions.checkArgument(!pullStopState.isEmpty());
        
        this.pullState = pullState;
        this.pullStopState = pullStopState;
    }
    
    public List<TemplateElement> getResult() {
        return new LinkedList<>(this.code);
    }

    @Override
    public void process(final InnerNode node) {
        this.code.clear();
        this.code.add(Topicstar.create());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.DispatchProcessor#dispatch(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.ExitNode)
     */
    @Override
    public void process(final ExitNode node) {
        this.code.clear();
        this.code.add(Text.create(this.pullState + AIML.WORD_DELIMITER));
        this.code.add(Topicstar.create());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.DispatchProcessor#dispatch(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode)
     */
    @Override
    public void process(final EnterNode node) {
        this.code.clear();
        this.code.add(Text.create(this.pullStopState + AIML.WORD_DELIMITER));
        this.code.add(Topicstar.create());
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.StackProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations.IsolatedProcessingNode)
     */
    @Override
    public void process(final IsolatedNode node) {
        this.code.clear();
    }
}