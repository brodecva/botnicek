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
package cz.cuni.mff.ms.brodecva.botnicek.ide.translate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.Template;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.BlockCondition;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Get;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.RawContent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Set;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.SinglePredicateCondition;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Sr;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Srai;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Star;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Think;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.DefaultListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.ListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.ValueOnlyListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.TestProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.CodeTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PatternArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.utils.Stack;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIMLIndex;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultTestProcessor implements TestProcessor {
    private final NormalWord testingPredicate;
    
    private final List<Category> categories = new LinkedList<>();

    public static DefaultTestProcessor create(final NormalWord testingPredicate) {
        return new DefaultTestProcessor(testingPredicate);
    }
    
    /**
     * @param categories
     */
    private DefaultTestProcessor(final NormalWord testingPredicate) {
        Preconditions.checkNotNull(testingPredicate);
        
        this.testingPredicate = testingPredicate;
    }
    
    public List<Category> getResult() {
        return new LinkedList<>(this.categories);
    }

    @Override
    public void process(final PatternArc arc) {                
        final List<TemplateElement> code = Lists.<TemplateElement>newArrayList(RawContent.create(arc.getCode().getText()));
        
        final TemplateElement updateStack = Stack.push(arc.getTo().getName());
        code.add(updateStack);
        
        final SimplePattern pattern = arc.getPattern();
        final List<TemplateElement> copiedPattern = copyPattern(pattern);
        final Srai passCopy = Srai.create(copiedPattern);
        code.add(passCopy);
        
        this.categories.clear();
        categories.add(Category.create(pattern, arc.getThat(), Template.create(code)));
        categories.add(Category.create(Patterns.createUniversal(), Patterns.createUniversal(), Template.create(Stack.set())));
    }

    /**
     * @param pattern
     * @return
     */
    private static List<TemplateElement> copyPattern(final SimplePattern pattern) {
        final List<TemplateElement> copiedPattern = new LinkedList<>();
        
        final List<String> words = Splitter.on(AIML.WORD_DELIMITER.getValue()).splitToList(pattern.getText());
        int starCounter = 1;
        for (final String word : words) {
            if (words.equals(AIML.STAR_WILDCARD.getValue()) || word.equals(AIML.UNDERSCORE_WILDCARD.getValue())) {
                copiedPattern.add(Star.create(new AIMLIndex(starCounter++)));
                continue;
            }
            
            copiedPattern.add(Text.create(word));
        }
        return copiedPattern;
    }
    
    @Override
    public void process(final PredicateTestArc arc) {        
        final List<ValueOnlyListItem> success = new ArrayList<>();
        final List<TemplateElement> successCode = Lists.<TemplateElement>newArrayList(RawContent.create(arc.getCode().getText()));
        final TemplateElement update = Stack.push(arc.getTo().getName());
        successCode.add(update);
        success.add(ValueOnlyListItem.create(arc.getValue(), successCode));
       
        final DefaultListItem fail = DefaultListItem.create(Stack.set());
        
        final SinglePredicateCondition choose = SinglePredicateCondition.create(arc.getPredicateName(), fail, success);
        final List<TemplateElement> code = new LinkedList<>();
        code.add(choose);
        
        code.add(Sr.create());
        this.categories.clear();
        categories.add(Category.create(Patterns.createUniversal(), Patterns.createUniversal(), Template.create(code)));
    }
    
    @Override
    public void process(final CodeTestArc arc) {
        final Set setTesting = Set.create(testingPredicate, Lists.<TemplateElement>newArrayList(RawContent.create(arc.getTested().getText())));
        final Think hideTesting = Think.create(setTesting);
        
        final List<TemplateElement> code = new LinkedList<>();
        code.add(hideTesting);
        
        final List<ValueOnlyListItem> success = new ArrayList<>();
        final List<TemplateElement> successCode = Lists.<TemplateElement>newArrayList(RawContent.create(arc.getCode().getText()));
        final TemplateElement update = Stack.push(arc.getTo().getName());
        successCode.add(update);
        success.add(ValueOnlyListItem.create(arc.getValue(), successCode));
       
        final DefaultListItem fail = DefaultListItem.create(Stack.set());
        
        final SinglePredicateCondition choose = SinglePredicateCondition.create(testingPredicate, fail, success);
        code.add(choose);
        
        final Set clear = Set.create(testingPredicate, Text.create(""));
        final Think hideClearing = Think.create(clear);
        code.add(hideClearing);
        
        code.add(Sr.create());
        this.categories.clear();
        categories.add(Category.create(Patterns.createUniversal(), Patterns.createUniversal(), Template.create(code)));
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.TestProcessor#test(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.RecurentArc)
     */
    @Override
    public void process(final RecurentArc arc) {
        final TemplateElement tested = Stack.push(arc.getTarget().getName());
        
        final Set setTesting = Set.create(testingPredicate, tested, Sr.create());
        final Think hideTesting = Think.create(setTesting);
        
        final List<TemplateElement> code = new LinkedList<>();
        code.add(hideTesting);
        
        final TemplateElement displayTesting = Get.create(testingPredicate);
        code.add(displayTesting);
        
        final List<ValueOnlyListItem> success = new ArrayList<>();
        final List<TemplateElement> successCode = Lists.<TemplateElement>newArrayList(RawContent.create(arc.getCode().getText()));
        final TemplateElement update = Stack.push(arc.getTo().getName());
        successCode.add(update);
        success.add(ValueOnlyListItem.create(arc.getValue(), successCode));
       
        final DefaultListItem fail = DefaultListItem.create(Stack.set());
        
        final SinglePredicateCondition choose = SinglePredicateCondition.create(testingPredicate, fail, success);
        code.add(choose);
        
        final Set clear = Set.create(testingPredicate, Text.create(""));
        final Think hideClearing = Think.create(clear);
        code.add(hideClearing);
        
        code.add(Sr.create());
        this.categories.clear();
        categories.add(Category.create(Patterns.createUniversal(), Patterns.createUniversal(), Template.create(code)));
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.TestProcessor#test(cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.arcs.TransitionArc)
     */
    @Override
    public void process(final TransitionArc arc) {
        this.categories.clear();
        categories.add(Category.create(Patterns.createUniversal(), Patterns.createUniversal(), Template.create(Stack.set())));
    }
}