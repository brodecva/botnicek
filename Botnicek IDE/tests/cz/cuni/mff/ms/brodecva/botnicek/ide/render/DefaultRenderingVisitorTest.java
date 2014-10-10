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
package cz.cuni.mff.ms.brodecva.botnicek.ide.render;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractRawElement;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.Element;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Attribute;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * Testuje implementaci návštěvníka generujícího zdrojový kód AIML z objektového
 * modelu dokumentu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see DefaultRenderingVisitor
 */
public class DefaultRenderingVisitorTest {

    private static final String TEST_URI_STRING = "http://test.uri.org/example";
    private DefaultRenderingVisitor notPrefixedAimlTested = Intended
            .nullReference();
    private DefaultRenderingVisitor prefixedAimlTested = Intended
            .nullReference();

    /**
     * Vytvoří testované objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.notPrefixedAimlTested =
                DefaultRenderingVisitor.create(ImmutableMap.of(
                        URI.create(AIML.NAMESPACE_URI.getValue()), "",
                        URI.create(TEST_URI_STRING), "test"));
        this.prefixedAimlTested =
                DefaultRenderingVisitor.create(ImmutableMap.of(
                        URI.create(AIML.NAMESPACE_URI.getValue()), "aiml",
                        URI.create(TEST_URI_STRING), "test"));
    }

    /**
     * Uklidí testované objekty.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.notPrefixedAimlTested = Intended.nullReference();
        this.prefixedAimlTested = Intended.nullReference();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitor#create(java.util.Map)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWhenAimlPrefixNotSpecified() {
        DefaultRenderingVisitor.create(ImmutableMap.<URI, String> of());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitor#create(java.util.Map)}
     * .
     */
    @Test
    public void testCreateWhenAimlSpecifiedExpectPasses() {
        DefaultRenderingVisitor.create(ImmutableMap.of(
                URI.create(AIML.NAMESPACE_URI.getValue()), "aiml"));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitor#visitEnter(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement)}
     * .
     */
    @Test
    public
            void
            testVisitEnterAndExitAbstractProperElementWhenWithAttributesAndWhitoutPrefixAndChildren() {
        final Attribute firstAttribute = EasyMock.createMock(Attribute.class);
        EasyMock.expect(firstAttribute.getName()).andStubReturn("first");
        EasyMock.expect(firstAttribute.getValue()).andStubReturn("firstValue");
        EasyMock.expect(firstAttribute.getNamespace()).andStubReturn(
                (URI) Intended.nullReference());
        EasyMock.replay(firstAttribute);

        final Attribute secondAttribute = EasyMock.createMock(Attribute.class);
        EasyMock.expect(secondAttribute.getName()).andStubReturn("second");
        EasyMock.expect(secondAttribute.getValue())
                .andStubReturn("secondValue");
        EasyMock.expect(secondAttribute.getNamespace()).andStubReturn(
                URI.create(TEST_URI_STRING));
        EasyMock.replay(secondAttribute);

        final AbstractProperElement elementStub =
                EasyMock.createMock(AbstractProperElement.class);
        EasyMock.expect(elementStub.getLocalName()).andStubReturn("element");
        EasyMock.expect(elementStub.getChildren()).andStubReturn(
                ImmutableList.<Element> of());
        EasyMock.expect(elementStub.hasChildren()).andStubReturn(false);
        EasyMock.expect(elementStub.getAttributes()).andStubReturn(
                ImmutableSet.<Attribute> of(firstAttribute, secondAttribute));
        EasyMock.replay(elementStub);

        this.notPrefixedAimlTested.visitEnter(elementStub);
        this.notPrefixedAimlTested.visitExit(elementStub);

        assertEquals(
                "<element first=\"firstValue\" test:second=\"secondValue\"/>",
                this.notPrefixedAimlTested.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitor#visitEnter(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement)}
     * .
     */
    @Test
    public
            void
            testVisitEnterAndExitAbstractProperElementWhenWithChildrenAndWhitoutAttributesAndPrefix() {
        final Element childDummy = EasyMock.createStrictMock(Element.class);
        EasyMock.replay(childDummy);

        final AbstractProperElement elementStub =
                EasyMock.createMock(AbstractProperElement.class);
        EasyMock.expect(elementStub.getLocalName()).andStubReturn("element");
        EasyMock.expect(elementStub.getChildren()).andStubReturn(
                ImmutableList.of(childDummy));
        EasyMock.expect(elementStub.hasChildren()).andStubReturn(true);
        EasyMock.expect(elementStub.getAttributes()).andStubReturn(
                ImmutableSet.<Attribute> of());
        EasyMock.replay(elementStub);

        this.notPrefixedAimlTested.visitEnter(elementStub);
        this.notPrefixedAimlTested.visitExit(elementStub);

        assertEquals("<element></element>",
                this.notPrefixedAimlTested.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitor#visitEnter(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement)}
     * .
     */
    @Test
    public
            void
            testVisitEnterAndExitAbstractProperElementWhenWithoutPrefixAndAttributesAndChildren() {
        final AbstractProperElement elementStub =
                EasyMock.createMock(AbstractProperElement.class);
        EasyMock.expect(elementStub.getLocalName()).andStubReturn("element");
        EasyMock.expect(elementStub.getChildren()).andStubReturn(
                ImmutableList.<Element> of());
        EasyMock.expect(elementStub.hasChildren()).andStubReturn(false);
        EasyMock.expect(elementStub.getAttributes()).andStubReturn(
                ImmutableSet.<Attribute> of());
        EasyMock.replay(elementStub);

        this.notPrefixedAimlTested.visitEnter(elementStub);
        this.notPrefixedAimlTested.visitExit(elementStub);

        assertEquals("<element/>", this.notPrefixedAimlTested.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitor#visitEnter(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractProperElement)}
     * .
     */
    @Test
    public
            void
            testVisitEnterAndExitAbstractProperElementWhenWithPrefixAndWithoutAttributesAndChildren() {
        final AbstractProperElement elementStub =
                EasyMock.createMock(AbstractProperElement.class);
        EasyMock.expect(elementStub.getLocalName()).andStubReturn("element");
        EasyMock.expect(elementStub.getChildren()).andStubReturn(
                ImmutableList.<Element> of());
        EasyMock.expect(elementStub.hasChildren()).andStubReturn(false);
        EasyMock.expect(elementStub.getAttributes()).andStubReturn(
                ImmutableSet.<Attribute> of());
        EasyMock.replay(elementStub);

        this.prefixedAimlTested.visitEnter(elementStub);
        this.prefixedAimlTested.visitExit(elementStub);

        assertEquals("<aiml:element/>", this.prefixedAimlTested.getResult());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.render.DefaultRenderingVisitor#visitEnter(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.AbstractRawElement)}
     * .
     */
    @Test
    public void testVisitEnterAndExitAbstractRawElement() {
        final AbstractRawElement elementStub =
                EasyMock.createMock(AbstractRawElement.class);
        EasyMock.expect(elementStub.getText()).andStubReturn(
                "<raw>some content</raw>");
        EasyMock.replay(elementStub);

        this.prefixedAimlTested.visitEnter(elementStub);
        this.prefixedAimlTested.visitExit(elementStub);

        assertEquals("<raw>some content</raw>",
                this.prefixedAimlTested.getResult());
    }
}
