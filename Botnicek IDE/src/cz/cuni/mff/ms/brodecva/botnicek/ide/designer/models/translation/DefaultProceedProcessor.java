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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.implementations.Sr;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.api.ProceedProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.InputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.ProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.nodes.implementations.IsolatedProcessingNode;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultProceedProcessor implements ProceedProcessor, TemplateElementsGenerator {
    private List<TemplateElement> code = new LinkedList<>();
    
    public static DefaultProceedProcessor create() {
        return new DefaultProceedProcessor();
    }
    
    /**
     * @param code
     */
    private DefaultProceedProcessor() {
    }
    
    public List<TemplateElement> getResult() {
        return new LinkedList<>(this.code);
    }

    @Override
    public void process(final ProcessingNode node) {
        this.code.clear();
        this.code.add(Sr.create());
    }
    
    @Override
    public void process(final InputNode node) {
        this.code.clear();
    }
}