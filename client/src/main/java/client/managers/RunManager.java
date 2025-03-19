package client.managers;

import client.client.UDPClient;
import client.exceptions.ServerIsUnavailableException;
import client.utils.InputFormat;
import client.utils.RunMode;
import server.response.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunManager {
    private RunMode runMode = RunMode.RUN;
    private final StreamManager stream;
    private final ScannerManager scanner;

    private final UDPClient client;

    public static final List<String> usedScripts = new ArrayList<>();

    public RunManager(UDPClient client, ScannerManager scanner, InputFormat inputFormat) throws IOException {
        this.client = client;
        this.scanner = scanner;
        this.stream = new StreamManager(System.out, inputFormat);
    }

    public void run() {
        while (runMode == RunMode.RUN) {
            stream.print("$ ");
            String nextCommand = scanner.nextLine().trim();
            if (nextCommand.isEmpty()) {
                continue;
            }
            try {
                Response response = client.makeRequest(nextCommand);
                switch (response.getType()) {
                    case PRINT_MESSAGE, ERROR -> stream.print(response.getMessage());
                    case COLLECTION -> stream.print(ResponseManager.collectionToString(response.getCollection()));
                }
            } catch (ServerIsUnavailableException e) {
                stream.printErr("Сервер в данный момент недоступен. Программа завершена\n");
                System.exit(1);
            } catch (IOException e) {
                stream.printErr("Вызвана ошибка ввода/вывода");
                throw new RuntimeException(e);
            }
        }
        UDPClient.logger.info("Клиент завершил работу");
    }

    public void setRunMode(RunMode runMode) {
        this.runMode = runMode;
    }
}
