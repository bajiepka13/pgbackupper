package org.bajiepka.pgbackupper.services.scheduled;


import org.bajiepka.pgbackupper.services.ShellCommandThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

/**
 * @author Valerii C.
 */

@Service
public class AsynchronousService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TaskExecutor taskExecutor;

    public void executeAsynchronously() {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ShellCommandThread shellThread = applicationContext.getBean(ShellCommandThread.class);
                taskExecutor.execute(shellThread);
            }
        });
    }

}
