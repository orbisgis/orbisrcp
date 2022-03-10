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
 *
 */
package org.orbisgis.ui.editors.groovy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.orbisgis.core.logger.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.orbisgis.ui.editors.groovy.ClassPathHandler.*;

class ClassPathHandlerTest {

    private static final Logger LOGGER = new Logger(ClassPathHandlerTest.class);
    private static java.net.URL notAjar = null;
    private static java.net.URL aJar = null;

    static {
        try {
            notAjar = new File("///d:" + "/notAJar").toURI().toURL();
            aJar = new File("/aJar" + "!/").toURI().toURL();
        } catch (MalformedURLException e) {
            LOGGER.error("MalformedURLException", e);
        }
    }

    @AfterEach
    void afterEach() throws MalformedURLException {
        for(int i=0; i< getUrls().size(); i++){
            deleteOneURL(i);
        }
    }

    @Test
    void addPathToUrlListIfDirectoryTest(){
        List<URL> urls = new ArrayList<>();
        urls.add(notAjar);

        addPathToUrlList("/notAJar", false);

        assertEquals(urls, getUrls());
    }

    @Test
    void addPathToUrlListIfJarTest(){
        List<URL> urls = new ArrayList<>();
        urls.add(aJar);

        addPathToUrlList("/aJar", true);

        assertEquals(urls, getUrls());
    }

    @Test
    void getUrlsInArrayTest(){
        List<URL> urls = new ArrayList<>();
        urls.add(notAjar);
        addPathToUrlList("/notAJar", false);

        URL[] urlList = urls.toArray( new URL[0] );

        assertEquals(urlList[0], getUrlsInArray()[0]);
    }

    @Test
    void getUrlsInArrayIfNullTest() {
        assertEquals(0, getUrlsInArray().length);
    }

    @Test
    void deleteOneURLTest() throws MalformedURLException {
        List<URL> urls = new ArrayList<>();
        urls.add(notAjar);
        urls.add(aJar);

        addPathToUrlList("/notAJar", false);
        addPathToUrlList("/aJar", true);
        deleteOneURL(1);

        assertEquals(1, getUrlsInArray().length);
        assertEquals(urls.get(0), getUrlsInArray()[0]);
    }

}