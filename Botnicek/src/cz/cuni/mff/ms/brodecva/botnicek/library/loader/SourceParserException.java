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
package cz.cuni.mff.ms.brodecva.botnicek.library.loader;

/**
 * Výjimka v průběhu načítání a zpracování zdrojových souborů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class SourceParserException extends Exception {
    
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 4444662262587032985L;
    
    /**
     * Hodnota indikující nepřítomné systémové či veřejné ID prvku s výskytem chyby.
     */
    public static final String NOT_PRESENT_ID = null;
    
    /**
     * Hodnota indikující nepřítomné číslo sloupce či řádky výskytu chyby.
     */
    public static final int NOT_PRESENT_NUMBER = -1;
    
    /**
     * Systémový identifikátor prvku s výskytem chyby.
     */
    private final String systemId;
    
    /**
     * Veřejný identifikátor prvku s výskytem chyby.
     */
    private final String publicId;
    
    /**
     * Číslo řádku výskytu chyby (první má číslo 1).
     */
    private final int lineNumber;
    
    /**
     * Číslo sloupce výskytu chyby (první má číslo 1).
     */
    private final int columnNumber;
    
    /**
     * Vytvoří novou výjimku s {@code null} místo podrobné zprávy. Příčina
     * není inicializována, může být dodatečně nastavena voláním
     * {@link Throwable#initCause(java.lang.Throwable)}.
     */
    public SourceParserException() {
        super();
        
        this.systemId = null;
        this.publicId = null;
        this.lineNumber = -1;
        this.columnNumber = -1;
    }

    /**
     * Vytvoří novou výjimku s danou podrobnou zprávou a příčinou. <br>
     * Je třeba brát v potaz, že podrobná zpráva o příčině není automaticky
     * začleněna do podrobné zprávy této výjimky.
     * 
     * @param message
     *            podrobná zpráva. Podrobná zprava je uložena por pozdější
     *            získání pomocí metody {@link Throwable#getMessage()}.
     * @param cause
     *            příčina (která je uložena pro pozdější získání pomocí metody
     *            {@link Throwable#getCause()}). (Hodnota {@code null} je
     *            povolena: říká, že příčina neexistuje či není známa.)
     */
    public SourceParserException(final String message, final Throwable cause) {
        super(message, cause);
        
        this.systemId = null;
        this.publicId = null;
        this.lineNumber = -1;
        this.columnNumber = -1;
    }

    /**
     * Vytvoří novou výjimku s podrobnou zprávou. Příčina není inicializována,
     * může být dodatečně nastavena voláním
     * {@link Throwable#initCause(java.lang.Throwable)}.
     * 
     * @param message
     *            podrobná zpráva. Podrobná zprava je uložena por pozdější
     *            získání pomocí metody {@link Throwable#getMessage()}.
     */
    public SourceParserException(final String message) {
        super(message);
        
        this.systemId = null;
        this.publicId = null;
        this.lineNumber = -1;
        this.columnNumber = -1;
    }

    /**
     * Vytvoří novou výjimku s danou příčinou a podrobnou zprávou
     * {@code (cause==null \? null : cause.toString()} (což typicky
     * obsahuje třídu a podrobnou zprávu o příčině). Tento konstruktor je
     * užitečný pro výjimky, které nejsou nic víc než obaly pro jiné vyhoditelné
     * objekty.
     * 
     * @param cause
     *            příčina (která je uložena pro pozdější získání pomocí metody
     *            {@link Throwable#getCause()}). (Hodnota {@code null} je
     *            povolena: říká, že příčina neexistuje či není známa.)
     */
    public SourceParserException(final Throwable cause) {
        super(cause);
        
        this.systemId = null;
        this.publicId = null;
        this.lineNumber = -1;
        this.columnNumber = -1;
    }
    
    /**
     * Vytvoří novou výjimku s danou příčinou a podrobnou zprávou
     * {@code (cause==null \? null : cause.toString()} (což typicky
     * obsahuje třídu a podrobnou zprávu o příčině). Tento konstruktor je
     * užitečný pro výjimky, které nejsou nic víc než obaly pro jiné vyhoditelné
     * objekty. Tento konstruktor navíc přikládá do výjimky informace o umístění chyby. 
     * 
     * @param cause
     *            příčina (která je uložena pro pozdější získání pomocí metody
     *            {@link Throwable#getCause()}). (Hodnota {@code null} je
     *            povolena: říká, že příčina neexistuje či není známa.)
     * @param systemId systémový identifikátor prvku s chybou (Hodnota {@code null} je
     *            povolena: říká, že ID není dostupné.)
     * @param publicId veřejný identifikátor prvku s chybou (Hodnota {@value #NOT_PRESENT_ID} je
     *            povolena: říká, že ID není dostupné.)
     * @param lineNumber číslo řádku s chybou (číslováno od 1, hodnota {@value #NOT_PRESENT_NUMBER} značí chybějící informaci)
     * @param columnNumber číslo sloupce s chybou (číslováno od 1, hodnota {@value #NOT_PRESENT_NUMBER} značí chybějící informaci)
     */
    public SourceParserException(final Throwable cause, final String systemId,
            final String publicId, final int lineNumber, final int columnNumber) {
        super(cause);
        
        this.systemId = systemId;
        this.publicId = publicId;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
    
    /**
     * @return systémový identifikátor prvku s chybou (Hodnota {@code null} je
     *            povolena: říká, že ID není dostupné.)
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * @return veřejný identifikátor prvku s chybou (Hodnota {@value #NOT_PRESENT_ID} je
     *            povolena: říká, že ID není dostupné.)
     */
    public String getPublicId() {
        return publicId;
    }

    /**
     * @return číslo řádku s chybou (číslováno od 1, hodnota {@value #NOT_PRESENT_NUMBER} značí chybějící informaci)
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * @return číslo sloupce s chybou (číslováno od 1, hodnota {@value #NOT_PRESENT_NUMBER} značí chybějící informaci)
     */
    public int getColumnNumber() {
        return columnNumber;
    }
}
