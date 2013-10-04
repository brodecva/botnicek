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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage.map;

import java.io.Serializable;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordNode;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.classes.ClassManagment;

/**
 * Tovární třída pro konstrukci efektivní mapy užité pro uložení odkazů na
 * synovské uzly stromové struktury.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
/**
 * @author user
 * @version 1.0
 * 
 */
public final class FrugalMapperFactory implements MapperFactory, Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 8708463654377923169L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(FrugalMapperFactory.class);

    /**
     * Číselné hodnoty faktorů užitých pro rozhodování o typu vytvořené mapy.
     * 
     * @author Václav Brodec
     * @version 1.0
     */
    public enum Factor {
        /**
         * Počet položek kořene.
         */
        ROOT(250),

        /**
         * Jednoprvkový uzel.
         */
        SINGLE(1),

        /**
         * Počet položek uzlu v hloubce 1.
         */
        DEPTH_ONE(5),

        /**
         * Délka krátkého slova.
         */
        SHORT_WORD(2);

        /**
         * Číselná hodnota.
         */
        private final int value;

        /**
         * Vrátí číselnou hodnotu faktoru.
         * 
         * @return číselná hodnota faktoru
         */
        public int getValue() {
            return value;
        }

        /**
         * Konstruktor faktoru.
         * 
         * @param value
         *            číselná hodnota
         */
        private Factor(final int value) {
            this.value = value;
        }
    }

    /**
     * Významnost hloubky uzlu.
     */
    private static final float DEPTH_IMPORTANCE = 0.5f;

    /**
     * Významnost otcovského slova.
     */
    private static final float PARENTAL_WORD_IMPORTANCE = 0.3f;

    /**
     * Významnost části vstupní cesty.
     */
    private static final float PART_IMPORTANCE = 0.2f;

    /**
     * Intervaly pro výběr určené svými spodními hranicemi.
     */
    private static final TreeMap<Integer, Class<? extends MapperCore<Word, WordNode>>> INTERVAL_TO_CHOICE =
            new TreeMap<Integer, Class<? extends MapperCore<Word, WordNode>>>();

    static {
        final Class<?> singleEntry = SingleEntryCore.class;
        @SuppressWarnings("unchecked")
        final Class<? extends MapperCore<Word, WordNode>> singleEntryCoreKlass =
                (Class<? extends MapperCore<Word, WordNode>>) singleEntry;
        INTERVAL_TO_CHOICE.put(SingleEntryCore.CAPACITY, singleEntryCoreKlass);

        final Class<?> array = ArrayCore.class;
        @SuppressWarnings("unchecked")
        final Class<? extends MapperCore<Word, WordNode>> arrayCoreKlass =
                (Class<? extends MapperCore<Word, WordNode>>) array;
        INTERVAL_TO_CHOICE.put(ArrayCore.MINIMUM_CAPACITY, arrayCoreKlass);

        final Class<?> hashMap = HashMapCore.class;
        @SuppressWarnings("unchecked")
        final Class<? extends MapperCore<Word, WordNode>> mapCoreKlass =
                (Class<? extends MapperCore<Word, WordNode>>) hashMap;
        INTERVAL_TO_CHOICE.put(ArrayCore.MAXIMUM_CAPACITY + 1, mapCoreKlass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.
     * MapperFactory#getMapper(int,
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.Word,
     * cz.cuni.mff.ms.brodecva.botnicek.library.storage.PartMarker)
     */
    @Override
    public Mapper<Word, WordNode> getMapper(final int depth,
            final Word parentalWord, final PartMarker part) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "storage.map.MapperCreationStart", new Object[] { depth,
                    parentalWord, part });
        }

        if (depth < 0) {
            throw new IllegalArgumentException(
                    MESSAGE_LOCALIZER.getMessage("storage.map.NegativeDepth"));
        }

        final int expectedSize =
                (int) Math.ceil(assessDepthFactor(depth) * DEPTH_IMPORTANCE
                        + assessParentalWord(parentalWord)
                        * PARENTAL_WORD_IMPORTANCE + assessPart(part)
                        * PART_IMPORTANCE);

        final Class<? extends MapperCore<Word, WordNode>> choice =
                INTERVAL_TO_CHOICE.floorEntry(expectedSize).getValue();

        final MapperCore<Word, WordNode> providedMapperCore =
                ClassManagment.getNewInstance(choice, Integer.TYPE,
                        expectedSize);

        final Mapper<Word, WordNode> mapper =
                new AdaptiveMapper<Word, WordNode>(providedMapperCore);
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "storage.map.MapperCreationFinished", mapper);
        }

        return mapper;
    }

    /**
     * Zhodnocení vlivu hloubky aktuálního uzlu na potřebný počet větvení.
     * 
     * @param depth
     *            hloubka uzlu, pro který se mapa vytváří, ve stromu
     * @return skóre příslušící dané hloubce uzlu
     */
    private static float assessDepthFactor(final int depth) {
        if (depth == 0) {
            // Kořen
            return Factor.ROOT.getValue();
        }

        return Math.max(Factor.SINGLE.getValue(), Factor.DEPTH_ONE.getValue()
                / depth); // S přibývající hloubkou menší uzly.
    }

    /**
     * Zhodnocení vlivu slova odkazujícího na aktuální uzel na velikost větvení.
     * Bere v potaz délku slova či jeho příslušnost mezi zástupné znaky.
     * 
     * @param parentalWord
     *            slovo přímo odkazující na uzel, pro který se mapa vytváří
     * @return skóre odpovídající charakteru odkazujícího slova
     */
    private static float assessParentalWord(final Word parentalWord) {
        if (parentalWord == null) {
            // Kořen
            return Factor.ROOT.getValue();
        }

        return Math.max(Factor.SINGLE.getValue(), Factor.SHORT_WORD.getValue()
                / parentalWord.getValue().length());
    }

    /**
     * Zhodnocení vlivu části vstupní cesty (vzor, that či tématická část), ve
     * které se nachází proces přidávání vstupní cesty do stromu.
     * 
     * @param part
     *            část vstupní cesty
     * @return ohodnocení
     */
    private static float assessPart(final PartMarker part) {
        return Factor.SINGLE.getValue(); // Není třeba zohlednit, stačí hloubka.
    }
}
