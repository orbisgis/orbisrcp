package org.orbisgis.ui.editors.groovy;

import java.net.URL
import java.util.prefs.Preferences
import groovy.io.FileType

def getClassPath(GroovyShell shell) {
	println "shell.getClass() : " + shell.getClass()
	
	File myJar = new File("/home/adrien/eclipse-workspace/demat/src/main/java/org/orbisgis/demat/target/demat-0.0.7-SNAPSHOT.jar")
	shell.getClassLoader().addURL(myJar.toURI().toURL())
	println shell.getClassLoader().getURLs()
	
	/*
	myJar?.each { file ->
                shell.getClassLoader().addURL(file.toURL())
            }
	println shell.getClassLoader().getURLs()
	*/

	//Preferences.userNodeForPackage(GroovyJob).put('currentClasspathJarDir', "/home/adrien/eclipse-workspace/demat/src/main/java/org/orbisgis/demat/target/demat-0.0.7-SNAPSHOT.jar")

	File classFile = new File("/home/adrien/.m2/repository/org/orbisgis/demat/0.0.7-SNAPSHOT/demat-0.0.7-SNAPSHOT/org/orbisgis/demat/Plot.class")
	shell.getClassLoader().addURL(classFile.toURL())
/*
	myJar.eachFileRecurse FileType.FILES, { file ->
		shell.getClassLoader().addURL(file.toURL())
	}*/

}
