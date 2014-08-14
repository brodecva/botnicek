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

import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Set;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Think;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Topicstar;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * <p>Pomocné metody pro práci s virtuálním zásobníkem, který je implementován pomocí porovnání vzorů vůči tématu.</p>
 * <p>Odebrání vrcholu je realizováno pomocí porovnání vzoru "VRCHOL *" vůči aktuálnímu tématu, a uložení části zachycené hvězdičkou jakožto nového tématu</p>
 * <p>Přidání vrcholu je realizováno uložením konkatenace nového vrcholu a aktuálního tématu jakožto nového tématu.</p>
 * <p>Metody slouží pouze ke konstrukci modifikačního kódu a neověřují, zda-li výstup parametrů vytvoří validní strukturu zásobníku v proměnné tématu.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a href="http://www.alicebot.org/TR/2011/#section-topic">http://www.alicebot.org/TR/2011/#section-topic</a>
 * @see <a href="http://www.alicebot.org/TR/2011/#section-aiml-pattern-matching">http://www.alicebot.org/TR/2011/#section-aiml-pattern-matching</a>
 */
public final class Stack {
    
    private static Joiner NORMAL_WORDS_JOINER = Joiner.on(AIML.WORD_DELIMITER.getValue());
    
    private Stack() {
    }

    /**
     * Vrátí strukturu, která odebere vrchol zásobníku, pokud je provedena. Očekává, že zbytek zásobníku je zachycen žolíkem hvězdička.
     * 
     * @return strom prvků šablony, který provede odebrání vrcholu zásobníku
     */
    public static TemplateElement pop() {
        return Stack.set(Topicstar.create());
    }
    
    /**
     * Vrátí strukturu, která přidá slova na uvolněný vrchol zásobníku, pokud je provedena.
     * 
     * @param words normální slova
     * @return strom prvků šablony, který provede přidání slov na zásobník
     */
    public static TemplateElement popAndPushWords(final NormalWord... words) {
        Preconditions.checkNotNull(words);
        
        return popAndPushWords(ImmutableList.copyOf(words));
    }
    
    /**
     * Vrátí strukturu, která přidá slova na uvolněný vrchol zásobníku, pokud je provedena.
     * 
     * @param words normální slova
     * @return strom prvků šablony, který provede přidání slov na zásobník
     */
    public static TemplateElement popAndPushWords(final List<NormalWord> words) {
        Preconditions.checkNotNull(words);
        
        final ImmutableList<NormalWord> wordsCopy = ImmutableList.copyOf(words);
        
        if (wordsCopy.isEmpty()) {
            return popAndPush(ImmutableList.<TemplateElement>of());
        } else {
            return popAndPush(Text.create(joinWithSpaces(wordsCopy)));
        }
    }
    
    /**
     * Spojí slova oddělovačem slov ve vzoru.
     * 
     * @param words normální slova
     * @return řetězec z normálních slov pospojovaných oddělovačem slov
     */
    public static String joinWithSpaces(final List<NormalWord> words) {
        Preconditions.checkNotNull(words);
        
        final List<NormalWord> wordsCopy = ImmutableList.copyOf(words);
        
        return NORMAL_WORDS_JOINER.join(Collections2.transform(wordsCopy, new Function<NormalWord, String>() {
            @Override
            public String apply(final NormalWord input) {
                return input.getText();
            }
        }));
    }

    /**
     * Vrátí strukturu, která přidá výsledek zpracování prvků šablony na uvolněný vrchol zásobníku, pokud je provedena.
     * 
     * @param content prvky, jež se mají zpracovat a výsledek uložit na zásobník
     * @return strom prvků šablony, který provede přidání výsledku na zásobník
     */
    public static TemplateElement popAndPush(final TemplateElement... content) {
        Preconditions.checkNotNull(content);
        
        return popAndPush(ImmutableList.copyOf(content));
    }
    
    /**
     * Vrátí strukturu, která přidá výsledek zpracování prvků šablony na vrchol zásobníku, pokud je provedena.
     * 
     * @param content prvky, jež se mají zpracovat a výsledek uložit na zásobník
     * @return strom prvků šablony, který provede přidání výsledku na zásobník
     */
    public static TemplateElement popAndPush(final List<TemplateElement> content) {
        Preconditions.checkNotNull(content);
        
        return concatenate(ImmutableList.copyOf(content), ImmutableList.<TemplateElement>of(Topicstar.create()));
    }
    
    /**
     * Vrátí strukturu, která při provedení nastaví zásobník podle parametrů.
     * 
     * @param words nová slova na zásobník
     * @param rest zbytek prvků, jejichž vyhodnocení bude nastaveno do zásobníku
     * @return strom prvků šablony, který provede nastavení zásobníku
     */
    public static TemplateElement prepend(final List<NormalWord> words, final List<TemplateElement> rest) {
        Preconditions.checkNotNull(words);
        Preconditions.checkNotNull(rest);
        
        final List<NormalWord> contentCopy = ImmutableList.copyOf(words);
        
        if (contentCopy.isEmpty()) {
            return concatenate(ImmutableList.<TemplateElement>of(), rest);
        } else {
            final List<TemplateElement> pushedList = ImmutableList.<TemplateElement>of(Text.create(joinWithSpaces(contentCopy)));
            return concatenate(pushedList, rest);
        }
    }
    
    /**
     * Vrátí strukturu, která při provedení nastaví zásobník podle parametrů.
     * 
     * @param first prvky, jejichž výstup bude přidán na začátek zásobníku
     * @param second prvky, jejichž výstup bude přidán za první, oddělené oddělovačem slov
     * @return strom prvků šablony, který provede nastavení zásobníku
     */
    public static TemplateElement concatenate(final List<TemplateElement> first, final List<TemplateElement> second) {
        final Builder<TemplateElement> newStackBuilder = ImmutableList.builder();
        if (!first.isEmpty()) {
            newStackBuilder.addAll(first);
        }
        if (!first.isEmpty() && !second.isEmpty()) {
            newStackBuilder.add(Text.create(AIML.WORD_DELIMITER.getValue()));
        }
        if (!second.isEmpty()) {
            newStackBuilder.addAll(second);
        }
        
        return set(newStackBuilder.build());
    }


    /**
     * Nastaví zásobníkem při provedení výstupem prvků.
     * 
     * @param content prvky k vytvoření obsahu zásobníku
     * @return strom prvků, který nastaví zásobník
     */
    public static TemplateElement set(TemplateElement... content) {
        Preconditions.checkNotNull(content);
        
        return set(ImmutableList.copyOf(content));
    }
    
    /**
     * Nastaví zásobníkem při provedení výstupem prvků.
     * 
     * @param content prvky k vytvoření obsahu zásobníku
     * @return strom prvků, který nastaví zásobník
     */
    public static TemplateElement set(final List<TemplateElement> content) {
        Preconditions.checkNotNull(content);
        
        final Set update = Set.create(NormalWords.of(AIML.TOPIC_PREDICATE.getValue()), ImmutableList.copyOf(content));
        final Think hide = Think.create(update);
        
        return hide;
    }
    
    /**
     * Vytvoří téma, jež bude voláno při stavu zásobníku, ve kterém se nachází název tématu na vrcholu.
     * 
     * @param name název tématu
     * @param categories kategorie tématu
     * @return téma stavu
     */
    public static Topic createState(final NormalWord name, final Category... categories) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(categories);
        
        return createState(ImmutableList.of(name), ImmutableList.copyOf(categories));
    }
    
    /**
     * Vytvoří téma, jež bude voláno při stavu zásobníku, ve kterém se nachází složený název tématu na vrcholu.
     * 
     * @param composedName složený název tématu
     * @param categories kategorie tématu
     * @return téma stavu
     */
    public static Topic createState(final List<NormalWord> composedName, final List<Category> categories) {
        Preconditions.checkNotNull(composedName);
        Preconditions.checkNotNull(categories);
        
        final List<NormalWord> namesCopy = ImmutableList.copyOf(composedName);
        Preconditions.checkArgument(!composedName.isEmpty());
        
        return Topic.create(Patterns.create(Stack.joinWithSpaces(namesCopy) + AIML.WORD_DELIMITER + AIML.STAR_WILDCARD), ImmutableList.copyOf(categories));
    }
}
