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
import com.google.common.collect.ImmutableSet;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.Template;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.RawContent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Set;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.SinglePredicateCondition;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Sr;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Srai;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Star;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Think;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.DefaultListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.lists.ValueOnlyListItem;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.TestProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.CodeTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PatternArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIMLIndex;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLWildcard;

/**
 * Výchozí implementace procesoru hran.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultTestProcessor implements TestProcessor<List<Topic>> {
    
    private final static java.util.Set<Character> WILDCARD_CHARACTERS;
    
    static {
        final AIMLWildcard[] wildcards = AIMLWildcard.values();
        final ImmutableSet.Builder<Character> wildcardsBuilder = ImmutableSet.builder();
        for (final AIMLWildcard wildcard : wildcards) {            
            wildcardsBuilder.add(wildcard.getValue().charAt(0));
        }
        
        WILDCARD_CHARACTERS = wildcardsBuilder.build();
    }
    
    private final NormalWord testingPredicate;
    private final NormalWord successState;
    private final NormalWord returnState;

    /**
     * Vytvoří testovací procesor.
     * 
     * @param testingPredicate rezervovaný název testovacího predikátu
     * @param successState slovo popisující stav, který indikuje úspěšné projití podsítě odkazované z následujícího stavu na zásobníku
     * @param returnState slovo popisující stav, který indikuje zpracování podsítě
     * @return testovací procesor
     */
    public static DefaultTestProcessor create(final NormalWord testingPredicate, final NormalWord successState, final NormalWord returnState) {
        return new DefaultTestProcessor(testingPredicate, successState, returnState);
    }
    
    private DefaultTestProcessor(final NormalWord testingPredicate, final NormalWord successState, final NormalWord returnState) {
        Preconditions.checkNotNull(testingPredicate);
        Preconditions.checkNotNull(successState);
        Preconditions.checkNotNull(returnState);
        
        this.testingPredicate = testingPredicate;
        this.successState = successState;
        this.returnState = returnState;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Výsledkem zpracování je téma, které je pojmenováno po hraně a které obsahuje dvě kategorie. První kategorie slouží k samotnému porovnání se vzorem, druhá pak v případě neúspěchu předá zpracování dál.</p>
     */
    @Override
    public List<Topic> process(final PatternArc arc) {                
        Preconditions.checkNotNull(arc);
        
        final MixedPattern pattern = arc.getPattern();
        final List<TemplateElement> patternCopy = copyPattern(pattern);
        
        final TemplateElement popArc = Stack.pop();
        final TemplateElement doCode = RawContent.create(arc.getCode().getText());
        final TemplateElement pushTarget = Stack.popAndPushWords(arc.getTo().getName());
        final TemplateElement passCopy = Srai.create(patternCopy);
        final TemplateElement pass = Sr.create();
        
        final List<TemplateElement> successCode = ImmutableList.of(doCode, pushTarget, passCopy);
        final List<TemplateElement> failCode = ImmutableList.of(popArc, pass);
        
        return ImmutableList.of(createArcTopic(arc,
                        Category.create(pattern, arc.getThat(), Template.create(successCode)),
                        Category.createUniversal(Template.create(failCode))));
    }
    
    /**
     * Zkopíruje vzor. Žolíky ze vzoru jsou převedeny na příslušné prvky šablony, jejichž výstupem je obsah zachycený žolíky.
     * 
     * @param pattern vzor
     * @return seznam prvků šablony, jejich výstupem je text odpovídající vzoru, včetně slov zachycených žolíky
     */
    private List<TemplateElement> copyPattern(final MixedPattern pattern) {        
        final String patternText = pattern.getText();
        final int patternTextLength = patternText.length();        
        
        final ImmutableList.Builder<TemplateElement> resultBuilder = ImmutableList.builder();
        final StringBuilder stringBuilder = new StringBuilder();
        int starIndex = 0;
        for (int characterIndex = 0; characterIndex < patternTextLength; characterIndex++) {
            final char character = patternText.charAt(characterIndex);
            
            if (isWildcardCharacter(character)) {
                if (stringBuilder.length() > 0) {
                    resultBuilder.add(Text.create(stringBuilder.toString()));
                }
                stringBuilder.setLength(0);
                
                resultBuilder.add(Star.create(new AIMLIndex(starIndex++)));
            } else {
                stringBuilder.append(character);
            }
        }
        if (stringBuilder.length() > 0) {
            resultBuilder.add(Text.create(stringBuilder.toString()));
        }
        
        return resultBuilder.build();
    }

    private static boolean isWildcardCharacter(final char character) {
        return WILDCARD_CHARACTERS.contains(character);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Výsledkem zpracování je téma, které je pojmenováno po hraně a které obsahuje jednu kategorii. V té je kód šablony, který porovná hodnotu predikátu s očekávanou a podle toho přidá cíl hrany na zásobník či nikoli.</p> 
     */
    @Override
    public List<Topic> process(final PredicateTestArc arc) {        
        Preconditions.checkNotNull(arc);
        
        final TemplateElement popArc = Stack.pop();
        final TemplateElement doCode = RawContent.create(arc.getCode().getText());
        final TemplateElement pushTarget = Stack.popAndPushWords(arc.getTo().getName());
        
        final ValueOnlyListItem success = ValueOnlyListItem.create(arc.getValue(), doCode, pushTarget);
        final DefaultListItem fail = DefaultListItem.create(popArc);
        
        final TemplateElement choose = SinglePredicateCondition.create(arc.getPredicateName(), fail, success);
        final TemplateElement pass = Sr.create();
        
        final List<TemplateElement> code = ImmutableList.of(choose, pass);
        
        return ImmutableList.of(createArcTopic(arc, code));
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>Výsledkem zpracování je téma, které je pojmenováno po hraně a které obsahuje jednu kategorii. V té je kód šablony, který porovná výstup testovaného kódu s očekávanou hodnotou a podle toho přidá cíl hrany na zásobník či nikoli.</p> 
     */
    @Override
    public List<Topic> process(final CodeTestArc arc) {
        Preconditions.checkNotNull(arc);
        
        final TemplateElement doTested = RawContent.create(arc.getTested().getText());
        
        final TemplateElement saveResult = Set.create(this.testingPredicate, doTested);
        
        final TemplateElement hideResult = Think.create(saveResult);
        final TemplateElement popArc = Stack.pop();
        final TemplateElement doCode = RawContent.create(arc.getCode().getText());
        final TemplateElement pushTarget = Stack.popAndPushWords(arc.getTo().getName());
        
        final ValueOnlyListItem success = ValueOnlyListItem.create(arc.getValue(), doCode, pushTarget);
        final DefaultListItem fail = DefaultListItem.create(popArc);
        
        final TemplateElement choose = SinglePredicateCondition.create(this.testingPredicate, fail, success);
        final TemplateElement clear = Set.create(this.testingPredicate, Text.empty());
        
        final TemplateElement hideClear = Think.create(clear);
        final TemplateElement pass = Sr.create();
        
        List<TemplateElement> code = ImmutableList.of(hideResult, choose, hideClear, pass);
        
        return ImmutableList.of(createArcTopic(arc, code));
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Výsledkem zpracování jsou dvě témata. První téma zavede cíl zanoření na zásobník, na který před tím uložilo jako návratovou hodnotu název druhého tématu, které se pak provede v případě úspěšného průchodu podsítí.</p> 
     */
    @Override
    public List<Topic> process(final RecurentArc arc) {
        Preconditions.checkNotNull(arc);
        
        final TemplateElement doCode = RawContent.create(arc.getCode().getText());
        final TemplateElement pushTarget = Stack.popAndPushWords(arc.getTo().getName());
        final TemplateElement dive = Stack.popAndPushWords(arc.getTarget().getName(), this.returnState, arc.getName());
        final TemplateElement pass = Sr.create();
        
        final List<TemplateElement> diveCode = ImmutableList.of(dive, pass);
        final List<TemplateElement> successCode = ImmutableList.of(doCode, pushTarget, pass);
        
        return ImmutableList.of(
                createArcTopic(arc, diveCode),
                Stack.createState(this.successState, Category.createUniversal(Template.create(successCode)))
        );
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Výsledkem zpracování jsou dvě témata. První téma zavede cíl zanoření na zásobník, na který před tím uložilo jako návratovou hodnotu název druhého tématu, které se pak provede v případě úspěšného průchodu podsítí.</p> 
     */
    @Override
    public List<Topic> process(final TransitionArc arc) {
        final TemplateElement doCode = RawContent.create(arc.getCode().getText());
        final TemplateElement pushTarget = Stack.popAndPushWords(arc.getTo().getName());
        final TemplateElement pass = Sr.create();
        
        final List<TemplateElement> code = ImmutableList.of(doCode, pushTarget, pass);
        
        return ImmutableList.of(createArcTopic(arc, code));
    }
    
    private static Topic createArcTopic(final Arc arc, final List<TemplateElement> code) {
        return Stack.createState(arc.getName(), Category.createUniversal(Template.create(code)));
    }
    
    private static Topic createArcTopic(final Arc arc, final Category... categories) {
        return Stack.createState(arc.getName(), categories);
    }
}