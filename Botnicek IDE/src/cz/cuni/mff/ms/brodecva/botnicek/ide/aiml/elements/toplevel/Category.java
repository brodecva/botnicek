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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.Pattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.Template;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.That;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.root.Toplevel;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns;

/**
 * <p>
 * Kategorie je prvek, který se může nacházet v rámci tématu, nebo přímo pod
 * kořenovým prvkem dokumentu, pak je jako vzor jejího tématu považován žolík
 * hvězdička.
 * </p>
 * <p>
 * Kategorie je složena ze tří částí:
 * </p>
 * <ul>
 * <li>vzor, který popisuje, pro jaký vstup bude proveden kód šablony (vzor je
 * složený, může obsahovat normální slova, žolíky či značku bot oddělaná
 * mezerami),</li>
 * <li>vzor, který popisuje, pro jaký předchozí výstup robota bude proveden kód
 * šablony (vzor je opět složený, může obsahovat normální slova, žolíky či
 * značku bot oddělaná mezerami),</li>
 * <li>šablona, která popisuje, jak bude vypadat výstup robota a může podstatně
 * ovlivnit další postup zpracování vstupu.
 * </ul>
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-category">http://www.alicebot.org/TR/2011/#section-category</a>
 */
public class Category extends AbstractProperElement implements Toplevel {
    private static final String NAME = "category";

    /**
     * Vytvoří kategorii.
     * 
     * @param pattern
     *            vzor pro uživatelský vstup
     * @param that
     *            vzor pro předchozí výstup robota
     * @param template
     *            šablona
     * @return kategorie, která jestliže na ni přijde řada, bude aktivována pro
     *         jakýkoli vstup
     */
    public static Category create(final MixedPattern pattern,
            final MixedPattern that, final Template template) {
        return new Category(pattern, that, template);
    }

    /**
     * Vytvoří kategorii, jejíž oba vzory tvoří žolíky.
     * 
     * @param template
     *            šablona
     * @return kategorie, která jestliže na ni přijde řada, bude aktivována pro
     *         jakýkoli vstup
     */
    public static Category createUniversal(final Template template) {
        return new Category(Patterns.createUniversal(),
                Patterns.createUniversal(), template);
    }

    private final MixedPattern pattern;

    private final MixedPattern that;

    private final Template template;

    private Category(final MixedPattern pattern, final MixedPattern that,
            final Template template) {
        Preconditions.checkNotNull(pattern);
        Preconditions.checkNotNull(that);
        Preconditions.checkNotNull(template);

        this.pattern = pattern;
        this.that = that;
        this.template = template;
    }

    @Override
    public List<Element> getChildren() {
        return ImmutableList.<Element> of(Pattern.create(this.pattern),
                That.create(this.that), this.template);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.AbstractElement
     * #getName()
     */
    @Override
    public String getLocalName() {
        return NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Category [pattern=" + this.pattern + ", that=" + this.that
                + "]";
    }
}
