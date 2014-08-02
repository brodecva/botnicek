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
package cz.cuni.mff.ms.brodecva.botnicek.ide.print;

/**
 * Chyba při výpisu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Printer
 */
public final class PrintException extends Exception {

    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = 622261012819229399L;

    /**
     * Vytvoří novou výjimku s {@code null} místo podrobné zprávy. Příčina
     * není inicializována, může být dodatečně nastavena voláním
     * {@link Throwable#initCause(java.lang.Throwable)}.
     */
    public PrintException() {
        super();
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
    public PrintException(final String message) {
        super(message);
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
    public PrintException(final Throwable cause) {
        super(cause);
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
    public PrintException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
