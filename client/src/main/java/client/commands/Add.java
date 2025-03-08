package client.commands;

import client.builders.MovieBuilder;
import client.client.UDPClient;
import client.managers.ScannerManager;
import client.managers.StreamManager;
import general.objects.Movie;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.requests.AddRequest;
import server.responds.AddRespond;

import java.io.IOException;

public class Add extends Command {
    private final UDPClient client;
    private static final Logger logger = LogManager.getLogger(Add.class);
    private final ScannerManager scanner;

    public Add(UDPClient client, StreamManager stream, ScannerManager scanner) {
        super("add", "добавить новый элемент в коллекцию", stream);
        this.client = client;
        this.scanner = scanner;
    }

    @Override
    public void run(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        logger.debug("Заполнение Movie...");
        Movie movie = new MovieBuilder(stream, scanner).build();
        logger.debug("Movie заполнен");
        var data = client.makeRequest(new AddRequest(movie));
        if (data.length == 0) {
            logger.warn("Сервер вернул пустой ответ");
        }
        AddRespond respond = SerializationUtils.deserialize(data);
        logger.info("Команда выполнена");
    }
}
