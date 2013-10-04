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
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text;

/**
 * Definice AIML robota.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLBot implements Bot, Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -5328209771058948784L;
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLBot.class);
    
    /**
     * Predikáty robota.
     */
    private final Map<String, String> predicates;

    /**
     * Umístění zdrojových souborů bota.
     */
    private final URI filesLocation;

    /**
     * Jedinečné jméno bota.
     */
    private final String name;

    /**
     * Slova načítaná v přednostním pořadí.
     */
    private final List<String> beforeLoadingOrder;

    /**
     * Slova načítaná dodatečně.
     */
    private final List<String> afterLoadingOrder;

    /**
     * Jazyk robota.
     */
    private final Language language;

    /**
     * Cesta k souboru s uloženými promluvami o věcech.
     */
    private final URI gossipPath;
    
    /**
     * Cachovaný {@link #hashCode()}.
     */
    private transient Integer cachedHash = null;

    /**
     * Konstruktor bota.
     * 
     * @param name
     *            jméno
     * @param language
     *            jazyk
     * @param filesLocation
     *            umístění zdrojových souborů bota
     * @param gossipPath
     *            umístění soubor k zaznamenání promluv
     * @param predicates
     *            uživatelské predikáty (výchozí)
     * @param beforeLoadingOrder
     *            slova načítaná v přednostním pořadí
     * @param afterLoadingOrder
     *            slova načítaná dodatečně
     */
    public AIMLBot(final String name, final Language language,
            final Path filesLocation, final Path gossipPath,
            final Map<String, String> predicates,
            final List<String> beforeLoadingOrder,
            final List<String> afterLoadingOrder) {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "responder.BotCreating", new Object[] { name, language,
                    filesLocation, gossipPath,
                    predicates,
                    beforeLoadingOrder, afterLoadingOrder });
        }
        
        this.name = name;
        this.language = language;
        this.filesLocation = filesLocation.toUri();
        this.gossipPath = gossipPath.toUri();
        this.predicates = new HashMap<String, String>(predicates);
        this.beforeLoadingOrder = new ArrayList<String>(beforeLoadingOrder);
        this.afterLoadingOrder = new ArrayList<String>(afterLoadingOrder);
        
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "responder.BotCreated", name);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot#getPredicateValue
     * (java.lang.String)
     */
    @Override
    public String getPredicateValue(final String name) {
        final String value = predicates.get(name);
        final String result;
        if (value == null) {
            result = Conversation.NOT_FOUND_PRED_VALUE;
        } else {
            result = value;
        }
        
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "responder.BotPredicateResult", new Object[] { this, name, result });
        }
        
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot#getFilesPath()
     */
    @Override
    public Path getFilesPath() {
        return Paths.get(filesLocation);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot#
     * getBeforeOrder ()
     */
    @Override
    public List<String> getBeforeOrder() {
        return beforeLoadingOrder;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot#
     * getAfterOrder ()
     */
    @Override
    public List<String> getAfterOrder() {
        return afterLoadingOrder;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot#getLanguage()
     */
    @Override
    public Language getLanguage() {
        return language;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot#getGossipPath()
     */
    @Override
    public Path getGossipPath() {
        return Paths.get(gossipPath);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (cachedHash != null) {
            return cachedHash;
        }
        
        final int prime = 31;
        int result = 1;
        result =
                prime
                        * result
                        + afterLoadingOrder.hashCode();
        result =
                prime
                        * result
                        + beforeLoadingOrder.hashCode();
        result =
                prime
                        * result
                        + filesLocation.hashCode();
        result =
                prime
                        * result
                        + gossipPath.hashCode();
        result =
                prime
                        * result
                        + language.hashCode();
        result =
                prime * result
                        + name.hashCode();
        result =
                prime * result
                        + predicates.hashCode();
        
        cachedHash = result;
        
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot#equals(java.lang
     * .Object)
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
        final AIMLBot other = (AIMLBot) obj;
        if (!name.equals(other.name)) {
            return false;
        }
        if (!filesLocation.equals(filesLocation)) {
            return false;
        }
        if (!gossipPath.equals(other.gossipPath)) {
            return false;
        }
        if (!afterLoadingOrder.equals(afterLoadingOrder)) {
            return false;
        }
        if (!beforeLoadingOrder.equals(other.beforeLoadingOrder)) {
            return false;
        }
        if (!language.equals(other.language)) {
            return false;
        }
        if (!predicates.equals(other.predicates)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot#toString()
     */
    @Override
    public String toString() {
        final int maxLen = 10;
        final StringBuilder builder = new StringBuilder();
        builder.append("AIMLBot [name=");
        builder.append(name);
        builder.append(", language=");
        builder.append(language);
        builder.append(", filesLocation=");
        builder.append(filesLocation);
        builder.append(", predicates=");
        builder.append(Text.toString(predicates.entrySet(), maxLen));
        builder.append(", beforeLoadingOrder=");
        builder.append(Text.toString(beforeLoadingOrder, maxLen));
        builder.append(", afterLoadingOrder=");
        builder.append(Text.toString(afterLoadingOrder, maxLen));
        builder.append(", gossipPath=");
        builder.append(gossipPath);
        builder.append("]");
        return builder.toString();
    }

}
