package org.bajiepka.pgbackupper.services;

import java.io.*;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * @author Valerii C.
 */

public class AppFileConfiguration {

    private Properties properties = new Properties();
    private final String propertiesFilename = "src/main/resources/configuration.properties";

    public AppFileConfiguration() {
        initializeConfigFile();
        readDefaultProperties();
    }

    public Properties getProperties() {
        return properties;
    }

    private void readDefaultProperties() {

        try {
            properties.load(new InputStreamReader(new FileInputStream(new File(propertiesFilename))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        Set's the default properties if the file doesn't exist or is empty,
        or if this method os invoked manually
     */
    private void setDefaultProperties() {
        initializeConfigFile();
    }

    private void initializeConfigFile() {

        File configurationFile = new File(propertiesFilename);

        if (!configurationFile.exists()) {
            try {
                configurationFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
        Set's the property value by it's key
     */
    public void setProperties(String property, String value) {
        properties.setProperty(property, value);
    }

    /*
        Set's the values of multiple properties, passed by a Map
     */
    public void setProperties(Map<String, String> props) {
        props.forEach((key, value) -> properties.setProperty(key, value));
        saveProperties();
    }

    /*
        Property getter, that returns property from the instance of configuration.properties
        file by it's String key
     */
    public String getProperty(String property) {
        return properties.getProperty(property);
    }

    /*
        Saves current properties, located at local variable Properties properties
        to configuration.properties file from resources folder
     */
    private void saveProperties() {
        try (FileOutputStream output = new FileOutputStream(new File(propertiesFilename))) {
            properties.store(
                    output,
                    String.format("Pgbackupper properties\nModified %s", new Date()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
