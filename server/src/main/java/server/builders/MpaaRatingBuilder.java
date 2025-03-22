package server.builders;

import general.objects.MovieGenre;
import general.objects.MpaaRating;
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

public class MpaaRatingBuilder extends Builder {
    public MpaaRatingBuilder(UDPDatagramChannel channel, SocketAddress clientAddress, Logger logger) {
        super(channel, clientAddress, logger);
    }

    @Override
    public MpaaRating build() throws IOException {
        return readMpaaRating();
    }

    public MpaaRating readMpaaRating() throws IOException {
        logger.debug("Заполнение мпаа рейтинга...");
        Response response = new Response("> Введите Мпаа Рейтинг " + Arrays.toString(MpaaRating.values()) + ":\n", ResponseType.NEXT_STEP);
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
                                logger.debug("Мпаа рейтинг заполнен");
                                return null;
                            }
                            try {
                                MpaaRating newMpa = MpaaRating.checkOf(res);
                                logger.debug("Мпаа рейтинг заполнен {}", newMpa);
                                return newMpa;
                            } catch (IllegalArgumentException e) {
                                channel.sendData(SerializationUtils.serialize(new Response("Введенный Мпаа рейтинг не является одним из предложенных\n* Повторная попытка ввода\n> Введите Мпаа Рейтинг " + Arrays.toString(MpaaRating.values()) + ":\n", ResponseType.ERROR)), clientAddress);
                            }
                        }
                    }
                }
            }
        }
    }
}
