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
        commands.put("remove_by_id", new RemoveById(client, stream));
        commands.put("clear", new Clear(client, stream));
        commands.put("head", new Head(client, stream));
        commands.put("add_if_max", new AddIfMax(client, stream, scanner));
        commands.put("remove_greater", new RemoveGreater(client, stream, scanner));
        commands.put("max_by_operator", new MaxByOperator(client, stream));
        commands.put("count_by_operator", new CountByOperator(client, stream, scanner));
        commands.put("count_less_than_genre", new CountLessThanGenre(client, stream, scanner));
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}