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
package cz.cuni.mff.ms.brodecva.botnicek.ide.check.code.model.checker;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.DefaultCheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.Source;
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
 * Výchozí implementace validátoru kódu šablony jazyka AIML užívá části interpretu, které mají na starosti načtení kódu.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultCodeChecker implements CodeChecker {
    /**
     * 
     */
    private static final String MESSAGE_PARTS_SEPARATOR = ";";
    private static final String CHECK_DOCUMENT_TEMPLATE_PART_ONE = "<?xml version=\"1.0\"?>" +
            "<%1$saiml %2$s%1$sversion=\"1.0.1\" %3$sschemaLocation=\"%4$s\">"+ 
            "<%1$scategory><%1$spattern>CHECK</%1$spattern><%1$sthat>CHECK</%1$sthat>\n";
    private static final String TEMPLATE = "<%1$stemplate>";
    private static final String CHECK_DOCUMENT_TEMPLATE_PART_TWO = "%5$s</%1$stemplate></%1$scategory></%1$saiml>";
    
    private final SourceParser parser = AIMLSourceParser.create();
    private final BotConfiguration botSettings;
    private final LanguageConfiguration languageSettings;
    private final BiMap<URI, String> namespacesToPrefixes;
    
    /**
     * Vytvoří validátor.
     * 
     * @param botSettings nastavení bota
     * @param languageSettings nastavení jazyka
     * @param namespacesToPrefixes prefix prostorů jmen
     * @return validátor
     */
    public static DefaultCodeChecker create(final BotConfiguration botSettings, final LanguageConfiguration languageSettings, final Map<URI, String> namespacesToPrefixes) {
        return new DefaultCodeChecker(botSettings, languageSettings, namespacesToPrefixes);
    }
    
    private DefaultCodeChecker(final BotConfiguration botSettings, final LanguageConfiguration languageSettings, final Map<URI, String> namespacesToPrefixes) {
        Preconditions.checkNotNull(botSettings);
        Preconditions.checkNotNull(languageSettings);
        Preconditions.checkNotNull(namespacesToPrefixes.containsKey(URI.create(AIML.NAMESPACE_URI.getValue())));
        Preconditions.checkNotNull(namespacesToPrefixes.containsKey(URI.create(XML.SCHEMA_NAMESPACE_URI.getValue())));
        Preconditions.checkNotNull(namespacesToPrefixes);
        
        this.botSettings = botSettings;
        this.languageSettings = languageSettings;
        this.namespacesToPrefixes = HashBiMap.create(namespacesToPrefixes);
    }
    
    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.editor.checker.CodeChecker#check(java.lang.String)
     */
    @Override
    public CheckResult check(final Source source, Object subject, final String snippetContent) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(subject);
        Preconditions.checkNotNull(snippetContent);
        
        final String aimlPrefix = this.namespacesToPrefixes.get(URI.create(AIML.NAMESPACE_URI.getValue()));
        final String aimlPrefixSeparated = aimlPrefix + (aimlPrefix.isEmpty() ? "" : ":");
        
        final String aimlSchemaPrefix = this.namespacesToPrefixes.get(URI.create(XML.SCHEMA_NAMESPACE_URI.getValue()));
        final String aimlSchemaPrefixSeparated = aimlSchemaPrefix + (aimlSchemaPrefix.isEmpty() ? "" : ":");
        
        final StringBuilder prefixDefinitions = new StringBuilder();
        for (final Entry<URI, String> namespacePrefix : this.namespacesToPrefixes.entrySet()) {
            final String prefix = namespacePrefix.getValue();
            final String namespace = namespacePrefix.getKey().toString();
            
            prefixDefinitions.append("xmlns" + (prefix.isEmpty() ? "" : ":") + prefix + "=\"" + namespace + "\" ");
        }
                
        final String testedDocumentPartOne = String.format(CHECK_DOCUMENT_TEMPLATE_PART_ONE, aimlPrefixSeparated, prefixDefinitions.toString(), aimlSchemaPrefixSeparated, AIML.BACKUP_SCHEMA_LOCATION.getValue(), snippetContent);
        final String template = String.format(TEMPLATE, aimlPrefixSeparated, prefixDefinitions.toString(), aimlSchemaPrefixSeparated, AIML.BACKUP_SCHEMA_LOCATION.getValue(), snippetContent);
        final String testedDocumentPartTwo = String.format(CHECK_DOCUMENT_TEMPLATE_PART_TWO, aimlPrefixSeparated, prefixDefinitions.toString(), aimlSchemaPrefixSeparated, AIML.BACKUP_SCHEMA_LOCATION.getValue(), snippetContent);
        final String combined = testedDocumentPartOne + template + testedDocumentPartTwo;
        
        final Language language = new AIMLLanguage(languageSettings.getName(), languageSettings.getSentenceDelim(), languageSettings.getGenderSubs(), languageSettings.getPersonSubs(), languageSettings.getPerson2Subs(), languageSettings.getAbbreviationsSubs(), languageSettings.getSpellingSubs(), languageSettings.getEmoticonsSubs(), languageSettings.getInnerPunctuationSubs());
        final Bot bot = new AIMLBot(botSettings.getName(), language, botSettings.getFilesLocation(), botSettings.getGossipPath(), botSettings.getPredicates(), botSettings.getBeforeLoadingOrder(), botSettings.getAfterLoadingOrder());
        
        try {
            final MatchingStructure filledStructure =
                    new WordTree(new FrugalMapperFactory());
            parser.parse(new ByteArrayInputStream(combined.getBytes(Charsets.UTF_8)), "Test", filledStructure, bot);
        } catch (final SourceParserException e) {
            return DefaultCheckResult.fail(adjustLineNumber(e.getLineNumber()), adjustColumnNumber(e.getColumnNumber(), e.getLineNumber(), template), cutMessage(e.getMessage()), source, subject);
        }
        
        return DefaultCheckResult.succeed(source, subject);
    }

    /**
     * Nejlepší snaha o odstranění implementačních podrobnost a zachování pouze samotného těla zprávy.
     */
    private static String cutMessage(final String message) {
        if (message == Intended.nullReference()) {
            return "";
        }
        
        return Iterables.getLast(Splitter.on(MESSAGE_PARTS_SEPARATOR).splitToList(message), "").trim();
    }
    
    private int adjustLineNumber(final int original) {
        return original - 1;
    }

    private int adjustColumnNumber(final int original, final int originalLineNumber, final String template) {
        if (originalLineNumber > 2) {
            return original;
        } else if (originalLineNumber == 2){
            return original - template.length();
        } else {
            throw new IllegalArgumentException(); 
        }
    }
    
}
