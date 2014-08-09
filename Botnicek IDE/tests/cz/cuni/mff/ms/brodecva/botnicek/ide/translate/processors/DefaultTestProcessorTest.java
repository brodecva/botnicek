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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.RenderingVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.IntegrationTest;

/**
 * @author Václav Brodec
 * @version 1.0
 */
@Category(IntegrationTest.class)
public class DefaultTestProcessorTest {

    private RenderingVisitor renderer = Intended.nullReference();

    /**
     * Vytvoří testovací vykreslovač značek AIML.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.renderer  = DefaultRenderingVisitor.create(Settings.getDefault().getNamespacesToPrefixes());
    }

    /**
     * Uklidí testovací vykreslovač.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.renderer = Intended.nullReference();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor#create(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)}.
     */
    @Test
    public void testCreate() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PatternArc)}.
     */
    @Test
    public void testProcessPatternArc() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PredicateTestArc)}.
     */
    @Test
    public void testProcessPredicateTestArc() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.CodeTestArc)}.
     */
    @Test
    public void testProcessCodeTestArc() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc)}.
     */
    @Test
    public void testProcessRecurentArc() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc)}.
     */
    @Test
    public void testProcessTransitionArc() {
        fail("Not yet implemented"); // TODO
    }

}
