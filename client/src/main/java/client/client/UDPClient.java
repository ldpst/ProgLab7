package client.client;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.managers.ConfigManager;
import server.requests.Request;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class UDPClient {
    private final int PACKET_SIZE = ConfigManager.getPacketSize();

    private final Logger logger = LogManager.getLogger(UDPClient.class);

    private final DatagramSocket client;

    public UDPClient() throws IOException {
        logger.info("Запуск клиента...");
        client = new DatagramSocket();
        logger.info("Клиент запущен");
    }

    public byte[] makeRequest(Request request) throws IOException {
        logger.debug("Отправка запроса на сервер...");
        byte[] data = SerializationUtils.serialize(request);
        DatagramPacket dp = new DatagramPacket(data, data.length, new InetSocketAddress(ConfigManager.getAddress(), ConfigManager.getPort()));
        client.send(dp);
        logger.debug("Запрос отправлен: {}", data);

        byte[] data1 = new byte[ConfigManager.getPacketSize()];
        DatagramPacket dp1 = new DatagramPacket(data1, data1.length);
        logger.debug("Ожидание ответа от сервера...");
        client.receive(dp1);
        logger.debug("Ответ получен: {}", data1);
        return data1;
    }
}
