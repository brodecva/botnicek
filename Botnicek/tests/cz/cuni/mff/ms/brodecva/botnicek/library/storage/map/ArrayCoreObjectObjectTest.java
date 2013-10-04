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

import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Otestuje {@link ArrayCore} s {@link Object} místo obou parametrů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
@Category(UnitTest.class)
public final class ArrayCoreObjectObjectTest extends
        AbstractArrayCoreTest<Object, Object> {

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractArrayCoreTest
     * #getUniqueValidKeys(int)
     */
    @Override
    protected Object[] getUniqueValidKeys(final int count) {
        return ObjectGenerator.getUniqueObjects(count);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractArrayCoreTest
     * #getEqualValidKeys(int)
     */
    @Override
    protected Object[] getEqualValidKeys(final int count) {
        return ObjectGenerator.getEqualObjects(count);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractArrayCoreTest
     * #getUniqueValidValues(int)
     */
    @Override
    protected Object[] getUniqueValidValues(final int count) {
        return ObjectGenerator.getUniqueObjects(count);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractArrayCoreTest
     * #getEqualValidValues(int)
     */
    @Override
    protected Object[] getEqualValidValues(final int count) {
        return ObjectGenerator.getEqualObjects(count);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractArrayCoreTest
     * #testArrayCoreInstantiation()
     */
    @Override
    public void testArrayCoreInstantiation() {
        new ArrayCore<Object, Object>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractArrayCoreTest
     * #testArrayCoreIntWhenLegalCapacityInstantiation()
     */
    @Override
    public void testArrayCoreIntWhenLegalCapacityInstantiation() {
        new ArrayCore<Object, Object>(ArrayCore.MINIMUM_CAPACITY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractArrayCoreTest
     * #testArrayCoreIntWhenMoreThanLegalCapacity()
     */
    @Override
    public void testArrayCoreIntWhenMoreThanLegalCapacity() {
        new ArrayCore<Object, Object>(ArrayCore.MAXIMUM_CAPACITY + 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractArrayCoreTest
     * #testArrayCoreIntWhenLessThanLegalCapacity()
     */
    @Override
    public void testArrayCoreIntWhenLessThanLegalCapacity() {
        new ArrayCore<Object, Object>(ArrayCore.MINIMUM_CAPACITY - 1);
    }

}
