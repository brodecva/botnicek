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
package cz.cuni.mff.ms.brodecva.botnicek.library.utils;

import java.util.ResourceBundle;

/**
 * Překladač zpráv do výjimek.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class ExceptionMessageLocalizer {
    /**
     * Lokální umístění překladu výjimek.
     */
    public static final String BUNDLE_PACKAGE_NAME = ".exceptions";

    /**
     * Výchozí zdroj překladu výjimek.
     */
    private static final ResourceBundle DEFAULT_LOCALE =
            ResourceBundle.getBundle("cz.cuni.mff.ms.brodecva.botnicek.library"
                    + BUNDLE_PACKAGE_NAME);

    /**
     * Zdroj s překlady.
     */
    private final ResourceBundle locale;

    /**
     * Vrátí překladač výjimek pro třídu využívající výchozí umístění.
     * 
     * @return překladač zpráv pro výjimky ve třídě
     */
    public static ExceptionMessageLocalizer getLocalizer() {
        return new ExceptionMessageLocalizer(DEFAULT_LOCALE);
    }

    /**
     * Vrátí překladač výjimek pro třídu.
     * 
     * @param utilizingClass
     *            využívající třída, nesmí být null, očekává se přítomnost
     *            exceptions.properties v daném balíčku
     * @return překladač zpráv pro výjimky ve třídě
     */
    public static ExceptionMessageLocalizer getLocalizer(
            final Class<?> utilizingClass) {
        return new ExceptionMessageLocalizer(
                getClassExceptionBundle(utilizingClass));
    }

    /**
     * Vrátí zdrojový balík s přeloženými výjimkami.
     * 
     * @param utilizingClass
     *            využívající třída, nesmí být null, očekává se přítomnost
     *            exceptions.properties v daném balíčku
     * @return zdrojový balík s přeloženými výjimkami
     */
    private static ResourceBundle getClassExceptionBundle(
            final Class<?> utilizingClass) {
        return ResourceBundle.getBundle(utilizingClass.getPackage().getName()
                + BUNDLE_PACKAGE_NAME);
    }

    /**
     * Konstruktor překladače zpráv pro výjimky.
     * 
     * @param locale
     *            balík s přeloženými zprávami výjimek, nesmí být null
     */
    public ExceptionMessageLocalizer(final ResourceBundle locale) {
        if (locale == null) {
            throw new NullPointerException();
        }

        this.locale = locale;
    }

    /**
     * Vrátí přeloženou zprávu pro výjimku.
     * 
     * @param key
     *            klíč pro hledaný řetězec, nesmí být null a musí pro něj
     *            existovat překlad
     * @param params
     *            objekty k substituci do lokalizované zprávy
     * @return řetězec pro daný klíč
     * @see java.util.logging.Logger#log(java.util.logging.Level, String,
     *      Object[]) Obdobně užitá metoda
     */
    public String getMessage(final String key, final Object... params) {
        final String rawLocalizedMessage = locale.getString(key);

        final String stringFormatFormattedMessage =
                Text.substituteBraceWildcards(rawLocalizedMessage);

        final String localizedMessage =
                String.format(stringFormatFormattedMessage, params);

        return localizedMessage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + locale.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
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
        final ExceptionMessageLocalizer other = (ExceptionMessageLocalizer) obj;
        if (!locale.equals(other.locale)) {
            return false;
        }
        return true;
    }
}
