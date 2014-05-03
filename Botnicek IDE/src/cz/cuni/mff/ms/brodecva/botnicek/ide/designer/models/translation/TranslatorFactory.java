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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.translation;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.PredicateName;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.types.PredicateNames;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.NamingAuthority;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.Network;
import cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.translation.utils.Stack;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class TranslatorFactory {
    
    private static final String DEFAULT_PREFIX = "1138BOTNICEK";
    
    private static final String DEFAULT_PULL_STATE_NAME = "PULL";
    private static final String DEFAULT_PULL_STOP_STATE_NAME = "PULLSTOP";
    private static final String DEFAULT_RANDOMIZE_STATE_NAME = "RANDOMIZE";
    private static final String DEFAULT_TESTING_PREDICATE_NAME = "TESTING";
    
    private final NamingAuthority namingAuthority;

    private final String pullState;
    private final String pullStopState;
    private final String randomizeState;
    private final PredicateName testingPredicate;
    
    public static TranslatorFactory create(final NamingAuthority namingAuthority) {
        return create(namingAuthority, DEFAULT_PREFIX + DEFAULT_PULL_STATE_NAME, DEFAULT_PREFIX + DEFAULT_PULL_STOP_STATE_NAME, PredicateNames.of(DEFAULT_PREFIX + DEFAULT_TESTING_PREDICATE_NAME), DEFAULT_PREFIX + DEFAULT_RANDOMIZE_STATE_NAME);
    }
    
    public static TranslatorFactory create(final NamingAuthority namingAuthority, final String pullState, final String pullStopState,
            final PredicateName testingPredicate, final String randomizeState) {
        namingAuthority.tryUse(pullState, pullStopState, randomizeState, testingPredicate.getValue());
        
        return new TranslatorFactory(namingAuthority, pullState, pullStopState, testingPredicate, randomizeState);
    }
    
    private TranslatorFactory(final NamingAuthority namingAuthority, final String pullState, final String pullStopState,
            final PredicateName testingPredicate, final String randomizeState) {
        Preconditions.checkNotNull(namingAuthority);
        Preconditions.checkNotNull(pullState);
        Preconditions.checkNotNull(pullStopState);
        Preconditions.checkNotNull(testingPredicate);
        Preconditions.checkNotNull(randomizeState);
        Preconditions.checkArgument(!pullState.isEmpty());
        Preconditions.checkArgument(!pullStopState.isEmpty());
        Preconditions.checkArgument(!randomizeState.isEmpty());
        Preconditions.checkArgument(Stack.allDifferent(pullState, pullStopState, testingPredicate.getValue(), randomizeState));
        
        this.namingAuthority = namingAuthority;
        this.pullState = pullState;
        this.pullStopState = pullStopState;
        this.testingPredicate = testingPredicate;
        this.randomizeState = randomizeState;
    }
    
    public TranslatingObserver createTranslator() {
        return TranslatingObserver.create(pullState, pullStopState, testingPredicate, randomizeState);
    }
    
    public TranslatingObserver createTranslator(final Network current) {
        return TranslatingObserver.create(pullState, pullStopState, testingPredicate, randomizeState);
    }

    /**
     * @return the namingAuthority
     */
    public NamingAuthority getNamingAuthority() {
        return namingAuthority;
    }
}
