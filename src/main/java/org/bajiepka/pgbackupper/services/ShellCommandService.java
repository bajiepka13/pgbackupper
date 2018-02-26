package org.bajiepka.pgbackupper.services;

import org.bajiepka.pgbackupper.services.scheduled.AsynchronousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

/**
 * @author Valerii C.
 */

@Component
public class ShellCommandService {

    @Autowired
    AsynchronousService service;

    public String execute(String command, boolean runAsThread) {
        StringBuffer stringBuffer = new StringBuffer();

        try {
            if (runAsThread) {

            } else {

                Process process = Runtime.getRuntime().exec(command);
                process.waitFor();

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }

    protected String executeByProcessBuilder(String command) {

        try {
            Process process = Runtime.getRuntime().exec(String.format("cmd.exe /c dir %s", System.getProperty("user.home")));
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::print);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = process.waitFor();
            assert exitCode == 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String output(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }

}
