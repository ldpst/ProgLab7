package client.managers;

import client.client.UDPClient;
import client.commands.*;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager(UDPClient client, StreamManager stream, ScannerManager scanner, RunManager runManager) {
        commands.put("show", new Show(client, stream));
        commands.put("add", new Add(client, stream, scanner));
        commands.put("execute_script", new ExecuteScript(client, stream));
        commands.put("exit", new Exit(stream, runManager));
        commands.put("update_id", new Update(client, stream, scanner));
        commands.put("help", new Help(stream, this));
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}