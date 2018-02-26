package org.bajiepka.pgbackupper.controller;

import org.bajiepka.pgbackupper.domain.CorporateBaseCredentials;
import org.bajiepka.pgbackupper.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author Valerii C.
 */

@Controller
@RequestMapping(value = "/corporate")
public class CorporateController {

    @Autowired
    FileService fileService;

    @GetMapping(value = "/bases")
    public String corporateBases(Model model) {
        model.addAttribute("bases", fileService.getCorporateBases());
        return "bases";
    }

    @RequestMapping(value = "/base")
    public String corporateBase(@RequestParam String id, Model model) {

        CorporateBaseCredentials credentials = new CorporateBaseCredentials();

        model.addAttribute("baseid", id);
        model.addAttribute("credentials", credentials);
        model.addAttribute("base", fileService.getBaseById(id));
        return "base";
    }

    @PostMapping(value = "/maintenance", params = "action=database")
    public String processDtFile(
            @ModelAttribute(value = "credentials") CorporateBaseCredentials credentials,
            @RequestParam String id) {

        boolean result = fileService.processDump(credentials, id, true);

        return "redirect:/corporate/bases";
    }

    @PostMapping(value = "/maintenance", params = "action=configuration")
    public String processCfFile(
            @ModelAttribute(value = "credentials") CorporateBaseCredentials credentials,
            @RequestParam String id) {

        boolean result = fileService.processDump(credentials, id, false);

        return "redirect:/corporate/bases";
    }
}
