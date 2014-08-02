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

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.Loader;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.LoaderException;
import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.parser.TemplateParserFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.TwoDimensionalIndex;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Normalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.preprocessor.Splitter;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.ProcessorException;
import cz.cuni.mff.ms.brodecva.botnicek.library.processor.set.DisplayStrategy;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.AIMLInputPath;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchResult;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Konverzace mezi daným botem a uživatelem postavená na AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLConversation implements Conversation, Serializable {
        
    /**
     * Úroveň vnoření při rekurzivním zpracování.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    private static final class ThreadLocalRecursionLevel extends
            ThreadLocal<Integer> {
        /* (non-Javadoc)
         * @see java.lang.ThreadLocal#initialValue()
         */
        @Override
        protected Integer initialValue() {
            return 0;
        }
    }

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 8413884890073705641L;

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLConversation.class);

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();
    
    /**
     * Velikost zásobníku pro vnořené volání metody {@link #attemptTalk(String)}.
     */
    public static final int STACK_SIZE = 100;    
    
    /**
     * Úroveň vnoření při rekurzivním zpracování.
     */
    private transient ThreadLocal<Integer> recursionLevel = new ThreadLocalRecursionLevel();
    
    /**
     * Načítací objekt pro rozhodovací strukturu.
     */
    private final Loader loader;

    /**
     * Uživatelské vstupy.
     */
    private final List<String> inputs = new ArrayList<String>();

    /**
     * Uživatelské výstupy.
     */
    private final List<String> outputs = new ArrayList<String>();

    /**
     * Odpověď čekající na odposlech.
     */
    private String lastResponse = null;

    /**
     * Uživatelské predikáty.
     */
    private final Map<String, String> predicates;

    /**
     * Názvy predikátů zobrazené na strategie pro zobrazení výstupu při jejich
     * nastavování.
     */
    private final Map<String, DisplayStrategy> predicatesSetBehavior;

    /**
     * Nástroj na dělení textu na zpracovatelné úseky.
     */
    private final Splitter splitter;

    /**
     * Nástroj pro normalizaci textu.
     */
    private final Normalizer normalizer;

    /**
     * Nahrazení v kontextu jazyka.
     */
    private final Language language;

    /**
     * Továrna na parsery šablon.
     */
    private final TemplateParserFactory parserFactory;
    
    /**
     * Exekutor vláken.
     */
    private transient ExecutorService executor;
    
    /**
     * Registrovaný posluchač.
     */
    private transient Listener listener;

    /**
     * Konstruktor nové konverzace. Pro určeného bota načte jeho zdrojové
     * soubory, do kterých dosadí jeho predikáty.
     * 
     * @param loader
     *            načítač zdrojových souborů
     * @param splitter
     *            dělič vět
     * @param normalizer
     *            normalizér
     * @param language
     *            jazyk konverzace
     * @param parserFactory
     *            továrna na parser šablon
     * @param defaultPredicates
     *            výchozí predikáty (názvy a jejich hodnoty)
     * @param predicatesSetBehavior
     *            názvy predikátů zobrazené na strategie pro zobrazení výstupu
     *            při jejich nastavování
     */
    public AIMLConversation(final Loader loader, final Splitter splitter,
            final Normalizer normalizer, final Language language,
            final TemplateParserFactory parserFactory,
            final Map<String, String> defaultPredicates,
            final Map<String, DisplayStrategy> predicatesSetBehavior) {
        this(loader, splitter, normalizer, language, parserFactory,
                defaultPredicates, predicatesSetBehavior, Executors
                        .newSingleThreadExecutor());
    }

    /**
     * Konstruktor nové konverzace. Pro určeného bota načte jeho zdrojové
     * soubory, do kterých dosadí jeho predikáty.
     * 
     * @param loader
     *            načítač zdrojových souborů
     * @param splitter
     *            dělič vět
     * @param normalizer
     *            normalizér
     * @param language
     *            jazyk konverzace
     * @param parserFactory
     *            továrna na parser šablon
     * @param defaultPredicates
     *            výchozí predikáty (názvy a jejich hodnoty)
     * @param predicatesSetBehavior
     *            názvy predikátů zobrazené na strategie pro zobrazení výstupu
     *            při jejich nastavování
     * @param executor
     *            exekutor vláken
     */
    public AIMLConversation(final Loader loader, final Splitter splitter,
            final Normalizer normalizer, final Language language,
            final TemplateParserFactory parserFactory,
            final Map<String, String> defaultPredicates,
            final Map<String, DisplayStrategy> predicatesSetBehavior,
            final ExecutorService executor) {
        LOGGER.log(Level.INFO, "responder.ConversationCreating", new Object[] { loader,
                splitter, normalizer, language, parserFactory, defaultPredicates, predicatesSetBehavior, executor });

        this.loader = loader;
        this.splitter = splitter;
        this.normalizer = normalizer;
        this.language = language;
        this.parserFactory = parserFactory;

        this.predicates = keysToUppercase(new HashMap<String, String>(defaultPredicates));
        this.predicatesSetBehavior =
                keysToUppercase(new HashMap<String, DisplayStrategy>(predicatesSetBehavior));

        this.executor = executor;
        
        LOGGER.log(Level.INFO, "responder.ConversationCreated");
    }
    
    /**
     * Převede klíče mapy na velká písmena. Duplicitní položky budou bez záruky přemazány poslední úpravou.
     * 
     * @param map mapa
     * @param <V> typ hodnoty
     * @return mapa s klíči s velkými písmeny
     */
    private static <V> Map<String, V> keysToUppercase(final Map<String, V> map) {
        final Set<Entry<String, V>> entries = map.entrySet();
        final Map<String, V> result = new HashMap<>();
        
        for (final Entry<String, V> entry : entries) {
            result.put(entry.getKey().toUpperCase(), entry.getValue());
        }
        
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation#
     * getUserInput
     * (cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIML2DIndex)
     */
    @Override
    public String getUserInput(final TwoDimensionalIndex index) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "responder.ConversationUserInput", new Object[] { this,
                    inputs, index });            
        }

        final String speech = inputs.get(inputs.size() - index.getFirstValue());

        final String substituted = substitute(speech);

        final String[] sentences = splitter.splitToSentences(substituted);
        
        final String sentence = sentences[index.getSecondValue() - 1];
        
        return sentence;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation#
     * getBotOutput
     * (cz.cuni.mff.ms.brodecva.botnicek.library.parser.AIML2DIndex)
     */
    @Override
    public String getBotOutput(final TwoDimensionalIndex index) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "responder.ConversationBotOutput", new Object[] { this,
                    outputs, index });
        }

        final String speech =
                outputs.get(outputs.size() - index.getFirstValue());

        final String substituted = substitute(speech);

        final String[] sentences = splitter.splitToSentences(substituted);

        final String sentence = sentences[index.getSecondValue() - 1];

        return sentence;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation#talk(
     * java.lang.String)
     */
    @Override
    public void talk(final String speech) throws ConversationException {
        LOGGER.log(Level.INFO, "responder.ConversationTalk",
                new Object[] { this, speech });
        
        resetAttempts();
        
        evaluate(speech, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation#talk(
     * java.lang.String,
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Listener)
     */
    @Override
    public void talk(final String speech, final Listener listener) {
        LOGGER.log(Level.INFO, "responder.ConversationTalkWithListener", new Object[] {
                this, speech, listener });
        
        if (speech == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("responder.NullSpeech"));
        }

        if (listener == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("responder.NullListener"));
        }
        
        resetAttempts();
        
        this.listener = listener;

        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }

        executor.submit(new Runnable() {

            /*
             * (non-Javadoc)
             * 
             * @see java.lang.Runnable#run()
             */
            @Override
            public void run() {
                try {
                    answerToListener(evaluate(speech, true));
                } catch (final Throwable e) {
                    notifyExceptionToListener(e);
                }
            }

        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation#tryTalk
     * (java.lang.String)
     */
    @Override
    public String attemptTalk(final String speech) throws ConversationException {
        if (isStackFull()) {
            throw new ConversationException(MESSAGE_LOCALIZER.getMessage("responder.StackFull", this, speech));
        } else {
            increaseRecursionLevel();
        }
        
        final String result = evaluate(speech, false);
        
        decreaseRecursionLevel();
        
        LOGGER.log(Level.FINE, "responder.ConversationTryTalk", new Object[] { this,
                speech, result });

        return result;
    }
    
    /**
     * Vrátí místní úroveň zanoření.
     * 
     * @return místní úroveň zanoření
     */
    private ThreadLocal<Integer> getLocalLevel() {
        if (recursionLevel == null) {
            recursionLevel = new ThreadLocalRecursionLevel();
        }
        
        return recursionLevel;
    }
    
    /**
     * Indikuje, zda-li je zásobník pro {@link #attemptTalk(String)} plný.
     * 
     * @return true, pokud je zásobník pro {@link #attemptTalk(String)} plný
     */
    private boolean isStackFull() {
        final int current = getLocalLevel().get();
        
        return current > STACK_SIZE;
    }
    
    /**
     * Resetuje počitadlo pokusů metody {@link #attemptTalk(String)}.
     */
    @Override
    public void resetAttempts() {
        getLocalLevel().remove();
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation#getStackSize()
     */
    @Override
    public int getStackSize() {
        return STACK_SIZE;
    }
    
    /**
     * Zvýší o jedna zaznamenanou úroveň zanoření.
     */
    private void increaseRecursionLevel() {
        final int current = getLocalLevel().get();
        
        getLocalLevel().set(current + 1);
    }
    
    /**
     * Sníží o jedna zaznamenanou úroveň zanoření.
     */
    private void decreaseRecursionLevel() {
        final int current = getLocalLevel().get();
        
        if (current > 0) {
            getLocalLevel().set(current - 1);
        }
    }

    /**
     * Vyhodnotí uživatelský vstup a vrátí reakci.
     * 
     * @param speech
     *            uživatelský vstup
     * @param record
     *            true pro zaznamenávání interakce do historie
     * @return celá reakce na vstup
     * @throws ConversationException
     *             pokud dojde k chybě při vyhodnocování vstupu
     */
    private synchronized String evaluate(final String speech,
            final boolean record) throws ConversationException {
        if (speech == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("responder.NullSpeech"));
        }

        if (record) {
            inputs.add(speech);
        }

        final StringBuilder resultString = new StringBuilder();

        try {
            final String substituted = substitute(speech);

            final String[] sentences = splitter.splitToSentences(substituted);

            for (final String sentence : sentences) {
                final String pattern =
                        normalizer.convertToNormalChars(sentence);

                String that = null;

                if (!outputs.isEmpty()) {
                    that = outputs.get(0);
                }

                if (that == null || that.equals(EMPTY_WORD_VALUE)) {
                    that = AIML.STAR_WILDCARD.getValue();
                } else {
                    that = normalizer.convertToNormalChars(that);
                }

                String topic = getPredicateValue("topic");

                if (topic == null
                        || topic.equals(Conversation.NOT_FOUND_PRED_VALUE)) {
                    topic = AIML.STAR_WILDCARD.getValue();
                } else {
                    topic = normalizer.convertToNormalChars(topic);
                }

                final MatchResult result =
                        loader.getFilledStructure().find(
                                new AIMLInputPath(pattern, that, topic));
                if (result.isSuccesful()) {
                    final String template = result.getTemplate().getValue();

                    final TemplateParser parser =
                            parserFactory
                                    .getParser(this, result, loader
                                            .getFilledStructure()
                                            .isForwardCompatible());

                    final String processedTemplate = parser.process(template);

                    if (record) {
                        outputs.add(processedTemplate);
                    }

                    resultString.append(processedTemplate
                            + XML.SPACE.getValue());
                }
            }
        } catch (final ProcessorException e) {
            throw new ConversationException(e);
        }

        if (resultString.length() > 0) {
            lastResponse =
                    resultString.substring(0, resultString.length()
                            - XML.SPACE.getValue().length());
        } else {
            lastResponse = resultString.toString();
        }
        
        return lastResponse;
    }

    /**
     * Odpoví registrovanému posluchači, pokud nějaký je.
     * 
     * @param response
     *            odpověď
     */
    private void answerToListener(final String response) {
        if (listener != null) {
            listener.answerReceived(new Answer(this, response));
        }
    }
    
    /**
     * Upozorní posluchače na výjimku.
     * 
     * @param e výjimka při zpracování
     */
    private void notifyExceptionToListener(final Throwable e) {
        if (listener != null) {
            listener.exceptionalStateCaught(new ExceptionalState(this, e));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation#listen()
     */
    @Override
    public String listen() {
        LOGGER.log(Level.INFO, "responder.ConversationListen", new Object[] { this,
                lastResponse });

        final String result = lastResponse;

        lastResponse = null;

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation#learn
     * (java.nio.file.Path)
     */
    @Override
    public void learn(final Path location) throws LoaderException {
        LOGGER.log(Level.INFO, "responder.ConversationLearn", new Object[] { this,
                location });

        loader.loadFromLocation(location, Arrays.asList(new String[0]),
                Arrays.asList(new String[0]));
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation#
     * getCategoryCount()
     */
    @Override
    public int getCategoryCount() {
        return loader.getFilledStructure().getCategoryCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation#
     * getPredicateValue(java.lang.String)
     */
    @Override
    public String getPredicateValue(final String name) {
        final String value = predicates.get(name.toUpperCase());
        final String result;
        if (value == null) {
            result = Conversation.NOT_FOUND_PRED_VALUE;
        } else {
            result = value;
        }
        
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "responder.ConversationPredicateResult", new Object[] { this, name, result });
        }
        
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation#
     * setPredicateValue(java.lang.String, java.lang.String)
     */
    @Override
    public String setPredicateValue(final String name, final String value) {
        if (name == null || value == null) {
            throw new NullPointerException(MESSAGE_LOCALIZER.getMessage("responder.NullPredicate"));
        }
        
        predicates.put(name.toUpperCase(), value);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "responder.ConversationPredicateSet", new Object[] { this, name, value });
        }

        final DisplayStrategy displayStrategy = predicatesSetBehavior.get(name.toUpperCase());
        if (displayStrategy == null) {
            return value;
        }
        
        final String output = displayStrategy.display(name, value);
        return output;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Conversation#getBot()
     */
    @Override
    public Bot getBot() {
        return loader.getBot();
    }

    /**
     * Zbaví text vnitřní interpunkce, emotikon, zkratek a opraví pravopis.
     * 
     * @param text
     *            text
     * @return ošetřený text
     */
    private String substitute(final String text) {
        final String emoticonsSubstituted = language.substituteEmoticons(text);

        final String abbreviationsExpanded =
                language.expandAbbreviations(emoticonsSubstituted);

        final String spellingCorrected =
                language.correctSpellingAndColloquialisms(abbreviationsExpanded);

        final String withoutPunctuation =
                language.removeInnerSentencePunctuation(spellingCorrected);

        return withoutPunctuation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("AIMLConversation [loader=");
        builder.append(loader);
        builder.append(", inputs=");
        builder.append(inputs);
        builder.append(", outputs=");
        builder.append(outputs);
        builder.append(", lastResponse=");
        builder.append(lastResponse);
        builder.append(", predicates=");
        builder.append(predicates);
        builder.append(", splitter=");
        builder.append(splitter);
        builder.append(", normalizer=");
        builder.append(normalizer);
        builder.append(", parserFactory=");
        builder.append(parserFactory);
        builder.append(", executor=");
        builder.append(executor);
        builder.append(", listener=");
        builder.append(listener);
        builder.append(", categoryCount=");
        builder.append(getCategoryCount());
        builder.append(", bot=");
        builder.append(getBot());
        builder.append("]");
        return builder.toString();
    }
}
