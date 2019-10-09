package org.orbisgis.core.logger;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.orbisgis.core.CoreActivator;

public class Logger implements ILogger {

    private static ILog eclipseLog;

    private String id;

    static {
        CoreActivator instance = CoreActivator.getInstance();
        try {
            eclipseLog = instance == null ? null : instance.getLog();
        } catch (Throwable e) {
            eclipseLog = null;
        }
    }

    public Logger(Class clazz) {
        this.id = clazz.getName();
    }

    protected void log(int severity, String message){
        log(severity, message, null);
    }

    protected void log(int severity, String message, Throwable throwable){
        IStatus status;
        if(throwable != null){
            status = new Status(severity, id, message, throwable);
        }
        else {
            status = new Status(severity, id, message);
        }
        eclipseLog.log(status);
    }

    @Override
    public void trace(String message) {
        log(IStatus.OK, message);
    }

    @Override
    public void trace(String message, Throwable throwable) {
        log(IStatus.OK, message, throwable);
    }

    @Override
    public void debug(String message) {
        log(IStatus.CANCEL, message);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        log(IStatus.CANCEL, message, throwable);
    }

    @Override
    public void info(String message) {
        log(IStatus.INFO, message);
    }

    @Override
    public void info(String message, Throwable throwable) {
        log(IStatus.INFO, message, throwable);
    }

    @Override
    public void warn(String message) {
        log(IStatus.WARNING, message);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        log(IStatus.WARNING, message, throwable);
    }

    @Override
    public void error(String message) {
        log(IStatus.ERROR, message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        log(IStatus.ERROR, message, throwable);
    }
}
