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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Definice znakových konstant jazyka XML, prostředky pro práci se zdrojovým
 * kódem XML.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public enum XML {
    /**
     * Začátek tagu.
     */
    TAG_START("<"),

    /**
     * Konec tagu.
     */
    TAG_END(">"),

    /**
     * Uvozovka.
     */
    QUOTE("\""),

    /**
     * Rovnítko.
     */
    EQ_SIGN("="),

    /**
     * Mezera.
     */
    SPACE(" "),

    /**
     * Atribut prostoru jmen.
     */
    XMLNS_ATT("xmlns"),

    /**
     * Konec prázdného tagu.
     */
    EMPTY_TAG_END("/>"),

    /**
     * Začátek uzavíracího tagu.
     */
    CLOSING_TAG_START("</"),

    /**
     * Lomítko.
     */
    SLASH("/"),

    /**
     * Začátek CDATA.
     */
    CDATA_START("<![CDATA["),

    /**
     * Konec CDATA.
     */
    CDATA_END("]]>"),

    /**
     * Začátek komentáře.
     */
    COMMENT_START("<!--"),

    /**
     * Konec komentáře.
     */
    COMMENT_END("-->"),

    /**
     * Atribut xml:space.
     */
    SPACE_ATT("xml:space"),

    /**
     * Hodnota atributu space deklarující záměr zachovat všechny bílé znaky.
     */
    SPACE_PRESERVE("preserve"),

    /**
     * Hodnota atributu space deklarující záměr ponechat výchozí zpracování
     * bílých znaků.
     */
    SPACE_DEFAULT("default"),

    /**
     * Oddělovač prefixu v názvu prvku.
     */
    PREFIX_DELIMITER(":");

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Prázdný řetězec.
     */
    private static final String EMPTY = "";

    /**
     * Řetězec s fragmentem kódu jazyka XML.
     */
    private final String value;

    /**
     * Vypíše fragment XML kódu obsahující začátek tagu.
     * 
     * @param uri
     *            URI prostoru jmen s daným prefixem
     * @param localName
     *            místní název
     * @param attributes
     *            atributy elementu jazyka XML rozpoznané SAXem
     * @return fragment zdrojového kódu XML - začátek tagu elementu s atributy
     */
    public static String createElementStart(final String uri,
            final String localName, final Attributes attributes) {
        if (uri == null || localName == null
                || attributes == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("platform.NullArgument"));
        }

        final StringBuilder result = new StringBuilder();

        result.append(TAG_START.getValue());
        result.append(localName);

        result.append(XML.SPACE);
        result.append(XML.XMLNS_ATT);
        result.append(EQ_SIGN);
        result.append(QUOTE);
        result.append(uri);
        result.append(QUOTE);

        result.append(printAttributes(attributes));
        result.append(TAG_END.getValue());

        return result.toString();
    }

    /**
     * Vypíše fragment XML kódu obsahující konec párového tagu.
     * 
     * @param localName
     *            místní název
     * @return fragment zdrojového kódu XML - konec tagu
     */
    public static String createElementEnd(final String localName) {
        if (localName == null) {
            throw new NullPointerException(
                    MESSAGE_LOCALIZER.getMessage("platform.NullArgument"));
        }

        final StringBuilder result = new StringBuilder();

        result.append(XML.CLOSING_TAG_START.getValue());
        result.append(localName);
        result.append(XML.TAG_END.getValue());

        return result.toString();
    }

    /**
     * Vytiskne začátek tagu.
     * 
     * @param element
     *            prvek
     * @return vytištěný začátek tagu
     */
    public static String createElementStart(final Element element) {
        final StringBuilder result = new StringBuilder();

        result.append(TAG_START);

        final String prefix = element.getPrefix();
        final String name =
                ((prefix == null) ? EMPTY : prefix + ":")
                        + element.getLocalName();

        result.append(name);
        result.append(printAttributes(element.getAttributes()));
        result.append(TAG_END);

        return result.toString();
    }

    /**
     * Vytiskne zkrácený tag.
     * 
     * @param element
     *            prvek
     * @return vytištěný zkrácený tag
     */
    public static String createEmptyElement(final Element element) {
        final StringBuilder result = new StringBuilder();

        result.append(TAG_START);

        final String prefix = element.getPrefix();
        final String name =
                ((prefix == null) ? EMPTY : prefix + ":")
                        + element.getLocalName();

        result.append(name);
        result.append(printAttributes(element.getAttributes()));
        result.append(EMPTY_TAG_END);

        return result.toString();
    }

    /**
     * Vytiskne konec tagu.
     * 
     * @param element
     *            prvek
     * @return vytištěný konec prvku
     */
    public static String createElementEnd(final Element element) {
        final String prefix = element.getPrefix();
        final String name =
                ((prefix == null) ? EMPTY : prefix + ":")
                        + element.getLocalName();

        return CLOSING_TAG_START + name + TAG_END;
    }

    /**
     * Vypíše atributy jako fragment XML kódu. Metoda vyžaduje, aby všechny
     * atributy měly platné kvalifikované jméno.
     * 
     * @param attributes
     *            atributy elementu jazyka XML rozpoznané SAXem
     * @return fragment zdrojového kódu XML s vypsanými atributy
     */
    public static String printAttributes(final Attributes attributes) {
        final StringBuilder result = new StringBuilder();

        final int attributesCount = attributes.getLength();
        for (int index = 0; index < attributesCount; index++) {
            final String name = attributes.getQName(index);

            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException(
                        MESSAGE_LOCALIZER
                                .getMessage("platform.QNameInvalid", attributes));
            }

            result.append(SPACE.getValue());
            result.append(name + EQ_SIGN + QUOTE + attributes.getValue(index)
                    + QUOTE);
        }

        return result.toString();
    }

    /**
     * Vypíše atributy jako fragment XML kódu.
     * 
     * @param attributes
     *            atributy elementu jazyka XML v DOM
     * @return fragment zdrojového kódu XML s vypsanými atributy
     */
    public static String printAttributes(final NamedNodeMap attributes) {
        final StringBuilder result = new StringBuilder();

        final int attributesCount = attributes.getLength();
        for (int index = 0; index < attributesCount; index++) {
            final Attr attribute = (Attr) attributes.item(index);

            final String prefix = attribute.getPrefix();
            
            final String name =
                    ((prefix == null) ? EMPTY : prefix + ":")
                            + attribute.getLocalName();

            result.append(SPACE);
            result.append(name + EQ_SIGN + QUOTE + attribute.getNodeValue()
                    + QUOTE);
        }

        return result.toString();
    }
    
    /**
     * Vrátí v pořadí takové synovské uzly prvky, které jsou také prvky.
     * 
     * @param element otcovský prvek
     * @return uspořádané synovské prvky
     */
    public static List<Element> listFilialElements(final Element element) {
        final List<Element> result = new ArrayList<Element>();
        final NodeList children = element.getChildNodes();
        final int count = children.getLength();
        for (int i = 0; i < count; i++) {
            final Node child = children.item(i);
            
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                result.add((Element) child);
            }
        }
        
        return result;
    }

    /**
     * Konstruktor znakové konstanty jazyka XML.
     * 
     * @param value
     *            textová hodnota
     */
    private XML(final String value) {
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
     * @return textová hodnota pojmenovaného fragmentu
     */
    @Override
    public String toString() {
        return value;
    }
}
