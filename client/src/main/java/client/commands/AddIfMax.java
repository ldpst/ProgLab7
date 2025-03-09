package client.commands;

import client.builders.MovieBuilder;
import client.client.UDPClient;
import client.managers.ScannerManager;
import client.managers.StreamManager;
import general.objects.Movie;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.requests.AddIfMaxRequest;
import server.responds.AddIfMaxRespond;

import java.io.IOException;

public class AddIfMax extends Command {
    private final UDPClient client;
    private final Logger logger = LogManager.getLogger(AddIfMax.class);
    private final ScannerManager scanner;

    public AddIfMax(UDPClient client, StreamManager stream, ScannerManager scanner) {
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции", stream);
        this.client = client;
        this.scanner = scanner;
    }

    @Override
    public void run(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        Movie movie = new MovieBuilder(logger, stream, scanner).build();
        byte[] data = client.makeRequest(new AddIfMaxRequest(movie));
        if (data.length == 0) {
            logger.warn("Сервер вернул пустой ответ");
        }
        AddIfMaxRespond respond = SerializationUtils.deserialize(data);
        if (respond.getError().isEmpty()) {
            stream.printSuccess("Элемент добавлен\n");
        } else {
            stream.printSuccess(respond.getError() + "\n");
        }
        logger.info("Команда выполнена");
    }
}
