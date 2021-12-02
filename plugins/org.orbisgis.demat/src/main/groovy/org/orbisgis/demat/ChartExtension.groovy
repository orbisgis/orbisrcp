/*
 * Demat is a library that allows to load an internal browser for your groovy graphs in the Eclipse RCP. 
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
package org.orbisgis.demat;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.orbisgis.demat.Chart;
import groovy.transform.Field
import groovy.lang.GroovyShell;
import org.orbisgis.ui.editors.groovy.logger.GroovyLogger;
import groovy.lang.Binding;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import groovy.transform.ThreadInterrupt;
import groovy.util.GroovyScriptEngine;

	//@Field static final Logger LOGGER = new Logger(ChartExtension.class);

	static void open(Chart chart) {
		
		File outputFile = null;
        try {
            outputFile = File.createTempFile("demat", ".html", chart.checkShowDir());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		try {
			IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport()
			IWebBrowser browser = null;
			try {
				browser = support.createBrowser("someId");
			} catch (Exception e) {
				//LOGGER.error("Unable to create a new browser.", e);
			}
			try {
				browser.openURL(new URL(outputFile.getAbsolutePath()));
			} catch (Exception e) {
				//LOGGER.error("Unable to open a new browser.", e);
			} catch (MalformedURLException e) {
				//LOGGER.error("Unable to open a new browser.", e);
			}
		} catch (Exception e) {
			//LOGGER.error("Unable to get a browser support.", e);
		}
	
	}
	
//}
