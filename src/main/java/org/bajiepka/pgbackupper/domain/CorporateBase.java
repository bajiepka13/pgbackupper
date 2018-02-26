package org.bajiepka.pgbackupper.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Valerii C.
 */

public class CorporateBase implements BaseFile {

    private CorporateBase(CorporateBaseBuilder builder) {
        this.name = builder.name;
        this.connect = builder.connect;
        this.fileDatabase = builder.fileDatabase;
        this.connectPath = builder.connectPath;
        this.id = builder.id;
        this.order = builder.order;
        this.folder = builder.folder;
        this.orderInTree = builder.orderInTree;
        this.external = builder.external;
        this.speed = builder.speed;
        this.app = builder.app;
        this.wa = builder.wa;
        this.version = builder.version;
    }

    private enum ClientConnectionSpeed {
        Slow, Normal, Fast
    }

    public CorporateBase() {
    }

    private enum App {
        Auto
    }

    private String name;
    private String connect;
    private String connectPath;
    private boolean fileDatabase;
    private String id;
    private String order;
    private String folder;
    private String orderInTree;
    private String external;
    private ClientConnectionSpeed speed;
    private App app;
    private String wa;
    private String version;

    public static class CorporateBaseBuilder {

        private String name;
        private String connect;
        private String connectPath;
        private boolean fileDatabase;
        private String id;
        private String order;
        private String folder;
        private String orderInTree;
        private String external;
        private ClientConnectionSpeed speed;
        private App app;
        private String wa;
        private String version;

        public CorporateBaseBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public CorporateBaseBuilder setConnect(String connect) {
            this.connect = connect;
            this.fileDatabase = connect.contains("File=");
            if (fileDatabase) {

                for (String toReplace : new String[]{"\"", ";", "File="}) {
                    connect = connect.replace(toReplace, "");
                }

                this.connectPath = connect;
            }
            return this;
        }

        public CorporateBaseBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public CorporateBaseBuilder setOrder(String order) {
            this.order = order;
            return this;
        }

        public CorporateBaseBuilder setFolder(String folder) {
            this.folder = folder;
            return this;
        }

        public CorporateBaseBuilder setOrderInTree(String orderInTree) {
            this.orderInTree = orderInTree;
            return this;
        }

        public CorporateBaseBuilder setExternal(String external) {
            this.external = external;
            return this;
        }

        public CorporateBaseBuilder setSpeed(String speed) {
            switch (speed) {
                case "Normal":
                    this.speed = ClientConnectionSpeed.Normal;
                    break;
                default:
                    this.speed = ClientConnectionSpeed.Slow;
            }
            return this;
        }

        public CorporateBaseBuilder setApp(String app) {
            switch (app) {
                case "Auto":
                    this.app = App.Auto;
            }
            return this;
        }

        public CorporateBaseBuilder setWa(String wa) {
            this.wa = wa;
            return this;
        }

        public CorporateBaseBuilder setversion(String version) {
            this.version = version;
            return this;
        }

        public CorporateBase build() {
            return new CorporateBase(this);
        }
    }

    public String getName() {
        return name;
    }

    public String getConnect() {
        return connect;
    }

    public String getId() {
        return id;
    }

    public String getOrder() {
        return order;
    }

    public String getFolder() {
        return folder;
    }

    public String getOrderInTree() {
        return orderInTree;
    }

    public String getExternal() {
        return external;
    }

    public ClientConnectionSpeed getSpeed() {
        return speed;
    }

    public App getApp() {
        return app;
    }

    public String getWa() {
        return wa;
    }

    public String getVersion() {
        return version;
    }

    public String getConnectPath() {
        return connectPath;
    }

    public boolean isFileDatabase() {
        return fileDatabase;
    }

    public String getTransliteratedName(boolean isDtDump) {
        return transliterateDt(this.getName(), isDtDump);
    }

    private String transliterateDt(String name, boolean isDtDump) {
        return String.format("%s_%s.%s",
                Transliterator.toTranslit(name),
                new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date()),
                isDtDump ? "dt" : "cf");
    }

    private static class Transliterator {

        private static final String[] charTable = new String[65536];

        static {
            charTable['А'] = "A";
            charTable['Б'] = "B";
            charTable['В'] = "V";
            charTable['Г'] = "G";
            charTable['Д'] = "D";
            charTable['Е'] = "E";
            charTable['Ё'] = "E";
            charTable['Ж'] = "ZH";
            charTable['З'] = "Z";
            charTable['И'] = "I";
            charTable['Й'] = "I";
            charTable['К'] = "K";
            charTable['Л'] = "L";
            charTable['М'] = "M";
            charTable['Н'] = "N";
            charTable['О'] = "O";
            charTable['П'] = "P";
            charTable['Р'] = "R";
            charTable['С'] = "S";
            charTable['Т'] = "T";
            charTable['У'] = "U";
            charTable['Ф'] = "F";
            charTable['Х'] = "H";
            charTable['Ц'] = "C";
            charTable['Ч'] = "CH";
            charTable['Ш'] = "SH";
            charTable['Щ'] = "SH";
            charTable['Ъ'] = "'";
            charTable['Ы'] = "Y";
            charTable['Ь'] = "'";
            charTable['Э'] = "E";
            charTable['Ю'] = "U";
            charTable['Я'] = "YA";

            for (int i = 0; i < charTable.length; i++) {
                char idx = (char) i;
                char lower = new String(new char[]{idx}).toLowerCase().charAt(0);
                if (charTable[i] != null) {
                    charTable[lower] = charTable[i].toLowerCase();
                }
            }

        }

        public static String toTranslit(String text) {
            char charBuffer[] = text.toCharArray();
            StringBuilder sb = new StringBuilder(text.length());
            for (char symbol : charBuffer) {
                String replace = charTable[symbol];
                sb.append(replace == null ? symbol : replace);
            }
            return sb.toString().replace(' ', '_');
        }
    }

    public String getSize(Integer fileSize) {
        return "123";
    }
}
