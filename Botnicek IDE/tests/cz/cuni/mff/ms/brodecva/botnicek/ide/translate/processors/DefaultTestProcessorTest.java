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

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWords;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.CodeTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PatternArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PredicateTestArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.project.model.Settings;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.render.RenderingVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.IntegrationTest;

/**
 * Testuje výchozí implementaci procesoru hran.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultTestProcessor
 */
@Category(IntegrationTest.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({ PatternArc.class, PredicateTestArc.class, CodeTestArc.class, RecurentArc.class, TransitionArc.class })
public class DefaultTestProcessorTest {

    private static final String RECURENT_ARC_NAME = "RECURENTARC";
    private static final String PATTERN_ARC_NAME = "PATTERNARC"; 
    private static final String PREDICATE_TEST_ARC_NAME = "PREDICATETESTARC";
    private static final String CODE_TEST_ARC_NAME = "CODETESTARC";
    private static final String TRANSITION_ARC_NAME = "TRANSITIONARC";
    
    private static final String TESTED_PREDICATE_NAME = "TESTEDPREDICATENAME";
    private static final String EXPECTED_VALUE_PATTERN = "EXPECTEDVALUEPATTERN";
    private static final String PREPARE_CODE = "Prepare code";
    private static final String TESTED_CODE = "Tested code";
    
    private static final String TARGET_NAME = "TARGET";
    
    private static final String MIXED_PATTERN = "MIXED PATTERN * WITH * WILDCARDS AND <bot name=\"bot\"/>";
    private static final String THAT_PATTERN = "THAT";
    
    private static final String ARC_CODE = "Arc code";
    
    private static final String REFERRED_NAME = "REFERRED";
    
    private RenderingVisitor renderer = Intended.nullReference();
    private DefaultTestProcessor tested = Intended.nullReference();
    private Code arcCodeStub = Intended.nullReference();
    private Node targetStub = Intended.nullReference();
    private Settings settings = Intended.nullReference();

    /**
     * Vytvoří testovací vykreslovač značek AIML a testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.settings = Settings.getDefault();
        
        this.arcCodeStub = EasyMock.createStrictMock(Code.class);
        EasyMock.expect(this.arcCodeStub.getText()).andStubReturn(ARC_CODE);
        EasyMock.replay(this.arcCodeStub);
        
        this.targetStub = EasyMock.createStrictMock(Node.class);
        EasyMock.expect(this.targetStub.getName()).andStubReturn(NormalWords.of(TARGET_NAME));
        EasyMock.replay(this.targetStub);
        
        this.tested = DefaultTestProcessor.create(settings.getTestingPredicate(), settings.getSuccessState(), settings.getReturnState());
        
        this.renderer  = DefaultRenderingVisitor.create(settings.getNamespacesToPrefixes());        
    }

    /**
     * Uklidí testovací vykreslovač a testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.renderer = Intended.nullReference();
        tested = Intended.nullReference();
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor#create(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)}.
     */
    @Test
    public void testCreate() {
        DefaultTestProcessor.create(settings.getTestingPredicate(), settings.getSuccessState(), settings.getReturnState());
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PatternArc)}.
     */
    @Test
    public void testProcessPatternArc() {
        final MixedPattern mixedPatternStub = EasyMock.createStrictMock(MixedPattern.class);
        EasyMock.expect(mixedPatternStub.getText()).andStubReturn(MIXED_PATTERN);
        EasyMock.replay(mixedPatternStub);
        
        final PatternArc patternArcStub = PowerMock.createMock(PatternArc.class);
        EasyMock.expect(patternArcStub.getCode()).andStubReturn(arcCodeStub);
        EasyMock.expect(patternArcStub.getName()).andStubReturn(NormalWords.of(PATTERN_ARC_NAME));
        EasyMock.expect(patternArcStub.getPattern()).andStubReturn(mixedPatternStub);
        EasyMock.expect(patternArcStub.getThat()).andStubReturn(Patterns.create(THAT_PATTERN));
        EasyMock.expect(patternArcStub.getTo()).andStubReturn(targetStub);
        PowerMock.replay(patternArcStub);
        
        AbstractElement.acceptForEach(this.tested.process(patternArcStub), this.renderer);
        assertEquals(
                "<topic name=\"" + PATTERN_ARC_NAME + " *\">"
                + "<category>"
                    + "<pattern>" + MIXED_PATTERN + "</pattern>"
                    + "<that>" + THAT_PATTERN + "</that>"
                    + "<template>"
                        + ARC_CODE
                        + "<think><set name=\"TOPIC\">" + TARGET_NAME + " <topicstar/></set></think>"
                        + "<srai>MIXED PATTERN <star index=\"1\"/> WITH <star index=\"2\"/> WILDCARDS AND <bot name=\"bot\"/></srai>"
                    +"</template>"
                + "</category>"
                + "<category>"
                    + "<pattern>*</pattern>"
                    + "<that>*</that>"
                    + "<template>"
                        + "<think><set name=\"TOPIC\"><topicstar/></set></think>"
                        + "<sr/>"
                    +"</template>"
                + "</category>"
            + "</topic>", this.renderer.getResult());
        
        PowerMock.verify(patternArcStub);
        EasyMock.verify(mixedPatternStub);
        EasyMock.verify(arcCodeStub);
        EasyMock.verify(targetStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.PredicateTestArc)}.
     */
    @Test
    public void testProcessPredicateTestArc() {
        final Code prepareCodeStub = EasyMock.createStrictMock(Code.class);
        EasyMock.expect(prepareCodeStub.getText()).andStubReturn(PREPARE_CODE);
        EasyMock.replay(prepareCodeStub);
        
        final PredicateTestArc predicateTestArcStub = PowerMock.createMock(PredicateTestArc.class);
        EasyMock.expect(predicateTestArcStub.getCode()).andStubReturn(arcCodeStub);
        EasyMock.expect(predicateTestArcStub.getName()).andStubReturn(NormalWords.of(PREDICATE_TEST_ARC_NAME));
        EasyMock.expect(predicateTestArcStub.getPrepareCode()).andStubReturn(prepareCodeStub);
        EasyMock.expect(predicateTestArcStub.getPredicateName()).andStubReturn(NormalWords.of(TESTED_PREDICATE_NAME));
        EasyMock.expect(predicateTestArcStub.getValue()).andStubReturn(Patterns.create(EXPECTED_VALUE_PATTERN));
        EasyMock.expect(predicateTestArcStub.getTo()).andStubReturn(targetStub);
        PowerMock.replay(predicateTestArcStub);
        
        AbstractElement.acceptForEach(this.tested.process(predicateTestArcStub), this.renderer);
        assertEquals(
                "<topic name=\"" + PREDICATE_TEST_ARC_NAME + " *\">"
                + "<category>"
                    + "<pattern>*</pattern>"
                    + "<that>*</that>"
                    + "<template>"
                        + PREPARE_CODE
                        + "<condition name=\"" + TESTED_PREDICATE_NAME + "\">"
                            + "<li value=\"" + EXPECTED_VALUE_PATTERN + "\">"
                                + ARC_CODE
                                + "<think><set name=\"TOPIC\">" + TARGET_NAME + " <topicstar/></set></think>"
                            + "</li>"
                            + "<li>"
                                + "<think><set name=\"TOPIC\"><topicstar/></set></think>"
                            + "</li>"
                        + "</condition>"
                        + "<sr/>"
                    +"</template>"
                + "</category>"
            + "</topic>", this.renderer.getResult());
        
        EasyMock.verify(prepareCodeStub);
        PowerMock.verify(predicateTestArcStub);
        EasyMock.verify(arcCodeStub);
        EasyMock.verify(targetStub);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.CodeTestArc)}.
     */
    @Test
    public void testProcessCodeTestArc() {
        final Code testedCodeStub = EasyMock.createStrictMock(Code.class);
        EasyMock.expect(testedCodeStub.getText()).andStubReturn(TESTED_CODE);
        EasyMock.replay(testedCodeStub);
        
        final CodeTestArc codeTestArcStub = PowerMock.createMock(CodeTestArc.class);
        EasyMock.expect(codeTestArcStub.getCode()).andStubReturn(arcCodeStub);
        EasyMock.expect(codeTestArcStub.getName()).andStubReturn(NormalWords.of(CODE_TEST_ARC_NAME));
        EasyMock.expect(codeTestArcStub.getTested()).andStubReturn(testedCodeStub);
        EasyMock.expect(codeTestArcStub.getValue()).andStubReturn(Patterns.create(EXPECTED_VALUE_PATTERN));
        EasyMock.expect(codeTestArcStub.getTo()).andStubReturn(targetStub);
        PowerMock.replay(codeTestArcStub);
        
        final String testingPredicateName = settings.getTestingPredicate().getText();
        
        AbstractElement.acceptForEach(this.tested.process(codeTestArcStub), this.renderer);
        assertEquals(
                "<topic name=\"" + CODE_TEST_ARC_NAME + " *\">"
                + "<category>"
                    + "<pattern>*</pattern>"
                    + "<that>*</that>"
                    + "<template>"
                        + "<think><set name=\"" + testingPredicateName  + "\">" + TESTED_CODE + "</set></think>"
                        + "<condition name=\"" + testingPredicateName + "\">"
                            + "<li value=\"" + EXPECTED_VALUE_PATTERN + "\">"
                                + ARC_CODE
                                + "<think><set name=\"TOPIC\">" + TARGET_NAME + " <topicstar/></set></think>"
                            + "</li>"
                            + "<li>"
                                + "<think><set name=\"TOPIC\"><topicstar/></set></think>"
                            + "</li>"
                        + "</condition>"
                        + "<think><set name=\"" + testingPredicateName + "\"></set></think>"
                        + "<sr/>"
                    +"</template>"
                + "</category>"
            + "</topic>", this.renderer.getResult());
        
        EasyMock.verify(testedCodeStub);
        PowerMock.verify(codeTestArcStub);
        EasyMock.verify(arcCodeStub);
        EasyMock.verify(targetStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.RecurentArc)}.
     */
    @Test
    public void testProcessRecurentArc() {
        final EnterNode referredNodeStub = EasyMock.createStrictMock(EnterNode.class);
        EasyMock.expect(referredNodeStub.getName()).andStubReturn(NormalWords.of(REFERRED_NAME));
        EasyMock.replay(referredNodeStub);
        
        final RecurentArc recurentArcStub = PowerMock.createMock(RecurentArc.class);
        EasyMock.expect(recurentArcStub.getCode()).andStubReturn(arcCodeStub);
        EasyMock.expect(recurentArcStub.getName()).andStubReturn(NormalWords.of(RECURENT_ARC_NAME));
        EasyMock.expect(recurentArcStub.getTarget()).andStubReturn(referredNodeStub);
        EasyMock.expect(recurentArcStub.getTo()).andStubReturn(targetStub);
        PowerMock.replay(recurentArcStub);
        
        AbstractElement.acceptForEach(this.tested.process(recurentArcStub), this.renderer);
        assertEquals(
                "<topic name=\"" + RECURENT_ARC_NAME + " *\">"
                + "<category>"
                    + "<pattern>*</pattern>"
                    + "<that>*</that>"
                    + "<template>"
                    + "<think><set name=\"TOPIC\">" + REFERRED_NAME
                            + AIML.WORD_DELIMITER
                            + settings.getReturnState().getText()
                            + AIML.WORD_DELIMITER
                            + RECURENT_ARC_NAME
                            + " <topicstar/></set></think>"
                        + "<sr/>"
                    +"</template>"
                + "</category>"
                + "</topic>"
                + "<topic name=\"" + settings.getSuccessState().getText() + AIML.WORD_DELIMITER + RECURENT_ARC_NAME + " *\">"
                + "<category>"
                    + "<pattern>*</pattern>"
                    + "<that>*</that>"
                    + "<template>"
                        + ARC_CODE
                        + "<think><set name=\"TOPIC\">" + TARGET_NAME + " <topicstar/></set></think>"
                        + "<sr/>"
                    +"</template>"
                + "</category>"
            + "</topic>", this.renderer.getResult());
        
        PowerMock.verify(recurentArcStub);
        EasyMock.verify(referredNodeStub);
        EasyMock.verify(arcCodeStub);
        EasyMock.verify(targetStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultTestProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.TransitionArc)}.
     */
    @Test
    public void testProcessTransitionArc() {
        final TransitionArc transitionArcStub = PowerMock.createMock(TransitionArc.class);
        EasyMock.expect(transitionArcStub.getCode()).andStubReturn(arcCodeStub);
        EasyMock.expect(transitionArcStub.getName()).andStubReturn(NormalWords.of(TRANSITION_ARC_NAME));
        EasyMock.expect(transitionArcStub.getTo()).andStubReturn(targetStub);
        PowerMock.replay(transitionArcStub);
        
        AbstractElement.acceptForEach(this.tested.process(transitionArcStub), this.renderer);
        assertEquals(
                "<topic name=\"" + TRANSITION_ARC_NAME + " *\">"
                + "<category>"
                    + "<pattern>*</pattern>"
                    + "<that>*</that>"
                    + "<template>"
                        + ARC_CODE
                        + "<think><set name=\"TOPIC\">" + TARGET_NAME + " <topicstar/></set></think>"
                        + "<sr/>"
                    +"</template>"
                + "</category>"
            + "</topic>", this.renderer.getResult());
        
        PowerMock.verify(transitionArcStub);
        EasyMock.verify(arcCodeStub);
        EasyMock.verify(targetStub);
    }

}
