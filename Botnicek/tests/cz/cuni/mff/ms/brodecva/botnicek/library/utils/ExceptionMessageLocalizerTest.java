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

import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;


/**
 * Test třídy pro překlad výjimek.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see ExceptionMessageLocalizer
 */
@Category(UnitTest.class)
public final class ExceptionMessageLocalizerTest {
    /**
     * Zdrojový balík nespoléhající se na souborový systém.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    static final class TestMessageBundle extends ListResourceBundle {

        /**
         * Obsah.
         */
        private final Object[][] contents = new Object[][] {

                { "dummyMessage", "translated dummy message" },

                { "oneStringParameterMessage", "My favourite color is {0}." },

                { "oneIntParameterMessage", "My favourite number is {0}." },

                { "oneNullParameterMessage", "Not initialized is {0}." },

                { "oneToStringParameterMessage", "{0} is a list of numbers." },

                { "threeParametersMessage", "{0} and {1} and {2} are three." },

                { "multipleOccurencesMessage",
                        "{0} and {0} are the same, not {1}, but {0} as well." }

        };

        /*
         * (non-Javadoc)
         * 
         * @see java.util.ListResourceBundle#getContents()
         */
        @Override
        protected Object[][] getContents() {
            return contents;
        }

    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer#ExceptionMessageLocalizer(java.util.ResourceBundle)}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testExceptionMessageLocalizerResourceBundleWhenNull() {
        new ExceptionMessageLocalizer(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer#ExceptionMessageLocalizer(java.util.ResourceBundle)}
     * .
     */
    @Test
    public void testExceptionMessageLocalizerResourceBundleCreation() {
        final ResourceBundle bundleStub = EasyMock.createMock(ResourceBundle.class);

        replay(bundleStub);

        new ExceptionMessageLocalizer(bundleStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer#getMessage(java.lang.String, java.lang.Object[])}
     * .
     */
    @Test
    public void testGetMessageWithoutParameters() {
        final ExceptionMessageLocalizer localizer =
                new ExceptionMessageLocalizer(new TestMessageBundle());

        final String output = localizer.getMessage("dummyMessage");

        assertEquals("translated dummy message", output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer#getMessage(java.lang.String, java.lang.Object[])}
     * .
     */
    @Test
    public void testGetMessageWithOneValidStringParameter() {
        final ExceptionMessageLocalizer localizer =
                new ExceptionMessageLocalizer(new TestMessageBundle());

        final String output =
                localizer.getMessage("oneStringParameterMessage", "red");

        assertEquals("My favourite color is red.", output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer#getMessage(java.lang.String, java.lang.Object[])}
     * .
     */
    @Test
    public void testGetMessageWithOneValidPrimitiveParameter() {
        final ExceptionMessageLocalizer localizer =
                new ExceptionMessageLocalizer(new TestMessageBundle());

        final String output = localizer.getMessage("oneIntParameterMessage", 7);

        assertEquals("My favourite number is 7.", output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer#getMessage(java.lang.String, java.lang.Object[])}
     * .
     */
    @Test
    public void testGetMessageWithOneNullParameter() {
        final ExceptionMessageLocalizer localizer =
                new ExceptionMessageLocalizer(new TestMessageBundle());

        final String output =
                localizer.getMessage("oneNullParameterMessage",
                        new Object[] { null });

        assertEquals("Not initialized is null.", output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer#getMessage(java.lang.String, java.lang.Object[])}
     * .
     */
    @Test
    public void testGetMessageWithNullParameters() {
        final ExceptionMessageLocalizer localizer =
                new ExceptionMessageLocalizer(new TestMessageBundle());

        final String output =
                localizer.getMessage("dummyMessage", (Object[]) null);

        assertEquals("translated dummy message", output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer#getMessage(java.lang.String, java.lang.Object[])}
     * .
     */
    @Test
    public void testGetMessageWithOneValidToStringEmployingParameter() {
        final ExceptionMessageLocalizer localizer =
                new ExceptionMessageLocalizer(new TestMessageBundle());

        final String output =
                localizer.getMessage("oneToStringParameterMessage",
                        Arrays.asList(new Integer[] { 1, 2, 3 }));

        assertEquals("[1, 2, 3] is a list of numbers.", output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer#getMessage(java.lang.String, java.lang.Object[])}
     * .
     */
    @Test(expected = NullPointerException.class)
    public void testGetMessageNullSourceMessage() {
        final ExceptionMessageLocalizer localizer =
                new ExceptionMessageLocalizer(new TestMessageBundle());

        localizer.getMessage(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer#getMessage(java.lang.String, java.lang.Object[])}
     * .
     */
    @Test
    public void testGetMessageWithMultipleOccurences() {
        final ExceptionMessageLocalizer localizer =
                new ExceptionMessageLocalizer(new TestMessageBundle());

        final String output =
                localizer.getMessage("multipleOccurencesMessage", 1, 2);

        assertEquals("1 and 1 are the same, not 2, but 1 as well.", output);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer#getMessage(java.lang.String, java.lang.Object[])}
     * .
     */
    @Test
    public void testGetMessageWithMultipleParameters() {
        final ExceptionMessageLocalizer localizer =
                new ExceptionMessageLocalizer(new TestMessageBundle());

        final String output =
                localizer.getMessage("threeParametersMessage", 1, 2, 3);

        assertEquals("1 and 2 and 3 are three.", output);
    }
}
