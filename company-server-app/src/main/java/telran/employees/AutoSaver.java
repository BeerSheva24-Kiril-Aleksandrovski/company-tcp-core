package telran.employees;

import telran.io.Persistable;
import static telran.employees.ServerConfigProperties.FILE_NAME;
import static telran.employees.ServerConfigProperties.AUTO_SAVE_INTERVAL;


public class AutoSaver extends Thread {
    private final Persistable persistable;

    public AutoSaver(Persistable persistable) {
        this.persistable = persistable;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                persistable.saveToFile(FILE_NAME);
                sleep(AUTO_SAVE_INTERVAL);
            } catch (InterruptedException e) {
            }
        }
    }
}