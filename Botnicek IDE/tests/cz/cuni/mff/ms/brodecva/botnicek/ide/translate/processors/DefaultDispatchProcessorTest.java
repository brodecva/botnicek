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
package cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
 * Testuje výchozí implementaci procesoru, která vytváří kód pro
 * (ne)deterministický přechod do dalších stavů.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultDispatchProcessor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Priority.class, Stack.class, Text.class, Srai.class })
public class DefaultDispatchProcessorTest {

    private static final String ORDERED_STACK_NAMES = "SECOND FIRST";

    private static final String MULTIPLIED_STACK_NAMES =
            "SECOND SECOND SECOND SECOND SECOND FIRST FIRST";

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
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        PowerMock.mockStatic(Stack.class);
        PowerMock.mockStatic(Text.class);
        PowerMock.mockStatic(Srai.class);

        this.randomizeStateStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(this.randomizeStateStub.getText()).andStubReturn(
                "RANDOMIZE");
        EasyMock.replay(this.randomizeStateStub);

        this.firstOutNameStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(this.firstOutNameStub.getText()).andStubReturn("FIRST");
        EasyMock.replay(this.firstOutNameStub);

        this.firstOutPriorityStub = PowerMock.createStrictMock(Priority.class);
        EasyMock.expect(this.firstOutPriorityStub.getValue()).andStubReturn(
                LOW_PRIORITY_VALUE);
        EasyMock.replay(this.firstOutPriorityStub);

        this.firstOutStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(this.firstOutStub.getName()).andStubReturn(
                this.firstOutNameStub);
        EasyMock.expect(this.firstOutStub.getPriority()).andStubReturn(
                this.firstOutPriorityStub);
        EasyMock.replay(this.firstOutStub);

        this.prioritizedOutNameStub =
                EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(this.prioritizedOutNameStub.getText()).andStubReturn(
                "FIRST");
        EasyMock.replay(this.prioritizedOutNameStub);

        this.prioritizedOutPriorityStub =
                PowerMock.createStrictMock(Priority.class);
        EasyMock.expect(this.prioritizedOutPriorityStub.getValue())
                .andStubReturn(HIGH_PRIORITY_VALUE);
        EasyMock.replay(this.prioritizedOutPriorityStub);

        this.prioritizedOutStub = EasyMock.createMock(Arc.class);
        EasyMock.expect(this.prioritizedOutStub.getName()).andStubReturn(
                this.prioritizedOutNameStub);
        EasyMock.expect(this.prioritizedOutStub.getPriority()).andStubReturn(
                this.prioritizedOutPriorityStub);
        EasyMock.replay(this.prioritizedOutStub);

        this.orderedStatesTextDummy = PowerMock.createStrictMock(Text.class);
        PowerMock.replay(this.orderedStatesTextDummy);

        this.orderedNodeStub = EasyMock.createStrictMock(OrderedNode.class);
        this.randomNodeStub = EasyMock.createStrictMock(RandomNode.class);
        this.exitNodeStub = EasyMock.createStrictMock(ExitNode.class);
        this.isolatedNodeStub = EasyMock.createStrictMock(IsolatedNode.class);

        this.enterRandomizeStateDummy =
                EasyMock.createStrictMock(TemplateElement.class);
        EasyMock.replay(this.enterRandomizeStateDummy);

        this.randomizeDirectiveTextDummy =
                PowerMock.createStrictMock(Text.class);
        EasyMock.replay(this.randomizeDirectiveTextDummy);

        this.multipliedStatesTextDummy = PowerMock.createStrictMock(Text.class);
        EasyMock.replay(this.multipliedStatesTextDummy);

        this.randomizeDirectiveDummy = PowerMock.createStrictMock(Srai.class);
        EasyMock.replay(this.randomizeDirectiveDummy);

        this.tested = DefaultDispatchProcessor.create(this.randomizeStateStub);
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.randomizeStateStub = Intended.nullReference();

        this.firstOutNameStub = Intended.nullReference();
        this.firstOutPriorityStub = Intended.nullReference();
        this.firstOutStub = Intended.nullReference();

        this.prioritizedOutNameStub = Intended.nullReference();
        this.prioritizedOutPriorityStub = Intended.nullReference();
        this.prioritizedOutStub = Intended.nullReference();

        this.orderedNodeStub = Intended.nullReference();
        this.randomNodeStub = Intended.nullReference();
        this.exitNodeStub = Intended.nullReference();
        this.isolatedNodeStub = Intended.nullReference();

        this.orderedStatesTextDummy = Intended.nullReference();
        this.enterRandomizeStateDummy = Intended.nullReference();
        this.randomizeDirectiveTextDummy = Intended.nullReference();
        this.multipliedStatesTextDummy = Intended.nullReference();

        this.randomizeDirectiveDummy = Intended.nullReference();

        this.tested = Intended.nullReference();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultDispatchProcessor#create(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)}
     * .
     */
    @Test
    public void testCreate() {
        DefaultDispatchProcessor.create(this.randomizeStateStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultDispatchProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode)}
     * .
     */
    @Test
    public void testProcessExitNode() {
        assertTrue(this.tested.process(this.exitNodeStub).isEmpty());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultDispatchProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode)}
     * .
     */
    @Test
    public void testProcessIsolatedNode() {
        assertTrue(this.tested.process(this.isolatedNodeStub).isEmpty());
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultDispatchProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.OrderedNode)}
     * .
     */
    @Test
    public void testProcessOrderedNode() {
        EasyMock.expect(this.orderedNodeStub.getOuts()).andStubReturn(
                ImmutableSet.of(this.firstOutStub, this.prioritizedOutStub));
        EasyMock.replay(this.orderedNodeStub);

        EasyMock.expect(
                Stack.joinWithSpaces(ImmutableList.of(
                        this.prioritizedOutNameStub, this.firstOutNameStub)))
                .andStubReturn(ORDERED_STACK_NAMES);
        PowerMock.replay(Stack.class);

        EasyMock.expect(Text.create(ORDERED_STACK_NAMES)).andStubReturn(
                this.orderedStatesTextDummy);
        PowerMock.replay(Text.class);

        assertEquals(ImmutableList.of(this.orderedStatesTextDummy),
                this.tested.process(this.orderedNodeStub));

        PowerMock.verify(Stack.class);
        PowerMock.verify(Text.class);
        EasyMock.verify(this.orderedStatesTextDummy);
        EasyMock.verify(this.orderedNodeStub);
        EasyMock.verify(this.firstOutNameStub);
        EasyMock.verify(this.firstOutPriorityStub);
        EasyMock.verify(this.firstOutStub);
        EasyMock.verify(this.prioritizedOutNameStub);
        EasyMock.verify(this.prioritizedOutPriorityStub);
        EasyMock.verify(this.prioritizedOutStub);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultDispatchProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RandomNode)}
     * .
     */
    @Test
    public void testProcessRandomNode() {
        EasyMock.expect(this.randomNodeStub.getOuts()).andStubReturn(
                ImmutableSet.of(this.firstOutStub, this.prioritizedOutStub));
        EasyMock.replay(this.randomNodeStub);

        EasyMock.expect(Stack.popAndPushWords(this.randomizeStateStub))
                .andStubReturn(this.enterRandomizeStateDummy);
        EasyMock.expect(
                Stack.joinWithSpaces(ImmutableList.copyOf(Iterables.concat(
                        Collections.nCopies(LOW_PRIORITY_VALUE,
                                this.firstOutNameStub), Collections.nCopies(
                                HIGH_PRIORITY_VALUE,
                                this.prioritizedOutNameStub))))).andStubReturn(
                MULTIPLIED_STACK_NAMES);
        PowerMock.replay(Stack.class);

        EasyMock.expect(Text.create("RANDOMIZE ")).andStubReturn(
                this.randomizeDirectiveTextDummy);
        EasyMock.expect(Text.create(MULTIPLIED_STACK_NAMES)).andStubReturn(
                this.multipliedStatesTextDummy);
        PowerMock.replay(Text.class);

        EasyMock.expect(
                Srai.create(this.randomizeDirectiveTextDummy,
                        this.multipliedStatesTextDummy)).andStubReturn(
                this.randomizeDirectiveDummy);
        PowerMock.replay(Srai.class);

        assertEquals(ImmutableList.of(this.enterRandomizeStateDummy,
                this.randomizeDirectiveDummy),
                this.tested.process(this.randomNodeStub));

        PowerMock.verify(Stack.class);
        PowerMock.verify(Text.class);
        PowerMock.verify(Srai.class);
        EasyMock.verify(this.enterRandomizeStateDummy);
        EasyMock.verify(this.multipliedStatesTextDummy);
        EasyMock.verify(this.randomizeDirectiveTextDummy);
        EasyMock.verify(this.randomizeDirectiveDummy);
        EasyMock.verify(this.randomizeStateStub);
        EasyMock.verify(this.firstOutNameStub);
        EasyMock.verify(this.firstOutPriorityStub);
        EasyMock.verify(this.firstOutStub);
        EasyMock.verify(this.prioritizedOutNameStub);
        EasyMock.verify(this.prioritizedOutPriorityStub);
        EasyMock.verify(this.prioritizedOutStub);
    }

}
