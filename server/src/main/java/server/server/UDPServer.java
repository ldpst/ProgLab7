package server.server;

import general.objects.Movie;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.CollectionManager;
import server.managers.CommandManager;
import server.managers.CommandRunManager;
import server.requests.Request;
import server.utils.Pair;

import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class UDPServer {
    private final InetSocketAddress address;
    private final CollectionManager collectionManager = new CollectionManager();
    private CommandManager commandManager;
    private CommandRunManager commandRunManager;

    public static Logger logger = LogManager.getLogger(UDPServer.class);

    public UDPServer(InetSocketAddress address) {
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public abstract Pair<byte[], InetSocketAddress> receive() throws IOException;

    public void run() throws IOException {
        boolean f = true;
        commandManager = new CommandManager(collectionManager);
        commandRunManager = new CommandRunManager(commandManager);
        logger.info("Сервер начал слушать на адресе {} и порту {} и обрабатывать запросы", address.getAddress(), address.getPort());
        while (f) {
            Pair<byte[], InetSocketAddress> data;
            try {
                logger.debug("Сервер ожидает пакет...");
                data = receive();
                logger.debug("Сервер получил пакет: {} ", data.first);
            } catch (IOException e) {
                logger.warn("IOException при получении пакета");
                throw e;
            }
            Request request;
            try {
                request = SerializationUtils.deserialize(data.first);
            } catch (SerializationException e) {
                logger.warn("SerializationException при сериализации запроса");
                throw e;
            }

            var respond = commandRunManager.runCommand(request);
            byte[] data1 = SerializationUtils.serialize(respond);
            logger.debug("Сервер отправляет ответ...");
            send(new Pair<>(data1, data.second));
            logger.debug("Сервер отправил пакет: {}", data1);
        }
        logger.info("Сервер перестал слушать на адресе {} и порту {} и обрабатывать запросы", address.getAddress(), address.getPort());

    }

    public abstract void send(Pair<byte[], InetSocketAddress> data) throws IOException;

//    public static void main(String[] args) throws SocketException, IOException {
//
//        try (DatagramSocket socket = new DatagramSocket(ConfigManager.getPort())) {
//            byte[] buffer = new byte[1024];
//            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
//            System.out.println("Сервер слушает на:");
//            System.out.println(" - Локальный IP: " + InetAddress.getLocalHost().getHostAddress());
//
//            for (InetAddress addr : InetAddress.getAllByName(InetAddress.getLocalHost().getHostName())) {
//                System.out.println(" - Доступен по: " + addr.getHostAddress());
//            }
//
//            while (true) {
//                socket.receive(packet);
//
//                String message = new String(packet.getData(), 0, packet.getLength());
//
//                System.out.println("Получено сообщение:");
//                System.out.println(message);
//                System.out.println();
//            }
//
//
//
//        } catch (SocketException e) {
//            //
//        }
//    }
}
