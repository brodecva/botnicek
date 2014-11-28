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
package cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events.Dispatcher;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.ConversationConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.SessionException;
import cz.cuni.mff.ms.brodecva.botnicek.library.language.AIMLLanguage;
import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLCategoryLoader;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLSourceParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.Loader;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.LoaderException;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.SourceParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParserFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParserFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleSplitter;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Splitter;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLBot;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordTree;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory;

/**
 * Výchozí implementace běhového prostředí. Obal knihovny Botníčku.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultRuntime implements Runtime {

    /**
     * Vytvoří instanci běhového prostředí dle přímých nastavení knihovny.
     * 
     * @param botConfiguration
     *            nastavení robota
     * @param languageConfiguration
     *            nastavení jazyka
     * @param conversationConfiguration
     *            nastavení konverzace
     * @param dispatcher
     *            rozesílač událostí
     * @return běhové prostředí
     * @throws SessionException
     *             pokud dojde k chybě při inicializaci
     */
    public static Runtime create(final BotConfiguration botConfiguration,
            final LanguageConfiguration languageConfiguration,
            final ConversationConfiguration conversationConfiguration,
            final Dispatcher dispatcher) throws SessionException {
        Preconditions.checkNotNull(botConfiguration);
        Preconditions.checkNotNull(languageConfiguration);
        Preconditions.checkNotNull(conversationConfiguration);
        Preconditions.checkNotNull(dispatcher);

        final MapperFactory mapperFactory = new FrugalMapperFactory();
        final MatchingStructure matchingStructure = new WordTree(mapperFactory);

        final String botName = botConfiguration.getName();
        final String languageName = languageConfiguration.getName();

        final Pattern sentenceDelimiter =
                languageConfiguration.getSentenceDelim();
        final Map<Pattern, String> genderSubs =
                languageConfiguration.getGenderSubs();
        final Map<Pattern, String> personSubs =
                languageConfiguration.getPersonSubs();
        final Map<Pattern, String> person2Subs =
                languageConfiguration.getPerson2Subs();
        final Map<Pattern, String> abbreviationsSubs =
                languageConfiguration.getAbbreviationsSubs();
        final Map<Pattern, String> spellingSubs =
                languageConfiguration.getSpellingSubs();
        final Map<Pattern, String> emoticonsSubstitution =
                languageConfiguration.getEmoticonsSubs();
        final Map<Pattern, String> innerPunctuationSubs =
                languageConfiguration.getInnerPunctuationSubs();

        final Language language =
                new AIMLLanguage(languageName, sentenceDelimiter, genderSubs,
                        personSubs, person2Subs, abbreviationsSubs,
                        spellingSubs, emoticonsSubstitution,
                        innerPunctuationSubs);

        final Path filesLocation = botConfiguration.getFilesLocation();
        final Path gossipPath = botConfiguration.getGossipPath();
        final Map<String, String> predicates = botConfiguration.getPredicates();
        final List<String> beforeloadingOrder =
                botConfiguration.getBeforeLoadingOrder();
        final List<String> afterLoadingOrder =
                botConfiguration.getAfterLoadingOrder();

        final Bot bot =
                new AIMLBot(botName, language, filesLocation, gossipPath,
                        predicates, beforeloadingOrder, afterLoadingOrder);

        final SourceParser parser = AIMLSourceParser.create();
        final Loader loader =
                new AIMLCategoryLoader(matchingStructure, bot, parser);
        final Splitter splitter = new SimpleSplitter(language);
        final Normalizer normalizer = new SimpleNormalizer();
        final TemplateParserFactory parserFactory;
        try {
            parserFactory = new AIMLTemplateParserFactory();
        } catch (final IOException e) {
            throw new SessionException(e);
        }
        final Map<String, String> defaultPredicates =
                ImmutableMap.copyOf(conversationConfiguration
                        .getDefaultPredicates());
        final Map<String, DisplayStrategy> predicatesSetBehavior =
                ImmutableMap.copyOf(conversationConfiguration
                        .getDisplayStrategies());

        return new DefaultRuntime(loader, splitter, normalizer, parserFactory,
                language, defaultPredicates, predicatesSetBehavior,
                DefaultRunFactory.create(), dispatcher);
    }

    /**
     * Vytvoří instanci běhového prostředí z přímých nastavení.
     * 
     * @param loader
     *            načítač definic robota
     * @param splitter
     *            rozdělovač vět
     * @param normalizer
     *            normalizér
     * @param parserFactory
     *            továrna parseru
     * @param language
     *            definici jazyka
     * @param defaultPredicates
     *            výchozí predikáty
     * @param predicatesSetBehavior
     *            nastavení chování nastavování predikátů (zobrazit výstup či
     *            jméno)
     * @param runFactory
     *            továrna na testovací konverzace
     * @param dispatcher
     *            rozesílač událostí
     * @return běhové prostředí
     */
    public static Runtime create(final Loader loader, final Splitter splitter,
            final Normalizer normalizer,
            final TemplateParserFactory parserFactory, final Language language,
            final Map<? extends String, ? extends String> defaultPredicates,
            final Map<? extends String, ? extends DisplayStrategy> predicatesSetBehavior,
            final RunFactory runFactory, final Dispatcher dispatcher) {
        return new DefaultRuntime(loader, splitter, normalizer, parserFactory,
                language, defaultPredicates, predicatesSetBehavior, runFactory,
                dispatcher);
    }

    /**
     * Vytvoří instanci běhového prostředí dle nastavení v projektu.
     * 
     * @param runtimeSettings
     *            nastavení běhového prostředí projektu
     * @param dispatcher
     *            rozesílač událostí
     * @return běhové prostředí
     * @throws SessionException
     *             pokud dojde k chybě při inicializaci
     */
    public static Runtime create(final RuntimeSettings runtimeSettings,
            final Dispatcher dispatcher) throws SessionException {
        Preconditions.checkNotNull(runtimeSettings);
        Preconditions.checkNotNull(dispatcher);

        return create(runtimeSettings.getBotConfiguration(),
                runtimeSettings.getLanguageConfiguration(),
                runtimeSettings.getConversationConfiguration(), dispatcher);
    }

    private final Dispatcher dispatcher;
    private final Loader loader;
    private final Language language;
    private final Splitter splitter;
    private final Normalizer normalizer;

    private final TemplateParserFactory parserFactory;

    private final Map<String, String> defaultPredicates;

    private final Map<String, DisplayStrategy> predicatesSetBehavior;

    private final RunFactory runFactory;

    private DefaultRuntime(final Loader loader, final Splitter splitter,
            final Normalizer normalizer,
            final TemplateParserFactory parserFactory, final Language language,
            final Map<? extends String, ? extends String> defaultPredicates,
            final Map<? extends String, ? extends DisplayStrategy> predicatesSetBehavior,
            final RunFactory runFactory, final Dispatcher dispatcher) {
        Preconditions.checkNotNull(loader);
        Preconditions.checkNotNull(splitter);
        Preconditions.checkNotNull(normalizer);
        Preconditions.checkNotNull(parserFactory);
        Preconditions.checkNotNull(language);
        Preconditions.checkNotNull(defaultPredicates);
        Preconditions.checkNotNull(predicatesSetBehavior);
        Preconditions.checkNotNull(runFactory);
        Preconditions.checkNotNull(dispatcher);

        this.loader = loader;
        this.splitter = splitter;
        this.normalizer = normalizer;
        this.parserFactory = parserFactory;
        this.language = language;
        this.defaultPredicates = ImmutableMap.copyOf(defaultPredicates);
        this.predicatesSetBehavior = ImmutableMap.copyOf(predicatesSetBehavior);
        this.dispatcher = dispatcher;
        this.runFactory = runFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.Runtime#load(java.
     * lang.String, java.lang.String)
     */
    @Override
    public void load(final String documentName, final String text)
            throws LoaderException {
        Preconditions.checkNotNull(documentName);
        Preconditions.checkNotNull(text);

        final InputStream textStream =
                new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));

        this.loader.loadFromStream(textStream, documentName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.runtime.model.Runtime#run()
     */
    @Override
    public Run run() {
        return this.runFactory.produce(this.loader, this.splitter,
                this.normalizer, this.language, this.parserFactory,
                this.defaultPredicates, this.predicatesSetBehavior,
                this.dispatcher);
    }
}
