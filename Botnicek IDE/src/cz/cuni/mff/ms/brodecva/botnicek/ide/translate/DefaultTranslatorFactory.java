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

import java.io.Serializable;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.data.Comparisons;

/**
 * <p>Výchozí implementace továrny překládajících pozorovatelů.</p>
 * <p>Je konfigurovatelná názvy pomocných stavů.</p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultTranslatorFactory implements TranslatorFactory, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final NormalWord pullState;
    private final NormalWord pullStopState;
    private final NormalWord randomizeState;
    private final NormalWord successState;
    private final NormalWord returnState;
    private final NormalWord testingPredicate;
    
    /**
     * Vytvoří továrna s daným nastavením názvů pomocných stavů.
     * 
     * @param pullState slovo popisující stav, který je vložen na zásobník po průchodu sítí až do koncového uzlu, a spustí tak proceduru uvolňování nezpracovaných stavů z něj 
     * @param pullStopState slovo popisující stav, který slouží jako zarážka při uvolňování nezpracovaných stavů úspěšně prošlé sítě ze zásobníku
     * @param randomizeState slovo popisující stav, který provede zamíchá přechody dle jejich priority před vložením na zásobník
     * @param successState slovo popisující stav, který indikuje úspěšné projití podsítě odkazované z následujícího stavu na zásobníku
     * @param returnState slovo popisující stav, který indikuje zpracování podsítě
     * @param testingPredicate rezervovaný název predikátu sloužící pro interní testy
     * @return továrna na překladače
     */
    public static DefaultTranslatorFactory create(final NormalWord pullState, final NormalWord pullStopState, final NormalWord randomizeState,
            NormalWord successState, NormalWord returnState, final NormalWord testingPredicate) {
        Preconditions.checkNotNull(pullState);
        Preconditions.checkNotNull(pullStopState);
        Preconditions.checkNotNull(randomizeState);
        Preconditions.checkNotNull(testingPredicate);
        Preconditions.checkArgument(Comparisons.allDifferent(pullState, pullStopState, randomizeState, successState, returnState));
        
        return new DefaultTranslatorFactory(pullState, pullStopState, randomizeState, successState, returnState, testingPredicate);
    }

    
    private DefaultTranslatorFactory(final NormalWord pullState, final NormalWord pullStopState, final NormalWord randomizeState,
            NormalWord successState, NormalWord returnState, final NormalWord testingPredicate) {
        this.pullState = pullState;
        this.pullStopState = pullStopState;
        this.randomizeState = randomizeState;
        this.successState = successState;
        this.returnState = returnState;
        this.testingPredicate = testingPredicate;
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.translate.TranslatorFactory#produce()
     */
    @Override
    public TranslatingObserver produce() {
        return DefaultTranslatingObserver.create(this.pullState, this.pullStopState, this.randomizeState, this.successState, this.returnState, this.testingPredicate);
    }
}
