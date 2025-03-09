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
import server.requests.RemoveGreaterRequest;
import server.responds.AddIfMaxRespond;
import server.responds.RemoveGreaterRespond;

import java.io.IOException;

public class RemoveGreater extends Command {
    private final ScannerManager scanner;
    private final Logger logger = LogManager.getLogger(RemoveGreater.class);
    private final UDPClient client;

    public RemoveGreater(UDPClient client, StreamManager stream, ScannerManager scanner) {
        super("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный", stream);
        this.scanner = scanner;
        this.client = client;
    }

    @Override
    public void run(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        Movie movie = new MovieBuilder(logger, stream, scanner).build();
        byte[] data = client.makeRequest(new RemoveGreaterRequest(movie));
        if (data.length == 0) {
            logger.warn("Сервер вернул пустой ответ");
        }
        RemoveGreaterRespond respond = SerializationUtils.deserialize(data);
        stream.printSuccessf("Удалено %s элементов\n", respond.getError());
        logger.info("Команда выполнена");
    }
}
