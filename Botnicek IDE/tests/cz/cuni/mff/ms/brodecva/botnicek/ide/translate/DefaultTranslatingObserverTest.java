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
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.DispatchProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.ProceedProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.StackProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.TestProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje výchozí implementaci překladače.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultTranslatingObserver
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Stack.class, Topic.class, Text.class })
@Category(UnitTest.class)
public class DefaultTranslatingObserverTest {

    private DefaultTranslatingObserver tested = Intended.nullReference();

    private NodeTopicFactory nodeTopicFactoryStub = Intended.nullReference();
    private TestProcessor<List<Topic>> testProcessorStub = Intended
            .nullReference();

    private StackProcessor<List<TemplateElement>> stackProcessorDummy =
            Intended.nullReference();
    private DispatchProcessor<List<TemplateElement>> dispatchProcessorDummy =
            Intended.nullReference();
    private ProceedProcessor<List<TemplateElement>> proceedProcessorDummy =
            Intended.nullReference();

    private System systemDummy = Intended.nullReference();
    private Network networkDummy = Intended.nullReference();
    private Arc arcDummy = Intended.nullReference();
    private Node nodeDummy = Intended.nullReference();

    private Topic topicDummy = Intended.nullReference();

    private TemplateElement stackProcessingResultElementDummy = Intended
            .nullReference();
    private TemplateElement dispatchProcessingResultElementDummy = Intended
            .nullReference();
    private TemplateElement proceedProcessingResultElementDummy = Intended
            .nullReference();
    private TemplateElement combinedProcessingResultElementDummy = Intended
            .nullReference();
    private Topic testProcessingResultElementDummy = Intended.nullReference();

    private Arc arcMock = Intended.nullReference();
    private Node nodeMock = Intended.nullReference();

    private Text spaceDummy = Intended.nullReference();

    /**
     * Vytvoří testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.systemDummy = EasyMock.createStrictMock(System.class);
        EasyMock.replay(this.systemDummy);

        this.networkDummy = EasyMock.createStrictMock(Network.class);
        EasyMock.replay(this.networkDummy);

        this.nodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(this.nodeDummy);

        this.arcDummy = EasyMock.createMock(Arc.class);
        EasyMock.replay(this.arcDummy);

        this.topicDummy = PowerMock.createStrictMock(Topic.class);
        PowerMock.replay(this.topicDummy);

        this.stackProcessorDummy =
                EasyMock.createStrictMock(StackProcessor.class);
        EasyMock.replay(this.stackProcessorDummy);

        this.dispatchProcessorDummy =
                EasyMock.createStrictMock(DispatchProcessor.class);
        EasyMock.replay(this.dispatchProcessorDummy);

        this.proceedProcessorDummy =
                EasyMock.createStrictMock(ProceedProcessor.class);
        EasyMock.replay(this.proceedProcessorDummy);

        this.stackProcessingResultElementDummy =
                EasyMock.createStrictMock(TemplateElement.class);
        EasyMock.replay(this.stackProcessingResultElementDummy);

        this.dispatchProcessingResultElementDummy =
                EasyMock.createStrictMock(TemplateElement.class);
        EasyMock.replay(this.dispatchProcessingResultElementDummy);

        this.proceedProcessingResultElementDummy =
                EasyMock.createStrictMock(TemplateElement.class);
        EasyMock.replay(this.proceedProcessingResultElementDummy);

        this.combinedProcessingResultElementDummy =
                EasyMock.createStrictMock(TemplateElement.class);
        EasyMock.replay(this.combinedProcessingResultElementDummy);

        this.testProcessingResultElementDummy =
                PowerMock.createStrictMock(Topic.class);
        PowerMock.replay(this.testProcessingResultElementDummy);

        this.nodeTopicFactoryStub =
                EasyMock.createStrictMock(NodeTopicFactory.class);
        this.testProcessorStub = EasyMock.createStrictMock(TestProcessor.class);

        this.nodeMock = EasyMock.createMock(Node.class);
        this.arcMock = EasyMock.createMock(Arc.class);

        this.spaceDummy = PowerMock.createStrictMock(Text.class);
        PowerMock.replay(this.spaceDummy);

        this.tested =
                DefaultTranslatingObserver.create(this.nodeTopicFactoryStub,
                        this.stackProcessorDummy, this.dispatchProcessorDummy,
                        this.proceedProcessorDummy, this.testProcessorStub);

        PowerMock.mockStatic(Stack.class);
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.systemDummy = Intended.nullReference();
        this.networkDummy = Intended.nullReference();
        this.arcDummy = Intended.nullReference();
        this.nodeDummy = Intended.nullReference();

        this.topicDummy = Intended.nullReference();

        this.stackProcessingResultElementDummy = Intended.nullReference();
        this.dispatchProcessingResultElementDummy = Intended.nullReference();
        this.proceedProcessingResultElementDummy = Intended.nullReference();
        this.combinedProcessingResultElementDummy = Intended.nullReference();
        this.testProcessingResultElementDummy = Intended.nullReference();

        this.nodeTopicFactoryStub = Intended.nullReference();
        this.stackProcessorDummy = Intended.nullReference();
        this.dispatchProcessorDummy = Intended.nullReference();
        this.proceedProcessorDummy = Intended.nullReference();
        this.testProcessorStub = Intended.nullReference();

        this.arcMock = Intended.nullReference();
        this.nodeMock = Intended.nullReference();

        this.spaceDummy = Intended.nullReference();

        this.tested = Intended.nullReference();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#create(NodeTopicFactory, StackProcessor, DispatchProcessor, ProceedProcessor, TestProcessor)}
     * .
     */
    @Test
    public void testCreate() {
        DefaultTranslatingObserver.create(this.nodeTopicFactoryStub,
                this.stackProcessorDummy, this.dispatchProcessorDummy,
                this.proceedProcessorDummy, this.testProcessorStub);

        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}
     * .
     */
    @Test
    public void testGetResultWhenNothingProcessedReturnEmptyResult() {
        assertTrue(this.tested.getResult().isEmpty());

        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyDiscovery(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * .
     */
    @Test(expected = IllegalStateException.class)
    public void testNotifyDiscoveryWhenNetworkNotVisitedBefore() {
        EasyMock.expect(this.nodeMock.accept(this.dispatchProcessorDummy))
                .andStubReturn(
                        ImmutableList
                                .of(this.dispatchProcessingResultElementDummy));
        EasyMock.expect(this.nodeMock.accept(this.stackProcessorDummy))
                .andStubReturn(
                        ImmutableList
                                .of(this.stackProcessingResultElementDummy));
        EasyMock.expect(this.nodeMock.accept(this.proceedProcessorDummy))
                .andStubReturn(
                        ImmutableList
                                .of(this.proceedProcessingResultElementDummy));
        EasyMock.replay(this.nodeMock);

        EasyMock.expect(
                Stack.popAndPush(ImmutableList.of(
                        this.dispatchProcessingResultElementDummy,
                        this.stackProcessingResultElementDummy)))
                .andStubReturn(this.combinedProcessingResultElementDummy);
        PowerMock.replay(Stack.class);

        EasyMock.expect(
                this.nodeTopicFactoryStub.produce(this.nodeMock, ImmutableList
                        .of(this.combinedProcessingResultElementDummy,
                                this.proceedProcessingResultElementDummy)))
                .andStubReturn(this.topicDummy);
        EasyMock.replay(this.nodeTopicFactoryStub);

        this.tested.notifyDiscovery(this.nodeMock);

        EasyMock.verify(this.nodeMock);
        PowerMock.verify(Stack.class);
        EasyMock.verify(this.nodeTopicFactoryStub);
        EasyMock.verify(this.stackProcessingResultElementDummy);
        EasyMock.verify(this.dispatchProcessingResultElementDummy);
        EasyMock.verify(this.proceedProcessingResultElementDummy);
        PowerMock.verify(this.topicDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyExamination(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}
     * .
     */
    @Test(expected = IllegalStateException.class)
    public void testNotifyExaminationWhenNetworkNotVisitedBefore() {
        EasyMock.expect(this.arcMock.accept(this.testProcessorStub))
                .andStubReturn(
                        ImmutableList.of(this.testProcessingResultElementDummy));
        EasyMock.replay(this.arcMock);

        this.tested.notifyExamination(this.arcMock);

        EasyMock.verify(this.testProcessingResultElementDummy);
        EasyMock.verify(this.arcMock);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}
     * .
     */
    @Test
    public void testNotifyVisitNetworkAndGetResultExpectEmptyResult() {
        this.tested.notifyVisit(this.networkDummy);

        assertTrue(this.tested.getResult().isEmpty());

        EasyMock.verify(this.networkDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyBack(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}
     * .
     */
    @Test
    public
            void
            testNotifyVisitNetworkAndNotifyBackAndGetResultExpectNoActionAndEmptyResult() {
        this.tested.notifyVisit(this.networkDummy);
        this.tested.notifyBack(this.arcDummy);

        assertTrue(this.tested.getResult().isEmpty());

        EasyMock.verify(this.networkDummy);
        EasyMock.verify(this.arcDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyCross(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}
     * .
     */
    @Test
    public
            void
            testNotifyVisitNetworkAndNotifyCrossAndGetResultExpectNoActionAndEmptyResult() {
        this.tested.notifyVisit(this.networkDummy);
        this.tested.notifyCross(this.arcDummy);

        assertTrue(this.tested.getResult().isEmpty());

        EasyMock.verify(this.networkDummy);
        EasyMock.verify(this.arcDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyDiscovery(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}
     * .
     */
    @Test
    public
            void
            testNotifyVisitNetworkAndNotifyDiscoveryAndGetResultExpectReturnsProcessorResultsInCorrectOrderForCurrentNetwork() {
        EasyMock.expect(this.nodeMock.accept(this.dispatchProcessorDummy))
                .andStubReturn(
                        ImmutableList
                                .of(this.dispatchProcessingResultElementDummy));
        EasyMock.expect(this.nodeMock.accept(this.stackProcessorDummy))
                .andStubReturn(
                        ImmutableList
                                .of(this.stackProcessingResultElementDummy));
        EasyMock.expect(this.nodeMock.accept(this.proceedProcessorDummy))
                .andStubReturn(
                        ImmutableList
                                .of(this.proceedProcessingResultElementDummy));
        EasyMock.replay(this.nodeMock);

        PowerMock.mockStatic(Text.class);
        EasyMock.expect(Text.create(AIML.WORD_DELIMITER.getValue()))
                .andStubReturn(this.spaceDummy);
        PowerMock.replay(Text.class);

        EasyMock.expect(
                Stack.popAndPush(ImmutableList
                        .of(this.dispatchProcessingResultElementDummy,
                                this.spaceDummy,
                                this.stackProcessingResultElementDummy)))
                .andStubReturn(this.combinedProcessingResultElementDummy);
        PowerMock.replay(Stack.class);

        EasyMock.expect(
                this.nodeTopicFactoryStub.produce(this.nodeMock, ImmutableList
                        .of(this.combinedProcessingResultElementDummy,
                                this.proceedProcessingResultElementDummy)))
                .andStubReturn(this.topicDummy);
        EasyMock.replay(this.nodeTopicFactoryStub);

        this.tested.notifyVisit(this.networkDummy);
        this.tested.notifyDiscovery(this.nodeMock);

        assertEquals(
                this.tested.getResult(),
                ImmutableMap.of(this.networkDummy,
                        ImmutableList.of(this.topicDummy)));

        EasyMock.verify(this.nodeMock);
        EasyMock.verify(this.nodeTopicFactoryStub);
        PowerMock.verify(Stack.class);
        EasyMock.verify(this.stackProcessingResultElementDummy);
        EasyMock.verify(this.dispatchProcessingResultElementDummy);
        EasyMock.verify(this.proceedProcessingResultElementDummy);
        PowerMock.verify(this.topicDummy);
        EasyMock.verify(this.networkDummy);
        PowerMock.verify(this.spaceDummy);
        PowerMock.verify(Text.class);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyExamination(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}
     * .
     */
    @Test
    public
            void
            testNotifyVisitNetworkAndNotifyExaminationAndGetResultExpectReturnsProcessorResultForCurrentNetwork() {
        EasyMock.expect(this.arcMock.accept(this.testProcessorStub))
                .andStubReturn(
                        ImmutableList.of(this.testProcessingResultElementDummy));
        EasyMock.replay(this.arcMock);

        this.tested.notifyVisit(this.networkDummy);
        this.tested.notifyExamination(this.arcMock);

        assertEquals(this.tested.getResult(), ImmutableMap.of(
                this.networkDummy,
                ImmutableList.of(this.testProcessingResultElementDummy)));

        EasyMock.verify(this.arcMock);
        EasyMock.verify(this.testProcessingResultElementDummy);
        EasyMock.verify(this.networkDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyFinish(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}
     * .
     */
    @Test
    public
            void
            testNotifyVisitNetworkAndNotifyFinishAndGetResultExpectNoActionAndEmptyResult() {
        this.tested.notifyVisit(this.networkDummy);
        this.tested.notifyFinish(this.nodeDummy);

        assertTrue(this.tested.getResult().isEmpty());

        EasyMock.verify(this.networkDummy);
        EasyMock.verify(this.nodeDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}
     * ,
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyTree(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}
     * .
     */
    @Test
    public
            void
            testNotifyVisitNetworkAndNotifyTreeAndGetResultExpectNoActionAndEmptyResult() {
        this.tested.notifyVisit(this.networkDummy);
        this.tested.notifyTree(this.arcDummy);

        assertTrue(this.tested.getResult().isEmpty());

        EasyMock.verify(this.networkDummy);
        EasyMock.verify(this.arcDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System)}
     * and
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}
     * .
     */
    @Test
    public void testNotifyVisitSystemAndGetResultExpectEmptyResult() {
        this.tested.notifyVisit(this.systemDummy);

        assertTrue(this.tested.getResult().isEmpty());

        EasyMock.verify(this.systemDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    private void verifyNodeProcessorDummiesNotTouched() {
        EasyMock.verify(this.stackProcessorDummy);
        EasyMock.verify(this.dispatchProcessorDummy);
        EasyMock.verify(this.proceedProcessorDummy);
    }

}
