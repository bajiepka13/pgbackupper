package org.bajiepka.pgbackupper.services;

import org.bajiepka.pgbackupper.configuration.PathSettings;
import org.bajiepka.pgbackupper.domain.CorporateBase;
import org.bajiepka.pgbackupper.domain.CorporateBaseCredentials;
import org.bajiepka.pgbackupper.domain.MaintenanceFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Valerii C.
 */

@Component
public class FileService {

//    private static @Log Logger Log;

    @Autowired
    PathSettings pathSettings;

    @Autowired
    ShellCommandService shellCommandService;

    public Collection<CorporateBase> getCorporateBases() {
        return pathSettings.getCorporateBases();
    }

    public CorporateBase getBaseById(String id) {
        return pathSettings.getByUniqueIdentifier(id);
    }

    public boolean processDump(CorporateBaseCredentials credentials, String id, boolean isDtDump) {

        String command = prepareCommand(id, credentials, isDtDump);
        shellCommandService.execute(command, false);

        return true;
    }

    public boolean processDump(String id, CorporateBaseCredentials credentials, boolean isDtDump) {

        String command = prepareCommand(id, credentials, isDtDump);
        shellCommandService.execute(command, false);

        return true;
    }

    private String prepareCommand(String id, CorporateBaseCredentials credentials, boolean isDtDump) {

        CorporateBase base = pathSettings.getByUniqueIdentifier(id);

        String user = credentials.getName();
        String password = credentials.getPassword();
        boolean disconnectUsers = credentials.isDisconnectUsers();

        //                      1         2  3  4  5  6  7  8   9
        return String.format("%s CONFIG %s %s %s %s %s %s %s\\%s",
                pathSettings.getLatestPlatform(),                                   //  1 - путь к платформе 1с
                base.isFileDatabase() ? "/F" : "/S",                                //  2 - признак файловой / клиент-серверной базы
                base.getConnectPath(),                                              //  3 - папка с базой
                user.equals("") ? "" : String.format("/N\"%s\"", user),             //  4 - user
                password.equals("") ? "" : String.format("/P\"%s\"", password),     //  5 - password
                disconnectUsers ? "/C\"ЗавершитьРаботуПользователей\"" : "",        //  6 - признак завершить работу пользователей
                isDtDump ? "/DumpIB" : "/DumpCfg",                                   //  7 - признак выгрузки "Дтшка", "Цфшка"
                pathSettings.getFilePath(),                                         //  8 - путь к папке файлов на компьютере
                base.getTransliteratedName(isDtDump)                                //  9 - путь к файлу DT
        );
    }

    public List<MaintenanceFile> getMaintenanceFiles(String filePath) {

        List<MaintenanceFile> files = new ArrayList<MaintenanceFile>();
        findFile(filePath, new File(filePath), files);

        return files;
    }

    private void findFile(String path, File file, List<MaintenanceFile> list) {
        File[] fileList = file.listFiles();
        if (fileList != null) {
            for (File fl : fileList) {
                if (fl.isDirectory()) {
                    findFile(path, fl, list);
                } else {
                    list.add(new MaintenanceFile(fl.getAbsolutePath()));
                }
            }
        }
    }
}
