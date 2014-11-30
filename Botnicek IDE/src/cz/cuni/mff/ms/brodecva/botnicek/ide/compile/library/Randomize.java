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
package cz.cuni.mff.ms.brodecva.botnicek.ide.compile.library;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.Template;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Random;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Srai;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Star;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIMLIndex;

/**
 * <p>
 * Knihovna s tématem pro náhodné uspořádání stavů ze vstupu na zásobník.
 * </p>
 * <p>
 * Na zásobník umístí pouze jednu kopii, ale počet opakování na vstupu ovlivňuje
 * pravděpodobnost výskytu na dané výsledné pozici.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class Randomize {
    private static final String RANDOM_START = "RANDOMSTART";
    private static final String RANDOM_END = "RANDOMEND";
    private static final String REMOVE_START = "REMOVESTART";
    private static final String REMOVE_END = "REMOVEEND";

    private static List<List<TemplateElement>> createChoices(
            final int starsCount) {
        final ImmutableList.Builder<List<TemplateElement>> builder =
                ImmutableList.builder();

        final String space = AIML.WORD_DELIMITER.getValue();
        final String randomStart = RANDOM_START;
        final String randomEnd = RANDOM_END;

        final ImmutableSortedSet<Integer> set =
                ContiguousSet.create(Range.closed(1, starsCount),
                        DiscreteDomain.integers());

        for (int picked = 1; picked <= starsCount; picked++) {
            final Star pickedStar = Star.create(new AIMLIndex(picked));
            final SetView<Integer> rest =
                    Sets.difference(set, ImmutableSet.of(picked));

            // @formatter:off
            // <li><star index="3"/> <srai>RANDOMSTART <srai>REMOVESTART <star index="3"/> <star index="1"/> <star index="2"/> <star index="4"/> REMOVEEND</srai> RANDOMEND</srai><li>
            builder.add(ImmutableList.<TemplateElement>of(
                    pickedStar,
                    Text.create(space),
                    Srai.create(
                            Text.create(randomStart + space),
                            Srai.create(removeCopies(pickedStar, rest)),
                            Text.create(space + randomEnd))));
            // @formatter:on
        }

        return builder.build();
    }

    private static String createStars(final int starsCount) {
        final String[] stars = new String[starsCount];
        Arrays.fill(stars, AIML.STAR_WILDCARD.getValue());

        return Joiner.on(AIML.WORD_DELIMITER.getValue()).join(stars);
    }

    /**
     * Vrátí seznam s jedním tématem (výpočet v jeho rámci je řešen pomocí
     * manipulací vzoru s krajními zarážkami, tudíž není nutně rezervovat další
     * stavy), které při přechodu do něj a uvozením vstupu ze stavů jeho názvem
     * uspořádá náhodně stavy na zásobník (čím více je kopií daného stavu, tím
     * větší má šanci na umístění blíže k vrcholu zásobníku).
     * 
     * @param randomizeState
     *            název stavu pro promíchání
     * @param maxPriority
     *            maximální povolená priorita hrany
     * @param maxBranchFactor
     *            maximální počet odchozích hran z uzlu
     * @param statesNamesAuthority
     *            autorita obsahující všechny použité názvy stavů
     * @return téma
     */
    public static List<Topic> getLibrary(final NormalWord randomizeState,
            final int maxPriority, final int maxBranchFactor,
            final NamingAuthority statesNamesAuthority) {
        Preconditions.checkNotNull(randomizeState);
        Preconditions.checkNotNull(statesNamesAuthority);
        Preconditions.checkArgument(maxPriority >= 0);
        Preconditions.checkArgument(maxBranchFactor >= 0);

        final String randomize = randomizeState.getText();
        final String star = AIML.STAR_WILDCARD.getValue();
        final String space = AIML.WORD_DELIMITER.getValue();

        final ImmutableList.Builder<Category> categories =
                ImmutableList.builder();

        shuffle(maxPriority, maxBranchFactor, randomizeState, categories);
        removeAll(statesNamesAuthority, categories);

        // @formatter:off
        //      <topic name="RANDOMIZE *">
        // @formatter:on
        return ImmutableList.of(Topic.create(
                Patterns.create(Joiner.on(space).join(randomize, star)),
                categories.build()));
    }

    private static String join(final String... parts) {
        return Joiner.on(AIML.WORD_DELIMITER.getValue()).join(parts);
    }

    /**
     * <p>
     * Vytvoří kód pro odebírání všech výskytů prvku ze seznamu (to jsou v tomto
     * případě ostatní míchané prvky).
     * </p>
     * <p>
     * Bohužel predikáty jazyka AIML nelze porovnávat mezi sebou, ale jen vůči
     * vzoru, proto je nutné vytvořit pro každý použitý stav (stačily by ovšem
     * jen odchozí hrany náhodných uzlů!!!) vlastní kopii procedury.
     * </p>
     */
    private static void removeAll(final NamingAuthority statesNamesAuthority,
            final Builder<Category> categories) {
        final java.util.Set<String> allStates =
                statesNamesAuthority.getSnapshot();

        for (final String state : allStates) {
            removeAllForState(state, categories);
        }
    }

    private static void removeAllForState(final String state,
            final Builder<Category> categories) {
        final String space = AIML.WORD_DELIMITER.getValue();
        final String star = AIML.STAR_WILDCARD.getValue();
        final String removeStart = REMOVE_START;
        final String removeEnd = REMOVE_END;

        // @formatter:off
        //    <category>
        //        <pattern>REMOVESTART A A * REMOVEEND</pattern>
        //        <that>*</that>
        //        <template><srai>REMOVESTART A <star/> REMOVEEND</srai></template>
        //    </category>
        categories.add(
                Category.create(
                        Patterns.create(join(removeStart, state, state, star, removeEnd)),
                        Patterns.createUniversal(),
                        Template.create(
                                Srai.create(
                                        Text.create(join(removeStart, state) + space),
                                        Star.create(),
                                        Text.create(space + removeEnd)))));        
        
        //    <category>
        //        <pattern>REMOVESTART A * * REMOVEEND</pattern>
        //        <that>*</that>
        //        <template><star index="1"> <srai>REMOVESTART A <star index="2"> REMOVEEND</srai></template>
        //    </category>
        categories.add(
                Category.create(
                        Patterns.create(join(removeStart, state, star, star, removeEnd)),
                        Patterns.createUniversal(),
                        Template.create(
                                Star.create(new AIMLIndex(1)),
                                Text.create(space),
                                Srai.create(
                                        Text.create(join(removeStart, state) + space),
                                        Star.create(new AIMLIndex(2)),
                                        Text.create(space + removeEnd)))));
        
        //    <category>
        //        <pattern>REMOVESTART A A REMOVEEND</pattern>
        //        <that>*</that>
        //        <template></template>
        //    </category>
        categories.add(
                Category.create(
                        Patterns.create(join(removeStart, state, state, removeEnd)),
                        Patterns.createUniversal(),
                        Template.create()));
        
        //    <category>
        //        <pattern>REMOVESTART A * REMOVEEND</pattern>
        //        <that>*</that>
        //        <template><star/></template>
        //    </category>
        categories.add(
                Category.create(
                        Patterns.create(join(removeStart, state, star, removeEnd)),
                        Patterns.createUniversal(),
                        Template.create(
                                Star.create())));
        // @formatter:on
    }

    private static List<TemplateElement> removeCopies(final Star pickedStar,
            final SetView<Integer> rest) {
        final String space = AIML.WORD_DELIMITER.getValue();
        final String removeStart = REMOVE_START;
        final String removeEnd = REMOVE_END;

        final ImmutableList.Builder<TemplateElement> removeCode =
                ImmutableList.builder();

        // @formatter:off
        // REMOVESTART <star index="3"/> <star index="1"/> <star index="2"/> <star index="4"/> REMOVEEND
        // @formatter:on
        removeCode.add(Text.create(removeStart + space));
        removeCode.add(pickedStar);
        for (final int other : rest) {
            removeCode.add(Text.create(space));
            removeCode.add(Star.create(new AIMLIndex(other)));
        }
        removeCode.add(Text.create(space + removeEnd));

        return removeCode.build();
    }

    /**
     * Vynásobí maximální prioritu a větvící faktor, vytvoří podle výsledku
     * příslušný počet zachytávacích žolíků, při výpočtu se pak díky prvku
     * náhoda vybere jedno zachycené slovo a předá na výstup, to se dále smaže
     * ze zbylých a zbytek se rekurzivně zpracuje.
     */
    private static void shuffle(final int maxPriority,
            final int maxBranchFactor, final NormalWord randomizeState,
            final ImmutableList.Builder<Category> categories) {
        final String randomize = randomizeState.getText();
        final String randomStart = RANDOM_START;
        final String randomEnd = RANDOM_END;
        final String star = AIML.STAR_WILDCARD.getValue();
        final String space = AIML.WORD_DELIMITER.getValue();

        // @formatter:off
        //    <category>
        //        <pattern>RANDOMIZE *</pattern>
        //        <that>*</that>
        //        <template><srai>RANDOMSTART <star/> RANDOMEND</srai></template>
        //    </category>
        categories.add(
                Category.create(
                        Patterns.create(join(randomize, star)),
                        Patterns.createUniversal(),
                        Template.create(
                                        Srai.create(
                                                Text.create(randomStart + space),
                                                Star.create(),
                                                Text.create(space + randomEnd)))));
        
        //    <category>
        //        <pattern>RANDOMSTART RANDOMEND</pattern>
        //        <that>*</that>
        //        <template></template>
        //    </category>
        categories.add(
                Category.create(
                        Patterns.create(join(randomStart, randomEnd)),
                        Patterns.createUniversal(),
                        Template.create()));
        
        //    <category>
        //        <pattern>RANDOMSTART * RANDOMEND</pattern>
        //        <that>*</that>
        //        <template><star/></template>
        //    </category>
        categories.add(
                Category.create(
                        Patterns.create(join(randomStart, star, randomEnd)),
                        Patterns.createUniversal(),
                        Template.create(
                                Star.create())));
        
        //    <category>
        //        <pattern>RANDOMSTART * * * * RANDOMEND</pattern>
        //        <that>*</that>
        //        <template><random>
        //            ...
        //            <li><star index="3"/> <srai>RANDOMSTART <srai>REMOVESTART <star index="3"/> <star index="1"/> <star index="2"/> <star index="4"/> REMOVEEND</srai> RANDOMEND</srai><li>
        //            ...
        //            </random></template>
        //    </category>
        //    ...
        final int maxStarsCount = maxPriority * maxBranchFactor;
        for (int starsCount = 2; starsCount <= maxStarsCount; starsCount++) {
            categories.add(
                    Category.create(
                            Patterns.create(join(randomStart, createStars(starsCount), randomEnd)),
                            Patterns.createUniversal(),
                            Template.create(
                                    Random.create(createChoices(starsCount)))));
        }
        // @formatter:on
    }

    private Randomize() {
    }
}
