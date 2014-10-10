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
package cz.cuni.mff.ms.brodecva.botnicek.ide.translate;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje výchozí implementaci továrny překládajících pozorovatelů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
@Category(UnitTest.class)
public class DefaultTranslatorFactoryTest {

    private final static class UniqueNormalWordStub implements NormalWord {

        private static AtomicInteger counter = new AtomicInteger();

        private final int order = counter.getAndIncrement();

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(final NormalWord other) {
            Preconditions.checkNotNull(other);

            return getText().compareTo(other.getText());
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
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

            final UniqueNormalWordStub other = (UniqueNormalWordStub) obj;
            if (this.order != other.order) {
                return false;
            }

            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord#getText()
         */
        @Override
        public String getText() {
            return String.valueOf(this.order);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + this.order;
            return result;
        }
    }

    private DefaultTranslatorFactory tested = Intended.nullReference();

    /**
     * Vytvoří testovanou instanci.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @Before
    public void setUp() throws Exception {
        this.tested =
                DefaultTranslatorFactory.create(new UniqueNormalWordStub(),
                        new UniqueNormalWordStub(), new UniqueNormalWordStub(),
                        new UniqueNormalWordStub(), new UniqueNormalWordStub(),
                        new UniqueNormalWordStub());
    }

    /**
     * Uklidí testovanou instance.
     * 
     * @throws java.lang.Exception
     *             pokud dojde k vyhození výjimky
     */
    @After
    public void tearDown() throws Exception {
        this.tested = Intended.nullReference();
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatorFactory#create(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)}
     * .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWhenSomeStatesEqual() {
        final NormalWord copy = new UniqueNormalWordStub();

        DefaultTranslatorFactory.create(new UniqueNormalWordStub(), copy, copy,
                new UniqueNormalWordStub(), new UniqueNormalWordStub(),
                new UniqueNormalWordStub());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatorFactory#create(cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord, cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord)}
     * .
     */
    @Test
    public void testCreateWhenStatesDifferent() {
        DefaultTranslatorFactory.create(new UniqueNormalWordStub(),
                new UniqueNormalWordStub(), new UniqueNormalWordStub(),
                new UniqueNormalWordStub(), new UniqueNormalWordStub(),
                new UniqueNormalWordStub());
    }

    /**
     * Test method for
     * {@link cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatorFactory#produce()}
     * .
     */
    @Test
    public void testProduce() {
        this.tested.produce();
    }
}
