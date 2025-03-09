package client.managers;

import client.client.UDPClient;
import client.commands.Command;
import client.exceptions.ServerIsUnavailableException;
import client.exceptions.ValidationError;
import client.utils.InputFormat;
import client.utils.RunMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RunManager {
    private RunMode runMode = RunMode.RUN;
    private final StreamManager stream;
    private final ScannerManager scanner;

    private final UDPClient client;
    private final CommandManager commandManager;

    private final Map<String, Command> commands;

    public static final List<String> usedScripts = new ArrayList<>();

    public RunManager(UDPClient client, ScannerManager scanner, InputFormat inputFormat) throws IOException {
        this.client = client;
        this.scanner = scanner;
        this.stream = new StreamManager(System.out, inputFormat);
        commandManager = new CommandManager(client, stream, scanner, this); // this для возможности выхода в exit
        commands = commandManager.getCommands();
    }

    public void run() {
        while (runMode == RunMode.RUN) {
            stream.print("$ ");
            String nextCommand = scanner.nextLine().trim();
            if (nextCommand.isEmpty()) {
                continue;
            }
            String[] splitCommand = nextCommand.split(" ");
            try {
                commands.get(splitCommand[0]).run(splitCommand);
            } catch (ValidationError e) {
                throw e;
            } catch (NullPointerException e) {
                stream.printErr("Команда не распознана\n");
                throw e;
            } catch (ServerIsUnavailableException e) {
                stream.printErr("Сервер в данный момент недоступен. Программа завершена\n");
                System.exit(1);
            } catch (Exception e) {
                stream.printErr("Неопознанная ошибка\n");
                throw new RuntimeException(e);
            }
        }
        UDPClient.logger.info("Клиент завершил работу");
    }

    public void setRunMode(RunMode runMode) {
        this.runMode = runMode;
    }
}
