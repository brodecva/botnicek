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
package cz.cuni.mff.ms.brodecva.botnicek.ide.compile.library;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.Template;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Set;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Sr;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Topicstar;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIMLIndex;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.Index;

/**
 * Knihovna s tématy pro vyhodnocení úspěchu zanoření do podsítě a průchodu celým systémem.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class Recursion {
    /**
     * Vrátí témata zajišťující: úklid zásobníku po úspěšném průchodu v zanořené sítí, návrat do původní a přechod na další stavy v zásobníku v případě neúspěchu. 
     * 
     * @param pullState stav pro úklid stavů, které jsme rozbalili, ale kvůli úspěchu je není třeba procházet (umisťuje se po opuštění sítě cílový stavem)
     * @param pullStopState zarážka úklidu (umisťuje se po vejití do sítě vstupním uzlem)
     * @param successState stav indikující úspěšně projití podsítě, v případě jinak prázdného zásobníku pak celého systému
     * @param failState stav indikující neúspěšné ukončení výpočtu
     * @param returnState stav uvozující návratový stav do sítě o úroveň výše při zanoření
     * @return knihovní témata
     */
    public static List<Topic> getLibrary(final NormalWord pullState, final NormalWord pullStopState, final NormalWord successState, final NormalWord failState, final NormalWord returnState) {
        Preconditions.checkNotNull(pullState);
        Preconditions.checkNotNull(pullStopState);
        Preconditions.checkNotNull(failState);
        Preconditions.checkNotNull(returnState);        
        
        final String star = AIML.STAR_WILDCARD.getValue();
        final String space = AIML.WORD_DELIMITER.getValue();
        final String pull = pullState.getText();
        final String pullStop = pullStopState.getText();
        final String reTurn = returnState.getText();
        final String success = successState.getText();
        final String fail = failState.getText();
        final NormalWord topic = NormalWords.of(AIML.TOPIC_PREDICATE.getValue());
        final Index two = new AIMLIndex(2);
        
        final ImmutableList.Builder<Topic> topics = ImmutableList.builder();
        
        // @formatter:off
        // PULL * PULLSTOP * -> PULL PULLSTOP *
        topics.add(Topic.create(
                Patterns.create(join(pull, star, pullStop, star)),
                Category.createUniversal(
                        Template.create(
                                Set.create(
                                        topic,
                                        Text.create(join(pull, pullStop) + space),
                                        Topicstar.create(two)),
                                Sr.create()))));
        
        // PULL PULLSTOP RETURN * -> SUCCESS *
        topics.add(Topic.create(
                Patterns.create(join(pull, pullStop, reTurn, star)),
                Category.createUniversal(
                        Template.create(
                                Stack.popAndPushWords(successState),
                                Sr.create()))));
        
        // PULL PULLSTOP * -> *
        topics.add(Topic.create(
                Patterns.create(join(pull, pullStop, star)),
                Category.createUniversal(
                        Template.create(
                                Set.create(
                                        topic,
                                        Topicstar.create()),
                                Sr.create()))));
        
        // PULL PULLSTOP -> SUCCESS
        topics.add(Topic.create(
                Patterns.create(join(pull, pullStop)),
                Category.createUniversal(
                        Template.create(
                                Set.create(
                                        topic,
                                        Text.create(success)),
                                Sr.create()))));
        
        // PULLSTOP RETURN * * -> 2nd*
        topics.add(Topic.create(
                Patterns.create(join(pullStop, reTurn, star, star)),
                Category.createUniversal(
                        Template.create(
                                Set.create(
                                        topic,
                                        Topicstar.create(two)),
                                Sr.create()))));
        
        // PULLSTOP * -> *
        topics.add(Topic.create(
                Patterns.create(join(pullStop, star)),
                Category.createUniversal(
                        Template.create(
                                Set.create(
                                        topic,
                                        Topicstar.create()),
                                Sr.create()))));
        
        // PULLSTOP ->  FAIL
        topics.add(Topic.create(
                Patterns.create(pullStop),
                Category.createUniversal(
                        Template.create(
                                Set.create(
                                        topic,
                                        Text.create(fail)),
                                Sr.create()))));
        
        // SUCCESS
        topics.add(Topic.create(
                Patterns.create(success),
                Category.createUniversal(
                        Template.create())));
        // @formatter:on
        
        return topics.build();
    }
    
    private static String join(final String... parts) {
        return Joiner.on(AIML.WORD_DELIMITER.getValue()).join(parts);
    }

    private Recursion() {
    }
}
