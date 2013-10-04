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

import java.util.HashMap;

import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.ObjectGenerator;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Otestuje {@link HashMapCore} s {@link Object} místo obou parametrů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
@Category(UnitTest.class)
public final class HashMapCoreObjectObjectTest extends
        AbstractHashMapCoreTest<Object, Object> {

    /**
     * Příklad platné kapacity.
     */
    private static final int LEGAL_CAPACITY_EXAMPLE = 1000;

    /**
     * Příklad neplatné kapacity.
     */
    private static final int ILLEGAL_CAPACITY_EXAMPLE = -1;

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractHashMapCoreTest
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
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractHashMapCoreTest
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
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractHashMapCoreTest
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
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractHashMapCoreTest
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
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractHashMapCoreTest
     * #testHashMapCoreInstantiation()
     */
    @Override
    public void testHashMapCoreInstantiation() {
        new HashMap<Object, Object>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractHashMapCoreTest
     * #testHashMapCoreIntWhenLegalCapacityInstantiation()
     */
    @Override
    public void testHashMapCoreIntWhenLegalCapacityInstantiation() {
        new HashMap<Object, Object>(LEGAL_CAPACITY_EXAMPLE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.AbstractHashMapCoreTest
     * #testHashMapCoreIntWhenLessThanLegalCapacity()
     */
    @Override
    public void testHashMapCoreIntWhenLessThanLegalCapacity() {
        new HashMap<Object, Object>(ILLEGAL_CAPACITY_EXAMPLE);
    }

}
