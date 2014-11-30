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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.validator;

import java.net.URI;
import java.util.Map;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.MixedPattern;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.events.CheckEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.Builder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.BuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.builder.DefaultMixedPatternBuilderFactory;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.checker.MixedPatternChecker;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;

/**
 * Výchozí implementace vezme přímý validátor a obalí jej mechanismem zasílání
 * událostí.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultMixedPatternValidator implements MixedPatternValidator {

    /**
     * Vytvoří vysílací validátor.
     * 
     * @param checker
     *            přímý validátor
     * @param dispatcher
     *            rozesílač událostí
     * @return vysílací validátor
     */
    public static DefaultMixedPatternValidator create(
            final MixedPatternChecker checker, final Dispatcher dispatcher) {
        return new DefaultMixedPatternValidator(checker, DefaultMixedPatternBuilderFactory.create(checker), dispatcher);
    }
    
    /**
     * Vytvoří vysílací validátor.
     * 
     * @param checker
     *            přímý validátor
     * @param builderFactory továrna na stavitele typu
     * @param dispatcher
     *            rozesílač událostí
     * @return vysílací validátor
     */
    public static DefaultMixedPatternValidator create(
            final MixedPatternChecker checker,
            final BuilderFactory<MixedPattern> builderFactory,
            final Dispatcher dispatcher) {
        return new DefaultMixedPatternValidator(checker, builderFactory, dispatcher);
    }

    private final MixedPatternChecker checker;

    private final Dispatcher dispatcher;

    private final BuilderFactory<MixedPattern> builderFactory;

    private DefaultMixedPatternValidator(final MixedPatternChecker checker,
            final BuilderFactory<MixedPattern> builderFactory,
            final Dispatcher dispatcher) {
        Preconditions.checkNotNull(checker);
        Preconditions.checkNotNull(builderFactory);
        Preconditions.checkNotNull(dispatcher);

        this.checker = checker;
        this.builderFactory = builderFactory;
        this.dispatcher = dispatcher;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Validator#clear
     * (java.lang.Object)
     */
    @Override
    public void clear(final Object subject) {
        Preconditions.checkNotNull(subject);

        this.dispatcher.fire(CheckEvent.create(DefaultCheckResult.succeed(this,
                subject)));
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.edit.check.mixedpattern.
     * MixedPatternValidator#validate(java.lang.String)
     */
    @Override
    public void validate(final Source source, final Object subject,
            final String content) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(content);

        final CheckResult result = this.checker.check(source, subject, content);
        this.dispatcher.fire(CheckEvent.create(result));
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.validator.MixedPatternValidator#getBotSettings()
     */
    @Override
    public BotConfiguration getBotSettings() {
        return this.checker.getBotSettings();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.validator.MixedPatternValidator#getLanguageSettings()
     */
    @Override
    public LanguageConfiguration getLanguageSettings() {
        return this.checker.getLanguageSettings();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.mixedpattern.model.validator.MixedPatternValidator#getNamespacesToPrefixes()
     */
    @Override
    public Map<URI, String> getNamespacesToPrefixes() {
        return this.checker.getNamespacesToPrefixes();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Validator#provideBuilder(java.lang.String)
     */
    @Override
    public Builder<MixedPattern> provideBuilder(String value) {
        Preconditions.checkNotNull(value);
        
        return this.builderFactory.produce(value);
    }
}
