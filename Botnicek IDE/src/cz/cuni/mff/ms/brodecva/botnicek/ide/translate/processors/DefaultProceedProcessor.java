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
package cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors;

import java.util.List;

import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Sr;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.ProceedProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ProcessingNode;

/**
 * Výchozí implementace procesoru, který generuje rekurzivní značky do šablony podle toho, zda-li má výpočet probíhat dál, či čekat na vstup.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultProceedProcessor implements ProceedProcessor<List<TemplateElement>> {
    
    /**
     * Vytvoří procesor.
     * 
     * @return vytvoří procesor
     */
    public static DefaultProceedProcessor create() {
        return new DefaultProceedProcessor();
    }
    
    private DefaultProceedProcessor() {
    }

    /** 
     * {@inheritDoc}
     * 
     * <p>Procesní uzel přidá do šablony instrukci pro rekurzivní zanoření výpočtu nad vstupem ze vzoru.</p>
     */
    public List<TemplateElement> process(final ProcessingNode node) {
        return ImmutableList.<TemplateElement>of(Sr.create());
    }
    
    /** 
     * {@inheritDoc}
     * 
     * <p>Zadávací uzel nepřidá do šablony žádný kód. Výpočet se tedy přeruší v daném tématu a bude vyčkávat na zadání od uživatele.</p>
     */
    public List<TemplateElement> process(final InputNode node) {
        return ImmutableList.of();
    }
}