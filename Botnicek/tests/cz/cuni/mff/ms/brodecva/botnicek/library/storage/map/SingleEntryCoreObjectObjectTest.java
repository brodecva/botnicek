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
 * Otestuje {@link SingleEntryCore} s {@link Object} místo obou parametrů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
@Category(UnitTest.class)
public final class SingleEntryCoreObjectObjectTest extends
        AbstractSingleEntryCoreTest<Object, Object> {

    /**
     * Příklad neplatné kapacity.
     */
    private static final int ILLEGAL_CAPACITY_EXAMPLE = 2;

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.
     * AbstractSingleEntryCoreTest#getUniqueValidKeys(int)
     */
    @Override
    protected Object[] getUniqueValidKeys(final int count) {
        return ObjectGenerator.getUniqueObjects(count);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.
     * AbstractSingleEntryCoreTest#getEqualValidKeys(int)
     */
    @Override
    protected Object[] getEqualValidKeys(final int count) {
        return ObjectGenerator.getEqualObjects(count);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.
     * AbstractSingleEntryCoreTest#getUniqueValidValues(int)
     */
    @Override
    protected Object[] getUniqueValidValues(final int count) {
        return ObjectGenerator.getUniqueObjects(count);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.
     * AbstractSingleEntryCoreTest#getEqualValidValues(int)
     */
    @Override
    protected Object[] getEqualValidValues(final int count) {
        return ObjectGenerator.getEqualObjects(count);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.
     * AbstractSingleEntryCoreTest#testSingleEntryCoreInstantiation()
     */
    @Override
    public void testSingleEntryCoreInstantiation() {
        new SingleEntryCore<Object, Object>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.
     * AbstractSingleEntryCoreTest
     * #testSingleEntryCoreIntWhenLegalCapacityInstantiation()
     */
    @Override
    public void testSingleEntryCoreIntWhenLegalCapacityInstantiation() {
        new SingleEntryCore<Object, Object>(SingleEntryCore.CAPACITY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.
     * AbstractSingleEntryCoreTest #testSingleEntryCoreIntWhenIllegalCapacity()
     */
    @Override
    public void testSingleEntryCoreIntWhenIllegalCapacity() {
        new SingleEntryCore<Object, Object>(ILLEGAL_CAPACITY_EXAMPLE);
    }

}
