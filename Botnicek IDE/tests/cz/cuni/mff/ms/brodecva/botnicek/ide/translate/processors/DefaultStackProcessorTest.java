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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.implementations.Text;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;

/**
 * Testuje výchozí implementaci procesoru zpracovávajícího uzly tak, že podle
 * nich vytváří instrukce pro modifikaci zásobníku pomocí prvků šablony.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultStackProcessor
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Text.class)
public class DefaultStackProcessorTest {

    private static final String PULL_STOP_STATE_NAME = "PULLSTOP";
    private static final String PULL_STATE_NAME = "PULL";

    private DefaultStackProcessor tested = Intended.nullReference();
    private NormalWord pullStateStub = Intended.nullReference();
    private NormalWord pullStopStateStub = Intended.nullReference();
    private InnerNode innerNodeDummy = Intended.nullReference();
    private IsolatedNode isolatedNodeDummy = Intended.nullReference();
    private EnterNode enterNodeDummy = Intended.nullReference();
    private ExitNode exitNodeDummy = Intended.nullReference();
    private Text pullStateTextDummy = Intended.nullReference();
    private Text pullStopStateTextDummy = Intended.nullReference();

    /**
     * Nastaví testovací objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        PowerMock.mockStatic(Text.class);

        this.pullStateStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(this.pullStateStub.getText()).andStubReturn(
                PULL_STATE_NAME);
        EasyMock.replay(this.pullStateStub);

        this.pullStopStateStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(this.pullStopStateStub.getText()).andStubReturn(
                PULL_STOP_STATE_NAME);
        EasyMock.replay(this.pullStopStateStub);

        this.innerNodeDummy = EasyMock.createStrictMock(InnerNode.class);
        EasyMock.replay(this.innerNodeDummy);

        this.isolatedNodeDummy = EasyMock.createStrictMock(IsolatedNode.class);
        EasyMock.replay(this.isolatedNodeDummy);

        this.enterNodeDummy = EasyMock.createStrictMock(EnterNode.class);
        EasyMock.replay(this.enterNodeDummy);

        this.exitNodeDummy = EasyMock.createStrictMock(ExitNode.class);
        EasyMock.replay(this.exitNodeDummy);

        this.pullStateTextDummy = EasyMock.createStrictMock(Text.class);
        EasyMock.replay(this.pullStateTextDummy);

        this.pullStopStateTextDummy = EasyMock.createStrictMock(Text.class);
        EasyMock.replay(this.pullStopStateTextDummy);

        this.tested =
                DefaultStackProcessor.create(this.pullStateStub,
                        this.pullStopStateStub);
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

        this.pullStateStub = Intended.nullReference();
        this.pullStopStateStub = Intended.nullReference();

        this.innerNodeDummy = Intended.nullReference();
        this.isolatedNodeDummy = Intended.nullReference();
        this.enterNodeDummy = Intended.nullReference();
        this.exitNodeDummy = Intended.nullReference();

        this.pullStateTextDummy = Intended.nullReference();
        this.pullStopStateTextDummy = Intended.nullReference();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultStackProcessor#create(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)}
     * .
     */
    @Test
    public void testCreate() {
        DefaultStackProcessor
                .create(this.pullStateStub, this.pullStopStateStub);

        verifyStateStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultStackProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.EnterNode)}
     * .
     */
    @Test
    public void testProcessEnterNode() {
        EasyMock.expect(Text.create(PULL_STOP_STATE_NAME)).andStubReturn(
                this.pullStopStateTextDummy);
        PowerMock.replay(Text.class);

        assertEquals(ImmutableList.of(this.pullStopStateTextDummy),
                this.tested.process(this.enterNodeDummy));

        PowerMock.verify(Text.class);
        EasyMock.verify(this.enterNodeDummy);
        EasyMock.verify(this.pullStopStateTextDummy);
        verifyStateStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultStackProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ExitNode)}
     * .
     */
    @Test
    public void testProcessExitNode() {
        EasyMock.expect(Text.create(PULL_STATE_NAME)).andStubReturn(
                this.pullStateTextDummy);
        PowerMock.replay(Text.class);

        assertEquals(ImmutableList.of(this.pullStateTextDummy),
                this.tested.process(this.exitNodeDummy));

        PowerMock.verify(Text.class);
        EasyMock.verify(this.exitNodeDummy);
        EasyMock.verify(this.pullStateTextDummy);
        verifyStateStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultStackProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InnerNode)}
     * .
     */
    @Test
    public void testProcessInnerNode() {
        assertTrue(this.tested.process(this.innerNodeDummy).isEmpty());

        EasyMock.verify(this.innerNodeDummy);
        verifyStateStubs();
    }

    /**
     * Testovací metoda pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.processors.DefaultStackProcessor#process(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.IsolatedNode)}
     * .
     */
    @Test
    public void testProcessIsolatedNode() {
        assertTrue(this.tested.process(this.isolatedNodeDummy).isEmpty());

        EasyMock.verify(this.isolatedNodeDummy);
        verifyStateStubs();
    }

    private void verifyStateStubs() {
        EasyMock.verify(this.pullStateStub);
        EasyMock.verify(this.pullStopStateStub);
    }

}
