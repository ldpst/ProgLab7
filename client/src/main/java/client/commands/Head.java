package client.commands;

import client.client.UDPClient;
import client.managers.StreamManager;
import general.objects.Movie;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.requests.HeadRequest;
import server.requests.ShowRequest;
import server.responds.HeadRespond;
import server.responds.ShowRespond;

import java.io.IOException;
import java.util.Deque;

public class Head extends Command {
    private final Logger logger = LogManager.getLogger(Head.class);
    private final UDPClient client;

    public Head(UDPClient client, StreamManager stream) {
        super("head", "вывести первый элемент коллекции", stream);
        this.client = client;
    }

    @Override
    public void run(String[] args) throws IOException {
        logger.info("Команда выполняется...");

        byte[] data = client.makeRequest(new HeadRequest());
        if (data.length == 0) {
            logger.warn("Сервер вернул пустой ответ");
        }
        HeadRespond respond = SerializationUtils.deserialize(data);

        Movie head = respond.getHead();
        if (head == null) {
            stream.printSuccess("Коллекция пуста\n");
        } else {
            stream.printSuccess("Первый элемент:\n");
            stream.print(head + "\n");
        }

        logger.info("Команда выполнена");
    }
}
