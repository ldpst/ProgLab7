package client.client;

import server.managers.ConfigManager;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.requests.Request;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPClient {
    private final int PACKET_SIZE = ConfigManager.getPacketSize();

    private final Logger logger = LogManager.getLogger(UDPClient.class);

    private final DatagramChannel client;

    public UDPClient() throws IOException {
        logger.info("Запуск клиента...");
        client = DatagramChannel.open()
                .bind(null).connect(new InetSocketAddress(ConfigManager.getAddress(), ConfigManager.getPort()));
//        client.configureBlocking(false);
        logger.info("Клиент запущен");
    }

    public byte[] makeRequest(Request request) throws IOException {
        logger.debug("Отправка запроса на сервер...");
        byte[] data = SerializationUtils.serialize(request);
        ByteBuffer bufferRequest = ByteBuffer.wrap(data);
        client.send(bufferRequest, new InetSocketAddress(ConfigManager.getAddress(), ConfigManager.getPort()));
        logger.debug("Запрос отправлен: {}", bufferRequest.array());

        ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);
        logger.debug("Ожидание ответа от сервера...");
        client.receive(buffer);
        byte[] res = buffer.array();
        logger.debug("Ответ получен: {}", res);
        return res;
    }
}
