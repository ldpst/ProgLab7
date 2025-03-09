package client.commands;

import client.builders.GenreBuilder;
import client.client.UDPClient;
import client.managers.ScannerManager;
import client.managers.StreamManager;
import general.objects.MovieGenre;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.requests.CountLessThanGenreRequest;
import server.responds.CountLessThanGenreRespond;

import java.io.IOException;

public class CountLessThanGenre extends Command {
    private final UDPClient client;
    private final ScannerManager scanner;
    private final Logger logger = LogManager.getLogger(CountLessThanGenre.class);

    public CountLessThanGenre(UDPClient client, StreamManager stream, ScannerManager scanner) {
        super("count_less_than_genre genre", "вывести количество элементов, значение поля genre которых меньше заданного", stream);
        this.client = client;
        this.scanner = scanner;
    }

    @Override
    public void run(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        MovieGenre genre;
        if (args.length == 2 && args[1].equals("null")) {
            genre = null;
        } else {
            genre = new GenreBuilder(logger, stream, scanner).build();
        }
        byte[] data = client.makeRequest(new CountLessThanGenreRequest(genre));
        if (data.length == 0) {
            logger.warn("Сервер вернул пустой ответ");
        }
        CountLessThanGenreRespond respond = SerializationUtils.deserialize(data);
        stream.printSuccessf("Элементов с жанром меньше данного: %s\n", respond.getCount());
        logger.info("Команда выполнена");
    }
}
