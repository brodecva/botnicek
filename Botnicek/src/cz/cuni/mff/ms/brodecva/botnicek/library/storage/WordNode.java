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
package cz.cuni.mff.ms.brodecva.botnicek.library.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.Mapper;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.MapperFactory;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * <p>
 * Uzel stromu WordTree uchovávající odkazy na své syny a šablonu reakce na
 * vstup, který je uložen ve struktuře stromu, pokud jej procházíme seshora
 * dolů. Obsahuje informaci o maximální vzdálenosti do některého z listů s
 * vyplněnou šablonou, díky tomu lze optimalizovat prohledávání při větvení.
 * </p>
 * 
 * <p>
 * Tato implementace není bezpečná pro vícevláknové využití.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class WordNode implements Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 944771285396619741L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(WordNode.class);

    /**
     * Mapa se synovskými uzly, klíčovaná podle slova ze vstupní cesty.
     */
    private final Mapper<Word, WordNode> branches;

    /**
     * Šablona s reakcí na vstup zaznamenaný po cestě k tomuto uzlu.
     */
    private Template template = null;

    /**
     * Počet odkazů na synovské uzly v nějaké cestě od tohoto uzlu do listu.
     */
    private int maxHeight = 0;

    /**
     * Úroveň na které se v hlavním stromu nachází tento uzel (0 - kořen).
     */
    private final int depth;

    /**
     * Konstruktor uzlu s určenou továrnou na větvení.
     * 
     * @param depth
     *            hloubka uzlu ve stromu
     * @param parentalWord
     *            slovo, které na uzel odkazuje
     * @param marker
     *            část vstupní cesty, ve které se nacházíme při tvorbě uzlu
     * @param mapperFactory
     *            platná továrna na mapu uzlů
     */
    public WordNode(final int depth, final Word parentalWord,
            final PartMarker marker, final MapperFactory mapperFactory) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "storage.NodeCreation", new Object[] { depth,
                    parentalWord, marker, mapperFactory });
        }

        this.depth = depth;

        if (marker == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("storage.MarkerNull"));
        }

        branches = mapperFactory.getMapper(depth, parentalWord, marker);
    }

    /**
     * Vrátí šablonu v uzlu.
     * 
     * @return šablona s reakcí na vstup zaznamenaný po cestě k tomuto uzlu
     *         (může být null, pokud není koncovým uzlem žádné cesty)
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * Vrátí hloubku uzlu.
     * 
     * @return úroveň na které se v hlavním stromu nachází tento uzel
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Vrátí maximální výška uzlu.
     * 
     * @return maximální počet odkazů na synovské uzly v nějaké cestě od tohoto
     *         uzlu do listu
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Nastaví maximální výšku uzlu.
     * 
     * @param maxHeight
     *            kandidát pro výšku k nastavení (nastavena bude pouze, pokud
     *            bude větší než stávající maximální výška)
     */
    private void setMaxHeight(final int maxHeight) {
        if (maxHeight > this.maxHeight) {
            this.maxHeight = maxHeight;
        }
    }

    /**
     * Prohledá podstrom uzlu.
     * 
     * @param path
     *            vstupní cesta obsahující dotaz, na který se hledá reakce
     * @param currentPart
     *            typ aktuálně porovnávané části vzorku
     * @return výsledek hledání vstupní cesty v podstromu daném tímto uzlem
     */
    public MatchResult find(final InputPath path, final PartMarker currentPart) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "storage.NodeMatching", new Object[] { this, path,
                    currentPart });
        }

        if (path.isEmpty()) {
            if (template == null) {
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, "storage.NodeTemplateNotFound", new Object[] {
                            this, path, currentPart });
                }

                return new FailedResult();
            }
            
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.log(Level.FINEST, "storage.NodeTemplateFound", new Object[] { this,
                        path, currentPart });
            }
            return new SuccesfulResult(template);
        }

        MatchResult result;

        result = suffixSearch(path, AIMLWildcard.UNDERSCORE, currentPart);
        if (result.isSuccesful()) {
            return result;
        }

        result = atomicSearch(path, currentPart);
        if (result.isSuccesful()) {
            return result;
        }

        result = suffixSearch(path, AIMLWildcard.ASTERISK, currentPart);
        if (result.isSuccesful()) {
            return result;
        }
        
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "storage.NodeTriesRunOut", new Object[] { this, path,
                    currentPart });
        }
        return new FailedResult();
    }

    /**
     * Provede hledání v podstromu udaném slovem (tj. bez použití žolíku).
     * 
     * @param path
     *            vstupní cesta obsahující dotaz, na který se hledá reakce
     * @param currentPart
     *            typ aktuálně porovnávané části vzorku
     * @return výsledek hledání v podstromě (pokud existuje) určeném uzlem s
     *         aktuálně hledaným slovem
     */
    private MatchResult atomicSearch(final InputPath path,
            final PartMarker currentPart) {
        final Word rootWord = path.head();

        final WordNode subtreeRoot = branches.get(rootWord);

        if (subtreeRoot == null) {
            return new FailedResult();
        }

        final PartMarker newCurrentPart = getNewPart(rootWord, currentPart);

        return subtreeRoot.find(path.tail(), newCurrentPart);
    }

    /**
     * Suffixové hledání v podstromě (pro žolíky).
     * 
     * @param path
     *            vstupní cesta obsahující dotaz, na který se hledá reakce
     * @param rootWord
     *            slovo ze vstupní cesty, v jehož podstromě jsou hledány suffix
     *            vstupní cesty
     * @param currentPart
     *            typ aktuálně porovnávané části vzorku
     * @return výsledek (alespoň na jednu shodu) hledání mezi suffixy v
     *         podstromě (pokud existuje) určeném slovem ze vstupní cesty
     */
    private MatchResult suffixSearch(final InputPath path, final AIMLWildcard rootWord,
            final PartMarker currentPart) {
        final WordNode subtreeRoot = branches.get(rootWord);

        if (subtreeRoot == null) {
            return new FailedResult();
        }

        final List<Word> wildcardMatchedPart = new ArrayList<Word>();
        Word capturedWord = path.head();
        if (!isPartMarker(capturedWord, currentPart)) {
            wildcardMatchedPart.add(capturedWord);
        }
        
        InputPath suffixPath = path.tail();
        for (; !suffixPath.isEmpty(); suffixPath =
                suffixPath.tail()) {
            final MatchResult result =
                    subtreeRoot.find(suffixPath, currentPart);
            
            if (result.isSuccesful()) {
                result.addStarMatchedPart(currentPart,
                            new AIMLInputPath(wildcardMatchedPart));
                return result;
            } else {
                capturedWord = suffixPath.head();
                if (!isPartMarker(capturedWord, currentPart)) {
                    wildcardMatchedPart.add(capturedWord);
                }
            }
        }

        final MatchResult result = subtreeRoot.find(suffixPath, currentPart);
        if (result.isSuccesful()) {
            result.addStarMatchedPart(currentPart,
                        new AIMLInputPath(wildcardMatchedPart));
        }
        
        return result;
    }

    /**
     * Změní současnou část, pokud je kořenové slovo oddělovačem.
     * 
     * @param rootWord
     *            kořenové slovo podstromu
     * @param currentPart
     *            současná část cesty
     * @return nová část
     */
    private static PartMarker getNewPart(final Word rootWord,
            final PartMarker currentPart) {
        if (currentPart.allValues().contains(rootWord)) {
            return (PartMarker) rootWord;
        }

        return currentPart;
    }
    
    /**
     * Zjistí, zda-li je kořenové slovo oddělovačem.
     * 
     * @param rootWord
     *            kořenové slovo podstromu
     * @param currentPart
     *            současná část cesty
     * @return true, pokud ano
     */
    private static boolean isPartMarker(final Word rootWord, final PartMarker currentPart) {
        final boolean result = currentPart.allValues().contains(rootWord);
        
        return result;
    }

    /**
     * Přidá šablonu na konec vstupní cesty s danou strategií pro větvení uzlu.
     * 
     * @param path
     *            vstupní cesta obsahující dotaz, která se zanáší do cesty od
     *            kořenu stromu
     * @param answer
     *            šablona určená k uložení do posledního uzlu na cestě z kořene
     * @param marker
     *            část vstupní cesty, ve které se nacházíme při přidávání
     * @param mapperFactory
     *            platná továrna na větvení
     */
    public void add(final InputPath path, final Template answer,
            final PartMarker marker, final MapperFactory mapperFactory) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "storage.NodeInsertingPath", new Object[] { this,
                    path, answer, marker });
        }

        if (mapperFactory == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("storage.NullFactory"));
        }

        if (path.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.log(Level.FINER, "storage.NodeTemplateSetting", new Object[] { this,
                        path, answer, marker });
            }
            
            if (template != null) {
                LOGGER.log(Level.WARNING, "storage.TemplateOverwrite", new Object[] { answer, template, this });
            }
            
            template = answer;
            return;
        }

        final Word currentPathWord = path.head();

        final PartMarker newMarker = getNewPart(currentPathWord, marker);

        final WordNode subtreeRoot = branches.get(currentPathWord);
        final InputPath rest = path.tail();
        if (subtreeRoot == null) {
            addChild(currentPathWord, newMarker, mapperFactory).add(rest,
                    answer, newMarker, mapperFactory);
        } else {
            subtreeRoot.add(rest, answer, newMarker, mapperFactory);
        }

        setMaxHeight(rest.getLength());
    }

    /**
     * Přidá nového syna.
     * 
     * @param pathWord
     *            slovo, které bude v rodičovskému uzlu ukazovat na nový uzel
     * @param marker
     *            část vstupní cesty, ve které se nacházíme při přidávání uzlu
     * @param mapperFactory
     *            platná továrna na větvení
     * @return nově přidaný uzel
     */
    private WordNode addChild(final Word pathWord, final PartMarker marker,
            final MapperFactory mapperFactory) {
        final WordNode newNode =
                new WordNode(depth + 1, pathWord, marker, mapperFactory);
        
        branches.put(pathWord, newNode);
        
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "storage.NodeChildAdd", new Object[] { this,
                    newNode });
        }
        
        return newNode;
    }

    /**
     * Synové uzlu.
     * 
     * @return synové uzlu
     */
    public Set<Word> getBranchWords() {
        final Set<Word> result = new HashSet<Word>(2 * branches.getSize());
        for (final Entry<Word, WordNode> branch : branches.getEntries()) {
            result.add(branch.getKey());
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final Set<Map.Entry<Word, WordNode>> branchEntries =
                branches.getEntries();
        final StringBuilder branchesBuilder = new StringBuilder();
        for (final Map.Entry<Word, WordNode> entry : branchEntries) {
            final Word key = entry.getKey();
            final WordNode value = entry.getValue();

            branchesBuilder.append(", " + key.getClass().getName() + '@'
                    + Integer.toHexString(key.hashCode()));
            branchesBuilder.append("=" + value.getClass().getName() + '@'
                    + Integer.toHexString(value.hashCode()));
        }

        final String branchesString =
                branchesBuilder.length() == 0 ? "" : branchesBuilder.toString()
                        .substring(2);

        final StringBuilder builder = new StringBuilder();
        builder.append("WordNode [branches={");
        builder.append(branchesString);
        builder.append("}, template=");
        builder.append(template);
        builder.append(", depth=");
        builder.append(depth);
        builder.append(", maxHeight=");
        builder.append(maxHeight);
        builder.append("]");
        return builder.toString();
    }
}
