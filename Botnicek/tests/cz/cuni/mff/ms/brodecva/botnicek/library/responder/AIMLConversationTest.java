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
package cz.cuni.mff.ms.brodecva.botnicek.library.responder;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.Loader;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.LoaderException;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParserFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Splitter;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorException;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.InputPath;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.Template;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje implementaci konverzace s AIML botem.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AIMLConversation
 */
@Category(UnitTest.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest(AIMLConversation.class)
public final class AIMLConversationTest {

    /**
     * Stub pro {@link DisplayStrategy}.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    private static final class DisplayStrategyStub implements DisplayStrategy, Serializable {
        /**
         * UID serializované verze.
         */
        private static final long serialVersionUID = -8786320470514780459L;

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy#display(java.lang.String, java.lang.String)
         */
        @Override
        public String display(final String name, final String value) {
            return STRATEGY_OUTPUT;
        }
    }

    /**
     * Stub pro {@link MatchingStructure}.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    private static final class MatchingStructureStub implements
            MatchingStructure, Serializable {
        /**
         * UID serializované verze.
         */
        private static final long serialVersionUID = 8736630061229022886L;

        /**
         * Dovolit použít dodatečně naučené.
         */
        private boolean allowLearnt = false;

        @Override
        public int getCategoryCount() {
            return EXPECTED_CATEGORY_COUNT;
        }

        @Override
        public MatchResult find(final InputPath path) {
            final String pathToString = path.toString();

            if (pathToString.startsWith(normalizerStub.convertToNormalChars(substitute(SPEECH)))) {
                final Template template =
                        EasyMock.createNiceMock(Template.class);
                expect(template.getValue()).andStubReturn(EXPECTED_RESPONSE);
                replay(template);

                final MatchResult result =
                        EasyMock.createNiceMock(MatchResult.class);
                expect(result.isSuccesful()).andStubReturn(true);
                expect(result.getTemplate()).andStubReturn(template);
                replay(result);

                return result;
            }

            if (pathToString.startsWith(normalizerStub.convertToNormalChars(substitute(FIRST_SENTENCE)))) {
                final Template template =
                        EasyMock.createNiceMock(Template.class);
                expect(template.getValue()).andStubReturn(
                        FIRST_PART_OF_EXPECTED_RESPONSE);
                replay(template);

                final MatchResult result =
                        EasyMock.createNiceMock(MatchResult.class);
                expect(result.isSuccesful()).andStubReturn(true);
                expect(result.getTemplate()).andStubReturn(template);
                replay(result);

                return result;
            }

            if (pathToString.startsWith(normalizerStub.convertToNormalChars(substitute(SECOND_SENTENCE)))) {
                final Template template =
                        EasyMock.createNiceMock(Template.class);
                expect(template.getValue()).andStubReturn(
                        SECOND_PART_OF_EXPECTED_RESPONSE);
                replay(template);

                final MatchResult result =
                        EasyMock.createNiceMock(MatchResult.class);
                expect(result.isSuccesful()).andStubReturn(true);
                expect(result.getTemplate()).andStubReturn(template);
                replay(result);

                return result;
            }

            if (pathToString.startsWith(normalizerStub.convertToNormalChars(substitute(THIRD_SENTENCE)))) {
                final Template template =
                        EasyMock.createNiceMock(Template.class);
                expect(template.getValue()).andStubReturn(DUMMY_ANSWER);
                replay(template);

                final MatchResult result =
                        EasyMock.createNiceMock(MatchResult.class);
                expect(result.isSuccesful()).andStubReturn(true);
                expect(result.getTemplate()).andStubReturn(template);
                replay(result);

                return result;
            }

            if (allowLearnt
                    && pathToString.startsWith(normalizerStub.convertToNormalChars(substitute(LEARNT_SPEECH)))) {
                final Template template =
                        EasyMock.createNiceMock(Template.class);
                expect(template.getValue()).andStubReturn(LEARNT_RESPONSE);
                replay(template);

                final MatchResult result =
                        EasyMock.createNiceMock(MatchResult.class);
                expect(result.isSuccesful()).andStubReturn(true);
                expect(result.getTemplate()).andStubReturn(template);
                replay(result);

                return result;
            }

            final MatchResult fail = EasyMock.createNiceMock(MatchResult.class);
            expect(fail.isSuccesful()).andStubReturn(false);
            replay(fail);

            return fail;
        }

        @Override
        public void add(final InputPath path, final Template answer) {
            allowLearnt = true;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure#isForwardCompatible()
         */
        @Override
        public boolean isForwardCompatible() {
            return false;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure#setForwardCompatible(boolean)
         */
        @Override
        public void setForwardCompatible(final boolean forwardCompatible) {
        }
    }

    /**
     * Stub pro {@link Splitter}.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    private static final class SplitterStub implements Splitter, Serializable {

        /**
         * UID serializované verze.
         */
        private static final long serialVersionUID = -8807875591976151695L;
        
        /**
         * Regulární výraz dělení.
         */
        private static final Pattern PATTERN = Pattern.compile("\\. *");
        
        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Splitter#
         * splitToSentences(java.lang.String)
         */
        @Override
        public String[] splitToSentences(final String text) {
            return PATTERN.split(text);
        }
    }

    /**
     * Stub pro {@link TemplateParserFactory}.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    private static final class ParserFactoryStub implements
            TemplateParserFactory, Serializable {

        /**
         * UID serializované verze.
         */
        private static final long serialVersionUID = 6841659044295586866L;

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.library.responder.TemplateParserFactory
         * #
         * getParser(cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation
         * , cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult)
         */
        @Override
        public TemplateParser getParser(final Conversation conversation,
                final MatchResult result, final boolean forwardProcessingEnabled) {
            final TemplateParser parser =
                    EasyMock.createMock(TemplateParser.class);

            try {
                expect(parser.process(anyObject(String.class))).andStubAnswer(
                        new IAnswer<String>() {
                            @Override
                            public String answer() throws Throwable {
                                return (String) EasyMock.getCurrentArguments()[0];
                            }
                        });
            } catch (final ProcessorException e) {
                e.printStackTrace();
            }
            replay(parser);

            return parser;
        }
    }

    /**
     * Stub pro {@link Normalizer}.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    private static final class NormalizerStub implements Normalizer,
            Serializable {

        /**
         * UID serializované verze.
         */
        private static final long serialVersionUID = -1441177285219190183L;

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer#
         * isNormal(char)
         */
        @Override
        public boolean isNormal(final char character) {
            return Character.isUpperCase(character);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer#
         * deconvertFromNormalChars(java.lang.String)
         */
        @Override
        public String deconvertFromNormalChars(final String normalizedText) {
            return normalizedText.toLowerCase();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer#
         * convertToNormalChars(java.lang.String)
         */
        @Override
        public String convertToNormalChars(final String text) {
            return text.toUpperCase().replaceAll("\\.", "");
        }
    }

    /**
     * Stub pro {@link Loader}.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    private static final class LoaderStub implements Loader, Serializable {

        /**
         * UID serializované verze.
         */
        private static final long serialVersionUID = -8667319859982658011L;

        /**
         * Prohledávací struktura.
         */
        private final MatchingStructure matchingStructure;

        /**
         * Vytvoří stub.
         * 
         * @param matchingStructure
         *            prohledávací struktura
         */
        public LoaderStub(final MatchingStructure matchingStructure) {
            this.matchingStructure = matchingStructure;
        }

        @Override
        public void loadFromLocation(final Path path,
                final List<String> beforeLoadingOrder,
                final List<String> afterLoadingOrder) throws LoaderException {
            matchingStructure.add(null, null); // Jen dovolí použít naučené.
        }

        @Override
        public void loadIndividualFile(final Path source)
                throws LoaderException {
        }

        @Override
        public void loadFromStream(final InputStream stream,
                final String systemId) throws LoaderException {
        }

        @Override
        public MatchingStructure getFilledStructure() {
            return matchingStructure;
        }

        @Override
        public Bot getBot() {
            return null;
        }

        @Override
        public void load() throws LoaderException {
        }
    }
    
    /**
     * Stub jazyka.
     * 
     * @author Václav Brodec
     * @version 1.0
     * @see Language
     */
    private static final class LanguageStub implements Language, Serializable {

        /**
         * UID serializované verze.
         */
        private static final long serialVersionUID = 3621430582921366299L;

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#getSentenceDelim()
         */
        @Override
        public Pattern getSentenceDelim() {
            return Pattern.compile("\\.");
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#swapGender(java.lang.String)
         */
        @Override
        public String swapGender(final String text) {
            return null;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#transformPerson(java.lang.String)
         */
        @Override
        public String transformPerson(final String text) {
            return null;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#transformPerson2(java.lang.String)
         */
        @Override
        public String transformPerson2(final String text) {
            return null;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#expandAbbreviations(java.lang.String)
         */
        @Override
        public String expandAbbreviations(final String text) {
            return text;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#correctSpellingAndColloquialisms(java.lang.String)
         */
        @Override
        public String correctSpellingAndColloquialisms(final String text) {
            return text;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#substituteEmoticons(java.lang.String)
         */
        @Override
        public String substituteEmoticons(final String text) {
            return text;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#getName()
         */
        @Override
        public String getName() {
            return null;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#removeInnerSentencePunctuation(java.lang.String)
         */
        @Override
        public String removeInnerSentencePunctuation(final String text) {
            return text;
        }
        
    }

    /**
     * Klíč očekávaného predikátu.
     */
    private static final String EXPECTED_PREDICATE_KEY = "expectedPredicateKey";

    /**
     * Hodnota očekávaného predikátu.
     */
    private static final String EXPECTED_PREDICATE_VALUE =
            "expectedPredicateValue";

    /**
     * Třetí, dodatečná věta promluvy.
     */
    private static final String THIRD_SENTENCE = "THIRD SENTENCE.";

    /**
     * Vyplňovací odpověď.
     */
    private static final String DUMMY_ANSWER = "dummy answer";

    /**
     * Druhá věta promluvy.
     */
    private static final String SECOND_SENTENCE = "SECOND ONE.";

    /**
     * První věta promluvy.
     */
    private static final String FIRST_SENTENCE = "FIRST SENTENCE.";

    /**
     * Hodnota nového predikátu.
     */
    private static final String NEW_PREDICATE_VALUE = "newPredicateValue";

    /**
     * Klíč nového predikátu.
     */
    private static final String NEW_PREDICATE_KEY = "newPredicateKey";

    /**
     * Promluva na kterou bot nezná odpověď.
     */
    private static final String UNRECOGNIZABLE_SPEECH = "unrecognizable speech";

    /**
     * Promluva na kterou bot nezná odpověď před naučením.
     */
    private static final String LEARNT_SPEECH = "learnt speech";

    /**
     * Naučená odpověď.
     */
    private static final String LEARNT_RESPONSE = "learnt response";

    /**
     * Prázdný řetězec. Očekávaný výsledek v případě neúspěšného hledání či
     * nedefinovaného predikátu.
     */
    private static final String EMPTY_STRING = "";

    /**
     * Časový limit testu.
     */
    private static final int TEST_TIME_LIMIT = 500;

    /**
     * Hodnota indexu pro poslední vstup či výstup či první větu.
     */
    private static final int MOST_RECENT_INDEX_VALUE = 1;

    /**
     * První část (věta) očekávané odpovědi.
     */
    private static final String FIRST_PART_OF_EXPECTED_RESPONSE =
            "EXPECTEDRESPONSE.";

    /**
     * Druhá část (věta) očekávané odpovědi.
     */
    private static final String SECOND_PART_OF_EXPECTED_RESPONSE =
            "NEXT SENTENCE.";

    /**
     * Očekávaná odpověď.
     */
    private static final String EXPECTED_RESPONSE =
            FIRST_PART_OF_EXPECTED_RESPONSE + ' '
                    + SECOND_PART_OF_EXPECTED_RESPONSE;

    /**
     * Promluva.
     */
    private static final String SPEECH = "Speech";

    /**
     * Dvojrozměrný index první věty posledního vstupu či výstupu.
     */
    private static final TwoDimensionalIndex MOST_RECENT_2D_INDEX =
            new TwoDimensionalIndex() {

                @Override
                public int getSecondValue() {
                    return MOST_RECENT_INDEX_VALUE;
                }

                @Override
                public int getFirstValue() {
                    return MOST_RECENT_INDEX_VALUE;
                }
            };

    /**
     * Hodnota indexu druhé věty.
     */
    private static final int SECOND_INDEX = 2;

    /**
     * Dvojrozměrný index druhé věty předposledního vstupu či výstupu.
     */
    private static final TwoDimensionalIndex NEXT_MOST_RECENT_SECOND_SENTENCE =
            new TwoDimensionalIndex() {

                @Override
                public int getSecondValue() {
                    return SECOND_INDEX;
                }

                @Override
                public int getFirstValue() {
                    return SECOND_INDEX;
                }
            };

    /**
     * Očekávaný počet kategorií.
     */
    private static final int EXPECTED_CATEGORY_COUNT = 6;

    /**
     * Predikát, jehož nastavení je ovlivněno zobrazovací strategií.
     */
    private static final String STRATEGY_AFFECTED_PREDICATE_NAME = "strategyAffectedPredicateName";

    /**
     * Výstup zobrazovací strategie.
     */
    private static final String STRATEGY_OUTPUT = "strategyOutput";
    
    /**
     * Neměnné výchozí predikáty.
     */
    private static Map<String, String> defaultPredicatesStub = null;
    
    /**
     * Stuby zobrazení příkazů na strategie pro zobrazení výstupu nastavení predikátů.
     */
    private static Map<String, DisplayStrategy> predicatesSetBehaviorStub = null;

    /**
     * Stub děliče vět.
     */
    private static Splitter splitterStub = null;

    /**
     * Stub normalizéru.
     */
    private static Normalizer normalizerStub = null;
    
    /**
     * Stub jazyka.
     */
    private static Language languageStub = null;

    /**
     * Stub továrny.
     */
    private static TemplateParserFactory parserFactoryStub = null;

    /**
     * Stub zobrazovací strategie.
     */
    private static DisplayStrategy strategyStub = null;

    /**
     * Testovaná konverzace.
     */
    private AIMLConversation conversation = null;

    /**
     * Exekutor vláken.
     */
    private ExecutorService executorServiceStub = null;

    /**
     * Záklopka pro synchronizaci testu s více vlákny.
     */
    private CountDownLatch updateLatch = null;

    /**
     * Stub prohledávací struktury.
     */
    private MatchingStructure matchingStructureStub = null;

    /**
     * Stub načítače.
     */
    private Loader loaderStub = null;

    /**
     * Inicializace testu.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        final HashMap<String, String> defaultsInitMap = new HashMap<String, String>();
        defaultsInitMap.put(EXPECTED_PREDICATE_KEY, EXPECTED_PREDICATE_VALUE);
        defaultPredicatesStub = Collections.unmodifiableMap(defaultsInitMap);

        strategyStub = new DisplayStrategyStub();
        
        final HashMap<String, DisplayStrategy> behaviorsInitMap = new HashMap<String, DisplayStrategy>();
        behaviorsInitMap.put(STRATEGY_AFFECTED_PREDICATE_NAME, strategyStub);
        predicatesSetBehaviorStub = Collections.unmodifiableMap(behaviorsInitMap);
        
        splitterStub = new SplitterStub();

        normalizerStub = new NormalizerStub();
        
        languageStub = new LanguageStub();

        parserFactoryStub = new ParserFactoryStub();
    }

    /**
     * Úklid testu.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        defaultPredicatesStub = null;
        
        strategyStub = null;
        
        splitterStub = null;

        normalizerStub = null;
        
        languageStub = null;

        parserFactoryStub = null;
    }

    /**
     * 
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     */
    @Before
    public void setUp() throws ConversationException {
        executorServiceStub = Executors.newSingleThreadExecutor();

        updateLatch = new CountDownLatch(1);

        matchingStructureStub = new MatchingStructureStub();

        loaderStub = new LoaderStub(matchingStructureStub);
        
        conversation =
                new AIMLConversation(loaderStub, splitterStub, normalizerStub,
                        languageStub, parserFactoryStub, defaultPredicatesStub,
                        predicatesSetBehaviorStub, executorServiceStub);
    }

    /**
     * Uklidí testované objekty.
     */
    @After
    public void tearDown() {
        executorServiceStub.shutdown();

        conversation = null;

        updateLatch = null;

        loaderStub = null;

        matchingStructureStub = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#AIMLConversation(Loader, Splitter, Normalizer, Language, TemplateParserFactory, Map, Map, ExecutorService)}
     * .
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     */
    @Test
    public void testAIMLConversation() throws ConversationException {
        new AIMLConversation(loaderStub, splitterStub, normalizerStub, languageStub,
                parserFactoryStub, defaultPredicatesStub, predicatesSetBehaviorStub, executorServiceStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#talk(java.lang.String)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#getUserInput(TwoDimensionalIndex)}
     * .
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     */
    @Test
    public void testTalkStringAndGetUserInput() throws ConversationException {
        conversation.talk(FIRST_SENTENCE + ' ' + SECOND_SENTENCE);
        conversation.talk(THIRD_SENTENCE);

        assertEquals(substitute(SECOND_SENTENCE.substring(0, SECOND_SENTENCE.length() - 1)),
                conversation.getUserInput(NEXT_MOST_RECENT_SECOND_SENTENCE));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#getBotOutput(TwoDimensionalIndex)}
     * .
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     */
    @Test
    public void testGetBotOutput() throws ConversationException {
        conversation.talk(SPEECH);
        conversation.talk(THIRD_SENTENCE);

        assertEquals(
                substitute(SECOND_PART_OF_EXPECTED_RESPONSE.substring(0, SECOND_PART_OF_EXPECTED_RESPONSE.length() - 1)),
                conversation.getBotOutput(NEXT_MOST_RECENT_SECOND_SENTENCE));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#talk(java.lang.String)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#listen()}
     * . .
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     */
    @Test
    public void testTalkStringAndListen() throws ConversationException {
        conversation.talk(SPEECH);
        assertEquals(EXPECTED_RESPONSE, conversation.listen());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#talk(java.lang.String)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#listen()}
     * . .
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     */
    @Test(expected = ConversationException.class)
    public void
            testTalkStringAndListenWhenSpeechUnrecognizable()
                    throws ConversationException {
        conversation.talk(UNRECOGNIZABLE_SPEECH);
        assertEquals(EMPTY_STRING, conversation.listen());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#talk(java.lang.String)}
     * . .
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     */
    @Test(expected = NullPointerException.class)
    public void testTalkStringWhenSpeechNull() throws ConversationException {
        conversation.talk(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#listen()}
     * .
     */
    @Test
    public void testListenWhenNotTalkedBefore() {
        assertNull(conversation.listen());
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#talk(java.lang.String)
     * , cz.cuni.mff.ms.brodecva.botnicek.library.responder.Listener}.
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     * @throws InterruptedException
     *             pokud je přerušená západka
     */
    @Test(timeout = TEST_TIME_LIMIT)
    public void testTalkStringListenerReturnsExpected()
            throws ConversationException, InterruptedException {
        conversation.talk(SPEECH, new Listener() {

            @Override
            public void answerReceived(final Answer answer) {
                assertEquals(EXPECTED_RESPONSE, answer.getAnswer());
                updateLatch.countDown();
            }

            @Override
            public void exceptionalStateCaught(final ExceptionalState status) {
            }

        });
        updateLatch.await();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#talk(java.lang.String)
     * , cz.cuni.mff.ms.brodecva.botnicek.library.responder.Listener}.
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     * @throws InterruptedException
     *             pokud je přerušená západka
     */
    @Test(timeout = TEST_TIME_LIMIT)
    public void testTalkStringListenerRecordsBotOutput()
            throws ConversationException, InterruptedException {
        conversation.talk(SPEECH, new Listener() {

            @Override
            public void answerReceived(final Answer answer) {
                updateLatch.countDown();
            }

            @Override
            public void exceptionalStateCaught(final ExceptionalState status) {
            }

        });
        updateLatch.await();
        assertEquals(
                substitute(FIRST_PART_OF_EXPECTED_RESPONSE.substring(0, FIRST_PART_OF_EXPECTED_RESPONSE.length() - 1)),
                conversation.getBotOutput(MOST_RECENT_2D_INDEX));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#talk(java.lang.String)
     * , cz.cuni.mff.ms.brodecva.botnicek.library.responder.Listener}.
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     * @throws InterruptedException
     *             pokud je přerušená západka
     */
    @Test(timeout = TEST_TIME_LIMIT)
    public void testTalkStringListenerRecordsUserInput()
            throws ConversationException, InterruptedException {
        conversation.talk(SPEECH, new Listener() {

            @Override
            public void answerReceived(final Answer answer) {
                updateLatch.countDown();
            }

            @Override
            public void exceptionalStateCaught(final ExceptionalState status) {
            }

        });
        updateLatch.await();
        assertEquals(substitute(SPEECH),
                conversation.getUserInput(MOST_RECENT_2D_INDEX));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#talk(java.lang.String)
     * , cz.cuni.mff.ms.brodecva.botnicek.library.responder.Listener}.
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     * @throws InterruptedException
     *             pokud je přerušená západka
     */
    @Test(timeout = TEST_TIME_LIMIT)
    public void
            testTalkStringListenerWhenUnrecognizableSpeechCallsExceptionalStateHandler()
                    throws ConversationException, InterruptedException {
        conversation.talk(UNRECOGNIZABLE_SPEECH, new Listener() {

            @Override
            public void answerReceived(final Answer answer) {
                fail();
            }

            @Override
            public void exceptionalStateCaught(final ExceptionalState status) {
                assertTrue(status.getThrowable() instanceof ConversationException);
                updateLatch.countDown();
            }

        });
        updateLatch.await();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#talk(java.lang.String)
     * , cz.cuni.mff.ms.brodecva.botnicek.library.responder.Listener}.
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     * @throws InterruptedException
     *             pokud je přerušená západka
     */
    @Test(timeout = TEST_TIME_LIMIT, expected = NullPointerException.class)
    public void testTalkStringListenerWhenSpeechNull()
            throws ConversationException, InterruptedException {
        conversation.talk(null, new Listener() {

            @Override
            public void answerReceived(final Answer answer) {
                updateLatch.countDown();
            }

            @Override
            public void exceptionalStateCaught(final ExceptionalState status) {
            }

        });
        updateLatch.await();
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#talk(java.lang.String)
     * , cz.cuni.mff.ms.brodecva.botnicek.library.responder.Listener}.
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     * @throws InterruptedException
     *             pokud je přerušená západka
     */
    @Test(timeout = TEST_TIME_LIMIT, expected = NullPointerException.class)
    public void testTalkStringListenerWhenListenerNull()
            throws ConversationException, InterruptedException {
        conversation.talk(SPEECH, null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#attemptTalk(java.lang.String)}
     * .
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     */
    @Test
    public void testAttemptTalkReturnsExpected() throws ConversationException {
        assertEquals(EXPECTED_RESPONSE, conversation.attemptTalk(SPEECH));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#attemptTalk(java.lang.String)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#learn(java.nio.file.Path)}
     * .
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     * @throws LoaderException
     *             chyba v načítání
     */
    @Test
    public void testLearnAndAttemptTalkReturnsLearnt()
            throws ConversationException, LoaderException {
        conversation.learn(Paths.get("foo", "bar"));
        assertEquals(LEARNT_RESPONSE, conversation.attemptTalk(LEARNT_SPEECH));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#attemptTalk(java.lang.String)}
     * .
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     */
    @Test(expected = ConversationException.class)
    public void testAttemptTalkWhenUnrecognizable()
            throws ConversationException {
        assertEquals(EMPTY_STRING, conversation.attemptTalk(UNRECOGNIZABLE_SPEECH));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#attemptTalk(java.lang.String)}
     * .
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     */
    @Test(expected = NullPointerException.class)
    public void testAttemptTalkWhenSpeechNull() throws ConversationException {
        conversation.attemptTalk(null);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#attemptTalk(java.lang.String)}
     * .
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAttemptTalkDoesNotMakeUserRecords()
            throws ConversationException {
        conversation.attemptTalk(SPEECH);
        conversation.getUserInput(MOST_RECENT_2D_INDEX);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#attemptTalk(java.lang.String)}
     * .
     * 
     * @throws ConversationException
     *             chyba v konverzaci
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAttemptTalkDoesNotMakeBotRecords() throws ConversationException {
        conversation.attemptTalk(SPEECH);
        conversation.getBotOutput(MOST_RECENT_2D_INDEX);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#attemptTalk(java.lang.String)}
     * .
     * @throws Exception chyba při testování
     */
    @Test(expected = ConversationException.class)
    public void testAttemptTalkStackOverflow() throws Exception {
        final Conversation conversation = PowerMock.createPartialMock(AIMLConversation.class, new String[] { "evaluate" }, loaderStub, splitterStub, normalizerStub,
                languageStub, parserFactoryStub, defaultPredicatesStub,
                predicatesSetBehaviorStub, executorServiceStub);
        PowerMock.expectPrivate(conversation, "evaluate", SPEECH, false).andStubAnswer(new IAnswer<String>() {

            @Override
            public String answer() throws Throwable {
                return conversation.attemptTalk(SPEECH);
            }
            
        });
        PowerMock.replay(conversation);
        
        conversation.attemptTalk(SPEECH);
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#getPredicateValue(java.lang.String)}
     * .
     */
    @Test
    public void testGetPredicateValue() {
        assertEquals(EXPECTED_PREDICATE_VALUE,
                conversation.getPredicateValue(EXPECTED_PREDICATE_KEY));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#setPredicateValue(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testSetPredicateValueWithoutKnownStrategyReturnsValue() {
        assertEquals(NEW_PREDICATE_VALUE, conversation.setPredicateValue(NEW_PREDICATE_KEY, NEW_PREDICATE_VALUE));
    }
    
    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLConversation#setPredicateValue(java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testSetPredicateValueWithKnownStrategyReturnsStrategyOutput() {
        assertEquals(STRATEGY_OUTPUT, conversation.setPredicateValue(STRATEGY_AFFECTED_PREDICATE_NAME, "dummy"));
    }

    /**
     * Test serializace.
     * 
     * @throws IOException
     *             pokud dojde k I/O chybě
     * @throws ClassNotFoundException
     *             pokud není nalezena třída
     */
    @Test
    public void testRoundTripSerialization() throws IOException,
            ClassNotFoundException {
        final AIMLConversation original = conversation;

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        final byte[] pickled = out.toByteArray();
        final InputStream in = new ByteArrayInputStream(pickled);
        final ObjectInputStream ois = new ObjectInputStream(in);
        final Object o = ois.readObject();
        final AIMLConversation copy = (AIMLConversation) o;

        assertEquals(original.getBot(), copy.getBot());
        assertEquals(original.getCategoryCount(), copy.getCategoryCount());
    }
    
    /**
     * Text k substituci.
     * 
     * @param text text
     * @return substituovaný text
     */
    private static String substitute(final String text) {
        final String emoticonsSubstituted = languageStub.substituteEmoticons(text);

        final String abbreviationsExpanded =
                languageStub.expandAbbreviations(emoticonsSubstituted);

        final String spellingCorrected =
                languageStub.correctSpellingAndColloquialisms(abbreviationsExpanded);

        final String withoutPunctuation =
                languageStub.removeInnerSentencePunctuation(spellingCorrected);

        return withoutPunctuation;
    }
    
}
