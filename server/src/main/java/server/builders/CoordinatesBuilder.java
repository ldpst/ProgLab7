package server.builders;

import client.builders.Builder;
import general.objects.Coordinates;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.Logger;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

public class CoordinatesBuilder extends Builder {

    public CoordinatesBuilder(UDPDatagramChannel channel, SocketAddress clientAddress, Logger logger) {
        super(channel, clientAddress, logger);
    }

    @Override
    public Object build() {
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
        while (true) {
            Response response = new Response("> Введите координату x:\n$ ", ResponseType.NEXT_STEP);
            channel.sendData(SerializationUtils.serialize(response), clientAddress);
            Request request = SerializationUtils.deserialize(channel.getData().first);
            String res = request.getMessage();
            try {
                Float x = Float.parseFloat(res);
                logger.debug("х заполнен");
                return x;
            } catch (NumberFormatException e) {
                channel.sendData(("Координата x должна быть целым или вещественным числом\n* Повторная попытка ввода\n");
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
        while (true) {
            Response response = new Response("> Введите координату y:\n$ ", ResponseType.NEXT_STEP);
            channel.sendData(SerializationUtils.serialize(response), clientAddress);
            Request request = SerializationUtils.deserialize(channel.getData().first);
            String res = request.getMessage();
            try {
                int y = Integer.parseInt(res);
                logger.debug("y заполнен");
                return y;
            } catch (NumberFormatException e) {
                channel.sendData(("Координата y должна быть целым или вещественным числом\n* Повторная попытка ввода\n");
            }
        }
    }
}
