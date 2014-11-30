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
package cz.cuni.mff.ms.brodecva.botnicek.ide.translate;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.Template;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * Výchozí implementace {@link NodeTopicFactory}. Vytvoří dle definice téma z
 * kanonických součástí.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class DefaultNodeTopicFactory implements NodeTopicFactory {

    /**
     * Vytvoří továrnu.
     * 
     * @return továrna
     */
    public static DefaultNodeTopicFactory create() {
        return new DefaultNodeTopicFactory();
    }

    private DefaultNodeTopicFactory() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.translate.NodeTopicFactory#produce
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node,
     * java.util.List)
     */
    @Override
    public Topic produce(final Node node, final List<? extends TemplateElement> code) {
        Preconditions.checkNotNull(node);
        Preconditions.checkNotNull(code);

        final List<TemplateElement> codeCopy = ImmutableList.copyOf(code);

        return Topic.create(
                Patterns.create(node.getName().getText() + AIML.WORD_DELIMITER
                        + AIML.STAR_WILDCARD),
                Category.createUniversal(Template.create(codeCopy)));
    }

}
