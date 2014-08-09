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

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.category.Template;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.template.TemplateElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Patterns;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.SimplePattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje výchozí implementaci {@link NodeTopicFactory}.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultNodeTopicFactory
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Template.class, Topic.class, Patterns.class, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category.class })
@Category(UnitTest.class)
public class DefaultNodeTopicFactoryTest {

    private DefaultNodeTopicFactory tested;
    
    /**
     * Vytvoří testovanou instanci.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.tested = DefaultNodeTopicFactory.create();
        
        PowerMock.mockStatic(Template.class);
        PowerMock.mockStatic(Topic.class);
        PowerMock.mockStatic(Patterns.class);
        PowerMock.mockStatic(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category.class);
    }

    /**
     * Uklidí testovanou instanci.
     * 
     * @throws java.lang.Exception pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.tested = Intended.nullReference();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultNodeTopicFactory#create()}.
     */
    @Test
    public void testCreate() {
        DefaultNodeTopicFactory.create();
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultNodeTopicFactory#produce(cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node, java.util.List)}.
     */
    @Test
    public void testProduce() {
        final NormalWord nameStub = EasyMock.createStrictMock(NormalWord.class);
        EasyMock.expect(nameStub.getText()).andStubReturn("NODENAME");
        EasyMock.replay(nameStub);
        
        final Node nodeStub = EasyMock.createStrictMock(Node.class);
        EasyMock.expect(nodeStub.getName()).andStubReturn(nameStub);
        EasyMock.replay(nodeStub);
        
        final TemplateElement codeElementDummy = EasyMock.createStrictMock(TemplateElement.class);
        EasyMock.replay(codeElementDummy);
        
        final SimplePattern patternDummy = EasyMock.createStrictMock(SimplePattern.class);
        EasyMock.replay(patternDummy);
        
        EasyMock.expect(Patterns.create("NODENAME *")).andStubReturn(patternDummy);
        PowerMock.replay(Patterns.class);
        
        final Template templateDummy = PowerMock.createStrictMock(Template.class);
        PowerMock.replay(templateDummy);
        
        EasyMock.expect(Template.create(ImmutableList.of(codeElementDummy))).andStubReturn(templateDummy);
        PowerMock.replay(Template.class);
        
        final cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category categoryDummy = EasyMock.createStrictMock(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category.class);
        PowerMock.replay(categoryDummy);
        
        EasyMock.expect(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category.createUniversal(templateDummy)).andStubReturn(categoryDummy);
        PowerMock.replay(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category.class);
        
        final Topic topicDummy = PowerMock.createStrictMock(Topic.class);
        PowerMock.replay(topicDummy);
        
        EasyMock.expect(Topic.create(patternDummy, categoryDummy)).andStubReturn(topicDummy);
        PowerMock.replay(Topic.class);
        
        this.tested.produce(nodeStub, ImmutableList.of(codeElementDummy));
        
        EasyMock.verify(nameStub);
        EasyMock.verify(nodeStub);
        EasyMock.verify(codeElementDummy);
        PowerMock.verify(patternDummy);
        PowerMock.verify(templateDummy);
        PowerMock.verify(categoryDummy);
        PowerMock.verify(topicDummy);
        PowerMock.verify(Patterns.class);
        PowerMock.verify(Topic.class);
        PowerMock.verify(Template.class);
        PowerMock.verify(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Category.class);
    }

}
