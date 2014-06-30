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

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.NormalWord;
import cz.cuni.mff.ms.brodecva.botnicek.ide.translate.utils.Stack;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultTranslatorFactory implements TranslatorFactory {
    
    public static DefaultTranslatorFactory create(final NormalWord pullState, final NormalWord pullStopState, final NormalWord randomizeState,
            final NormalWord testingPredicate) {
        return new DefaultTranslatorFactory(pullState, pullStopState, randomizeState, testingPredicate);
    }

    private final NormalWord pullState;
    private final NormalWord pullStopState;
    private final NormalWord randomizeState;
    private final NormalWord testingPredicate;
    
    private DefaultTranslatorFactory(final NormalWord pullState, final NormalWord pullStopState, final NormalWord randomizeState,
            final NormalWord testingPredicate) {
        Preconditions.checkNotNull(pullState);
        Preconditions.checkNotNull(pullStopState);
        Preconditions.checkNotNull(randomizeState);
        Preconditions.checkNotNull(testingPredicate);
        Preconditions.checkNotNull(Stack.allDifferent(pullState, pullStopState, randomizeState));
        
        this.pullState = pullState;
        this.pullStopState = pullStopState;
        this.randomizeState = randomizeState;
        this.testingPredicate = testingPredicate;
    }
    
    @Override
    public TranslatingObserver produce() {
        return DefaultTranslatingObserver.create(this.pullState, this.pullStopState, this.randomizeState, this.testingPredicate);
    }
}
