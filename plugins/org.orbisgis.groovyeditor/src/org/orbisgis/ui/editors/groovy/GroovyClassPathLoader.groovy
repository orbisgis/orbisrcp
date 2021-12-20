package org.orbisgis.ui.editors.groovy;

import java.net.URL

//class Console {

def getClassPath(GroovyShell shell) {
	println "Hello, world!"
	File sourceFile = new File("/home/adrien/eclipse-workspace/demat/src/main/java/org/orbisgis/demat/Plot.java")
	println sourceFile
		
	println shell.getClassLoader()
	
	println "shell.getClass() : " + shell.getClass()
	
	/*
	shell.getClassLoader().getURLs().each {url->
     println "- ${url.toString()}"
  	}
  	*/
  	
  	println shell.getClassLoader().getURLs()
  	shell.getClassLoader().addURL(sourceFile.toURI().toURL())
  	println shell.getClassLoader().getURLs()
	
	File myJar = new File("/home/adrien/eclipse-workspace/demat/src/main/java/org/orbisgis/demat/target/demat-0.0.7-SNAPSHOT.jar")
	shell.getClassLoader().addURL(myJar.toURI().toURL())
	println shell.getClassLoader().getURLs()
	
	/*
	myJar?.each { file ->
                shell.getClassLoader().addURL(file.toURL())
            }
	println shell.getClassLoader().getURLs()
	*/
	/*
	ClassLoader cl = shell.classLoader
	while (cl instanceof URLClassLoader) {
		cl.getURLs().each { url -> urls << url }
		cl = cl.parent
	}*/
	
	def myclass = getClass();
	println myclass
	println myclass.classLoader.rootLoader
	
	//shell.getClassLoader().rootLoader.addURL(myJar.toURI().toURL())
	//this.class.classLoader.rootLoader.addURL(new URL("file:///home/adrien/eclipse-workspace/demat/"))
	//println this.class.classLoader.rootLoader
	
	//this.class.classLoader.addUrl(url)
	
	//} 
	
}
