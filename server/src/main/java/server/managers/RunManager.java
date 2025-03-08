package server.managers;

import server.commands.server.Command;
import server.commands.server.Exit;
import server.server.UDPDatagramServer;
import server.utils.RunMode;
import server.utils.ValidationError;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class RunManager {
    private final UDPDatagramServer server;
    private RunMode runMode = RunMode.RUN;

    private final PrintStream stream = System.out;
    private final Scanner scanner = new Scanner(System.in);

    private final Map<String, Command> commands = new HashMap<>();

    public RunManager(UDPDatagramServer server) {
        this.server = server;
        commands.put("exit", new Exit(this, stream));
    }

    public void run() throws IOException {
        try (Selector selector = Selector.open();
             DatagramChannel channel = DatagramChannel.open();) {

            while (runMode == RunMode.RUN) {
                DatagramSocket socket = channel.socket();
                channel.bind(new InetSocketAddress(ConfigManager.getAddress(), ConfigManager.getPort()));
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);

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
                    stream.print("Команда не распознана\n");
                } catch (Exception e) {
                    stream.print("Неопознанная ошибка\n");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void handleClientRequest(DatagramChannel channel) throws IOException {

    }

    public void setRunMode(RunMode runMode) {
        this.runMode = runMode;
    }
}
