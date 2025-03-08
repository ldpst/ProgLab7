package server.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.ConfigManager;
import server.utils.Pair;

import java.io.IOException;
import java.net.*;

public class UDPDatagramServer extends UDPServer {
    public final int PACKET_LENGTH = ConfigManager.getPacketSize();
    public final int DATA_LENGTH = PACKET_LENGTH - 1;

    private final DatagramSocket datagramSocket;

    public static Logger logger = LogManager.getLogger(UDPDatagramServer.class);

    public UDPDatagramServer() throws SocketException {
        super(new InetSocketAddress(ConfigManager.getAddress(), ConfigManager.getPort()));
        logger.info("Запуск сервера...");
        this.datagramSocket = new DatagramSocket(getAddress());
        logger.info("Сервер запущен");
    }


    @Override
    public Pair<byte[], InetSocketAddress> receive() throws IOException {
        byte[] buffer = new byte[PACKET_LENGTH];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(datagramPacket);
        return new Pair<>(buffer, (InetSocketAddress) datagramPacket.getSocketAddress());
    }

    @Override
    public void send(Pair<byte[], InetSocketAddress> data) throws IOException {
        DatagramPacket packet = new DatagramPacket(data.first, data.first.length, data.second.getAddress(), data.second.getPort());
        datagramSocket.send(packet);
    }
}
