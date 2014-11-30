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
package cz.cuni.mff.ms.brodecva.botnicek.ide.compile;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.elements.toplevel.Topic;
import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DefaultDfsVisitorFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsVisitor;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.api.dfs.DfsVisitorFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.networks.model.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.design.system.model.System;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.DefaultTranslatorFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.TranslatingObserver;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.TranslatorFactory;

/**
 * Výchozí implementace kompilátoru.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultCompiler implements Compiler {

    /**
     * Vytvoří kompilátor s výchozími nastaveními.
     * 
     * @param pullState
     *            název stavu začínajícího vyhození nezpracovaných stavů ze
     *            zásobníku
     * @param pullStopState
     *            název stavu ukončujícího vyhození nezpracovaných stavů ze
     *            zásobníku
     * @param successState
     *            název stavu pro úspěšný průchod podsítí
     * @param returnState
     *            název stavu návratu z podsítě
     * @param randomizeState
     *            název stavu pro zamíchání stavy
     * @param testingPredicate
     *            název testovacího predikátu
     * @return kompilátor
     */
    public static DefaultCompiler create(final NormalWord pullState,
            final NormalWord pullStopState, final NormalWord successState,
            final NormalWord returnState, final NormalWord randomizeState,
            final NormalWord testingPredicate) {
        final DefaultTranslatorFactory translatorFactory =
                DefaultTranslatorFactory.create(pullState, pullStopState,
                        randomizeState, successState, returnState,
                        testingPredicate);

        return create(translatorFactory, DefaultDfsVisitorFactory.create());
    }

    /**
     * Vytvoří kompilátor, který bude pomocí návštěvníka (dodaného továrnou)
     * procházet systém a nahlášené prvky grafu pak budou zpracovávány
     * překládajícím pozorovatelem.
     * 
     * @param translatorFactory
     *            továrna na překládající pozorovatele průchodu grafem
     * @param dfsVisitorFactory
     *            továrna na návštěvníky grafu
     * @return kompilátor
     */
    public static DefaultCompiler create(
            final TranslatorFactory translatorFactory,
            final DfsVisitorFactory dfsVisitorFactory) {
        Preconditions.checkNotNull(translatorFactory);
        Preconditions.checkNotNull(dfsVisitorFactory);

        return new DefaultCompiler(translatorFactory, dfsVisitorFactory);
    }

    private final TranslatorFactory translatorFactory;

    private final DfsVisitorFactory dfsVisitorFactory;

    private DefaultCompiler(final TranslatorFactory translatorFactory,
            final DfsVisitorFactory dfsVisitorFactory) {
        Preconditions.checkNotNull(translatorFactory);
        Preconditions.checkNotNull(dfsVisitorFactory);

        this.translatorFactory = translatorFactory;
        this.dfsVisitorFactory = dfsVisitorFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.projects.model.Compiler#compile(
     * java.lang.System)
     */
    @Override
    public Map<Network, List<Topic>> compile(final System system) {
        final TranslatingObserver translator = this.translatorFactory.produce();
        final DfsVisitor dfsVisitor =
                this.dfsVisitorFactory.produce(translator);
        system.accept(dfsVisitor);

        return translator.getResult();
    }

}
