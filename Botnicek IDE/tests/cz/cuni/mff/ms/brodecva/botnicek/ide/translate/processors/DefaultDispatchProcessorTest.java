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

import java.util.Collections;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Srai;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.OrderedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RandomNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.Stack;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * Testuje výchozí implementaci procesoru, která vytváří kód pro (ne)deterministický přechod do dalších stavů.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultDispatchProcessor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Priority.class, Stack.class, Text.class, Srai.class })
public class DefaultDispatchProcessorTest {

    private static final String ORDERED_STACK_NAMES = "SECOND FIRST";

    private static final String MULTIPLIED_STACK_NAMES = "SECOND SECOND SECOND SECOND SECOND FIRST FIRST";

    private static final int LOW_PRIORITY_VALUE = 2;

    private static final int HIGH_PRIORITY_VALUE = 5;

    private DefaultDispatchProcessor tested = Intended.nullReference();
    
    private NormalWord randomizeStateStub = Intended.nullReference();

    private NormalWord firstOutNameStub = Intended.nullReference();

    private Priority firstOutPriorityStub = Intended.nullReference();

    private Arc firstOutStub = Intended.nullReference();

    private NormalWord prioritizedOutNameStub = Intended.nullReference();

    private Priority prioritizedOutPriorityStub = Intended.nullReference();

    private Arc prioritizedOutStub = Intended.nullReference();

    private OrderedNode orderedNodeStub = Intended.nullReference();

    private Text orderedStatesTextDummy = Intended.nullReference();

    private RandomNode randomNodeStub = Intended.nullReference();

    private TemplateElement enterRandomizeStateDummy = Intended.nullReference();

    private Text randomizeDirectiveTextDummy = Intended.nullReference();

    private Text multipliedStatesTextDummy = Intended.nullReference();

    private Srai randomizeDirectiveDummy = Intended.nullReference();

    private ExitNode exitNodeStub = Intended.nullReference();

    private IsolatedNode isolatedNodeStub = Intended.nullReference();

    /**
     * Nastaví testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        PowerMock.mockStatic(Stack.class);
        PowerMock.mockStatic(Text.class);
        PowerMock.mockStatic(Srai.class);
        
        randomizeStateStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(randomizeStateStub.getText()).andStubReturn("RANDOMIZE");
        EasyMock.replay(randomizeStateStub);
        
        firstOutNameStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(firstOutNameStub.getText()).andStubReturn("FIRST");
        EasyMock.replay(firstOutNameStub);
        
        firstOutPriorityStub = PowerMock.createStrictMock(Priority.class);
        EasyMock.expect(firstOutPriorityStub.getValue()).andStubReturn(LOW_PRIORITY_VALUE);
        EasyMock.replay(firstOutPriorityStub);
        
        firstOutStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(firstOutStub.getName()).andStubReturn(firstOutNameStub);
        EasyMock.expect(firstOutStub.getPriority()).andStubReturn(firstOutPriorityStub);
        EasyMock.replay(firstOutStub);
        
        prioritizedOutNameStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(prioritizedOutNameStub.getText()).andStubReturn("FIRST");
        EasyMock.replay(prioritizedOutNameStub);
        
        prioritizedOutPriorityStub = PowerMock.createStrictMock(Priority.class);
        EasyMock.expect(prioritizedOutPriorityStub.getValue()).andStubReturn(HIGH_PRIORITY_VALUE);
        EasyMock.replay(prioritizedOutPriorityStub);
        
        prioritizedOutStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(prioritizedOutStub.getName()).andStubReturn(prioritizedOutNameStub);
        EasyMock.expect(prioritizedOutStub.getPriority()).andStubReturn(prioritizedOutPriorityStub);
        EasyMock.replay(prioritizedOutStub);
        
        orderedStatesTextDummy = PowerMock.createStrictMock(Text.class);
        PowerMock.replay(orderedStatesTextDummy);
        
        orderedNodeStub = EasyMock.createStrictMock(OrderedNode.class);
        randomNodeStub = EasyMock.createStrictMock(RandomNode.class);
        exitNodeStub = EasyMock.createStrictMock(ExitNode.class);
        isolatedNodeStub = EasyMock.createStrictMock(IsolatedNode.class);
        
        enterRandomizeStateDummy = EasyMock.createStrictMock(TemplateElement.class);
        EasyMock.replay(enterRandomizeStateDummy);
        
        randomizeDirectiveTextDummy = PowerMock.createStrictMock(Text.class);
        EasyMock.replay(randomizeDirectiveTextDummy);
        
        multipliedStatesTextDummy = PowerMock.createStrictMock(Text.class);
        EasyMock.replay(multipliedStatesTextDummy);
        
        randomizeDirectiveDummy = PowerMock.createStrictMock(Srai.class);
        EasyMock.replay(randomizeDirectiveDummy);
        
        this.tested = DefaultDispatchProcessor.create(randomizeStateStub);
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.randomizeStateStub = Intended.nullReference();
        
        firstOutNameStub = Intended.nullReference();
        firstOutPriorityStub = Intended.nullReference();
        firstOutStub = Intended.nullReference();

        prioritizedOutNameStub = Intended.nullReference();
        prioritizedOutPriorityStub = Intended.nullReference();
        prioritizedOutStub = Intended.nullReference();
                
        orderedNodeStub = Intended.nullReference();
        randomNodeStub = Intended.nullReference();
        exitNodeStub = Intended.nullReference();
        isolatedNodeStub = Intended.nullReference();
        
        orderedStatesTextDummy = Intended.nullReference();
        enterRandomizeStateDummy = Intended.nullReference();
        randomizeDirectiveTextDummy = Intended.nullReference();
        multipliedStatesTextDummy = Intended.nullReference();
        
        randomizeDirectiveDummy = Intended.nullReference();
        
        this.tested = Intended.nullReference();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultDispatchProcessor#create(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)}.
     */
    @Test
    public void testCreate() {
        DefaultDispatchProcessor.create(randomizeStateStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultDispatchProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.OrderedNode)}.
     */
    @Test
    public void testProcessOrderedNode() {
        EasyMock.expect(orderedNodeStub.getOuts()).andStubReturn(ImmutableSet.of(firstOutStub, prioritizedOutStub));
        EasyMock.replay(orderedNodeStub);
        
        EasyMock.expect(Stack.joinWithSpaces(ImmutableList.of(prioritizedOutNameStub, firstOutNameStub))).andStubReturn(ORDERED_STACK_NAMES);
        PowerMock.replay(Stack.class);
        
        EasyMock.expect(Text.create(ORDERED_STACK_NAMES)).andStubReturn(orderedStatesTextDummy);
        PowerMock.replay(Text.class);
        
        assertEquals(ImmutableList.of(orderedStatesTextDummy), this.tested.process(orderedNodeStub));
        
        PowerMock.verify(Stack.class);
        PowerMock.verify(Text.class);
        EasyMock.verify(orderedStatesTextDummy);
        EasyMock.verify(orderedNodeStub);
        EasyMock.verify(firstOutNameStub);
        EasyMock.verify(firstOutPriorityStub);
        EasyMock.verify(firstOutStub);
        EasyMock.verify(prioritizedOutNameStub);
        EasyMock.verify(prioritizedOutPriorityStub);
        EasyMock.verify(prioritizedOutStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultDispatchProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RandomNode)}.
     */
    @Test
    public void testProcessRandomNode() {
        EasyMock.expect(randomNodeStub.getOuts()).andStubReturn(ImmutableSet.of(firstOutStub, prioritizedOutStub));
        EasyMock.replay(randomNodeStub);
        
        EasyMock.expect(Stack.popAndPushWords(randomizeStateStub)).andStubReturn(enterRandomizeStateDummy);
        EasyMock.expect(Stack.joinWithSpaces(ImmutableList.copyOf(Iterables.concat(Collections.nCopies(LOW_PRIORITY_VALUE, firstOutNameStub), Collections.nCopies(HIGH_PRIORITY_VALUE, prioritizedOutNameStub))))).andStubReturn(MULTIPLIED_STACK_NAMES);
        PowerMock.replay(Stack.class);
        
        EasyMock.expect(Text.create("RANDOMIZE ")).andStubReturn(randomizeDirectiveTextDummy);
        EasyMock.expect(Text.create(MULTIPLIED_STACK_NAMES)).andStubReturn(multipliedStatesTextDummy);
        PowerMock.replay(Text.class);
        
        EasyMock.expect(Srai.create(randomizeDirectiveTextDummy, multipliedStatesTextDummy)).andStubReturn(randomizeDirectiveDummy);
        PowerMock.replay(Srai.class);
        
        assertEquals(ImmutableList.of(enterRandomizeStateDummy, randomizeDirectiveDummy), this.tested.process(randomNodeStub));
        
        PowerMock.verify(Stack.class);
        PowerMock.verify(Text.class);
        PowerMock.verify(Srai.class);
        EasyMock.verify(enterRandomizeStateDummy);
        EasyMock.verify(multipliedStatesTextDummy);
        EasyMock.verify(randomizeDirectiveTextDummy);
        EasyMock.verify(randomizeDirectiveDummy);
        EasyMock.verify(randomizeStateStub);
        EasyMock.verify(firstOutNameStub);
        EasyMock.verify(firstOutPriorityStub);
        EasyMock.verify(firstOutStub);
        EasyMock.verify(prioritizedOutNameStub);
        EasyMock.verify(prioritizedOutPriorityStub);
        EasyMock.verify(prioritizedOutStub);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultDispatchProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode)}.
     */
    @Test
    public void testProcessExitNode() {
        assertTrue(this.tested.process(exitNodeStub).isEmpty());
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultDispatchProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode)}.
     */
    @Test
    public void testProcessIsolatedNode() {
        assertTrue(this.tested.process(isolatedNodeStub).isEmpty());
    }

}
