package org.orbisgis.ui.editors.groovy;

import groovy.lang.MissingPropertyException;
import groovy.lang.Script;
import org.orbisgis.core.logger.Logger;

import java.util.List;

public abstract class GroovyScript extends Script {

    public static final String CLOSED_DATA_SOURCES = "closedDataSources";

    private static final Logger LOGGER = new Logger(GroovyScript.class);

    public void println(Object o){
        if(o != null) {
            LOGGER.info(o.toString());
        }
    }

    public void print(Object o){
        if(o != null) {
            LOGGER.info(o.toString());
        }
    }

    public void propertyMissing(String name) {
        Object closedDataSources = getBinding().getProperty(CLOSED_DATA_SOURCES);
        if(closedDataSources instanceof List){
            if(((List)closedDataSources).contains(name)) {
                LOGGER.error("The database '" + name + "' is closed.");
                throw new MissingPropertyException("The database '" + name + "' is closed.");
            }
        }
        LOGGER.error("Property '"+name+"' not found.");
        throw new MissingPropertyException("Property '"+name+"' not found.");
    }
}
