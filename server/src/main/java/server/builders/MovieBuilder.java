package server.builders;

import general.objects.*;
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

public class MovieBuilder extends Builder {

    public MovieBuilder(UDPDatagramChannel channel, SocketAddress clientAddress, Logger logger) {
        super(channel, clientAddress, logger);
    }

    @Override
    public Movie build() throws IOException {
        logger.debug("Заполнение Movie...");
        Movie newMovie = new Movie(
                readName(),
                readCoordinates(),
                readOscarCount(),
                readGenre(),
                readMpaaRating(),
                readPerson());
        logger.debug("Movie заполнен");
        return newMovie;
    }

    private String readName() throws IOException {
        logger.debug("Заполнение названия");
        Response response = new Response("> Введите название фильма:\n", ResponseType.NEXT_STEP);
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

                            if (res.isEmpty()) {
                                channel.sendData(SerializationUtils.serialize(new Response("Название не должно быть пустым\n* Повторная попытка ввода\n> Введите название фильма:\n", ResponseType.ERROR)), clientAddress);
                            } else {
                                logger.debug("Название заполнено {}", res);
                                return res;
                            }
                        }
                    }
                }
            }
        }
    }

    private Coordinates readCoordinates() throws IOException {
        return new CoordinatesBuilder(channel, clientAddress, logger).build();
    }

    private Long readOscarCount() throws IOException {
        logger.debug("Заполнение кол-ва оскаров...");
        Response response = new Response("> Введите количество оскаров:\n", ResponseType.NEXT_STEP);
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

                            long count;
                            try {
                                count = Long.parseLong(res);
                                if (count <= 0) {
                                    channel.sendData(SerializationUtils.serialize(new Response("Количество оскаров должно быть больше нуля\n* Повторная попытка ввода\n> Введите количество оскаров:\n", ResponseType.ERROR)), clientAddress);
                                } else {
                                    logger.debug("Кол-во оскаров заполнено {}", count);
                                    return count;
                                }
                            } catch (NumberFormatException e) {
                                channel.sendData(SerializationUtils.serialize(new Response("Количество оскаров должно быть целым числом\n* Повторная попытка ввода\n> Введите количество оскаров:\n", ResponseType.ERROR)), clientAddress);
                            }

                        }
                    }
                }
            }
        }
    }

    private MovieGenre readGenre() throws IOException {
        return new GenreBuilder(channel, clientAddress, logger).build();
    }

    private MpaaRating readMpaaRating() throws IOException {
        return new MpaaRatingBuilder(channel, clientAddress, logger).build();
    }

    private Person readPerson() throws IOException {
        Response response = new Response("> Оператор == null? y/n ", ResponseType.NEXT_STEP);
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

                            if (res.equals("n") || res.isEmpty() || res.equals("no")) {
                                return new PersonBuilder(channel, clientAddress, logger).build();
                            } else if (res.equals("y") || res.equals("yes")) {
                                return null;
                            } else {
                                channel.sendData(SerializationUtils.serialize(new Response("Введённая строка не соответствует y или n\n* Повторная попытка ввода\n> Оператор == null? y/n ", ResponseType.ERROR)), clientAddress);
                            }
                        }
                    }
                }
            }
        }
    }
}
