package org.bajiepka.pgbackupper.domain;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * @author Valerii C.
 */

public class MaintenanceFile extends File {

    private String adaptedSize;
    private FileTypes fileType;

    public MaintenanceFile(String pathname) {
        super(pathname);
        setAdaptedSize();
        setFileType();
    }

    private void setFileType() {
        switch (FilenameUtils.getExtension(this.getAbsolutePath()).toLowerCase()) {
            case "epf":
                this.fileType = FileTypes.EPF;
                break;
            case "erf":
                this.fileType = FileTypes.ERF;
                break;
            case "dt":
                this.fileType = FileTypes.DT;
                break;
            case "cf":
                this.fileType = FileTypes.CF;
                break;
            default:
                this.fileType = FileTypes.OTHER;
        }
    }

    private enum FileTypes {
        CF, DT, EPF, ERF, OTHER
    }

    private void setAdaptedSize() {

        long size = this.length();

        long kb = size / 1024;
        long mb = size / (1024 * 1024);
        long gb = size / (1024 * 1024 * 1024);

        DecimalFormat decimal = new DecimalFormat("0.00");

        if (kb > 1) {
            this.adaptedSize = decimal.format(kb).concat(" Кб");
        }
        if (mb > 1) {
            this.adaptedSize = decimal.format(mb).concat(" Мб");
        }
        if (gb > 1) {
            this.adaptedSize = decimal.format(gb).concat(" Гб");
        }
    }

    public FileTypes getFileType() {
        return fileType;
    }

    public String getAdaptedSize() {
        return adaptedSize;
    }
}
