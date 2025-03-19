package server.server;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.ConfigManager;
import server.response.Response;
import server.utils.Pair;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.spi.SelectorProvider;

public abstract class UDPDatagramChannel extends DatagramChannel {
    private final Logger logger = LogManager.getLogger(UDPDatagramChannel.class);
    protected UDPDatagramChannel(SelectorProvider provider) {
        super(provider);
    }

    public void sendData(byte[] data, SocketAddress clientAddress) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        logger.debug("Сервер отправляет ответ...");
        send(buffer, clientAddress);
        logger.debug("Сервер отправил пакет: {}", data);
    }

    public Pair<byte[], SocketAddress> getData() throws IOException {
        logger.debug("Сервер ожидает пакет...");
        ByteBuffer buffer = ByteBuffer.allocate(ConfigManager.getPacketSize());
        SocketAddress clientAddress = receive(buffer);
        logger.debug("Клиент с адресом {} подключился", clientAddress);

        if (clientAddress != null) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            logger.debug("Сервер получил пакет: {} ", data);
            return new Pair<>(data, clientAddress);
        }
        else return null;
    }
}
