package client.commands;

import client.client.UDPClient;
import client.managers.StreamManager;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.requests.RemoveByIdRequest;
import server.requests.UpdateRequest;
import server.responds.RemoveByIdRespond;
import server.responds.UpdateRespond;

import java.io.IOException;

public class RemoveById extends Command {
    private final UDPClient client;

    private final Logger logger = LogManager.getLogger(RemoveById.class);

    public RemoveById(UDPClient client, StreamManager stream) {
        super("remove_by_id id", "удалить элемент из коллекции по его id", stream);
        this.client = client;
    }

    @Override
    public void run(String[] args) throws IOException {
        if (args.length != 2) {
            stream.printErr("Неверный формат команды\n");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            stream.printErr("Введённый id должен быть целым числом\n");
            return;
        }

        var data = client.makeRequest(new RemoveByIdRequest(id));
        if (data.length == 0) {
            logger.warn("Сервер вернул пустой ответ");
        }
        RemoveByIdRespond respond = SerializationUtils.deserialize(data);
        if (!respond.getError().isEmpty()) {
            stream.printErr(respond.getError() + "\n");
        } else {
            stream.printSuccess("Элемент успешно удален\n");
        }
    }
}
