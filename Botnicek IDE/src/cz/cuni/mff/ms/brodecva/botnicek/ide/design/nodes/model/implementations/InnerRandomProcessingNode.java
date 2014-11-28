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
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.implementations;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.DispatchProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.ProceedProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.StackProcessor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.AbstractNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.InnerNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.Node;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.ProcessingNode;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model.RandomNode;

/**
 * Vnitřní náhodný procesní uzel.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class InnerRandomProcessingNode extends AbstractNode implements
        RandomNode, ProcessingNode, InnerNode {

    private static final long serialVersionUID = 1L;

    /**
     * Zkopíruje uzel.
     * 
     * @param original
     *            původní uzel
     * @return kopie
     */
    public static InnerRandomProcessingNode create(final Node original) {
        return new InnerRandomProcessingNode(original);
    }

    /**
     * Vytvoří uzel dle parametrů.
     * 
     * @param name
     *            název uzlu
     * @param parent
     *            rodičovská síť
     * @param x
     *            umístění uzlu v souřadnici x
     * @param y
     *            umístění uzlu v souřadnici y
     * @return uzel
     */
    public static InnerRandomProcessingNode create(final NormalWord name,
            final Network parent, final int x, final int y) {
        return new InnerRandomProcessingNode(name, parent, x, y);
    }

    private InnerRandomProcessingNode(final Node original) {
        super(original);
    }

    private InnerRandomProcessingNode(final NormalWord name,
            final Network parent, final int x, final int y) {
        super(name, parent, x, y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.DispatchProcessible#accept
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.DispatchProcessor)
     */
    @Override
    public <T> T accept(final DispatchProcessor<? extends T> processor) {
        return processor.process(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.ProceedProcessible#accept
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.ProceedProcessor)
     */
    @Override
    public <T> T accept(final ProceedProcessor<? extends T> processor) {
        return processor.process(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.StackProcessible#accept
     * (cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.StackProcessor)
     */
    @Override
    public <T> T accept(final StackProcessor<? extends T> processor) {
        return processor.process(this);
    }
}
