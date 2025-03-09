package client.commands;

import client.managers.CommandManager;
import client.managers.StreamManager;

import java.io.IOException;
import java.util.Map;

public class Help extends Command {
    private final CommandManager commandManager;

    public Help(StreamManager stream, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам", stream);
        this.commandManager = commandManager;
    }

    @Override
    public void run(String[] args) throws IOException {
        var commands = commandManager.getCommands();
        stream.printSuccess("Справка по доступным командам:\n");
        for(Command command : commands.values()) {
            stream.print(command.getName() + " : " + command.getDescription() + "\n");
        }
    }
}
