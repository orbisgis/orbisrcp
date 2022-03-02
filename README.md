# OrbisRCP

OrbisRCP is the next generation of OrbisGIS components. The components are RCP eclipse based and must be used inside [DBeaver](https://dbeaver.io/).

[DBeaver](https://dbeaver.io/) is a free and opensource multi-platform database tool to query, explore and manage data. Since the 6.2.2 version,the OrbisGIS team collaborates with the DBeaver team to integrate its own components : database, scripting console, map viewer...

Please look at the readme file available in the directory of the plugins you want to install.

If you have any questions, comments or troubles with the plugins, feel free to push an issue. We will do our best to address them.

## Developing

In order to compile OrbisRCP, DBeaver should be build first.

```bash
export JAVA_HOME=/path/to/the/jdk/11
git clone https://github.com/dbeaver/dbeaver
cd dbeaver
mvn clean install -Dmaven.test.skip=true
#Wait
cd ..
git clone https://github.com/orbisgis/orbisrcp
cd orbisrcp
mvn clean install
```

## Note

The repository to install the OrbisRCP plugins is  : https://raw.githubusercontent.com/orbisgis/orbisrcp/p2-repository/ 

It contains  the current snaphot versions (deployed after each PR).

