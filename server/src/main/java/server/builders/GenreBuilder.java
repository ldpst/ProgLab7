package server.builders;

import general.objects.MovieGenre;
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
import java.util.Arrays;
import java.util.Iterator;

public class GenreBuilder extends Builder {
    public GenreBuilder(UDPDatagramChannel channel, SocketAddress clientAddress, Logger logger) {
        super(channel, clientAddress, logger);
    }

    @Override
    public MovieGenre build() throws IOException {
        return readGenre();
    }

    /**
     * Метод для чтения жанра
     *
     * @return Найденный жанр
     */
    private MovieGenre readGenre() throws IOException {
        logger.debug("Заполнение жанра...");
        Response response = new Response("> Введите жанр " + Arrays.toString(MovieGenre.values()) + ":\n", ResponseType.NEXT_STEP);
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

                            if (res.isEmpty() || res.equals("null")) {
                                logger.debug("Жанр заполнен");
                                return null;
                            }
                            try {
                                MovieGenre newMovieGenre = MovieGenre.checkOf(res);
                                logger.debug("Жанр заполнен {}", newMovieGenre);
                                return newMovieGenre;
                            } catch (IllegalArgumentException e) {
                                channel.sendData(SerializationUtils.serialize(new Response(RED + "Введенный жанр не является одним из предложенных\n" + RESET + "* Повторная попытка ввода\n> Введите жанр " + Arrays.toString(MovieGenre.values()) + ":\n", ResponseType.ERROR)), clientAddress);
                            }
                        }
                    }
                }
            }
        }
    }

}
