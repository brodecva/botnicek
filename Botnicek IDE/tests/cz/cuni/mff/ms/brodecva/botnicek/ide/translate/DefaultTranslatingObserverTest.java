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

import static org.junit.Assert.*;

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
    private TestProcessor<List<Topic>> testProcessorStub = Intended.nullReference();
    
    private StackProcessor<List<TemplateElement>> stackProcessorDummy = Intended.nullReference();
    private DispatchProcessor<List<TemplateElement>> dispatchProcessorDummy = Intended.nullReference();
    private ProceedProcessor<List<TemplateElement>> proceedProcessorDummy = Intended.nullReference();
    
    private System systemDummy = Intended.nullReference();
    private Network networkDummy = Intended.nullReference();
    private Arc arcDummy = Intended.nullReference();
    private Node nodeDummy = Intended.nullReference();
    
    private Topic topicDummy = Intended.nullReference();
    
    private TemplateElement stackProcessingResultElementDummy = Intended.nullReference();
    private TemplateElement dispatchProcessingResultElementDummy = Intended.nullReference();
    private TemplateElement proceedProcessingResultElementDummy = Intended.nullReference();
    private TemplateElement combinedProcessingResultElementDummy = Intended.nullReference();
    private Topic testProcessingResultElementDummy = Intended.nullReference();
    
    private Arc arcMock = Intended.nullReference();
    private Node nodeMock = Intended.nullReference();

    private Text spaceDummy = Intended.nullReference();

    /**
     * Vytvoří testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.systemDummy = EasyMock.createStrictMock(System.class);
        EasyMock.replay(this.systemDummy);
        
        networkDummy = EasyMock.createStrictMock(Network.class);
        EasyMock.replay(networkDummy);
        
        nodeDummy = EasyMock.createStrictMock(Node.class);
        EasyMock.replay(nodeDummy);
        
        arcDummy = EasyMock.createMock(Arc.class);
        EasyMock.replay(arcDummy);
        
        topicDummy = PowerMock.createStrictMock(Topic.class);
        PowerMock.replay(topicDummy);
        
        this.stackProcessorDummy = EasyMock.createStrictMock(StackProcessor.class);
        EasyMock.replay(this.stackProcessorDummy);
        
        this.dispatchProcessorDummy = EasyMock.createStrictMock(DispatchProcessor.class);
        EasyMock.replay(this.dispatchProcessorDummy);
        
        this.proceedProcessorDummy = EasyMock.createStrictMock(ProceedProcessor.class);
        EasyMock.replay(this.proceedProcessorDummy);
        
        stackProcessingResultElementDummy = EasyMock.createStrictMock(TemplateElement.class);
        EasyMock.replay(stackProcessingResultElementDummy);
        
        dispatchProcessingResultElementDummy = EasyMock.createStrictMock(TemplateElement.class);
        EasyMock.replay(dispatchProcessingResultElementDummy);
        
        proceedProcessingResultElementDummy = EasyMock.createStrictMock(TemplateElement.class);
        EasyMock.replay(proceedProcessingResultElementDummy);
        
        combinedProcessingResultElementDummy = EasyMock.createStrictMock(TemplateElement.class);
        EasyMock.replay(combinedProcessingResultElementDummy);
        
        testProcessingResultElementDummy = PowerMock.createStrictMock(Topic.class);
        PowerMock.replay(testProcessingResultElementDummy);
        
        this.nodeTopicFactoryStub = EasyMock.createStrictMock(NodeTopicFactory.class);
        this.testProcessorStub = EasyMock.createStrictMock(TestProcessor.class);
        
        nodeMock = EasyMock.createMock(Node.class);
        arcMock = EasyMock.createMock(Arc.class);
        
        spaceDummy = PowerMock.createStrictMock(Text.class);
        PowerMock.replay(spaceDummy);
        
        this.tested = DefaultTranslatingObserver.create(this.nodeTopicFactoryStub, this.stackProcessorDummy, this.dispatchProcessorDummy, this.proceedProcessorDummy, this.testProcessorStub);
        
        PowerMock.mockStatic(Stack.class);
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        systemDummy = Intended.nullReference();
        networkDummy = Intended.nullReference();
        arcDummy = Intended.nullReference();
        nodeDummy = Intended.nullReference();
        
        topicDummy = Intended.nullReference();
        
        stackProcessingResultElementDummy = Intended.nullReference();
        dispatchProcessingResultElementDummy = Intended.nullReference();
        proceedProcessingResultElementDummy = Intended.nullReference();
        combinedProcessingResultElementDummy = Intended.nullReference();
        testProcessingResultElementDummy = Intended.nullReference();
        
        this.nodeTopicFactoryStub = Intended.nullReference();
        this.stackProcessorDummy = Intended.nullReference();
        this.dispatchProcessorDummy = Intended.nullReference();
        this.proceedProcessorDummy = Intended.nullReference();
        this.testProcessorStub = Intended.nullReference();
        
        arcMock = Intended.nullReference();
        nodeMock = Intended.nullReference();
        
        spaceDummy = Intended.nullReference();
        
        this.tested = Intended.nullReference();
    }
    
    private void verifyNodeProcessorDummiesNotTouched() {
        EasyMock.verify(this.stackProcessorDummy);
        EasyMock.verify(this.dispatchProcessorDummy);
        EasyMock.verify(this.proceedProcessorDummy);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#create(NodeTopicFactory, StackProcessor, DispatchProcessor, ProceedProcessor, TestProcessor)}.
     */
    @Test
    public
            void
            testCreate() {
        DefaultTranslatingObserver.create(nodeTopicFactoryStub, stackProcessorDummy, dispatchProcessorDummy, proceedProcessorDummy, testProcessorStub);
        
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}.
     */
    @Test
    public void testNotifyVisitSystemAndGetResultExpectEmptyResult() {
        this.tested.notifyVisit(this.systemDummy);
        
        assertTrue(this.tested.getResult().isEmpty());
        
        EasyMock.verify(this.systemDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}.
     */
    @Test
    public void testNotifyVisitNetworkAndGetResultExpectEmptyResult() {
        this.tested.notifyVisit(networkDummy);
        
        assertTrue(this.tested.getResult().isEmpty());
        
        EasyMock.verify(networkDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyDiscovery(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}.
     */
    @Test
    public void testNotifyVisitNetworkAndNotifyDiscoveryAndGetResultExpectReturnsProcessorResultsInCorrectOrderForCurrentNetwork() {
        EasyMock.expect(nodeMock.accept(this.dispatchProcessorDummy)).andStubReturn(ImmutableList.of(dispatchProcessingResultElementDummy));
        EasyMock.expect(nodeMock.accept(this.stackProcessorDummy)).andStubReturn(ImmutableList.of(stackProcessingResultElementDummy));
        EasyMock.expect(nodeMock.accept(this.proceedProcessorDummy)).andStubReturn(ImmutableList.of(proceedProcessingResultElementDummy));
        EasyMock.replay(nodeMock);
        
        PowerMock.mockStatic(Text.class);
        EasyMock.expect(Text.create(AIML.WORD_DELIMITER.getValue())).andStubReturn(spaceDummy);
        PowerMock.replay(Text.class);
        
        EasyMock.expect(Stack.popAndPush(ImmutableList.of(dispatchProcessingResultElementDummy, spaceDummy, stackProcessingResultElementDummy))).andStubReturn(combinedProcessingResultElementDummy);
        PowerMock.replay(Stack.class);
        
        EasyMock.expect(this.nodeTopicFactoryStub.produce(nodeMock, ImmutableList.of(combinedProcessingResultElementDummy, proceedProcessingResultElementDummy))).andStubReturn(topicDummy);
        EasyMock.replay(this.nodeTopicFactoryStub);
        
        this.tested.notifyVisit(networkDummy);
        this.tested.notifyDiscovery(nodeMock);
        
        assertEquals(this.tested.getResult(), ImmutableMap.of(networkDummy, ImmutableList.of(topicDummy)));
        
        EasyMock.verify(nodeMock);
        EasyMock.verify(this.nodeTopicFactoryStub);
        PowerMock.verify(Stack.class);
        EasyMock.verify(stackProcessingResultElementDummy);
        EasyMock.verify(dispatchProcessingResultElementDummy);
        EasyMock.verify(proceedProcessingResultElementDummy);
        PowerMock.verify(topicDummy);
        EasyMock.verify(networkDummy);
        PowerMock.verify(spaceDummy);
        PowerMock.verify(Text.class);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyExamination(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}.
     */
    @Test
    public void testNotifyVisitNetworkAndNotifyExaminationAndGetResultExpectReturnsProcessorResultForCurrentNetwork() {
        EasyMock.expect(arcMock.accept(this.testProcessorStub)).andStubReturn(ImmutableList.of(testProcessingResultElementDummy));
        EasyMock.replay(arcMock);
        
        this.tested.notifyVisit(networkDummy);
        this.tested.notifyExamination(arcMock);
        
        assertEquals(this.tested.getResult(), ImmutableMap.of(networkDummy, ImmutableList.of(testProcessingResultElementDummy)));
        
        EasyMock.verify(arcMock);
        EasyMock.verify(testProcessingResultElementDummy);
        EasyMock.verify(networkDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyFinish(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}.
     */
    @Test
    public void testNotifyVisitNetworkAndNotifyFinishAndGetResultExpectNoActionAndEmptyResult() {
        this.tested.notifyVisit(networkDummy);
        this.tested.notifyFinish(nodeDummy);
        
        assertTrue(this.tested.getResult().isEmpty());
        
        EasyMock.verify(networkDummy);
        EasyMock.verify(nodeDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyTree(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}.
     */
    @Test
    public void testNotifyVisitNetworkAndNotifyTreeAndGetResultExpectNoActionAndEmptyResult() {
        this.tested.notifyVisit(networkDummy);
        this.tested.notifyTree(arcDummy);
        
        assertTrue(this.tested.getResult().isEmpty());
        
        EasyMock.verify(networkDummy);
        EasyMock.verify(arcDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyBack(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}.
     */
    @Test
    public void testNotifyVisitNetworkAndNotifyBackAndGetResultExpectNoActionAndEmptyResult() {
        this.tested.notifyVisit(networkDummy);
        this.tested.notifyBack(arcDummy);
        
        assertTrue(this.tested.getResult().isEmpty());
        
        EasyMock.verify(networkDummy);
        EasyMock.verify(arcDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyVisit(cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network)}, {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyCross(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)} and {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}.
     */
    @Test
    public void testNotifyVisitNetworkAndNotifyCrossAndGetResultExpectNoActionAndEmptyResult() {
        this.tested.notifyVisit(networkDummy);
        this.tested.notifyCross(arcDummy);
        
        assertTrue(this.tested.getResult().isEmpty());
        
        EasyMock.verify(networkDummy);
        EasyMock.verify(arcDummy);
        verifyNodeProcessorDummiesNotTouched();
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyDiscovery(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node)}.
     */
    @Test(expected = IllegalStateException.class)
    public void testNotifyDiscoveryWhenNetworkNotVisitedBefore() {
        EasyMock.expect(nodeMock.accept(this.dispatchProcessorDummy)).andStubReturn(ImmutableList.of(dispatchProcessingResultElementDummy));
        EasyMock.expect(nodeMock.accept(this.stackProcessorDummy)).andStubReturn(ImmutableList.of(stackProcessingResultElementDummy));
        EasyMock.expect(nodeMock.accept(this.proceedProcessorDummy)).andStubReturn(ImmutableList.of(proceedProcessingResultElementDummy));
        EasyMock.replay(nodeMock);
        
        EasyMock.expect(Stack.popAndPush(ImmutableList.of(dispatchProcessingResultElementDummy, stackProcessingResultElementDummy))).andStubReturn(combinedProcessingResultElementDummy);
        PowerMock.replay(Stack.class);
        
        EasyMock.expect(this.nodeTopicFactoryStub.produce(nodeMock, ImmutableList.of(combinedProcessingResultElementDummy, proceedProcessingResultElementDummy))).andStubReturn(topicDummy);
        EasyMock.replay(this.nodeTopicFactoryStub);
        
        this.tested.notifyDiscovery(nodeMock);
        
        EasyMock.verify(nodeMock);
        PowerMock.verify(Stack.class);
        EasyMock.verify(this.nodeTopicFactoryStub);
        EasyMock.verify(stackProcessingResultElementDummy);
        EasyMock.verify(dispatchProcessingResultElementDummy);
        EasyMock.verify(proceedProcessingResultElementDummy);
        PowerMock.verify(topicDummy);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#notifyExamination(cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.Arc)}.
     */
    @Test(expected = IllegalStateException.class)
    public void testNotifyExaminationWhenNetworkNotVisitedBefore() {
        EasyMock.expect(arcMock.accept(this.testProcessorStub)).andStubReturn(ImmutableList.of(testProcessingResultElementDummy));
        EasyMock.replay(arcMock);
        
        this.tested.notifyExamination(arcMock);
        
        EasyMock.verify(testProcessingResultElementDummy);
        EasyMock.verify(arcMock);
        verifyNodeProcessorDummiesNotTouched();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatingObserver#getResult()}.
     */
    @Test
    public void testGetResultWhenNothingProcessedReturnEmptyResult() {
        assertTrue(this.tested.getResult().isEmpty());
        
        verifyNodeProcessorDummiesNotTouched();
    }

}
