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
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResult;
import cz.cuni.mff.ms.brodecva.botnicek.ide.check.common.model.CheckResultImplementation;
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
 * @author Václav Brodec
 * @version 1.0
 */
public final class DefaultCodeChecker implements CodeChecker {
    private static final String CHECK_DOCUMENT_TEMPLATE = "<?xml version=\"1.0\"?>" +
            "<%1$saiml %2$s%1$sversion=\"1.0.1\" %3$sschemaLocation=\"%4$s\">"+ 
            "<%1$scategory><%1$spattern>CHECK</%1$spattern><%1$stemplate>%5$s</%5$stemplate></%1$scategory></%1$saiml>";
    
    private final SourceParser parser = AIMLSourceParser.create();
    private final BotConfiguration botSettings;
    private final LanguageConfiguration languageSettings;
    private final BiMap<URI, String> namespacesToPrefixes;
    
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
    public CheckResult check(final Object source, final String snippetContent) {
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
                
        final String testedDocument = String.format(CHECK_DOCUMENT_TEMPLATE, aimlPrefixSeparated, prefixDefinitions.toString(), aimlSchemaPrefixSeparated, AIML.BACKUP_SCHEMA_LOCATION.getValue(), snippetContent);
        
        final Language language = new AIMLLanguage(languageSettings.getName(), languageSettings.getSentenceDelim(), languageSettings.getGenderSubs(), languageSettings.getPersonSubs(), languageSettings.getPerson2Subs(), languageSettings.getAbbreviationsSubs(), languageSettings.getSpellingSubs(), languageSettings.getEmoticonsSubs(), languageSettings.getInnerPunctuationSubs());
        final Bot bot = new AIMLBot(botSettings.getName(), language, botSettings.getFilesLocation(), botSettings.getGossipPath(), botSettings.getPredicates(), botSettings.getBeforeLoadingOrder(), botSettings.getAfterLoadingOrder());
        
        try {
            final MatchingStructure filledStructure =
                    new WordTree(new FrugalMapperFactory());
            parser.parse(new ByteArrayInputStream(testedDocument.getBytes(Charsets.UTF_8)), "Test", filledStructure, bot);
        } catch (final SourceParserException e) {
            return CheckResultImplementation.fail(source, e.getLineNumber(), e.getColumnNumber(), e.getMessage());
        }
        
        return CheckResultImplementation.succeed(source);
    }

}
