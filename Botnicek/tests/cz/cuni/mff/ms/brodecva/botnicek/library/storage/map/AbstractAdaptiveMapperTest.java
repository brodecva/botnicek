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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage.map;

import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

/**
 * Testuje adaptivní mapu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AdaptiveMapper
 * 
 * @param <K>
 *            klíč
 * @param <V>
 *            hodnota
 */
public abstract class AbstractAdaptiveMapperTest<K, V> {

    /**
     * Mapa.
     */
    private AdaptiveMapper<K, V> mapper = null;

    /**
     * Výsledek volání @{link MapperCore#toString} na jádru.
     */
    private MapperCore<K, V> coreMock = null;

    /**
     * Vytvoří novou prázdnou mapu z mocku jádra.
     */
    @SuppressWarnings("unchecked")
    @Before
    public final void setUp() {
        coreMock = EasyMock.createNiceMock(MapperCore.class);
        replay(coreMock);

        mapper = new AdaptiveMapper<K, V>(coreMock);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AdaptiveMapper#AdaptiveMapper(MapperCore)}
     * .
     */
    @Test
    public final void testAdaptiveMapperInstantiation() {
        @SuppressWarnings("unchecked")
        final MapperCore<K, V> core = EasyMock.createNiceMock(MapperCore.class);
        replay(core);

        new AdaptiveMapper<K, V>(core);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AdaptiveMapper#AdaptiveMapper(MapperCore)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public final void testAddaptiveMapperWhenCoreNull() {
        new AdaptiveMapper<K, V>(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AdaptiveMapper#equals(java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AdaptiveMapper#hashCode()}
     * .
     */
    @Test
    public final void testEqualsAndHashCode() {
        EqualsVerifier.forClass(AdaptiveMapper.class)
                .suppress(Warning.NONFINAL_FIELDS, Warning.NULL_FIELDS)
                .usingGetClass().verify();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AdaptiveMapper#toString()}
     * .
     */
    @Test
    public final void testToString() {
        final String expected =
                "AdaptiveMapper [core=" + coreMock.toString() + "]";

        final String result = mapper.toString();

        assertEquals(expected, result);
    }

}
