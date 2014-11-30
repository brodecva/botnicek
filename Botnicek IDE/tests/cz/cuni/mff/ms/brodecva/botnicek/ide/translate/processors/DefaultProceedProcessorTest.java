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

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.ImmutableList;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Sr;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InputNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * Testuje výchozí implementaci procesoru, který generuje rekurzivní značky do
 * šablony podle toho, zda-li má výpočet probíhat dál, či čekat na vstup.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultProceedProcessor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Sr.class)
public class DefaultProceedProcessorTest {

    private DefaultProceedProcessor tested = Intended.nullReference();
    private InputNode inputNodeDummy = Intended.nullReference();
    private ProcessingNode processingNodeDummy = Intended.nullReference();
    private Sr srDummy = Intended.nullReference();

    /**
     * Inicializuje testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        PowerMock.mockStatic(Sr.class);

        this.tested = DefaultProceedProcessor.create();

        this.inputNodeDummy = EasyMock.createStrictMock(InputNode.class);
        EasyMock.replay(this.inputNodeDummy);

        this.processingNodeDummy =
                EasyMock.createStrictMock(ProcessingNode.class);
        EasyMock.replay(this.processingNodeDummy);

        this.srDummy = EasyMock.createStrictMock(Sr.class);
        EasyMock.replay(this.srDummy);
    }

    /**
     * Uklidí testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.tested = Intended.nullReference();
        this.inputNodeDummy = Intended.nullReference();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultProceedProcessor#create()}
     * .
     */
    @Test
    public void testCreate() {
        DefaultProceedProcessor.create();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultProceedProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InputNode)}
     * .
     */
    @Test
    public void testProcessInputNode() {
        assertTrue(this.tested.process(this.inputNodeDummy).isEmpty());

        EasyMock.verify(this.inputNodeDummy);
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultProceedProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ProcessingNode)}
     * .
     */
    @Test
    public void testProcessProcessingNode() {
        EasyMock.expect(Sr.create()).andStubReturn(this.srDummy);
        PowerMock.replay(Sr.class);

        assertEquals(ImmutableList.of(this.srDummy),
                this.tested.process(this.processingNodeDummy));

        PowerMock.verify(Sr.class);
        EasyMock.verify(this.srDummy);
        EasyMock.verify(this.processingNodeDummy);
    }

}
