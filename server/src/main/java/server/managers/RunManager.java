package server.managers;

import general.utils.ValidationError;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.commands.server.*;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;
import server.utils.Pair;
import server.utils.RunMode;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class RunManager {
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";

    private final Logger logger = LogManager.getLogger(RunManager.class);

    private RunMode runMode = RunMode.RUN;

    private final CommandManager commandManager;
    private final CollectionManager collectionManager = new CollectionManager();

    private final PrintStream stream = System.out;
    private final Scanner scanner = new Scanner(System.in);

    public final Map<String, Command> commands = new HashMap<>();

    public RunManager() {
        new CSVManager(stream, collectionManager).loadFromCSV();

        this.commandManager = new CommandManager(collectionManager);

        commands.put("exit", new Exit(this, stream));
        commands.put("save", new Save(collectionManager, stream));
        commands.put("help", new Help(stream, this));
        commands.put("show", new Show(collectionManager, stream));
    }

    public void run() throws IOException {
        logger.info("Запуск сервера...");
        try (Selector selector = Selector.open();
             UDPDatagramChannel channel = (UDPDatagramChannel) DatagramChannel.open()) {

            DatagramSocket socket = channel.socket();
            socket.bind(new InetSocketAddress(ConfigManager.getAddress(), ConfigManager.getPort()));
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);


            logger.info("Сервер начал слушать на адресе {} и порту {} и обрабатывать запросы", ConfigManager.getAddress(), ConfigManager.getPort());

            stream.print("$ ");

            while (runMode == RunMode.RUN) {
                if (selector.select(100) > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();

                        if (key.isReadable()) {
                            handleClientRequest(channel);
                        }
                    }
                }
                if (System.in.available() > 0) {
                    executeCommandFromServer();
                }
            }
        }
    }

    private void executeCommandFromServer() {
        String nextCommand = scanner.nextLine().trim();
        if (nextCommand.isEmpty()) {
            return;
        }
        String[] splitCommand = nextCommand.split(" ");
        try {
            commands.get(splitCommand[0]).run(splitCommand);
        } catch (ValidationError e) {
            throw e;
        } catch (NullPointerException e) {
            stream.print("Команда не распознана\n");
        } catch (Exception e) {
            stream.print("Неопознанная ошибка\n");
            throw new RuntimeException(e);
        }
        if (runMode == RunMode.RUN) {
            stream.print("$ ");
        }
    }

    private void handleClientRequest(UDPDatagramChannel channel) throws IOException {
        Pair<byte[], SocketAddress> data = channel.getData();
        Request request;
            try {
                request = SerializationUtils.deserialize(data.first);
            } catch (SerializationException e) {
                logger.warn("SerializationException при сериализации запроса");
                throw e;
            }
            Response response = executeCommandFromClient(request);
            channel.sendData(SerializationUtils.serialize(response), data.second);
        }

    private Response executeCommandFromClient(Request request) {
        String[] commandLine = request.getMessage().split(" ");
        try {
            return commandManager.getCommands().get(commandLine[0]).execute(commandLine);
        } catch (NullPointerException e) {
            logger.debug("Команда не распознана");
            String message = RED + "Команда не распознана\n" + RESET;
            return new Response(message, ResponseType.ERROR, null);
        }
    }

    public void setRunMode(RunMode runMode) {
        this.runMode = runMode;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }
}
