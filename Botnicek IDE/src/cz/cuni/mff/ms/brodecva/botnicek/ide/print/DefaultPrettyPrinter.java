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
package cz.cuni.mff.ms.brodecva.botnicek.ide.print;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.google.common.base.Preconditions;

/**
 * @author Václav Brodec
 * @version 1.0
 */
public class DefaultPrettyPrinter implements Printer {
    private static final String ENABLE_OPTION_VALUE = "yes";
    private static final String INDENT_NUMBER_OPTION_NAME = "indent-number";
    
    private final Transformer transformer;
    
    public static DefaultPrettyPrinter create(final int indent) throws PrintConfigurationException {
        Preconditions.checkArgument(indent >= 0);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(INDENT_NUMBER_OPTION_NAME, indent);
        
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (final TransformerConfigurationException e) {
            throw new PrintConfigurationException(e);
        }
        
        transformer.setOutputProperty(OutputKeys.INDENT, ENABLE_OPTION_VALUE);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, ENABLE_OPTION_VALUE);
        
        return new DefaultPrettyPrinter(transformer);
    }
    
    /**
     * 
     */
    private DefaultPrettyPrinter(final Transformer transformer) {
        Preconditions.checkNotNull(transformer);
        
        this.transformer = transformer;
    }

    /* (non-Javadoc)
     * @see cz.cuni.mff.ms.brodecva.botnicek.ide.designer.models.rendering.Printer#print(java.lang.String)
     */
    @Override
    public String print(final String input) throws PrintException {
        Preconditions.checkNotNull(input);
        
        return prettify(input);
    }
    
    private String prettify(final String input) throws PrintException {
        final Source xmlInput = new StreamSource(new StringReader(input));
        
        final StringWriter stringWriter = new StringWriter();
        final StreamResult xmlOutput = new StreamResult(stringWriter);
        
        try {
            this.transformer.transform(xmlInput, xmlOutput);
        } catch (final TransformerException e) {
            throw new PrintException(e);
        }
        
        return xmlOutput.getWriter().toString();
    }

}
