package org.orbisgis.ui.editors.groovy.completion;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DemoJson {

    public static void main(String[] args) {

        File grabFolder = new File(System.getProperty("user.home") + File.separator + "/.groovy");

        if(grabFolder.exists()&& grabFolder.isDirectory()){
            // find files matched `png` file extension from folder C:\\test
            try (Stream<Path> walk = Files.walk(grabFolder.toPath())) {
                JsonArray  jars = new JsonArray();
                 walk
                        .filter(p -> !Files.isDirectory(p))   // not a directory
                        .map(p -> p.toString().toLowerCase()) // convert path to string
                        .filter(f -> f.endsWith("jar"))       // check end with
                        .forEach(it -> jars.add(it));        // collect all matched to a List

                JsonObject classpath = new JsonObject();
                classpath.add("classpath", jars);

                JsonObject groovy = new JsonObject();
                groovy.add("groovy", classpath);

                JsonObject settings = new JsonObject();
                settings.add("settings", groovy);

                System.out.println(settings.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Check if the .groovy folder exists

        //Load the jars installed by dbeaver
        //Contact dbeaver team to explain where the jars are ?


    }
}
