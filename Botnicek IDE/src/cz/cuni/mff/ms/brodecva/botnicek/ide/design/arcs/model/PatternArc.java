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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.types.Priority;

/**
 * Implementace hrany, jejíž test projde tehdy, pokud uživatelský vstup odpovídá
 * vzorům kategorie jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class PatternArc extends AbstractCodeArc {

    private static final long serialVersionUID = 1L;

    /**
     * Vytvoří hranu.
     * 
     * @param parent
     *            rodičovská síť
     * @param name
     *            název hrany
     * @param priority
     *            priorita
     * @param code
     *            kód k provedení v případě splnění testu
     * @param pattern
     *            očekávaná hodnota testu
     * @param that
     *            kód generující testovanou hodnotu
     * @return hrana
     */
    public static PatternArc create(final Network parent,
            final NormalWord name, final Priority priority, final Code code,
            final MixedPattern pattern, final MixedPattern that) {
        return new PatternArc(parent, name, priority, code, pattern, that);
    }

    private final MixedPattern pattern;

    private final MixedPattern that;

    private PatternArc(final Network parent, final NormalWord name,
            final Priority priority, final Code code,
            final MixedPattern pattern, final MixedPattern that) {
        super(parent, name, priority, code);

        Preconditions.checkNotNull(pattern);
        Preconditions.checkNotNull(that);

        this.pattern = pattern;
        this.that = that;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processible
     * #accept
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.arcs.model.api.Processor)
     */
    @Override
    public <T> T accept(final Processor<T> processor) {
        return processor.process(this);
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
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PatternArc other = (PatternArc) obj;
        if (!this.pattern.equals(other.pattern)) {
            return false;
        }
        if (!this.that.equals(other.that)) {
            return false;
        }
        return true;
    }

    /**
     * Vrátí vzor.
     * 
     * @return vzor
     */
    public MixedPattern getPattern() {
        return this.pattern;
    }

    /**
     * Vrátí vzor zmínky.
     * 
     * @return vzor zmínky
     */
    public MixedPattern getThat() {
        return this.that;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.pattern.hashCode();
        result = prime * result + this.that.hashCode();
        return result;
    }

    private void readObject(final ObjectInputStream objectInputStream)
            throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();

        Preconditions.checkNotNull(this.pattern);
        Preconditions.checkNotNull(this.that);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PatternArc [getName()=" + getName() + ", getNetwork()="
                + getNetwork() + ", getFrom()=" + getFrom() + ", getTo()="
                + getTo() + ", getPriority()=" + getPriority() + ", pattern="
                + this.pattern + ", that=" + this.that + "]";
    }

    private void writeObject(final ObjectOutputStream objectOutputStream)
            throws IOException {
        objectOutputStream.defaultWriteObject();
    }
}
