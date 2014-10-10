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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources;

import java.util.ResourceBundle;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.library.utils.Text;

/**
 * <p>
 * Překladač zpráv.
 * </p>
 * <p>
 * Využívá zdrojový balík zadaný dědící třídou. Dovoluje přitom užít parametrů,
 * které nahradí v textu ze zdrojového balíku místa vyznačená pomocí složených
 * závorek uzavírajících číslici s pořadím parametru.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public class AbstractBundleLocalizer implements Localizer {

    /**
     * Kořenový balík (výchozí umístění zdrojových balíků).
     */
    protected static final String ROOT_PACKAGE =
            "cz.cuni.mff.ms.brodecva.botnicek.ide";

    private final ResourceBundle locale;

    /**
     * Konstruktor překladače zpráv pro výjimky.
     * 
     * @param locale
     *            balík s přeloženými zprávami
     */
    protected AbstractBundleLocalizer(final ResourceBundle locale) {
        Preconditions.checkNotNull(locale);

        this.locale = locale;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.Localizer#getMessage
     * (java.lang.String, java.lang.Object)
     */
    @Override
    public final String getMessage(final String key, final Object... params) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(params);

        final String rawLocalizedMessage = this.locale.getString(key);

        final String stringFormatFormattedMessage =
                Text.substituteBraceWildcards(rawLocalizedMessage);

        final String localizedMessage =
                String.format(stringFormatFormattedMessage, params);
        return localizedMessage;
    }
}
