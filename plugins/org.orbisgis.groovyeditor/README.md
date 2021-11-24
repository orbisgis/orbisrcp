Groovy Editor (GE) is a UI console to run Groovy script in an RCP eclipse environment.
Groovy Editor (GE) can be used in the [DBeaver tool](https://dbeaver.io/).

[DBeaver](https://dbeaver.io/) is a free and opensource multi-platform database tool to query, explore and manage data. Since the 6.2.2 version,the OrbisGIS team collaborates with the DBeaver team to integrate its own components : database, scripting console, map viewer...


For example, DBeaver supports the [H2GIS](http://h2gis.org/)-[H2 database](https://www.h2database.com/) engine. 


To install the Groovy console

1. Go to the Main menu `Help` -> `Install New Software...`
2. In the `Work with` field, paste the following URL https://raw.githubusercontent.com/orbisgis/orbisrcp/p2-repository/ (which is an extension P2 repository URL) and press `Enter`
3. Check the `OrbisGIS` item  and check  the `Groovy Editor` plugin.  You can select the `H2GIS driver` too.
4. Click `Next` -> `Finish` and Restart DBeaver.

![](https://github.com/orbisgis/geoclimate/blob/master/docs/resources/images/for_users/dbeaver_install_plugins.png)





------

### Remarks

#### Accept the license

You may be asked to accept the open license ([LGPL v3](https://www.gnu.org/licenses/lgpl-3.0.en.html)) of the `Groovy Editor` and the `h2gis_driver` . If so, check on `I accept the terms of the licence agreement` and click on `Finish`.

![dbeaver_accept_licence](https://github.com/orbisgis/geoclimate/blob/master/docs/resources/images/for_users/dbeaver_accept_licence.png)



#### Security warning

You also may have a `Security Warning` since these two "plugins" are not officially developed by the DBeaver team. So you are asked to confirm the installation by clicking on `Install anyway`.

![dbeaver_install_anyway](https://github.com/orbisgis/geoclimate/blob/master/docs/resources/images/for_users/dbeaver_install_anyway.png)

------



Once DBeaver has restarted, select the main menu `Groovy Editor`, click on `Open editor`, then you will have a Groovy Console.

![](https://github.com/orbisgis/geoclimate/blob/master/docs/resources/images/for_users/dbeaver_groovy_console_text.png)



|                             Icon                             |              Action               |
| :----------------------------------------------------------: | :-------------------------------: |
| ![dbeaver_groovy_console_execute](https://github.com/orbisgis/geoclimate/blob/master/docs/resources/images/for_users/dbeaver_groovy_console_execute.png) | Execute the selected instructions |
| ![dbeaver_groovy_console_execute_all](https://github.com/orbisgis/geoclimate/blob/master/docs/resources/images/for_users/dbeaver_groovy_console_execute_all.png) |      Execute all the console      |
| ![dbeaver_groovy_console_erase](https://github.com/orbisgis/geoclimate/blob/master/docs/resources/images/for_users/dbeaver_groovy_console_erase.png) |         Clear the console         |





