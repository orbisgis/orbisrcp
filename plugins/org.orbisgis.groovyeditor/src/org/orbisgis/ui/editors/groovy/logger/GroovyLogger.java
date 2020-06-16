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
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.eclipse.core.runtime.IStatus;
import org.orbisgis.core.logger.Logger;

public class GroovyLogger extends Logger implements IGroovyLogger, GroovyObject {

    private MetaClass metaClass = InvokerHelper.getMetaClass(this.getClass());

    public GroovyLogger(Class clazz) {
        super(clazz);
    }

    private void log(int severity, GString message){
        log(severity, message, null);
    }

    private void log(int severity, GString message, Throwable throwable){
        log(severity, message.toString(), throwable);
    }

    @Override
    public void trace(GString message) {
        log(IStatus.OK, message);
    }

    @Override
    public void trace(GString message, Throwable throwable) {
        log(IStatus.OK, message, throwable);
    }

    @Override
    public void debug(GString message) {
        log(IStatus.CANCEL, message);
    }

    @Override
    public void debug(GString message, Throwable throwable) {
        log(IStatus.CANCEL, message, throwable);
    }

    @Override
    public void info(GString message) {
        log(IStatus.INFO, message);
    }

    @Override
    public void info(GString message, Throwable throwable) {
        log(IStatus.INFO, message, throwable);
    }

    @Override
    public void warn(GString message) {
        log(IStatus.WARNING, message);
    }

    @Override
    public void warn(GString message, Throwable throwable) {
        log(IStatus.WARNING, message, throwable);
    }

    @Override
    public void error(GString message) {
        log(IStatus.ERROR, message);
    }

    @Override
    public void error(GString message, Throwable throwable) {
        log(IStatus.ERROR, message, throwable);
    }


    @Override
    public void trace(Object obj) {
        log(IStatus.OK, obj != null ? obj.toString() : "null");
    }

    @Override
    public void trace(Object obj, Throwable throwable) {
        log(IStatus.OK, obj != null ? obj.toString() : "null", throwable);
    }

    @Override
    public void debug(Object obj) {
        log(IStatus.CANCEL, obj != null ? obj.toString() : "null");
    }

    @Override
    public void debug(Object obj, Throwable throwable) {
        log(IStatus.CANCEL, obj != null ? obj.toString() : "null", throwable);
    }

    @Override
    public void info(Object obj) {
        log(IStatus.INFO, obj != null ? obj.toString() : "null");
    }

    @Override
    public void info(Object obj, Throwable throwable) {
        log(IStatus.INFO, obj != null ? obj.toString() : "null", throwable);
    }

    @Override
    public void warn(Object obj) {
        log(IStatus.WARNING, obj != null ? obj.toString() : "null");
    }

    @Override
    public void warn(Object obj, Throwable throwable) {
        log(IStatus.WARNING, obj != null ? obj.toString() : "null", throwable);
    }

    @Override
    public void error(Object obj) {
        log(IStatus.ERROR, obj != null ? obj.toString() : "null");
    }

    @Override
    public void error(Object obj, Throwable throwable) {
        log(IStatus.ERROR, obj != null ? obj.toString() : "null", throwable);
    }

    @Override
    public MetaClass getMetaClass() {
        return metaClass;
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass != null ? metaClass : InvokerHelper.getMetaClass(this.getClass());
}
