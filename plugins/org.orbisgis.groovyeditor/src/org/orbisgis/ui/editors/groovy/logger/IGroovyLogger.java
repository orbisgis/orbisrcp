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
