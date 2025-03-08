package client.commands;

import client.managers.RunManager;
import client.managers.StreamManager;
import client.utils.RunMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Exit extends Command {
    private final Logger logger = LogManager.getLogger(Exit.class);
    private final RunManager runManager;

    public Exit(StreamManager stream, RunManager runManager) {
        super("exit", "завершить программу (без сохранения в файл)", stream);
        this.runManager = runManager;
    }

    @Override
    public void run(String[] args) {
        stream.printSuccess("Программа завершена\n");
        logger.info("Команда выполнена");
        runManager.setRunMode(RunMode.EXIT);
    }
}
