package org.bajiepka.pgbackupper.configuration;

import org.bajiepka.pgbackupper.domain.CorporateBase;
import org.bajiepka.pgbackupper.services.AppFileConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

import static org.bajiepka.pgbackupper.configuration.PathSettings.OSType.Windows;

/**
 * @author Valerii C.
 */

@Component
@Scope(value = "singleton")
public class PathSettings {

    private PathVariables pathCheck;
    private CorporateCheck corpCheck;
    private OperatingSystemCheck osCheck;

    enum OSType {
        Windows, MacOS, Linux, Other
    }

    public PathSettings() {
        osCheck = new OperatingSystemCheck();
        pathCheck = new PathVariables(osCheck.getDetectedSystem());
        corpCheck = new CorporateCheck(this.osCheck, this.pathCheck);
    }

    /**
     *
     */
    protected class CorporateCheck {

        private List<CorporateBase> bases = new ArrayList<CorporateBase>();

        private String userHomeDirectoryPath;
        private String basesFileName = "ibases.v8i";
        private String basesFullPath;
        private String configFullPath;
        private File file;

        public CorporateCheck(OperatingSystemCheck osCheck, PathVariables pathCheck) {

            userHomeDirectoryPath = System.getProperty("user.home");

            switch (osCheck.getDetectedSystem()) {
                case Windows:

                    basesFullPath = String.format("%s\\%s\\%s",
                            userHomeDirectoryPath,
                            "AppData\\Roaming\\1C\\1CEStart",
                            basesFileName);

                    configFullPath = "C:\\ProgramData\\1C\\1CEStart\\1CEStart.cfg";

                    break;
            }

            if (!basesFullPath.isEmpty()) {
                file = new File(basesFullPath);

                parseConfigurationFile(basesFullPath);

            }
        }

        private void parseConfigurationFile(String filePath) {

            String line = null;
            boolean processed = false;
            StringBuilder sb = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

                while (true) {
                    line = reader.readLine();
                    if (line == null) break;

                    if (line.contains("[") || line.contains("]")) {
                        if (processed) {

                            Properties properties = new Properties();
                            properties.load(new StringReader(sb.toString().replace("\\", "\\\\")));

                            if (properties.size() != 6) {
                                bases.add(new CorporateBase.CorporateBaseBuilder()
                                        .setName(properties.getProperty("Name"))
                                        .setConnect(properties.getProperty("Connect"))
                                        .setId(properties.getProperty("ID"))
                                        .setOrder(properties.getProperty("OrderInList"))
                                        .setFolder(properties.getProperty("Folder"))
                                        .setOrderInTree(properties.getProperty("OrderInTree"))
                                        .setExternal(properties.getProperty("External"))
                                        .setSpeed(properties.getProperty("ClientConnectionSpeed"))
                                        .setApp(properties.getProperty("App"))
                                        .setWa(properties.getProperty("WA"))
                                        .setversion(properties.getProperty("Version"))
                                        .build());
                            }

                            sb.setLength(0);
                        }
                        sb.append(String.format("Name=%s\n", line.substring(line.indexOf('[') + 1, line.indexOf(']'))));
                        processed = true;
                    } else {
                        sb.append(line + "\n");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("2");

        }

        public void refresh() {

        }

        public List<CorporateBase> getBases() {
            return bases;
        }

    }

    public Properties getProperties() {
        return pathCheck.getProperties();
    }

    /**
     *
     */
    protected class OperatingSystemCheck {

        private OSType detectedSystem;

        public OperatingSystemCheck() {
            if (detectedSystem == null) {
                String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
                if (OS.contains("windows")) {
                    detectedSystem = Windows;
                } else if (OS.contains("nux")) {
                    detectedSystem = OSType.Linux;
                } else if (OS.contains("mac") || (OS.contains("darwin"))) {
                    detectedSystem = OSType.MacOS;
                } else {
                    detectedSystem = OSType.Other;
                }
            }
        }

        public OSType getDetectedSystem() {
            return detectedSystem;
        }

    }

    /**
     *
     */
    protected class PathVariables {

        private OSType currentOperatingSystem;
        private AppFileConfiguration fileConfiguration;

        public PathVariables(OSType currentOperatingSystem) {

            this.fileConfiguration = new AppFileConfiguration();
            this.currentOperatingSystem = currentOperatingSystem;

            if (this.fileConfiguration.getProperties().isEmpty()) setDefaultPathVariables(fileConfiguration);
        }

        public Properties getProperties() {
            return fileConfiguration.getProperties();
        }

        /**
         * Method defines current operating system with OSType value
         * evaluated in OperatingSystemCheck subclass
         */
        private void setDefaultPathVariables(AppFileConfiguration configuration) {

            fileConfiguration.setProperties("user-home-directory", System.getProperty("user.home"));

            Map<String, String> propertyMap = new HashMap<String, String>();

            switch (this.currentOperatingSystem) {
                case Windows:
                    propertyMap.put("postgres-bin-dir", "G:\\Program Files\\PostgresPro\\9.6\\bin");
                    propertyMap.put("file-directory", "D:\\files");
                    propertyMap.put("corporate-cfg-location", "C:\\ProgramData\\1C\\1CEStart\\1CEStart.cfg");
                    propertyMap.put("corporate-base-location", String.format(
                            "%s\\AppData\\Roaming\\1C\\1CEStart\\ibases.v8i",
                            fileConfiguration.getProperty("file-directory")));
                    break;
                case Linux:
                    propertyMap.put("file-directory", "/var/files");
                    break;
                case MacOS:
                    propertyMap.put("file-directory", "/");
                    break;
                case Other:
                    propertyMap.put("file-directory", "/");
                    break;
            }
            fileConfiguration.setProperties(propertyMap);
        }

        /**
         * returns default path to application data folder.
         * Depends on operation system type
         *
         * @return String
         */
        public String getFilePath() {
            return this.fileConfiguration.getProperty("file-directory");
        }
    }

    public String getFilePath() {
        return pathCheck.getFilePath();
    }

    public List<CorporateBase> getCorporateBases() {
        return corpCheck.bases;
    }

    /**
     * Returns a CorporateBase type by unique identifier
     *
     * @return CorporateBase base
     */
    public CorporateBase getByUniqueIdentifier(String uid) {
        for (CorporateBase base : getCorporateBases()) {
            if (base.getId().equals(uid)) {
                return base;
            }
        }
        return null;
    }

    public String getLatestPlatform() {
        //  TODO parse oneAssStartCfg and get available platforms
        return "E:\\Program Files (x86)\\1cv8\\8.3.11.2867\\bin\\1cv8.exe";
    }

}
