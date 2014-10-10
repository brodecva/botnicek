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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.Template;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Date;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Get;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Random;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.RenderingVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.IntegrationTest;

/**
 * Integrační test pomocných metod pro práci s virtuálním zásobníkem a výchozí
 * implementace návštěvníka generujícího zdrojový kód AIML z objektového modelu
 * dokumentu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Stack
 * @see DefaultRenderingVisitor
 */
@Category(IntegrationTest.class)
public class StackTest {

    private RenderingVisitor renderer = Intended.nullReference();

    /**
     * Vytvoří testovací vykreslovač značek AIML.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.renderer =
                DefaultRenderingVisitor.create(Settings.getDefault()
                        .getNamespacesToPrefixes());
    }

    /**
     * Uklidí testovací vykreslovač.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.renderer = Intended.nullReference();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#concatenate(java.util.List, java.util.List)}
     * .
     */
    @Test
    public void testConcatenateWhenFirstEmptySecondEmpty() {
        Stack.concatenate(ImmutableList.<TemplateElement> of(),
                ImmutableList.<TemplateElement> of()).accept(this.renderer);

        assertEquals("<think><set name=\"TOPIC\"/></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#concatenate(java.util.List, java.util.List)}
     * .
     */
    @Test
    public void testConcatenateWhenFirstEmptySecondNotEmpty() {
        Stack.concatenate(
                ImmutableList.<TemplateElement> of(),
                ImmutableList.<TemplateElement> of(Text.create("THIRD"),
                        Text.create(AIML.WORD_DELIMITER.getValue()),
                        Get.create(NormalWords.of("FOURTH")))).accept(
                this.renderer);

        assertEquals(
                "<think><set name=\"TOPIC\">THIRD <get name=\"FOURTH\"/></set></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#concatenate(java.util.List, java.util.List)}
     * .
     */
    @Test
    public void testConcatenateWhenFirstNotEmptySecondEmpty() {
        Stack.concatenate(
                ImmutableList.<TemplateElement> of(Date.create(), Text
                        .create(AIML.WORD_DELIMITER.getValue()), Random
                        .create(ImmutableList.<List<TemplateElement>> of(
                                ImmutableList.<TemplateElement> of(Text
                                        .create("ONE")), ImmutableList
                                        .<TemplateElement> of(Text
                                                .create("TWO"))))),
                ImmutableList.<TemplateElement> of()).accept(this.renderer);

        assertEquals(
                "<think><set name=\"TOPIC\"><date/> <random><li>ONE</li><li>TWO</li></random></set></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#concatenate(java.util.List, java.util.List)}
     * .
     */
    @Test
    public void testConcatenateWhenFirstNotEmptySecondNotEmpty() {
        Stack.concatenate(
                ImmutableList.<TemplateElement> of(Date.create(), Text
                        .create(AIML.WORD_DELIMITER.getValue()), Random
                        .create(ImmutableList.<List<TemplateElement>> of(
                                ImmutableList.<TemplateElement> of(Text
                                        .create("ONE")), ImmutableList
                                        .<TemplateElement> of(Text
                                                .create("TWO"))))),
                ImmutableList.<TemplateElement> of(Text.create("THIRD"),
                        Text.create(AIML.WORD_DELIMITER.getValue()),
                        Get.create(NormalWords.of("FOURTH")))).accept(
                this.renderer);

        assertEquals(
                "<think><set name=\"TOPIC\"><date/> <random><li>ONE</li><li>TWO</li></random> THIRD <get name=\"FOURTH\"/></set></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#createState(List, java.util.List)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public
            void
            testCreateStateListOfNormalWordListOfCategoryWhenWordsEmptyAndCategoriesEmpty() {
        Stack.createState(
                ImmutableList.<NormalWord> of(),
                ImmutableList
                        .<cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category> of())
                .accept(this.renderer);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#createState(List, java.util.List)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public
            void
            testCreateStateListOfNormalWordListOfCategoryWhenWordsEmptyAndCategoriesNotEmpty() {
        Stack.createState(
                ImmutableList.<NormalWord> of(),
                ImmutableList
                        .<cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category> of(
                                cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category
                                        .createUniversal(Template.create(Text
                                                .create("First"))),
                                cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category
                                        .create(Patterns.create("TEST"),
                                                Patterns.create("THATTEST"),
                                                Template.create(Text
                                                        .create("Second")))))
                .accept(this.renderer);
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#createState(List, java.util.List)}
     * .
     */
    @Test
    public
            void
            testCreateStateListOfNormalWordListOfCategoryWhenWordsNotEmptyAndCategoriesEmpty() {
        Stack.createState(
                ImmutableList.of(NormalWords.of("HEAD"),
                        NormalWords.of("OTHER")),
                ImmutableList
                        .<cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category> of())
                .accept(this.renderer);

        assertEquals("<topic name=\"HEAD OTHER *\"/>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#createState(List, java.util.List)}
     * .
     */
    @Test
    public
            void
            testCreateStateListOfNormalWordListOfCategoryWhenWordsNotEmptyAndCategoriesNotEmpty() {
        Stack.createState(
                ImmutableList.of(NormalWords.of("HEAD"),
                        NormalWords.of("OTHER")),
                ImmutableList
                        .<cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category> of(
                                cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category
                                        .createUniversal(Template.create(Text
                                                .create("First"))),
                                cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category
                                        .create(Patterns.create("TEST"),
                                                Patterns.create("THATTEST"),
                                                Template.create(Text
                                                        .create("Second")))))
                .accept(this.renderer);

        assertEquals(
                "<topic name=\"HEAD OTHER *\"><category><pattern>*</pattern><that>*</that><template>First</template></category><category><pattern>TEST</pattern><that>THATTEST</that><template>Second</template></category></topic>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#joinWithSpaces(java.util.List)}
     * .
     */
    @Test
    public void testJoinWithSpacesWhenEmpty() {
        assertEquals("", Stack.joinWithSpaces(ImmutableList.<NormalWord> of()));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#joinWithSpaces(java.util.List)}
     * .
     */
    @Test
    public void testJoinWithSpacesWhenNotEmpty() {
        assertEquals("FIRST SECOND", Stack.joinWithSpaces(ImmutableList.of(
                NormalWords.of("FIRST"), NormalWords.of("SECOND"))));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#pop()}.
     */
    @Test
    public void testPop() {
        Stack.pop().accept(this.renderer);

        assertEquals("<think><set name=\"TOPIC\"><topicstar/></set></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#prepend(java.util.List, java.util.List)}
     * .
     */
    @Test
    public void testPrependWhenWordsEmptyRestEmpty() {
        Stack.prepend(ImmutableList.<NormalWord> of(),
                ImmutableList.<TemplateElement> of()).accept(this.renderer);

        assertEquals("<think><set name=\"TOPIC\"/></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#prepend(java.util.List, java.util.List)}
     * .
     */
    @Test
    public void testPrependWhenWordsEmptyRestNotEmpty() {
        Stack.prepend(
                ImmutableList.<NormalWord> of(),
                ImmutableList.<TemplateElement> of(Text.create("FIRST"),
                        Text.create(AIML.WORD_DELIMITER.getValue()),
                        Get.create(NormalWords.of("SECOND")))).accept(
                this.renderer);

        assertEquals(
                "<think><set name=\"TOPIC\">FIRST <get name=\"SECOND\"/></set></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#prepend(java.util.List, java.util.List)}
     * .
     */
    @Test
    public void testPrependWhenWordsNotEmptyRestEmpty() {
        Stack.prepend(
                ImmutableList.of(NormalWords.of("FIRST"),
                        NormalWords.of("SECOND")),
                ImmutableList.<TemplateElement> of()).accept(this.renderer);

        assertEquals("<think><set name=\"TOPIC\">FIRST SECOND</set></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#prepend(java.util.List, java.util.List)}
     * .
     */
    @Test
    public void testPrependWhenWordsNotEmptyRestNotEmpty() {
        Stack.prepend(
                ImmutableList.of(NormalWords.of("FIRST"),
                        NormalWords.of("SECOND")),
                ImmutableList.<TemplateElement> of(Text.create("THIRD"),
                        Text.create(AIML.WORD_DELIMITER.getValue()),
                        Get.create(NormalWords.of("FOURTH")))).accept(
                this.renderer);

        assertEquals(
                "<think><set name=\"TOPIC\">FIRST SECOND THIRD <get name=\"FOURTH\"/></set></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#popAndPush(java.util.List)}
     * .
     */
    @Test
    public void testPushListOfTemplateElementWhenEmpty() {
        Stack.popAndPush(ImmutableList.<TemplateElement> of()).accept(
                this.renderer);

        assertEquals("<think><set name=\"TOPIC\"><topicstar/></set></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#popAndPush(java.util.List)}
     * .
     */
    @Test
    public void testPushListOfTemplateElementWhenNotEmpty() {
        Stack.popAndPush(
                ImmutableList.<TemplateElement> of(Text.create("FIRST"),
                        Text.create(AIML.WORD_DELIMITER.getValue()),
                        Get.create(NormalWords.of("SECOND")))).accept(
                this.renderer);

        assertEquals(
                "<think><set name=\"TOPIC\">FIRST <get name=\"SECOND\"/> <topicstar/></set></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#popAndPushWords(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord[])}
     * .
     */
    @Test
    public void testPushWordsWhenEmpty() {
        Stack.popAndPushWords(ImmutableList.<NormalWord> of()).accept(
                this.renderer);

        assertEquals("<think><set name=\"TOPIC\"><topicstar/></set></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#popAndPushWords(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord[])}
     * .
     */
    @Test
    public void testPushWordsWhenNotEmpty() {
        Stack.popAndPushWords(
                ImmutableList.of(NormalWords.of("FIRST"),
                        NormalWords.of("SECOND"))).accept(this.renderer);

        assertEquals(
                "<think><set name=\"TOPIC\">FIRST SECOND <topicstar/></set></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#set(java.util.List)}
     * .
     */
    @Test
    public void testSetListOfTemplateElementWhenEmpty() {
        Stack.set(ImmutableList.<TemplateElement> of()).accept(this.renderer);

        assertEquals("<think><set name=\"TOPIC\"/></think>",
                this.renderer.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack#set(java.util.List)}
     * .
     */
    @Test
    public void testSetListOfTemplateElementWhenNotEmpty() {
        Stack.set(
                ImmutableList.<TemplateElement> of(Date.create(), Text
                        .create(AIML.WORD_DELIMITER.getValue()), Random
                        .create(ImmutableList.<List<TemplateElement>> of(
                                ImmutableList.<TemplateElement> of(Text
                                        .create("ONE")), ImmutableList
                                        .<TemplateElement> of(Text
                                                .create("TWO")))), Text
                        .create(AIML.WORD_DELIMITER.getValue()), Text
                        .create("THIRD"), Text.create(AIML.WORD_DELIMITER
                        .getValue()), Get.create(NormalWords.of("FOURTH"))))
                .accept(this.renderer);

        assertEquals(
                "<think><set name=\"TOPIC\"><date/> <random><li>ONE</li><li>TWO</li></random> THIRD <get name=\"FOURTH\"/></set></think>",
                this.renderer.getResult());
    }

}
