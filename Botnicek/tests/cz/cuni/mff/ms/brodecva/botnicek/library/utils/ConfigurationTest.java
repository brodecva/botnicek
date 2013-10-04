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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils;

import static org.junit.Assert.assertEquals;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;

import cz.cuni.mff.ms.brodecva.botnicek.library.api.ConfigurationException;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassMap;

/**
 * Testuje pomocnou třídu pro konfiguraci.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Configuration
 */
public final class ConfigurationTest {
    
    /**
     * Testovací validní implementace {@link DisplayStrategy}.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    static final class ValidDisplayStrategyStub implements DisplayStrategy, Serializable {

        /**
         * UID serializované verze.
         */
        private static final long serialVersionUID = -6831910735894251548L;

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy#display(java.lang.String, java.lang.String)
         */
        @Override
        public String display(final String name, final String value) {
            return "output";
        }
        
        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy
         * #hashCode()
         */
        @Override
        public int hashCode() {
            return getClass().hashCode();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy
         * #equals( java.lang.Object)
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            return true;
        }        
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration#readValue(java.util.Map, java.lang.String)}.
     * @throws ConfigurationException chyba v konfiguraci
     */
    @Test(expected = ConfigurationException.class)
    public void testReadValueWhenValueNull() throws ConfigurationException {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", null);
        
        Configuration.readValue(properties, "key");
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration#readPath(java.util.Map, java.lang.String)}.
     * @throws ConfigurationException chyba v konfiguraci
     */
    @Test(expected = ConfigurationException.class)
    public void testReadPathWhenInvalidPath() throws ConfigurationException {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", "//////");
        
        Configuration.readPath(properties, "key");
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration#readLoadingOrder(java.util.Map, java.lang.String)}.
     * @throws ConfigurationException chyba v konfiguraci
     */
    @Test
    public void testReadLoadingOrderWhenNone() throws ConfigurationException {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", "");
        
        assertEquals(new ArrayList<String>(0), Configuration.readLoadingOrder(properties, "key"));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration#readLoadingOrder(java.util.Map, java.lang.String)}.
     * @throws ConfigurationException chyba v konfiguraci
     */
    @Test
    public void testReadLoadingOrderWhenOne() throws ConfigurationException {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", "file.txt");
        
        assertEquals(Arrays.asList(new String[] { "file.txt" }), Configuration.readLoadingOrder(properties, "key"));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration#readLoadingOrder(java.util.Map, java.lang.String)}.
     * @throws ConfigurationException chyba v konfiguraci
     */
    @Test
    public void testReadLoadingOrderWhenMultiple() throws ConfigurationException {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", "file.txt/other/another.file.txt");
        
        assertEquals(Arrays.asList(new String[] { "file.txt", "other", "another.file.txt" }), Configuration.readLoadingOrder(properties, "key"));
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration#readPattern(java.util.Map, java.lang.String)}.
     * @throws ConfigurationException chyba v konfiguraci
     */
    @Test(expected = ConfigurationException.class)
    public void testReadPatternWhenInvalid() throws ConfigurationException {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("key", "{([");
        
        Configuration.readPattern(properties, "key");
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration#readReplacements(java.util.Map)}.
     * @throws ConfigurationException chyba v konfiguraci
     */
    @Test(expected = ConfigurationException.class)
    public void testReadReplacementsWhenValueNull() throws ConfigurationException {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("\\.", null);
        
        Configuration.readReplacements(properties);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration#readReplacements(java.util.Map)}.
     * @throws ConfigurationException chyba v konfiguraci
     */
    @Test(expected = ConfigurationException.class)
    public void testReadReplacementsWhenKeyPatternInvalid() throws ConfigurationException {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("[{(", "dummyReplacement");
        
        Configuration.readReplacements(properties);
    }

    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration#readValidStrategies(java.util.Map, cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassMap)}.
     * @throws ConfigurationException chyba v konfiguraci
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testReadValidStrategiesWhenValidReturnsExpected() throws ConfigurationException {
        final Map<String, String> displayStrategies = new HashMap<String, String>();
        displayStrategies.put("name", "dummyStrategy");
        
        final DisplayStrategy strategyStub = new ValidDisplayStrategyStub();
        
        @SuppressWarnings("unchecked")
        final ClassMap<String, DisplayStrategy> strategiesMap = EasyMock.createMock(ClassMap.class);
        expect((Class) strategiesMap.get("dummyStrategy")).andStubReturn(strategyStub.getClass());
        replay(strategiesMap);
        
        final Map<String, DisplayStrategy> expected = new HashMap<String, DisplayStrategy>();
        expected.put("name", strategyStub);
        
        assertEquals(expected, Configuration.readValidStrategies(displayStrategies, strategiesMap));
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration#readValidStrategies(java.util.Map, cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassMap)}.
     * @throws ConfigurationException chyba v konfiguraci
     */
    @SuppressWarnings("rawtypes")
    @Test(expected = ConfigurationException.class)
    public void testReadValidStrategiesWhenNullName() throws ConfigurationException {
        final Map<String, String> displayStrategies = new HashMap<String, String>();
        displayStrategies.put(null, "dummyStrategy");
        
        final DisplayStrategy strategyStub = new ValidDisplayStrategyStub();
        
        @SuppressWarnings("unchecked")
        final ClassMap<String, DisplayStrategy> strategiesMap = EasyMock.createMock(ClassMap.class);
        expect((Class) strategiesMap.get("dummyStrategy")).andStubReturn(strategyStub.getClass());
        replay(strategiesMap);
        
        Configuration.readValidStrategies(displayStrategies, strategiesMap);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration#readValidStrategies(java.util.Map, cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassMap)}.
     * @throws ConfigurationException chyba v konfiguraci
     */
    @SuppressWarnings("rawtypes")
    @Test(expected = ConfigurationException.class)
    public void testReadValidStrategiesWhenImplementationInacessible() throws ConfigurationException {
        final Map<String, String> displayStrategies = new HashMap<String, String>();
        displayStrategies.put("name", "dummyStrategy");
        
        @SuppressWarnings("unchecked")
        final ClassMap<String, DisplayStrategy> strategiesMap = EasyMock.createMock(ClassMap.class);
        expect((Class) strategiesMap.get("dummyStrategy")).andStubReturn(InacessibleDisplayStrategyStub.class);
        replay(strategiesMap);
        
        Configuration.readValidStrategies(displayStrategies, strategiesMap);
    }
    
    /**
     * Test method for {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.Configuration#readValidStrategies(java.util.Map, cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassMap)}.
     * @throws ConfigurationException chyba v konfiguraci
     */
    @SuppressWarnings("rawtypes")
    @Test(expected = ConfigurationException.class)
    public void testReadValidStrategiesWhenImplementationUninstantiable() throws ConfigurationException {
        final Map<String, String> displayStrategies = new HashMap<String, String>();
        displayStrategies.put("name", "dummyStrategy");
        
        @SuppressWarnings("unchecked")
        final ClassMap<String, DisplayStrategy> strategiesMap = EasyMock.createMock(ClassMap.class);
        expect((Class) strategiesMap.get("dummyStrategy")).andStubReturn(DisplayStrategy.class);
        replay(strategiesMap);
        
        Configuration.readValidStrategies(displayStrategies, strategiesMap);
    }
}
