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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.validator;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.events.CheckEvent;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.builder.Builder;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;

/**
 * Rozhraní pro provedení kontroly a vyslání výsledku.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @param <T> validovaný typ
 */
public interface Validator<T> {
    /**
     * Odstraní výsledky pro daný předmět.
     * 
     * @param subject
     *            předmět.
     */
    void clear(Object subject);

    /**
     * Zkontroluje textový řetězec, zda-li odpovídá požadavkům, a vytvoří po
     * provedení kontroly příslušnou událost {@link CheckEvent}.
     * 
     * @param source
     *            zdroj řetězce
     * @param subject
     *            identifikátor opakovaných pokusů o kontrolu
     * @param content
     *            vstupní řetězec
     */
    void validate(Source source, Object subject, String content);

    /**
     * Poskytne stavitele typované hodnoty. Pomáhá odstranit nutnost duplikace validace při běžné kontrole a sestavení.
     * 
     * @param value textový hodnota
     * @return stavitel typu
     */
    Builder<T> provideBuilder(String value);
}
