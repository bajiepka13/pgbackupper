package org.bajiepka.pgbackupper.controller;

import org.bajiepka.pgbackupper.configuration.PathSettings;
import org.bajiepka.pgbackupper.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.charset.Charset;

@Controller
public class HomeController {

    @Autowired
    PathSettings pathSettings;

    @Autowired
    FileService fileService;

    @RequestMapping(value = "/")
    public String home(Model model) {
        return "home";
    }

    @RequestMapping(value = "/file")
    public String file(@RequestParam File file, Model model) {
        model.addAttribute("file", file);
        return "file";
    }

    @RequestMapping(value = "/files")
    public String files(Model model) {

        model.addAttribute("files", fileService.getMaintenanceFiles(pathSettings.getFilePath()));
        return "files";
    }

    @RequestMapping(value = "/files/download", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, @RequestParam File file) throws IOException {

        if (!file.exists()) {
            String errorMessage = "Sorry. The file you are looking for does not exist";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }

        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }

        System.out.println("mimetype : " + mimeType);

        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
        response.setContentLength((int) file.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());

    }

    @RequestMapping(value = "/files/delete", method = RequestMethod.GET)
    public String deleteFile(@RequestParam File file) {
        file.delete();
        return "redirect:/files";
    }
}
