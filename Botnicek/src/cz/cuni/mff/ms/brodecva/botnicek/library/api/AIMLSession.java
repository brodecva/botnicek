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
package cz.cuni.mff.ms.brodecva.botnicek.library.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import cz.cuni.mff.ms.brodecva.botnicek.library.language.AIMLLanguage;
import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLCategoryLoader;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLSourceParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.Loader;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.LoaderException;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.SourceParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIMLTemplateParserFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParserFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleNormalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.SimpleSplitter;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Splitter;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLBot;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordTree;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Property;

/**
 * Relace s konverzačním robotem technologie AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLSession implements Session {

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLSession.class);

    /**
     * Vedená konverzace.
     */
    private final Conversation conversation;

    /**
     * Vytvoří relaci konverzace.
     * 
     * @param botConfig
     *            nastavení robota
     * @param languageConfiguration
     *            nastavení jazyka konverzace
     * @param conversationConfig
     *            nastavení konverzace
     * @return nová relace
     * @throws SessionException
     *             chyba při vytváření relace
     */
    public static Session start(final BotConfiguration botConfig,
            final LanguageConfiguration languageConfiguration,
            final ConversationConfiguration conversationConfig)
            throws SessionException {
        final MapperFactory mapperFactory = new FrugalMapperFactory();

        final MatchingStructure matchingStructure = new WordTree(mapperFactory);

        final String botName = botConfig.getName();

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

        final Path filesLocation = botConfig.getFilesLocation();

        final Path gossipPath = botConfig.getGossipPath();

        final Map<String, String> predicates = botConfig.getPredicates();

        final List<String> beforeloadingOrder =
                botConfig.getBeforeLoadingOrder();

        final List<String> afterLoadingOrder = botConfig.getAfterLoadingOrder();

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
                conversationConfig.getDefaultPredicates();
        final Map<String, DisplayStrategy> predicatesSetBehavior =
                conversationConfig.getDisplayStrategies();

        final Conversation conversation;
        try {
            loader.load();
            conversation =
                    new AIMLConversation(loader, splitter, normalizer,
                            language, parserFactory, defaultPredicates,
                            predicatesSetBehavior);
        } catch (final LoaderException e) {
            throw new SessionException(e);
        }

        return new AIMLSession(conversation);
    }

    /**
     * Vytvoří relaci konverzace v daném kontextu za využití standardních jmen
     * souborů.
     * 
     * @param currentClass
     *            kontextová třída
     * @return nová relace
     * @throws ConfigurationException
     *             chyba v konfiguraci
     * @throws IOException
     *             chyba při načítání zdrojových souborů či souborů konfigurace
     * @throws SessionException
     *             chyba v zakládání relace
     */
    public static Session start(final Class<?> currentClass)
            throws ConfigurationException, IOException, SessionException {
        final BotConfiguration botConfig =
                AIMLBotConfiguration
                        .create(Property.load(currentClass, "bot.properties"),
                                Property.load(currentClass,
                                        "botpredicates.properties"));

        final LanguageConfiguration languageConfiguration =
                AIMLLanguageConfiguration
                        .create(Property.load(currentClass,
                                "language.properties"), Property.load(
                                currentClass, "gender.properties"), Property
                                .load(currentClass, "person.properties"),
                                Property.load(currentClass,
                                        "person2.properties"), Property.load(
                                        currentClass,
                                        "abbreviations.properties"), Property
                                        .load(currentClass,
                                                "spelling.properties"),
                                Property.load(currentClass,
                                        "emoticons.properties"), Property.load(
                                        currentClass, "punctuation.properties"));

        final ConversationConfiguration conversationConfig =
                AIMLConversationConfiguration.create(Property.load(
                        currentClass, "defaultpredicates.properties"), Property
                        .load(currentClass, "setbehavior.properties"));

        final Session session =
                AIMLSession.start(botConfig, languageConfiguration,
                        conversationConfig);
        return session;
    }

    /**
     * Vytvoří relaci konverzace z konfigurace se standardními jmény v daném
     * umístění.
     * 
     * @param configurationLocation
     *            umístění souborů konfigurace
     * @return nová relace
     * @throws ConfigurationException
     *             chyba v konfiguraci
     * @throws IOException
     *             chyba při načítání zdrojových souborů či souborů konfigurace
     * @throws SessionException
     *             chyba v zakládání relace
     */
    public static Session start(final Path configurationLocation)
            throws ConfigurationException, IOException, SessionException {
        final BotConfiguration botConfig =
                AIMLBotConfiguration.create(Property.load(new FileInputStream(
                        configurationLocation.resolve("bot.properties")
                                .toFile())), Property.load(new FileInputStream(
                        configurationLocation.resolve(
                                "botpredicates.properties").toFile())));

        final LanguageConfiguration languageConfiguration =
                AIMLLanguageConfiguration
                        .create(Property.load(new FileInputStream(
                                configurationLocation.resolve(
                                        "language.properties").toFile())),
                                Property.load(new FileInputStream(
                                        configurationLocation.resolve(
                                                "gender.properties").toFile())),
                                Property.load(new FileInputStream(
                                        configurationLocation.resolve(
                                                "person.properties").toFile())),
                                Property.load(new FileInputStream(
                                        configurationLocation.resolve(
                                                "person2.properties").toFile())),
                                Property.load(new FileInputStream(
                                        configurationLocation.resolve(
                                                "abbreviations.properties")
                                                .toFile())), Property
                                        .load(new FileInputStream(
                                                configurationLocation.resolve(
                                                        "spelling.properties")
                                                        .toFile())), Property
                                        .load(new FileInputStream(
                                                configurationLocation.resolve(
                                                        "emoticons.properties")
                                                        .toFile())),
                                Property.load(new FileInputStream(
                                        configurationLocation.resolve(
                                                "punctuation.properties")
                                                .toFile())));

        final ConversationConfiguration conversationConfig =
                AIMLConversationConfiguration.create(Property
                        .load(new FileInputStream(configurationLocation
                                .resolve("defaultpredicates.properties")
                                .toFile())), Property.load(new FileInputStream(
                        configurationLocation.resolve("setbehavior.properties")
                                .toFile())));

        final Session session =
                AIMLSession.start(botConfig, languageConfiguration,
                        conversationConfig);
        return session;
    }

    /**
     * Vytvoří relaci.
     * 
     * @param conversation
     *            vedená konverzace
     */
    private AIMLSession(final Conversation conversation) {
        this.conversation = conversation;
        LOGGER.log(Level.INFO, "api.SessionCreated",
                new Object[] { conversation });
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.Session#getBot()
     */
    @Override
    public Bot getBot() {
        return conversation.getBot();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.api.Session#getConversation()
     */
    @Override
    public Conversation getConversation() {
        return conversation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.api.Session#getLangugage()
     */
    @Override
    public Language getLangugage() {
        return conversation.getBot().getLanguage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AIMLSession [conversation=" + conversation + "]";
    }

}
