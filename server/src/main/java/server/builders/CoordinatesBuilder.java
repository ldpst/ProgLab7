package server.builders;

import general.objects.Coordinates;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.Logger;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;
import server.utils.Pair;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class CoordinatesBuilder extends Builder {

    public CoordinatesBuilder(UDPDatagramChannel channel, SocketAddress clientAddress, Logger logger) {
        super(channel, clientAddress, logger);
    }

    @Override
    public Coordinates build() throws IOException {
        logger.debug("Заполнение координат...");
        Coordinates newCoordinates = new Coordinates(
                readX(),
                readY());
        logger.debug("Координаты заполнены");
        return newCoordinates;
    }

    /**
     * Метод для чтения координаты X
     *
     * @return Найденное число
     */
    private Float readX() throws IOException {
        logger.debug("Заполнение х...");
        Response response = new Response("> Введите координату x:\n", ResponseType.NEXT_STEP);
        channel.sendData(SerializationUtils.serialize(response), clientAddress);
        try (Selector selector = Selector.open()) {
            channel.getChannel().register(selector, SelectionKey.OP_READ);
            while (true) {
                if (selector.select(100) > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();

                        if (key.isReadable()) {
                            Pair<byte[], SocketAddress> data = channel.getData();
                            Request request = SerializationUtils.deserialize(data.first);
                            String res = request.getMessage();
                            try {
                                Float x = Float.parseFloat(res);
                                logger.debug("х заполнен");
                                return x;
                            } catch (NumberFormatException e) {
                                channel.sendData(SerializationUtils.serialize(new Response("Координата x должна быть целым или вещественным числом\n* Повторная попытка ввода\n> Введите координату x:\n", ResponseType.ERROR)), clientAddress);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Метод для чтения координаты Y
     *
     * @return Найденное число
     */
    private int readY() throws IOException {
        logger.debug("Заполнение y...");
        Response response = new Response("> Введите координату y:\n", ResponseType.NEXT_STEP);
        channel.sendData(SerializationUtils.serialize(response), clientAddress);

        try (Selector selector = Selector.open()) {
            channel.getChannel().register(selector, SelectionKey.OP_READ);
            while (true) {
                if (selector.select(100) > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();

                        if (key.isReadable()) {
                            Pair<byte[], SocketAddress> data = channel.getData();
                            Request request = SerializationUtils.deserialize(data.first);

                            String res = request.getMessage();
                            try {
                                int y = Integer.parseInt(res);
                                logger.debug("y заполнен");
                                return y;
                            } catch (NumberFormatException e) {
                                channel.sendData(SerializationUtils.serialize(new Response("Координата y должна быть целым числом\n* Повторная попытка ввода\n> Введите координату y:\n", ResponseType.ERROR)), clientAddress);
                            }
                        }
                    }
                }
            }
        }
    }
}
