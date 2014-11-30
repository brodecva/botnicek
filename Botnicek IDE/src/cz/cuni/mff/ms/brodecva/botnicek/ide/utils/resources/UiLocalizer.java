/**
 * Copyright Václav Brodec 2014.
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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources;

import java.util.ResourceBundle;

/**
 * Překladač řetězců v uživatelském rozhraní.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see AbstractBundleLocalizer pro specifikaci parametrů v textu balíku
 */
public final class UiLocalizer extends AbstractBundleLocalizer {

    /**
     * Lokální umístění překladu řetězců.
     */
    public static final String BUNDLE_PACKAGE_NAME = ".ui";

    /**
     * Výchozí zdroj překladu řetězců.
     */
    private static final ResourceBundle DEFAULT_LOCALE = ResourceBundle
            .getBundle(ROOT_PACKAGE + BUNDLE_PACKAGE_NAME);

    /**
     * Vrátí překladač řetězců pro třídu využívající výchozí umístění.
     * 
     * @return překladač zpráv pro výjimky ve třídě
     */
    public static UiLocalizer get() {
        return new UiLocalizer(DEFAULT_LOCALE);
    }

    /**
     * Vrátí přeloženou zprávu.
     * 
     * @param key
     *            klíč pro hledaný řetězec, musí pro něj existovat překlad
     * @param params
     *            objekty k substituci do lokalizované zprávy
     * @return řetězec pro daný klíč
     * @see java.util.logging.Logger#log(java.util.logging.Level, String,
     *      Object[]) Obdobně užitá metoda
     */
    public static String print(final String key, final Object... params) {
        return get().getMessage(key, params);
    }

    private UiLocalizer(final ResourceBundle locale) {
        super(locale);
    }
}
