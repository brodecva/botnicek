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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.translation.utils;

import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.implementations.Set;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.implementations.Think;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.elements.template.implementations.Topicstar;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.PredicateNames;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public final class Stack {
    private static final Joiner WORDS_JOINER = Joiner.on(AIML.WORD_DELIMITER.getValue());
        
    private Stack() {
    }

    public static TemplateElement push(final String word) {
        final String delimiter = AIML.WORD_DELIMITER.getValue();
        
        Preconditions.checkArgument(!word.contains(delimiter));
        
        return push(Text.create(word), Text.create(delimiter));
    }
    
    public static TemplateElement push(final TemplateElement... content) {
        return push(ImmutableList.copyOf(content));
    }
    
    public static TemplateElement push(final List<TemplateElement> content) {
        return update(content, ImmutableList.copyOf(new TemplateElement[] { Topicstar.create() }));
    }
    
    public static TemplateElement update(final String content, final List<TemplateElement> oldStack) {
        final String delimiter = AIML.WORD_DELIMITER.getValue();
        final List<TemplateElement> pushedList = ImmutableList.copyOf(new TemplateElement[] { Text.create(content + delimiter) });
        
        return update(pushedList, oldStack);
    }
    
    public static TemplateElement update(final List<TemplateElement> pushed, final List<TemplateElement> old) {
        final Builder<TemplateElement> newStackBuilder = ImmutableList.builder();
        newStackBuilder.addAll(pushed);
        newStackBuilder.addAll(old);
        
        return set(newStackBuilder.build());
    }


    public static TemplateElement set(TemplateElement... content) {
        return set(ImmutableList.copyOf(content));
    }
    
    public static TemplateElement set(final List<TemplateElement> content) {
        final Set update = Set.create(PredicateNames.of(AIML.TOPIC.getValue()), ImmutableList.copyOf(content));
        final Think hide = Think.create(update);
        
        return hide;
    }

    public static String joinPushed(final List<String> words) {
        final String result = WORDS_JOINER.join(words);
        return result;
    }
    
    public static boolean allDifferent(String... statesNames) {
        return ImmutableSet.of(statesNames).size() == statesNames.length;
    }
}
