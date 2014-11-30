/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Checker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.simplepattern.model.checker.DefaultSimplePatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;

/**
 * Pomocné metody pro práci se vzory jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see <a
 *      href="http://www.alicebot.org/TR/2011/#section-pattern-expression-syntax">http://www.alicebot.org/TR/2011/#section-pattern-expression-syntax</a>
 */
public final class Patterns {

    /**
     * Interní implementace prostého vzoru. Složený vzor není při konstrukci
     * výpočetních prvků potřeba.
     */
    private static final class SimplePatternImplementation implements
            SimplePattern, Serializable {
        private static final long serialVersionUID = 1L;

        public static SimplePatternImplementation create(final String text) {
            return new SimplePatternImplementation(text);
        }

        private final String text;

        private SimplePatternImplementation(final String text) {
            Preconditions.checkNotNull(text);
            Preconditions.checkArgument(!text.isEmpty());

            this.text = text;
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
            if (Objects.isNull(obj)) {
                return false;
            }
            if (!(obj instanceof SimplePattern)) {
                return false;
            }
            final SimplePattern other = (SimplePattern) obj;
            if (!this.text.equals(other.getText())) {
                return false;
            }
            return true;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.Pattern
         * #getText()
         */
        @Override
        public String getText() {
            return this.text;
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
            result = prime * result + this.text.hashCode();
            return result;
        }

        private void readObject(final ObjectInputStream objectInputStream)
                throws ClassNotFoundException, IOException {
            objectInputStream.defaultReadObject();

            Preconditions.checkNotNull(this.text);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "SimplePattern [text=" + this.text + "]";
        }

        private void writeObject(final ObjectOutputStream objectOutputStream)
                throws IOException {
            objectOutputStream.defaultWriteObject();
        }
    }

    private static Checker checker =
            new DefaultSimplePatternChecker();

    /**
     * Vytvoří vzor. Text musí splňovat syntaktická pravidla prostého vzoru, tj.
     * být neprázdný, obsahovat pouze normální znaky a žolíky.
     * 
     * @param text
     *            text vzoru
     * @return vrátí vzor s daným popisem
     */
    public static SimplePattern create(final String text) {
        Preconditions.checkNotNull(text);
        Preconditions.checkArgument(checker.check(text).isValid());

        return SimplePatternImplementation.create(text);
    }

    /**
     * Vytvoří vzor obsahující pouze žolík hvězdičku, tj. vzor, vůči kterému
     * projde při porovnání jakýkoli text.
     * 
     * @return vzor s hvězdičkou
     */
    public static SimplePattern createUniversal() {
        return SimplePatternImplementation
                .create(AIML.STAR_WILDCARD.getValue());
    }
}
