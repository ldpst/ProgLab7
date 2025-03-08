package client.commands;

import client.builders.MovieBuilder;
import client.client.UDPClient;
import client.managers.ScannerManager;
import client.managers.StreamManager;
import general.objects.Movie;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.requests.UpdateRequest;
import server.responds.UpdateRespond;

import java.io.IOException;

public class Update extends Command {
    private final ScannerManager scanner;
    private final Logger logger = LogManager.getLogger(Update.class);
    private final UDPClient client;

    public Update(UDPClient client, StreamManager stream, ScannerManager scanner) {
        super("update id {element}", "обновить значение элемента коллекции, id которого равен заданному", stream);
        this.scanner = scanner;
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
        Movie newMovie = new MovieBuilder(logger, stream, scanner).build();

        var data = client.makeRequest(new UpdateRequest(id, newMovie));
        if (data.length == 0) {
            logger.warn("Сервер вернул пустой ответ");
        }
        UpdateRespond respond = SerializationUtils.deserialize(data);
        if (!respond.getError().isEmpty()) {
            stream.printErr(respond.getError() + "\n");
        } else {
            stream.printSuccess("Элемент успешно изменен\n");
        }
    }
}
