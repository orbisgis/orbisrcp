package org.orbisgis.ui.editors.groovy.logger;

import groovy.lang.GString;
import org.eclipse.core.runtime.IStatus;
import org.orbisgis.core.logger.Logger;

public class GroovyLogger extends Logger implements IGroovyLogger {

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
}
