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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;

import cz.cuni.mff.ms.brodecva.botnicek.ide.aiml.types.Code;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.DefaultCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.checker.Source;
import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.concepts.Intended;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.BotConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.api.LanguageConfiguration;
import cz.cuni.mff.ms.brodecva.botnicek.library.language.AIMLLanguage;
import cz.cuni.mff.ms.brodecva.botnicek.library.language.Language;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLSourceParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.SourceParser;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.SourceParserException;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.AIML;
import cz.cuni.mff.ms.brodecva.botnicek.library.platform.XML;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.AIMLBot;
import cz.cuni.mff.ms.brodecva.botnicek.library.responder.Bot;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.MatchingStructure;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.WordTree;
import cz.cuni.mff.ms.brodecva.botnicek.library.storage.map.FrugalMapperFactory;

/**
 * Výchozí implementace validátoru kódu šablony jazyka AIML užívá části
 * interpretu, které mají na starosti načtení kódu.
 * 
 * @author Václav Brodec
 * @version 1.0
 * @see Code
 */
public final class DefaultCodeChecker implements CodeChecker, Source {
    private static final String MESSAGE_PARTS_SEPARATOR = ";";
    private static final String CHECK_DOCUMENT_TEMPLATE_PART_ONE =
            "<?xml version=\"1.0\"?>"
                    + "<%1$saiml %2$sversion=\"1.0.1\" %3$sschemaLocation=\"%4$s\">"
                    + "<%1$scategory><%1$spattern>CHECK</%1$spattern><%1$sthat>CHECK</%1$sthat>\n";
    private static final String TEMPLATE = "<%1$stemplate>";
    private static final String CHECK_DOCUMENT_TEMPLATE_PART_TWO =
            "%5$s</%1$stemplate></%1$scategory></%1$saiml>";

    /**
     * Vytvoří validátor.
     * 
     * @param botSettings
     *            nastavení bota
     * @param languageSettings
     *            nastavení jazyka
     * @param namespacesToPrefixes
     *            prefix prostorů jmen
     * @return validátor
     */
    public static CodeChecker create(final BotConfiguration botSettings,
            final LanguageConfiguration languageSettings,
            final Map<URI, String> namespacesToPrefixes) {
        return new DefaultCodeChecker(botSettings, languageSettings,
                namespacesToPrefixes);
    }

    /**
     * Nejlepší snaha o odstranění implementačních podrobnost a zachování pouze
     * samotného těla zprávy.
     */
    private static String cutMessage(final String message) {
        if (message == Intended.nullReference()) {
            return "";
        }

        return Iterables.getLast(
                Splitter.on(MESSAGE_PARTS_SEPARATOR).splitToList(message), "")
                .trim();
    }

    private final SourceParser parser = AIMLSourceParser.create();
    private final BotConfiguration botSettings;

    private final LanguageConfiguration languageSettings;

    private final Map<URI, String> namespacesToPrefixes;

    private DefaultCodeChecker(final BotConfiguration botSettings,
            final LanguageConfiguration languageSettings,
            final Map<URI, String> namespacesToPrefixes) {
        Preconditions.checkNotNull(botSettings);
        Preconditions.checkNotNull(languageSettings);
        Preconditions.checkNotNull(namespacesToPrefixes.containsKey(URI
                .create(AIML.NAMESPACE_URI.getValue())));
        Preconditions.checkNotNull(namespacesToPrefixes.containsKey(URI
                .create(XML.SCHEMA_NAMESPACE_URI.getValue())));
        Preconditions.checkNotNull(namespacesToPrefixes);
        HashBiMap.create(namespacesToPrefixes);
        
        this.botSettings = botSettings;
        this.languageSettings = languageSettings;
        this.namespacesToPrefixes = namespacesToPrefixes;
    }

    private int adjustColumnNumber(final int original,
            final int originalLineNumber, final String template) {
        if (originalLineNumber > 2) {
            return original;
        } else if (originalLineNumber == 2) {
            return original - template.length();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private int adjustLineNumber(final int original) {
        return original - 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CodeChecker#check
     * (java.lang.String)
     */
    @Override
    public CheckResult check(final Source source, final Object subject,
            final String content) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(subject);
        Preconditions.checkNotNull(content);

        final String aimlPrefix =
                this.namespacesToPrefixes.get(URI.create(AIML.NAMESPACE_URI
                        .getValue()));
        final String aimlPrefixSeparated =
                aimlPrefix + (aimlPrefix.isEmpty() ? "" : ":");

        final String aimlSchemaPrefix =
                this.namespacesToPrefixes.get(URI
                        .create(XML.SCHEMA_NAMESPACE_URI.getValue()));
        final String aimlSchemaPrefixSeparated =
                aimlSchemaPrefix + (aimlSchemaPrefix.isEmpty() ? "" : ":");

        final StringBuilder prefixDefinitions = new StringBuilder();
        for (final Entry<URI, String> namespacePrefix : this.namespacesToPrefixes
                .entrySet()) {
            final String prefix = namespacePrefix.getValue();
            final String namespace = namespacePrefix.getKey().toString();

            prefixDefinitions.append("xmlns" + (prefix.isEmpty() ? "" : ":")
                    + prefix + "=\"" + namespace + "\" ");
        }

        final String testedDocumentPartOne =
                String.format(CHECK_DOCUMENT_TEMPLATE_PART_ONE,
                        aimlPrefixSeparated, prefixDefinitions.toString(),
                        aimlSchemaPrefixSeparated,
                        AIML.NAMESPACE_URI.getValue() + " "
                                + AIML.BACKUP_SCHEMA_LOCATION.getValue(),
                        content);
        final String template =
                String.format(TEMPLATE, aimlPrefixSeparated,
                        prefixDefinitions.toString(),
                        aimlSchemaPrefixSeparated,
                        AIML.BACKUP_SCHEMA_LOCATION.getValue(), content);
        final String testedDocumentPartTwo =
                String.format(CHECK_DOCUMENT_TEMPLATE_PART_TWO,
                        aimlPrefixSeparated, prefixDefinitions.toString(),
                        aimlSchemaPrefixSeparated,
                        AIML.BACKUP_SCHEMA_LOCATION.getValue(), content);
        final String combined =
                testedDocumentPartOne + template + testedDocumentPartTwo;

        final Language language =
                new AIMLLanguage(this.languageSettings.getName(),
                        this.languageSettings.getSentenceDelim(),
                        this.languageSettings.getGenderSubs(),
                        this.languageSettings.getPersonSubs(),
                        this.languageSettings.getPerson2Subs(),
                        this.languageSettings.getAbbreviationsSubs(),
                        this.languageSettings.getSpellingSubs(),
                        this.languageSettings.getEmoticonsSubs(),
                        this.languageSettings.getInnerPunctuationSubs());
        final Bot bot =
                new AIMLBot(this.botSettings.getName(), language,
                        this.botSettings.getFilesLocation(),
                        this.botSettings.getGossipPath(),
                        this.botSettings.getPredicates(),
                        this.botSettings.getBeforeLoadingOrder(),
                        this.botSettings.getAfterLoadingOrder());

        try {
            final MatchingStructure filledStructure =
                    new WordTree(new FrugalMapperFactory());
            this.parser.parse(
                    new ByteArrayInputStream(combined
                            .getBytes(StandardCharsets.UTF_8)), "Test",
                    filledStructure, bot);
        } catch (final SourceParserException e) {
            final int lineNumber = e.getLineNumber();
            final int columnNumber = e.getColumnNumber();
            final String message = e.getMessage();

            return DefaultCheckResult.fail(adjustLineNumber(lineNumber),
                    adjustColumnNumber(columnNumber, lineNumber, template),
                    cutMessage(message), source, subject);
        }

        return DefaultCheckResult.succeed(source, subject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Checker#check
     * (java.lang.String)
     */
    @Override
    public CheckResult check(final String content) {
        return check(this, new Object(), content);
    }

    /**
     * Nastavení robota užitá pro validaci.
     * 
     * @return nastavení robota
     */
    @Override
    public BotConfiguration getBotSettings() {
        return this.botSettings;
    }

    /**
     * Nastavení jazyka užitá pro validaci.
     * 
     * @return nastavení jazyka
     */
    @Override
    public LanguageConfiguration getLanguageSettings() {
        return this.languageSettings;
    }

    /**
     * Vrátí vzájemně jednoznačné zobrazení jmenných prostorů a jejich výchozích prefixů.
     * 
     * @return vzájemně jednoznačné zobrazení jmenných prostorů a jejich výchozích prefixů
     */
    @Override
    public Map<URI, String> getNamespacesToPrefixes() {
        return this.namespacesToPrefixes;
    }
}
