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

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.collect.ImmutableSortedSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.RenderingVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.IntegrationTest;

/**
 * Testuje knihovnu s tématem pro náhodné uspořádání stavů ze vstupu na zásobník.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Randomize
 */
@Category(IntegrationTest.class)
public class RandomizeTest {

    private static final int TESTABLE_MAX_PRIORITY = 2;
    private static final int TESTABLE_MAX_BRANCH_FACTOR = 2;
    
    private RenderingVisitor renderer = Intended.nullReference();
    private NormalWord randomizeState;

    /**
     * Inicializuje vykreslovač.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        final Settings settings = Settings.getDefault();
        
        this.randomizeState = settings.getRandomizeState();
        
        this.renderer = DefaultRenderingVisitor.create(settings.getNamespacesToPrefixes());
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
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.compile.library.Randomize#getLibrary(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, int, int, cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority)}.
     */
    @Test
    public void testGetLibraryWhenCommonParameters() {
        final NamingAuthority fullStatesNamesAuthorityStub = EasyMock.createStrictMock(NamingAuthority.class);
        EasyMock.expect(fullStatesNamesAuthorityStub.getSnapshot()).andReturn(ImmutableSortedSet.of("FIRST", "SECOND"));
        EasyMock.replay(fullStatesNamesAuthorityStub);
        
        final String randomizeStateName = randomizeState.getText();
        
        AbstractElement.acceptForEach(Randomize.getLibrary(randomizeState, TESTABLE_MAX_PRIORITY, TESTABLE_MAX_BRANCH_FACTOR, fullStatesNamesAuthorityStub), this.renderer);
        assertEquals(
          "<topic name=\"" + randomizeStateName + " *\">"
            + "<category>"
                + "<pattern>" + randomizeStateName + " *</pattern>"
                + "<that>*</that>"
                + "<template><srai>RANDOMSTART <star/> RANDOMEND</srai></template>"
            + "</category>"
                
            + "<category>"
                + "<pattern>RANDOMSTART RANDOMEND</pattern>"
                + "<that>*</that>"
                + "<template/>"
            + "</category>"
                
            + "<category>"
                + "<pattern>RANDOMSTART * RANDOMEND</pattern>"
                + "<that>*</that>"
                + "<template><star/></template>"
            + "</category>"

            + "<category>"
            + "<pattern>RANDOMSTART * * RANDOMEND</pattern>"
            + "<that>*</that>"
            + "<template>"
                + "<random>"
                    + "<li>"
                        + "<star index=\"1\"/> "
                        + "<srai>"
                            + "RANDOMSTART "
                            + "<srai>"
                                + "REMOVESTART "
                                + "<star index=\"1\"/> "
                                + "<star index=\"2\"/>"
                                + " REMOVEEND"
                            + "</srai>"
                            + " RANDOMEND"
                        + "</srai>"
                    + "</li>"
                    + "<li>"
                        + "<star index=\"2\"/> "
                        + "<srai>"
                            + "RANDOMSTART "
                            + "<srai>"
                                + "REMOVESTART "
                                + "<star index=\"2\"/> "
                                + "<star index=\"1\"/>"
                                + " REMOVEEND"
                            + "</srai>"
                            + " RANDOMEND"
                        + "</srai>"
                    + "</li>"
                + "</random>"
            + "</template>"
            + "</category>"            
            
            + "<category>"
            + "<pattern>RANDOMSTART * * * RANDOMEND</pattern>"
            + "<that>*</that>"
            + "<template>"
                + "<random>"
                    + "<li>"
                        + "<star index=\"1\"/> "
                        + "<srai>"
                            + "RANDOMSTART "
                            + "<srai>"
                                + "REMOVESTART "
                                + "<star index=\"1\"/> "
                                + "<star index=\"2\"/> <star index=\"3\"/>"
                                + " REMOVEEND"
                            + "</srai>"
                            + " RANDOMEND"
                        + "</srai>"
                    + "</li>"
                    + "<li>"
                        + "<star index=\"2\"/> "
                        + "<srai>"
                            + "RANDOMSTART "
                            + "<srai>"
                                + "REMOVESTART "
                                + "<star index=\"2\"/> "
                                + "<star index=\"1\"/> <star index=\"3\"/>"
                                + " REMOVEEND"
                            + "</srai>"
                            + " RANDOMEND"
                        + "</srai>"
                    + "</li>"
                    + "<li>"
                        + "<star index=\"3\"/> "
                        + "<srai>"
                            + "RANDOMSTART "
                            + "<srai>"
                                + "REMOVESTART "
                                + "<star index=\"3\"/> "
                                + "<star index=\"1\"/> <star index=\"2\"/>"
                                + " REMOVEEND"
                            + "</srai>"
                            + " RANDOMEND"
                        + "</srai>"
                    + "</li>"
                + "</random>"
            + "</template>"
            + "</category>"
            
            + "<category>"
                + "<pattern>RANDOMSTART * * * * RANDOMEND</pattern>"
                + "<that>*</that>"
                + "<template>"
                    + "<random>"
                        + "<li>"
                            + "<star index=\"1\"/> "
                            + "<srai>"
                                + "RANDOMSTART "
                                + "<srai>"
                                    + "REMOVESTART "
                                    + "<star index=\"1\"/> "
                                    + "<star index=\"2\"/> <star index=\"3\"/> <star index=\"4\"/>"
                                    + " REMOVEEND"
                                + "</srai>"
                                + " RANDOMEND"
                            + "</srai>"
                        + "</li>"
                        + "<li>"
                            + "<star index=\"2\"/> "
                            + "<srai>"
                                + "RANDOMSTART "
                                + "<srai>"
                                    + "REMOVESTART "
                                    + "<star index=\"2\"/> "
                                    + "<star index=\"1\"/> <star index=\"3\"/> <star index=\"4\"/>"
                                    + " REMOVEEND"
                                + "</srai>"
                                + " RANDOMEND"
                            + "</srai>"
                        + "</li>"
                        + "<li>"
                            + "<star index=\"3\"/> "
                            + "<srai>"
                                + "RANDOMSTART "
                                + "<srai>"
                                    + "REMOVESTART "
                                    + "<star index=\"3\"/> "
                                    + "<star index=\"1\"/> <star index=\"2\"/> <star index=\"4\"/>"
                                    + " REMOVEEND"
                                + "</srai>"
                                + " RANDOMEND"
                            + "</srai>"
                        + "</li>"
                        + "<li>"
                            + "<star index=\"4\"/> "
                            + "<srai>"
                                + "RANDOMSTART "
                                + "<srai>"
                                    + "REMOVESTART "
                                    + "<star index=\"4\"/> "
                                    + "<star index=\"1\"/> <star index=\"2\"/> <star index=\"3\"/>"
                                    + " REMOVEEND"
                                + "</srai>"
                                + " RANDOMEND"
                            + "</srai>"
                        + "</li>"
                    + "</random>"
                + "</template>"
            + "</category>"

            
            + "<category>"
                + "<pattern>REMOVESTART FIRST FIRST * REMOVEEND</pattern>"
                + "<that>*</that>"
                + "<template>"
                    + "<srai>"
                        + "REMOVESTART FIRST <star/> REMOVEEND"
                    + "</srai>"
                + "</template>"
            + "</category>"

            + "<category>"
                + "<pattern>REMOVESTART FIRST * * REMOVEEND</pattern>"
                + "<that>*</that>"
                + "<template><star index=\"1\"/> <srai>REMOVESTART FIRST <star index=\"2\"/> REMOVEEND</srai></template>"
            + "</category>"

            + "<category>"
                + "<pattern>REMOVESTART FIRST FIRST REMOVEEND</pattern>"
                + "<that>*</that>"
                + "<template/>"
            + "</category>"

            + "<category>"
                + "<pattern>REMOVESTART FIRST * REMOVEEND</pattern>"
                + "<that>*</that>"
                + "<template><star/></template>"
            + "</category>"            

            + "<category>"
                + "<pattern>REMOVESTART SECOND SECOND * REMOVEEND</pattern>"
                + "<that>*</that>"
                + "<template>"
                    + "<srai>"
                        + "REMOVESTART SECOND <star/> REMOVEEND"
                    + "</srai>"
                + "</template>"
            + "</category>"

            + "<category>"
                + "<pattern>REMOVESTART SECOND * * REMOVEEND</pattern>"
                + "<that>*</that>"
                + "<template><star index=\"1\"/> <srai>REMOVESTART SECOND <star index=\"2\"/> REMOVEEND</srai></template>"
            + "</category>"
    
            + "<category>"
                + "<pattern>REMOVESTART SECOND SECOND REMOVEEND</pattern>"
                + "<that>*</that>"
                + "<template/>"
            + "</category>"
    
            + "<category>"
                + "<pattern>REMOVESTART SECOND * REMOVEEND</pattern>"
                + "<that>*</that>"
                + "<template><star/></template>"
            + "</category>"
        
        + "</topic>" , this.renderer.getResult());
        
        EasyMock.verify(fullStatesNamesAuthorityStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.compile.library.Randomize#getLibrary(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, int, int, cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.NamingAuthority)}.
     */
    @Test
    public void testGetLibraryWhenZeroAndEmptyParameters() {
        final NamingAuthority emptyStatesNamesAuthorityStub = EasyMock.createStrictMock(NamingAuthority.class);
        EasyMock.expect(emptyStatesNamesAuthorityStub.getSnapshot()).andReturn(ImmutableSortedSet.<String>of());
        EasyMock.replay(emptyStatesNamesAuthorityStub);
        
        final String randomizeStateName = randomizeState.getText();
        
        AbstractElement.acceptForEach(Randomize.getLibrary(randomizeState, 0, 0, emptyStatesNamesAuthorityStub), this.renderer);
        assertEquals(
          "<topic name=\"" + randomizeStateName + " *\">"
            + "<category>"
                + "<pattern>" + randomizeStateName + " *</pattern>"
                + "<that>*</that>"
                + "<template><srai>RANDOMSTART <star/> RANDOMEND</srai></template>"
            + "</category>"
                
            + "<category>"
                + "<pattern>RANDOMSTART RANDOMEND</pattern>"
                + "<that>*</that>"
                + "<template/>"
            + "</category>"
                
            + "<category>"
                + "<pattern>RANDOMSTART * RANDOMEND</pattern>"
                + "<that>*</that>"
                + "<template><star/></template>"
            + "</category>"        
        + "</topic>" , this.renderer.getResult());
        
        EasyMock.verify(emptyStatesNamesAuthorityStub);
    }

}
