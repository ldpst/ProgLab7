package server.managers;

import server.commands.Command;
import server.requests.Request;
import server.responds.Respond;

public class CommandRunManager {
    private final CommandManager commandManager;

    public CommandRunManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public Respond runCommand(Request request) {
        Command command = commandManager.getCommands().get(request.getName());
        if (command != null) {
            return command.execute(request);
        } else {
            return null;
        }
    }
}
