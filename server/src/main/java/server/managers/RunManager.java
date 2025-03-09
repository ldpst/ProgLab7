package server.managers;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.commands.server.Command;
import server.commands.server.Exit;
import server.commands.server.Help;
import server.commands.server.Save;
import server.requests.Request;
import server.responds.Respond;
import server.utils.RunMode;
import server.utils.ValidationError;

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
    private final Logger logger = LogManager.getLogger(RunManager.class);

    private RunMode runMode = RunMode.RUN;

    private CommandManager commandManager;
    private CommandRunManager commandRunManager;
    private final CollectionManager collectionManager = new CollectionManager();

    private final PrintStream stream = System.out;
    private final Scanner scanner = new Scanner(System.in);

    public final Map<String, Command> commands = new HashMap<>();

    public RunManager() {
        new CSVManager(stream, collectionManager).loadFromCSV();

        commands.put("exit", new Exit(this, stream));
        commands.put("save", new Save(collectionManager, stream));
        commands.put("help", new Help(stream, this));
    }

    public void run() throws IOException {
        try (Selector selector = Selector.open();
             DatagramChannel channel = DatagramChannel.open()) {

            DatagramSocket socket = channel.socket();
            socket.bind(new InetSocketAddress(ConfigManager.getAddress(), ConfigManager.getPort()));
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);

            commandManager = new CommandManager(collectionManager);
            commandRunManager = new CommandRunManager(commandManager);
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
                    executeCommand();
                }
            }
        }
    }

    private void executeCommand() {
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

    private void handleClientRequest(DatagramChannel channel) throws IOException {
        logger.debug("Сервер ожидает пакет...");
        ByteBuffer buffer = ByteBuffer.allocate(ConfigManager.getPacketSize());
        SocketAddress clientAddress = channel.receive(buffer);

        if (clientAddress != null) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            logger.debug("Сервер получил пакет: {} ", data);
            Request request;
            try {
                request = SerializationUtils.deserialize(data);
            } catch (SerializationException e) {
                logger.warn("SerializationException при сериализации запроса");
                throw e;
            }
            Respond respond = commandRunManager.runCommand(request);
            byte[] data1 = SerializationUtils.serialize(respond);
            ByteBuffer buffer1 = ByteBuffer.wrap(data1);
            logger.debug("Сервер отправляет ответ...");
            channel.send(buffer1, clientAddress);
            logger.debug("Сервер отправил пакет: {}", data1);
        }
    }

    public void setRunMode(RunMode runMode) {
        this.runMode = runMode;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }
}
