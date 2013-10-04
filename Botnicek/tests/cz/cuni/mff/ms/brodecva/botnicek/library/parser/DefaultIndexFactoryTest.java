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
package cz.cuni.mff.ms.brodecva.botnicek.library.parser;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Element;

import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML2DIndex;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIMLIndex;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje továrnu na indexy {@link AIMLIndex} a {@link AIML2DIndex}.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
@Category(UnitTest.class)
public final class DefaultIndexFactoryTest {
    
    /**
     * Jméno atributu obsahujícího index.
     */
    private static final String INDEX_ATTRIBUTE_NAME = AIML.ATT_INDEX.getValue();

    /**
     * Testovaná továrna.
     */
    private DefaultIndexFactory factory = null;
    
    /**
     * Stub elementu.
     */
    private Element elementStub = null;
    
    /**
     * Výchozí index.
     */
    private static final AIMLIndex DEFAULT_1D_INDEX = new AIMLIndex();
    
    /**
     * Výchozí 2D index.
     */
    private static final AIML2DIndex DEFAULT_2D_INDEX = new AIML2DIndex();

    /**
     * Prázdná hodnota atributu.
     */
    private static final String EMPTY_VALUE = "";

    /**
     * Nastaví stub a testovanou továrnu.
     */
    @Before
    public void setUp() {
        factory = new DefaultIndexFactory();
        
        elementStub = EasyMock.createMock(Element.class);
    }

    /**
     * Uklidí stub a testovanou továrnu.
     */
    @After
    public void tearDown() {
        factory = null;
        
        elementStub = null;
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#createIndex(org.w3c.dom.Element)}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateIndexWhenLessThanOne() {
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn("0");
        replay(elementStub);
        
        factory.createIndex(elementStub);
        
        verify(elementStub);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#createIndex(org.w3c.dom.Element)}.
     */
    @Test
    public void testCreateIndexWhenValidReturnsIndex() {
        final int value = 33423;
        
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn(String.valueOf(value));
        replay(elementStub);
        
        final AIMLIndex result = (AIMLIndex) factory.createIndex(elementStub);
        
        verify(elementStub);
        
        assertEquals(new AIMLIndex(value), result);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#createIndex(org.w3c.dom.Element)}.
     */
    @Test
    public void testCreateIndexWhenValueNotIntegerReturnsDefault() {
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn("notAnInteger");
        replay(elementStub);
        
        final AIMLIndex result = (AIMLIndex) factory.createIndex(elementStub);
        
        verify(elementStub);
        
        assertEquals(DEFAULT_1D_INDEX, result);
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#create2DIndex(org.w3c.dom.Element)}.
     */
    @Test
    public void testCreate2DIndexWhenValueEmptyReturnsDefault() {
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn(EMPTY_VALUE);
        replay(elementStub);
        
        final AIML2DIndex result = (AIML2DIndex) factory.create2DIndex(elementStub);
        
        verify(elementStub);
        
        assertEquals(DEFAULT_2D_INDEX, result);
    }

    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#create2DIndex(org.w3c.dom.Element)}.
     */
    @Test
    public void testCreate2DIndexWhenOneDimensionalReturnsIndexWithSecondDimensionDefault() {
        final int value = 33423;
        
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn(String.valueOf(value));
        replay(elementStub);
        
        final AIML2DIndex result = (AIML2DIndex) factory.create2DIndex(elementStub);
        
        verify(elementStub);
        
        assertEquals(new AIML2DIndex(value, TwoDimensionalIndex.DEFAULT_VALUE), result);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#create2DIndex(org.w3c.dom.Element)}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreate2DIndexWhenOneDimensionalAndLessThanOne() {
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn("0");
        replay(elementStub);
        
        factory.create2DIndex(elementStub);
        
        verify(elementStub);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#create2DIndex(org.w3c.dom.Element)}.
     */
    @Test
    public void testCreate2DIndexWhenNoCommaAndValueNotIntegerReturnsDefault() {
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn("notAnInteger");
        replay(elementStub);
        
        final AIML2DIndex result = (AIML2DIndex) factory.create2DIndex(elementStub);
        
        verify(elementStub);
        
        assertEquals(DEFAULT_2D_INDEX, result);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#create2DIndex(org.w3c.dom.Element)}.
     */
    @Test
    public void testCreate2DIndexWhenCommaAndFirstValueNotIntegerReturnsDefault() {
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn("notAnInteger,1231");
        replay(elementStub);
        
        final AIML2DIndex result = (AIML2DIndex) factory.create2DIndex(elementStub);
        
        verify(elementStub);
        
        assertEquals(DEFAULT_2D_INDEX, result);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#create2DIndex(org.w3c.dom.Element)}.
     */
    @Test
    public void testCreate2DIndexWhenCommaPresentAndSecondValueNotIntegerReturnsIndexWithSecondDimensionDefault() {
        final int firstValue = 1231;
        
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn(String.valueOf(firstValue) + ",notAnInteger");
        replay(elementStub);
        
        final AIML2DIndex result = (AIML2DIndex) factory.create2DIndex(elementStub);
        
        verify(elementStub);
        
        assertEquals(new AIML2DIndex(firstValue, TwoDimensionalIndex.DEFAULT_VALUE), result);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#create2DIndex(org.w3c.dom.Element)}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreate2DIndexWhenCommaPresentAndFirstLessThanOneAndSecondValueNotInteger() {
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn("0,notAnInteger");
        replay(elementStub);
        
        factory.create2DIndex(elementStub);
        
        verify(elementStub);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#create2DIndex(org.w3c.dom.Element)}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreate2DIndexWhenFirstValuesLessThanOne() {
        final int firstValue = 0;
        final int secondValue = 1231;
        
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn(String.valueOf(firstValue) + "," + String.valueOf(secondValue));
        replay(elementStub);
        
        factory.create2DIndex(elementStub);
        
        verify(elementStub);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#create2DIndex(org.w3c.dom.Element)}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreate2DIndexWhenSecondValueLessThanOne() {
        final int firstValue = 1231;
        final int secondValue = 0;
        
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn(String.valueOf(firstValue) + "," + String.valueOf(secondValue));
        replay(elementStub);
        
        factory.create2DIndex(elementStub);
        
        verify(elementStub);
    }
    
    /**
     * Test pro {@link cz.cuni.mff.ms.brodecva.botnicek.library.parser.DefaultIndexFactory#create2DIndex(org.w3c.dom.Element)}.
     */
    @Test
    public void testCreate2DIndexWhenValidReturnsIndex() {
        final int firstValue = 1231;
        final int secondValue = 43231;
        
        expect(elementStub.getAttribute(INDEX_ATTRIBUTE_NAME)).andReturn(String.valueOf(firstValue) + "," + String.valueOf(secondValue));
        replay(elementStub);
        
        final AIML2DIndex result = (AIML2DIndex) factory.create2DIndex(elementStub);
        
        verify(elementStub);
        
        assertEquals(new AIML2DIndex(firstValue, secondValue), result);
    }
}
