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
package cz.cuni.mff.ms.brodecva.botnicek.library.platform;

/**
 * Definice klíčových slov a namespace URI jazyka AIML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
/**
 * @author user
 * 
 */
public enum AIML {
    /**
     * Prvek pattern.
     */
    PATTERN("pattern"),

    /**
     * Prvek bot.
     */
    BOT("bot"),

    /**
     * Prvek that.
     */
    THAT("that"),

    /**
     * Prvek topic.
     */
    TOPIC("topic"),

    /**
     * Prvek template.
     */
    TEMPLATE("template"),

    /**
     * Prvek gender.
     */
    GENDER("gender"),

    /**
     * Prvek star.
     */
    STAR("star"),

    /**
     * Prvek srai.
     */
    SRAI("srai"),

    /**
     * Prvek person.
     */
    PERSON("person"),

    /**
     * Prvek person2.
     */
    PERSON2("person2"),

    /**
     * Prvek name.
     */
    NAME("name"),

    /**
     * Žolík hvězdička.
     */
    STAR_WILDCARD("*"),
    
    /**
     * Žolík podtržítko.
     */
    UNDERSCORE_WILDCARD("_"),

    /**
     * Přípona souborů.
     */
    FILE_SUFFIX("aiml"),

    /**
     * Rozpoznávaný prostor jmen.
     */
    NAMESPACE_URI("http://alicebot.org/2001/AIML-1.0.1"),
    
    /**
     * Prostor jmen schématu.
     */
    BACKUP_SCHEMA_LOCATION("http://aitools.org/aiml/schema/AIML.xsd"),
    
    /**
     * Atribut name.
     */
    ATT_NAME("name"),

    /**
     * Atribut value.
     */
    ATT_VALUE("value"),

    /**
     * Atribut index.
     */
    ATT_INDEX("index"),
    
    /**
     * Kořen AIML dokumentu.
     */
    AIML("aiml"),
    
    /**
     * Atribut verze dokumentu.
     */
    ATT_VERSION("version"),
    
    /**
     * Označení implementované verze.
     */
    IMPLEMENTED_VERSION("1.0.1"),
    
    /**
     * Prvek kategorie.
     */
    CATEGORY("category"),
    
    /**
     * Oddělovač slov.
     */
    WORD_DELIMITER(" "),
    
    
    /**
     * Oddělovač indexů ve 2D indexu.
     */
    INDICES_DELIMITER(", "),
    
    /**
     * Výchozí prefix.
     */
    DEFAULT_PREFIX(""),
    
    /**
     * Název predikátu pro nastavení tématu.
     */
    TOPIC_PREDICATE("TOPIC");

    /**
     * Textová hodnota výrazu.
     */
    private final String value;

    /**
     * Konstruktor klíčového slova.
     * 
     * @param value
     *            textová hodnota výrazu
     */
    private AIML(final String value) {
        this.value = value;
    }

    /**
     * Vrátí textovou hodnotu.
     * 
     * @return textová hodnota
     */
    public String getValue() {
        return value;
    }

    /**
     * Předefinovaná metoda toString pro výčtový typ vrací přímo využitelnou
     * textovou hodnotu.
     * 
     * @return textová hodnota klíčového slova
     */
    @Override
    public String toString() {
        return getValue();
    }

}
