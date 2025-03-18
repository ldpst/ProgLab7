package client.commands;

import client.client.UDPClient;
import client.managers.StreamManager;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.requests.ClearRequest;

import java.io.IOException;

public class Clear extends Command {
    private final Logger logger = LogManager.getLogger(Clear.class);
    private final UDPClient client;

    public Clear(UDPClient client, StreamManager stream) {
        super("clear", "очистить коллекцию", stream);
        this.client = client;
    }

    @Override
    public void run(String[] args) throws IOException {
        logger.info("Команда выполняется...");

        byte[] data = client.makeRequest(new ClearRequest());
        if (data.length == 0) {
            logger.warn("Сервер вернул пустой ответ");
        }
        SerializationUtils.deserialize(data);
        stream.printSuccess("Коллекция очищена\n");
        logger.info("Команда выполнена");
    }
}
