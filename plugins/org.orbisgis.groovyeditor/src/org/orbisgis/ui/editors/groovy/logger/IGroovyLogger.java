/*
 * Groovy Editor (GE) is a library that brings a groovy console to the Eclipse RCP. 
 * GE is developed by CNRS http://www.cnrs.fr/.
 *
 * GE is part of the OrbisGIS project. GE is free software;
 * you can redistribute it and/or modify it under the terms of the GNU Lesser 
 * General Public License as published by the Free Software Foundation;
 * version 3.0 of the License.
 *
 * GE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details http://www.gnu.org/licenses.
 *
 *
 *For more information, please consult: http://www.orbisgis.org
 *or contact directly: info_at_orbisgis.org
 */
package org.orbisgis.ui.editors.groovy.logger;

import groovy.lang.GString;
import org.orbisgis.core.logger.ILogger;

public interface IGroovyLogger extends ILogger {

    void trace(GString message);
    void trace(GString message, Throwable throwable);

    void debug(GString message);
    void debug(GString message, Throwable throwable);

    void info(GString message);
    void info(GString message, Throwable throwable);

    void warn(GString message);
    void warn(GString message, Throwable throwable);

    void error(GString message);
    void error(GString message, Throwable throwable);

    void trace(Object message);
    void trace(Object message, Throwable throwable);

    void debug(Object message);
    void debug(Object message, Throwable throwable);

    void info(Object message);
    void info(Object message, Throwable throwable);

    void warn(Object message);
    void warn(Object message, Throwable throwable);

    void error(Object message);
    void error(Object message, Throwable throwable);
}
