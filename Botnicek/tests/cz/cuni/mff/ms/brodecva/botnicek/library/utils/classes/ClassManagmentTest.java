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

import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testy pro správu tříd.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see ClassManagment
 */
@Category(UnitTest.class)
public final class ClassManagmentTest {

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassManagment#getNewInstance(java.lang.Class)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testGetNewInstanceWhenClassNull() {
        ClassManagment.getNewInstance(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassManagment#getNewInstance(java.lang.Class)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testGetNewInstanceWhenClassArgumentsNull() {
        ClassManagment.getNewInstance(Object.class, null, (Object[]) null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassManagment#getNewInstance(java.lang.Class)}
     * .
     */
    @Test(expected = ClassControlError.class)
    public void testGetNewInstanceWhenConstructorWtihArgumentsNotExists() {
        ClassManagment.getNewInstance(Object.class, String.class,
                "invalidArgument");
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassManagment#getNewInstance(java.lang.Class)}
     * .
     */
    @Test
    public void testGetNewInstanceWhenConstructorWithoutArguments() {
        assertTrue(ClassManagment.getNewInstance(Object.class) instanceof Object);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassManagment#getNewInstance(java.lang.Class)}
     * .
     */
    @Test
    public void testGetNewInstanceWhenConstructorWithOneArgument() {
        assertTrue(ClassManagment.getNewInstance(String.class, String.class,
                "argument") instanceof String);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassManagment#getNewInstance(java.lang.Class)}
     * .
     */
    @Test
    public void testGetNewInstanceWhenConstructorWithMultipleArguments() {
        assertTrue(ClassManagment.getNewInstance(Point.class, Integer.TYPE, 1,
                2) instanceof Point);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassManagment#getNewInstance(java.lang.Class)}
     * .
     */
    @Test(expected = ClassControlError.class)
    public void testGetNewInstanceWhenArgumentsMismatchType() {
        assertTrue(ClassManagment.getNewInstance(Point.class, Object.class, 1,
                2) instanceof Point);
    }
}
