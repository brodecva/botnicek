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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje pomocné metody pro práci s komponentami.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Components
 */
@Category(UnitTest.class)
public class ComponentsTest {

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.Components#hasParent(java.awt.Container)}
     * .
     */
    @Test
    public void testHasParentWhenWithoutReturnsFalse() {
        assertFalse(Components.hasParent((new JComponent() {

            private static final long serialVersionUID = 1L;

        }).getParent()));
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.utils.swing.Components#hasParent(java.awt.Container)}
     * .
     */
    @Test
    public void testHasParentWhenWithReturnsTrue() {
        final Component component = new JComponent() {

            private static final long serialVersionUID = 1L;

        };
        final Container container = new JComponent() {

            private static final long serialVersionUID = 1L;

        };
        container.add(component);

        assertTrue(Components.hasParent(component.getParent()));
    }
}
