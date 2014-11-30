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
package cz.cuni.mff.ms.brodecva.botnicek.ide.utils.events;

/**
 * <p>
 * Registr událostí. Poskytuje metody pro přidávání a odebírání posluchače
 * běžných i mapovaných událostí.
 * </p>
 * <p>
 * Mapované události jsou doručovány i těm posluchačům, které je mají
 * registrované bez klíče.
 * </p>
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public interface EventRegister {
    /**
     * Přidá posluchače běžné události.
     * 
     * @param type
     *            typ registrované události
     * @param listener
     *            posluchač
     */
    public <L> void addListener(Class<? extends Event<L>> type, L listener);

    /**
     * Přidá posluchače mapované události.
     * 
     * @param type
     *            typ registrované události
     * @param key
     *            klíč
     * @param listener
     *            posluchač
     */
    public <K, L> void addListener(Class<? extends MappedEvent<K, L>> type,
            K key, L listener);

    /**
     * Odebere všechny posluchače daného typu běžné události.
     * 
     * @param type
     *            typ události
     */
    public <K, L> void removeAllListeners(Class<? extends Event<L>> type);

    /**
     * Odebere všechny posluchače daného typu mapované události pod daným
     * klíčem.
     * 
     * @param type
     *            typ události
     * @param key
     *            klíč registrovaných posluchačů
     */
    public <K, L> void removeAllListeners(
            Class<? extends MappedEvent<K, L>> type, K key);

    /**
     * Odebere posluchače běžné události.
     * 
     * @param type
     *            typ registrované události
     * @param listener
     *            posluchač
     */
    public <L> void removeListener(Class<? extends Event<L>> type, L listener);

    /**
     * Odebere posluchače mapované události.
     * 
     * @param type
     *            typ registrované události
     * @param key
     *            klíč
     * @param listener
     *            posluchač
     */
    public <K, L> void removeListener(Class<? extends MappedEvent<K, L>> type,
            K key, L listener);
}
