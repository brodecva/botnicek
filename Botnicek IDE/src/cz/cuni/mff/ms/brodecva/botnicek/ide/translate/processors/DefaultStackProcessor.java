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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.StackProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Comparisons;

/**
 * Výchozí implementace procesoru zpracovávajícího uzly tak, že podle nich vytváří instrukce pro modifikaci zásobníku pomocí prvků šablony.  
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultStackProcessor implements StackProcessor<List<TemplateElement>> {
    private final NormalWord pullState;
    private final NormalWord pullStopState;

    /**
     * Vytvoří nový procesor.
     * 
     * @param pullState slovo popisující stav, který je vložen na zásobník po průchodu sítí až do koncového uzlu, a spustí tak proceduru uvolňování nezpracovaných stavů z něj 
     * @param pullStopState slovo popisující stav, který slouží jako zarážka při uvolňování nezpracovaných stavů úspěšně prošlé sítě ze zásobníku
     * @return zásobníkový procesor
     */
    public static DefaultStackProcessor create(final NormalWord pullState, final NormalWord pullStopState) {
        Preconditions.checkNotNull(pullState);
        Preconditions.checkNotNull(pullStopState);
        Preconditions.checkArgument(Comparisons.allDifferent(pullState, pullStopState));
        
        return new DefaultStackProcessor(pullState, pullStopState);
    }
    
    private DefaultStackProcessor(final NormalWord pullState, final NormalWord pullStopState) {
        this.pullState = pullState;
        this.pullStopState = pullStopState;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Vnitřní uzel nijak nemodifikuje zásobník.</p>
     */
    @Override
    public List<TemplateElement> process(final InnerNode node) {
        return ImmutableList.of(Stack.pop());
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Výstupní uzel umístí na zásobník značku pro úklid.</p>
     */
    @Override
    public List<TemplateElement> process(final ExitNode node) {
        return ImmutableList.of(Stack.pushWords(this.pullState));
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Vstupní uzel umístí na zásobník zarážku úklidu nezpracovaných stavů.</p>
     */
    @Override
    public List<TemplateElement> process(final EnterNode node) {
        return ImmutableList.of(Stack.pushWords(this.pullStopState));
    }

    /** 
     * {@inheritDoc}
     * 
     * <p>Izolovaný uzel je nedosažitelný, tedy jím provedené úpravy zásobníku jsou irelevantní.</p>
     */
    @Override
    public List<TemplateElement> process(final IsolatedNode node) {
        return ImmutableList.of();
    }
}