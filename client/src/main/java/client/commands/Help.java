package client.commands;

import client.managers.CommandManager;
import client.managers.StreamManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Help extends Command {
    private final CommandManager commandManager;
    private final Logger logger = LogManager.getLogger(Help.class);

    public Help(StreamManager stream, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам", stream);
        this.commandManager = commandManager;
    }

    @Override
    public void run(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        var commands = commandManager.getCommands();
        stream.printSuccess("Справка по доступным командам:\n");
        for(Command command : commands.values()) {
            stream.print(command.getName() + " : " + command.getDescription() + "\n");
        }
        logger.info("Команда выполнена");
    }
}
