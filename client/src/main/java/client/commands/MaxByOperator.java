package client.commands;

import client.client.UDPClient;
import client.managers.StreamManager;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.requests.MaxByOperatorRequest;
import server.responds.MaxByOperatorRespond;

import java.io.IOException;

public class MaxByOperator extends Command {
    private final UDPClient client;
    private final Logger logger = LogManager.getLogger(MaxByOperator.class);

    public MaxByOperator(UDPClient client, StreamManager stream) {
        super("max_by_operator", "вывести любой объект из коллекции, значение поля operator которого является максимальным", stream);
        this.client = client;
    }

    @Override
    public void run(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        byte[] data = client.makeRequest(new MaxByOperatorRequest());
        if (data.length == 0) {
            logger.warn("Сервер вернул пустой ответ");
        }
        MaxByOperatorRespond respond = SerializationUtils.deserialize(data);
        if (respond.getMovie() != null) {
            stream.printSuccess("Максимальный элемент по оператору:\n");
            stream.print(respond.getMovie().toString() + "\n");
        } else {
            stream.printSuccess("Коллекция пуста\n");
        }
        logger.info("Команда выполнена");
    }
}
