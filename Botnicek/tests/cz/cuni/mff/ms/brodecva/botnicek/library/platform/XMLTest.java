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
package cz.cuni.mff.ms.brodecva.botnicek.library.platform;

import static org.junit.Assert.assertEquals;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje pomocné metody pro práci s XML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see XML
 */
@Category(UnitTest.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest(XML.class)
public final class XMLTest {

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML#createElementStart(java.lang.String, java.lang.String, org.xml.sax.Attributes)}.
     * @throws NoSuchMethodException neexistující metoda 
     */
    @Test
    public void testCreateElementStartStringStringStringAttributes() throws NoSuchMethodException {
        final Attributes attsStub = EasyMock.createMock(Attributes.class);
        replay(attsStub);
        
        PowerMock.mockStatic(XML.class, XML.class.getMethod("printAttributes", Attributes.class));
        expect(XML.printAttributes(attsStub)).andReturn(" prefix:att1=\"att1val\" att2=\"att2val\"");                
        PowerMock.replayAll();
                
        final String result = XML.createElementStart("http://my.ns.com", "localName", attsStub);
        
        PowerMock.verifyAll();
        
        assertEquals("<localName xmlns=\"http://my.ns.com\" prefix:att1=\"att1val\" att2=\"att2val\">", result);
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML#createElementEnd(java.lang.String)}.
     */
    @Test
    public void testCreateElementEndString() {
        final String result = XML.createElementEnd("localName");
        assertEquals("</localName>", result);
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML#createElementStart(org.w3c.dom.Element)}.
     * @throws NoSuchMethodException neexistující metoda
     */
    @Test
    public void testCreateElementStartElement() throws NoSuchMethodException {
        final NamedNodeMap attsStub = EasyMock.createMock(NamedNodeMap.class);
        replay(attsStub);
        
        PowerMock.mockStatic(XML.class, XML.class.getMethod("printAttributes", NamedNodeMap.class));
        expect(XML.printAttributes(attsStub)).andReturn(" att1=\"att1val\" att2=\"att2val\"");                
        PowerMock.replayAll();
        
        final Element elementStub = EasyMock.createMock(Element.class);
        expect(elementStub.getAttributes()).andReturn(attsStub);
        expect(elementStub.getPrefix()).andReturn("prefix");
        expect(elementStub.getLocalName()).andReturn("localName");
        replay(elementStub);
        
        final String result = XML.createElementStart(elementStub);
        
        PowerMock.verifyAll();
        
        assertEquals("<prefix:localName att1=\"att1val\" att2=\"att2val\">", result);
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML#createEmptyElement(org.w3c.dom.Element)}.
     * @throws NoSuchMethodException  neexistující metoda
     */
    @Test
    public void testCreateEmptyElement() throws NoSuchMethodException {
        final NamedNodeMap attsStub = EasyMock.createMock(NamedNodeMap.class);
        replay(attsStub);
        
        PowerMock.mockStatic(XML.class, XML.class.getMethod("printAttributes", NamedNodeMap.class));
        expect(XML.printAttributes(attsStub)).andReturn(" att1=\"att1val\" att2=\"att2val\"");                
        PowerMock.replayAll();
        
        final Element elementStub = EasyMock.createMock(Element.class);
        expect(elementStub.getAttributes()).andReturn(attsStub);
        expect(elementStub.getPrefix()).andReturn("prefix");
        expect(elementStub.getLocalName()).andReturn("localName");
        replay(elementStub);
        
        final String result = XML.createEmptyElement(elementStub);
        
        assertEquals("<prefix:localName att1=\"att1val\" att2=\"att2val\"/>", result);
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML#createElementEnd(org.w3c.dom.Element)}.
     */
    @Test
    public void testCreateElementEndElement() {
        final Element elementStub = EasyMock.createMock(Element.class);
        expect(elementStub.getPrefix()).andReturn("prefix");
        expect(elementStub.getLocalName()).andReturn("localName");
        replay(elementStub);
        
        final String result = XML.createElementEnd(elementStub);
        
        PowerMock.verifyAll();
        
        assertEquals("</prefix:localName>", result);
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML#printAttributes(org.xml.sax.Attributes)}.
     */
    @Test
    public void testPrintAttributesAttributes() {
        final Attributes attsStub = EasyMock.createMock(Attributes.class);
        expect(attsStub.getLength()).andReturn(2);
        expect(attsStub.getQName(0)).andReturn("prefix:att1");
        expect(attsStub.getQName(1)).andReturn("att2");
        expect(attsStub.getValue(0)).andReturn("att1val");
        expect(attsStub.getValue(1)).andReturn("att2val");
        replay(attsStub);
        
        final String result = XML.printAttributes(attsStub);
        
        assertEquals(" prefix:att1=\"att1val\" att2=\"att2val\"", result);
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML#printAttributes(org.w3c.dom.NamedNodeMap)}.
     */
    @Test
    public void testPrintAttributesNamedNodeMap() {
        final Attr firstAttStub = EasyMock.createMock(Attr.class);
        expect(firstAttStub.getPrefix()).andReturn("prefix");
        expect(firstAttStub.getLocalName()).andReturn("att1");
        expect(firstAttStub.getNodeValue()).andReturn("att1val");
        replay(firstAttStub);
        
        final Attr secondAttStub = EasyMock.createMock(Attr.class);
        expect(secondAttStub.getPrefix()).andReturn(null);
        expect(secondAttStub.getLocalName()).andReturn("att2");
        expect(secondAttStub.getNodeValue()).andReturn("att2val");
        replay(secondAttStub);        
        
        final NamedNodeMap attsStub = EasyMock.createMock(NamedNodeMap.class);
        expect(attsStub.getLength()).andReturn(2);
        expect(attsStub.item(0)).andReturn(firstAttStub);
        expect(attsStub.item(1)).andReturn(secondAttStub);
        replay(attsStub);
        
        final String result = XML.printAttributes(attsStub);
        
        assertEquals(" prefix:att1=\"att1val\" att2=\"att2val\"", result);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML#listFilialElements(Element)}.
     */
    @Test
    public void testListFilialElements() {
        final int elementsCount = 3;
        final List<Element> expected = new ArrayList<Element>(elementsCount);
        for (int i = 0; i < elementsCount; i++) {
            final Element child = EasyMock.createMock(Element.class);
            expect(child.getNodeType()).andStubReturn(Node.ELEMENT_NODE);
            replay(child);
            expected.add(child);
        }
        
        final Node text = EasyMock.createMock(Node.class);
        expect(text.getNodeType()).andStubReturn(Node.TEXT_NODE);
        replay(text);
        
        final Node comment = EasyMock.createMock(Node.class);
        expect(comment.getNodeType()).andStubReturn(Node.COMMENT_NODE);
        replay(comment);
        
        final int childrenCount = 5;
        final int textIndex = 1;
        final int commentIndex = 4;
        final int firstIndex = 0;
        final int secondIndex = 2;
        final int thirdIndex = 3;
        final NodeList childrenStub = EasyMock.createMock(NodeList.class);
        expect(childrenStub.getLength()).andStubReturn(childrenCount);
        expect(childrenStub.item(firstIndex)).andStubReturn(expected.get(0));
        expect(childrenStub.item(textIndex)).andStubReturn(text);
        expect(childrenStub.item(secondIndex)).andStubReturn(expected.get(1));
        expect(childrenStub.item(thirdIndex)).andStubReturn(expected.get(2));
        expect(childrenStub.item(commentIndex)).andStubReturn(comment);
        replay(childrenStub);
        
        final Element fatherStub = EasyMock.createMock(Element.class);
        expect(fatherStub.getChildNodes()).andStubReturn(childrenStub);
        replay(fatherStub);
        
        assertEquals(expected, XML.listFilialElements(fatherStub));
    }
}
