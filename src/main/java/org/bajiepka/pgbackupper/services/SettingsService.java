package org.bajiepka.pgbackupper.services;

import org.bajiepka.pgbackupper.configuration.PathSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Valerii C.
 */

@Component
public class SettingsService {

    @Autowired
    PathSettings pathSettings;

    public Map<String, String> getSettingsMap() {
        Properties properties = pathSettings.getProperties();
        Map<String, String> map = new HashMap<String, String>();

        for (final String name : properties.stringPropertyNames()) {
            map.put(name, properties.getProperty(name));
        }
        return map;
    }
}
