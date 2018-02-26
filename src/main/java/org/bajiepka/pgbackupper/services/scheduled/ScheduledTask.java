package org.bajiepka.pgbackupper.services.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Valerii C.
 */

@Component
public class ScheduledTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "45 8 23 * * *", zone = "Europe/Istanbul")
    public void reportCurrentTime() {
        System.out.println("I am a scheduled task, run at " + dateFormat.format(new Date()));
    }

}
