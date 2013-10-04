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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes;

import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Otestuje {@link AbstractSimpleClassMapTest} se {@link String} a
 * {@link Object} místo parametrů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
@Category(UnitTest.class)
public final class SimpleClassMapStringObjectTest extends
        AbstractSimpleClassMapTest<String, Object> {

    /**
     * Prázdné rozhraní pro implementaci anonymními třídami při vytváření
     * unikátních tříd.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    interface EmptyInterface {
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.
     * AbstractSimpleClassMapTest#getUniqueValidKeys(int)
     */
    @Override
    protected String[] getUniqueValidKeys(final int count) {
        return ObjectGenerator.getUniqueStrings(count);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.
     * AbstractSimpleClassMapTest#getEqualValidKeys(int)
     */
    @Override
    protected String[] getEqualValidKeys(final int count) {
        return ObjectGenerator.getEqualStrings(count);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.
     * AbstractSimpleClassMapTest#getUniqueValidValues(int)
     */
    @Override
    protected Class<? extends Object>[] getUniqueValidValues(final int count) {
        final Class<?>[] result = new Class<?>[count];

        for (int i = 0; i < count; i++) {
            result[i] = (new EmptyInterface() {
            }).getClass();
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.
     * AbstractSimpleClassMapTest#getEqualValidValues(int)
     */
    @Override
    protected Class<? extends Object>[] getEqualValidValues(final int count) {
        final Class<?>[] result = new Class<?>[count];

        final Class<Object> prototype = Object.class;

        for (int i = 0; i < count; i++) {
            result[i] = prototype;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.
     * AbstractSimpleClassMapTest#testHashMapCoreInstantiation()
     */
    @Override
    public void testHashMapCoreInstantiation() {
        new SimpleClassMap<String, Object>();
    }

}
