# OrbisRCP

OrbisRCP is the next generation of OrbisGIS components. The components are RCP eclipse based and must be used inside [DBeaver](https://dbeaver.io/).

[DBeaver](https://dbeaver.io/) is a free and opensource multi-platform database tool to query, explore and manage data. Since the 6.2.2 version,the OrbisGIS team collaborates with the DBeaver team to integrate its own components : database, scripting console, map viewer...

Please look at the readme file available in the directory of the plugins you want to install.

If you have any questions, comments or troubles with the plugins, feel free to push an issue. We will do our best to address them.


## Plugins installation

The repository to install the OrbisRCP plugins is  : https://orbisgis.github.io/orbisrcp/
It contains  the current snaphot versions (deployed after each PR).

In DBeaver :
 - Go to menu `help/Install New Software...`.
 - Add the repository by clicking on `Add...`
   - Put the name you want (like `OrbisRCP`) 
   - Put the location `https://orbisgis.github.io/orbisrcp/`
   - Click on `Add`.
  - Once back on the `Available Software` window, verify the newly add repository is selected in `Work with:`.
    - Check then the box at the left of `OrbisGIS` software. 
    - Click then on `Next`.
  - On the next window click on `Finish`.
  - A `Security Warning` will be show, click on `Install Anyway` 
  - On the next popup, click on `Restart Now`

  - TaDaa, you have the plusing and tools installed.


## Developing

In order to compile OrbisRCP, [DBeaver](https://github.com/dbeaver/dbeaver) and [Groovy language server](https://github.com/GroovyLanguageServer/groovy-language-server) should be build first.

```bash
export JAVA_HOME=/path/to/the/jdk/11
#DBeaver build
git clone https://github.com/dbeaver/dbeaver
cd dbeaver
mvn clean install -Dmaven.test.skip=true
#Wait
cd ..

#Groovy language server build
git clone https://github.com/GroovyLanguageServer/groovy-language-server
cd groovy-language-server
./gradlew build
cd ..

git clone https://github.com/orbisgis/orbisrcp
mkdir orbisrcp/plugins/dependencies/libs
cp groovy-language-server/build/libs/groovy-language-server-all.jar orbisrcp/plugins/dependencies/libs/groovy-language-server.jar
cd orbisrcp
mvn clean install
```

## Debugging

To do remote debugging, you have to launch DBeaver with the following command : 
```bash
dbeaver -vmargs "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
```
and your debugger with VM args:
```bash
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
```

