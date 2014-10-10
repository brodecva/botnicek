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
package cz.cuni.mff.ms.brodecva.botnicek.ide.project.views;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.google.common.io.Files;

import cz.cuni.mff.ms.brodecva.botnicek.ide.utils.resources.UiLocalizer;

/**
 * Filtr projektových souborů. Při aplikaci dovolí v souborovém dialogu zobrazit
 * pouze soubory s odpovídající koncovkou.
 * 
 * @author Václav Brodec
 * @version 1.0
 */
final class ProjectFileFilter extends FileFilter {

    /**
     * Koncovka projektových souborů botníčku.
     */
    public final static String PROJECT_FILE_EXTENSION = "btk";

    /**
     * Oddělovač koncovky souboru.
     */
    public static final String EXTENSION_SEPARATOR = ".";

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    @Override
    public boolean accept(final File f) {
        if (f.isDirectory()) {
            return true;
        }

        final String extension = Files.getFileExtension(f.getName());
        if (extension.isEmpty()) {
            return false;
        }

        return extension.equals(PROJECT_FILE_EXTENSION);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    @Override
    public String getDescription() {
        return UiLocalizer.print("PROJECT_FILE_FILTER_DESCRIPTION");
    }
}