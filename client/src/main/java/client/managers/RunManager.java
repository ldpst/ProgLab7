package client.managers;

import client.client.UDPClient;
import client.exceptions.ServerIsUnavailableException;
import client.exceptions.ValidationError;
import client.utils.InputFormat;
import client.utils.RunMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunManager {
    private RunMode runMode = RunMode.RUN;
    private final StreamManager stream;
    private final ScannerManager scanner;

    private final CommandManager commandManager;

    public static final List<String> usedScripts = new ArrayList<>();

    public RunManager(UDPClient client, ScannerManager scanner, InputFormat inputFormat) throws IOException {
        this.scanner = scanner;
        this.stream = new StreamManager(System.out, inputFormat);
        commandManager = new CommandManager(client, stream, scanner, this); // this для возможности выхода в exit
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
                commandManager.getCommands().get(splitCommand[0]).run(splitCommand);
            } catch (ValidationError e) {
                throw e;
            } catch (NullPointerException e) {
                stream.printErr("Команда не распознана\n");
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
