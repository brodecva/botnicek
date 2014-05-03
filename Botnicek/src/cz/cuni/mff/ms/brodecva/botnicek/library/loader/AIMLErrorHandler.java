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
package cz.cuni.mff.ms.brodecva.botnicek.library.loader;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import cz.cuni.mff.ms.brodecva.botnicek.library.logging.BotnicekLogger;
import cz.cuni.mff.ms.brodecva.botnicek.library.utils.ExceptionMessageLocalizer;

/**
 * Handler chybových stavů parsování striktních AIML souborů.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
public final class AIMLErrorHandler implements ErrorHandler, Serializable {
    /**
     * UID serializované verze.
     */
    private static final long serialVersionUID = -9116085350679084480L;

    /**
     * Lokalizátor hlášek výjimek.
     */
    private static final ExceptionMessageLocalizer MESSAGE_LOCALIZER =
            ExceptionMessageLocalizer.getLocalizer();

    /**
     * Logger.
     */
    private static final Logger LOGGER = BotnicekLogger
            .getLogger(AIMLErrorHandler.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    @Override
    public void error(final SAXParseException spe) throws SAXException {
        final String message =
                MESSAGE_LOCALIZER.getMessage("loader.AIMLParserError") + ": "
                        + getParseExceptionInfo(spe);

        throw new SAXException(message, spe);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    @Override
    public void fatalError(final SAXParseException spe) throws SAXException {
        final String message =
                MESSAGE_LOCALIZER.getMessage("loader.AIMLParserFatalError") + ": "
                        + getParseExceptionInfo(spe);

        throw new SAXException(message, spe);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    @Override
    public void warning(final SAXParseException spe) throws SAXException {
        final String message = ": " + getParseExceptionInfo(spe);

        LOGGER.log(Level.WARNING, "loader.AIMLParserWarning", message);
    }

    /**
     * Informace o původu výjimky.
     * 
     * @param spe
     *            výjimka parseru
     * @return podrobnosti o chybě (URI, číslo řádku a popis)
     */
    private String getParseExceptionInfo(final SAXParseException spe) {
        String systemId = spe.getSystemId();
        if (systemId == null) {
            systemId = "null";
        }

        final String info =
                MESSAGE_LOCALIZER.getMessage("loader.AIMLParserURI") + "=" + systemId
                        + " " + MESSAGE_LOCALIZER.getMessage("loader.AIMLParserLine")
                        + "=" + spe.getLineNumber() + ": " + spe.getMessage();
        return info;
    }
}
