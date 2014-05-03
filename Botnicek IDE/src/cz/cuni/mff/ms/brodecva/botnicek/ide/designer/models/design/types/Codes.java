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
package cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.design.types;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.common.base.Preconditions;

import cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLCategoryLoader;
import cz.cuni.mff.ms.brodecva.botnicek.library.loader.AIMLSourceParser;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class Codes {
    private static final class CodeImplementation implements Code {
        private final String text;
        
        public static CodeImplementation create(final String text) {
            return new CodeImplementation(text);
        }
        
        private CodeImplementation(final String text) {
            Preconditions.checkNotNull(text);
            Preconditions.checkArgument(!text.isEmpty());
            
            this.text = text;
        }

        /* (non-Javadoc)
         * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.aiml.Code#getText()
         */
        @Override
        public String getText() {
            return this.text;
        }
    }
    
    public static Code createEmpty() {
        return CodeImplementation.create(""); 
    }

    /**
     * @param text
     * @return
     */
    public static Code create(final String text) {
        return CodeImplementation.create(text);
    }
}
