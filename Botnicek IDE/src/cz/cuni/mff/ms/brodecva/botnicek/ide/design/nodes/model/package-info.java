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
/**
 * <p>Modely různých typů uzlů.</p>
 * <p>Typy uzlů se vyskytují v různých kombinacích, které vyplývají jak z jejich postavení v grafu, tak z uživatelovy volby.</p>
 * <p>Podle umístění v grafu se uzly dělí na:</p>
 * <ul>
 *     <li>izolované, které nejsou spojeny s žádným jiným uzlem</li>
 *     <li>vstupní, všechny související hrany jsou pouze odchozí</li>
 *     <li>výstupní, všechny související hrany jsou pouze vstupní</li>
 *     <li>vnitřní, které se nacházejí na orientované cestě mezi nějakým vstupní a výstupním uzlem.</li>
 * </ul>
 * <p>Podle míry interakce s uživatelem pro další postup výpočtu se dělí na:</p>
 * <ul>
 *     <li>procesní, u kterých se výpočet nezastaví, aby čekal na vstup od uživatele,</li>
 *     <li>zadávací, u kterých se zastaví a pak pokračuje ze stavu uchovaném na zásobníku dále.</li>
 * </ul>
 * <p>Podle míry determinismu přechodu na odchozí hrany během výpočtu se dělí na:</p>
 * <ul>
 *     <li>řadící, které seřadí odchozí hrany na zásobníku podle sestupné priority,</li>
 *     <li>náhodné, které na zásobník umístí hrany společně s instrukcí pro vážený náhodný výběr podle priority.</li>
 * </ul>
 * <p>Některé kombinace typů přirozeně chybí (např. nezáleží na tom, jakým způsobem jsou voleny odchozí hrany u výstupního uzlu sítě), některé existují pouze pro úplnost.</p>
 *
 * @version 1.0
 */
package cz.cuni.mff.ms.brodecva.botnicek.ide.design.nodes.model;

