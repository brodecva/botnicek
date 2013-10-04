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

import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.easymock.EasyMock;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.test.UnitTest;

/**
 * Testuje implementaci AIML bota.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AIMLBot
 */
@Category(UnitTest.class)
public final class AIMLBotTest {

    /**
     * Definovaný klíč.
     */
    private static final String DEFINED_KEY = "definedKey";

    /**
     * Hodnota definovaného klíče.
     */
    private static final String DEFINED_VALUE = "definedValue";

    /**
     * Nedefinovaný klíč.
     */
    private static final String NOT_DEFINED_KEY = "notDefinedKey";

    /**
     * Hodnota klíče null.
     */
    private static final String VALUE_WITH_NULL_KEY = "valueWithNullKey";

    /**
     * Stub jména.
     */
    private static final String NAME_STUB = "name";

    /**
     * Stub názvů přednostních souborů.
     */
    private static List<String> beforeStub = null;

    /**
     * Stub názvů dodatečných souborů.
     */
    private static List<String> afterStub = null;

    /**
     * Stub jazyka.
     */
    private static Language languageStub = null;

    /**
     * Stub umístění souborů.
     */
    private static Path locationStub = null;

    /**
     * Stub umístění uložených promluv.
     */
    private static Path gossipPathStub = null;

    /**
     * Stub mapy predikátů.
     */
    private static Map<String, String> predicatesStub = null;

    /**
     * Testovaná neměnná instance.
     */
    private static Bot bot = null;

    /**
     * Nastavení stubů.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        predicatesStub = new HashMap<String, String>();

        predicatesStub.put(DEFINED_KEY, DEFINED_VALUE);
        predicatesStub.put(null, VALUE_WITH_NULL_KEY);
        predicatesStub.put(null, VALUE_WITH_NULL_KEY);

        beforeStub = Arrays.asList(new String[] { "before1", "before2" });
        afterStub = Arrays.asList(new String[] { "after1", "after2" });

        locationStub = Paths.get("foo1", "bar1");
        gossipPathStub = Paths.get("foo2", "bar2");

        languageStub = EasyMock.createNiceMock(Language.class);
        replay(languageStub);

        bot =
                new AIMLBot(NAME_STUB, languageStub, locationStub,
                        gossipPathStub, predicatesStub, beforeStub, afterStub);
    }

    /**
     * Úklid stubů.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        predicatesStub = null;

        beforeStub = null;
        afterStub = null;

        locationStub = null;
        gossipPathStub = null;

        bot = null;
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLBot#AIMLBot(java.lang.String, cz.cuni.mff.ms.brodecva.botnicek.library.language.Language, java.nio.file.Path, java.nio.file.Path, java.util.Map, java.util.List, java.util.List)}
     * .
     */
    @Test
    public void testAIMLBot() {
        new AIMLBot(NAME_STUB, languageStub, locationStub, gossipPathStub,
                predicatesStub, beforeStub, afterStub);
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLBot#getPredicateValue(java.lang.String)}
     * .
     */
    @Test
    public void testGetPredicateValueWhenNotDefinedReturnsNotFoundValue() {
        assertEquals(Conversation.NOT_FOUND_PRED_VALUE,
                bot.getPredicateValue(NOT_DEFINED_KEY));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLBot#getPredicateValue(java.lang.String)}
     * .
     */
    @Test
    public void testGetPredicateValueWhenDefinedReturnsDefinedValue() {
        assertEquals(DEFINED_VALUE, bot.getPredicateValue(DEFINED_KEY));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLBot#getPredicateValue(java.lang.String)}
     * .
     */
    @Test
    public void testGetPredicateValueWhenKeyNull() {
        assertEquals(VALUE_WITH_NULL_KEY, bot.getPredicateValue(null));
    }

    /**
     * Test pro
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLBot#equals(java.lang.Object)}
     * a
     * {@link cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLBot#hashCode()}
     * .
     */
    @Test
    public void testEqualsAndHash() {
        EqualsVerifier.forClass(AIMLBot.class).usingGetClass();
    }

    /**
     * Stub jazyka.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    private static class LanguageStub implements Language, Serializable {

        /**
         * UID serializované verze.
         */
        private static final long serialVersionUID = 159503812162432817L;

        /**
         * Náhradní pole.
         */
        private final String dummyField = "dummyLanguageField";

        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#
         * getSentenceDelim()
         */
        @Override
        public Pattern getSentenceDelim() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#swapGender
         * (java.lang.String)
         */
        @Override
        public String swapGender(final String text) {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#
         * transformPerson(java.lang.String)
         */
        @Override
        public String transformPerson(final String text) {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#
         * transformPerson2(java.lang.String)
         */
        @Override
        public String transformPerson2(final String text) {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#getName()
         */
        @Override
        public String getName() {
            return null;
        }
        
        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#expandAbbreviations(java.lang.String)
         */
        @Override
        public String expandAbbreviations(final String text) {
            return null;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#correctSpellingAndColloquialisms(java.lang.String)
         */
        @Override
        public String correctSpellingAndColloquialisms(final String text) {
            return null;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#substituteEmoticons(java.lang.String)
         */
        @Override
        public String substituteEmoticons(final String text) {
            return null;
        }
        
        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.library.language.Language#removeInnerSentencePunctuation(java.lang.String)
         */
        @Override
        public String removeInnerSentencePunctuation(final String text) {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result =
                    prime
                            * result
                            + ((dummyField == null) ? 0 : dummyField.hashCode());
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot#equals(java
         * .lang.Object)
         */
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LanguageStub other = (LanguageStub) obj;
            if (dummyField == null) {
                if (other.dummyField != null) {
                    return false;
                }
            } else if (!dummyField.equals(other.dummyField)) {
                return false;
            }
            return true;
        }
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
        final AIMLBot original =
                new AIMLBot(NAME_STUB, new LanguageStub(), locationStub,
                        gossipPathStub, predicatesStub, beforeStub, afterStub);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        final byte[] pickled = out.toByteArray();
        final InputStream in = new ByteArrayInputStream(pickled);
        final ObjectInputStream ois = new ObjectInputStream(in);
        final Object o = ois.readObject();
        final AIMLBot copy = (AIMLBot) o;

        assertEquals(original, copy);
    }
}
