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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.RenderingVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.IntegrationTest;

/**
 * Testuje knihovnu tématy pro vyhodnocení úspěchu zanoření do podsítě a průchodu celým systémem.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Recursion
 */
@Category(IntegrationTest.class)
public class RecursionTest {

    private static final String PULL = "PULL";
    private static final String PULLSTOP = "PULLSTOP";
    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";
    private static final String RETURN = "RETURN";
    
    private RenderingVisitor renderer = Intended.nullReference();

    /**
     * Inicializuje vykreslovač.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.renderer = DefaultRenderingVisitor.create(Settings.getDefault().getNamespacesToPrefixes());
    }

    /**
     * Uklidí vykreslovač.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        renderer = Intended.nullReference();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.compile.library.Recursion#getLibrary(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)}.
     */
    @Test
    public void testGetLibrary() {
        final NormalWord pullState = NormalWords.of(PULL);
        final NormalWord pullStopState = NormalWords.of(PULLSTOP);
        final NormalWord successState = NormalWords.of(SUCCESS);
        final NormalWord failState = NormalWords.of(FAIL);
        final NormalWord returnState = NormalWords.of(RETURN);
        
        AbstractElement.acceptForEach(Recursion.getLibrary(pullState, pullStopState, successState, failState, returnState), this.renderer);
        assertEquals("<topic name=\"" + PULL + " * " + PULLSTOP + " *\">"
            + "<category>"
                + "<pattern>*</pattern>"
                + "<that>*</that>"
                + "<template>"
                    + "<think>"
                        + "<set name=\"TOPIC\">"
                            + PULL + " " + PULLSTOP + " "
                            + "<topicstar index=\"2\"/>"
                        + "</set>"
                    + "</think>"
                    + "<sr/>"
                + "</template>"
            + "</category>"
        + "</topic>"
            
        + "<topic name=\"" + PULL + " " + PULLSTOP + " " + RETURN + " *\">"
            + "<category>"
                + "<pattern>*</pattern>"
                + "<that>*</that>"
                + "<template>"
                    + "<think>"
                        + "<set name=\"TOPIC\">"
                            + SUCCESS + " "
                            + "<topicstar/>"
                        + "</set>"
                    + "</think>"
                    + "<sr/>"
                + "</template>"
            + "</category>"        
        + "</topic>"
        
        + "<topic name=\"" + PULL + " " + PULLSTOP + " *\">"
            + "<category>"
                + "<pattern>*</pattern>"
                + "<that>*</that>"
                + "<template>"
                    + "<think>"
                        + "<set name=\"TOPIC\">"
                            + "<topicstar/>"
                        + "</set>"
                    + "</think>"
                    + "<sr/>"
                + "</template>"
            + "</category>"        
        + "</topic>"
            
        + "<topic name=\"" + PULL + " " + PULLSTOP + "\">"
            + "<category>"
                + "<pattern>*</pattern>"
                + "<that>*</that>"
                + "<template>"
                    + "<think>"
                        + "<set name=\"TOPIC\">"
                            + SUCCESS
                        + "</set>"
                    + "</think>"
                    + "<sr/>"
                + "</template>"
            + "</category>"        
        + "</topic>"
    
        + "<topic name=\"" + PULLSTOP + " " + RETURN + " * *\">"
            + "<category>"
                + "<pattern>*</pattern>"
                + "<that>*</that>"
                + "<template>"
                    + "<think>"
                        + "<set name=\"TOPIC\">"
                            + "<topicstar index=\"2\"/>"
                        + "</set>"
                    + "</think>"
                    + "<sr/>"
                + "</template>"
            + "</category>"        
        + "</topic>"
            
        + "<topic name=\"" + PULLSTOP + " *\">"
            + "<category>"
                + "<pattern>*</pattern>"
                + "<that>*</that>"
                + "<template>"
                    + "<think>"
                        + "<set name=\"TOPIC\">"
                            + "<topicstar/>"
                        + "</set>"
                    + "</think>"
                    + "<sr/>"
                + "</template>"
            + "</category>"        
        + "</topic>"
            
        + "<topic name=\"" + PULLSTOP + "\">"
            + "<category>"
                + "<pattern>*</pattern>"
                + "<that>*</that>"
                + "<template>"
                    + "<think>"
                        + "<set name=\"TOPIC\">"
                            + FAIL
                        + "</set>"
                    + "</think>"
                    + "<sr/>"
                + "</template>"
            + "</category>"        
        + "</topic>"
            
        + "<topic name=\"" + SUCCESS + "\">"
            + "<category>"
                + "<pattern>*</pattern>"
                + "<that>*</that>"
                + "<template/>"
            + "</category>"        
        + "</topic>", this.renderer.getResult());
    }
}
