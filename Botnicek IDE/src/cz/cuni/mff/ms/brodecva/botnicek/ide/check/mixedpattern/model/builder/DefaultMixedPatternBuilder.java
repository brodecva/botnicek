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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.builder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.MixedPatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Objects;

/**
 * Výchozí implementace konstruktoru validního složeného vzoru dle specifikace
 * jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultMixedPatternBuilder implements MixedPatternBuilder, Source {

    private final static class MixedPatternImplementation implements
            MixedPattern, Serializable {
        private static final long serialVersionUID = 1L;

        public static MixedPatternImplementation
                create(final String rawContent) {
            return new MixedPatternImplementation(rawContent);
        }

        private final String text;

        private MixedPatternImplementation(final String rawContent) {
            Preconditions.checkNotNull(rawContent);

            this.text = rawContent;
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
            if (!(obj instanceof MixedPattern)) {
                return false;
            }
            final MixedPattern other = (MixedPattern) obj;
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

        private void writeObject(final ObjectOutputStream objectOutputStream)
                throws IOException {
            objectOutputStream.defaultWriteObject();
        }
    }

    /**
     * Vytvoří konstruktor.
     * 
     * @param checker
     *            přímý validátor
     * @param startContent
     *            úvodní řetězec k sestavení
     * @return konstruktor
     */
    public static DefaultMixedPatternBuilder create(
            final MixedPatternChecker checker, final String startContent) {
        return new DefaultMixedPatternBuilder(checker, startContent);
    }

    private final StringBuilder contentBuilder;

    private final MixedPatternChecker checker;

    private DefaultMixedPatternBuilder(final MixedPatternChecker checker,
            final String startContent) {
        Preconditions.checkNotNull(checker);
        Preconditions.checkNotNull(startContent);

        this.checker = checker;
        this.contentBuilder = new StringBuilder(startContent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.
     * ContentAggregator#add(java.lang.String)
     */
    @Override
    public void add(final String content) {
        this.contentBuilder.append(content);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.PatternContentBuilder
     * #build()
     */
    @Override
    public MixedPattern build() {
        if (!isValid()) {
            throw new IllegalStateException();
        }

        return MixedPatternImplementation
                .create(this.contentBuilder.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.PatternContentBuilder
     * #check()
     */
    @Override
    public CheckResult check() {
        return this.checker.check(this, this, this.contentBuilder.toString());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.PatternContentBuilder
     * #isValid()
     */
    @Override
    public boolean isValid() {
        return check().isValid();
    }
}
