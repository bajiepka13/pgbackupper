package org.bajiepka.pgbackupper.controller;

import org.bajiepka.pgbackupper.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author Valerii C.
 */

@Controller
@RequestMapping(value = "/settings")
public class SettingsController {

    @Autowired
    SettingsService settingsService;

    @Value("${postgres-bin-dir}")
    private String pgDirectory;

    @GetMapping(value = "/current")
    public String currentSettings(Model model) {

        Map<String, String> propertiesMap = settingsService.getSettingsMap();

        model.addAttribute("pgpath", pgDirectory);
        model.addAttribute("properties", propertiesMap);
        model.addAttribute("title", String.format("Текущие настройки PgBackupper'a (%d)", propertiesMap.size()));
        return "settings/settings";
    }

}
