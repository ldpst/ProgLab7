package client.commands;

import client.client.UDPClient;
import client.managers.StreamManager;
import general.objects.Movie;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.requests.ShowRequest;
import server.responds.ShowRespond;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * Класс команды show
 *
 * @author ldpst
 */
public class Show extends Command {
    private final UDPClient client;

    public static Logger logger = LogManager.getLogger(Show.class);

    public Show(UDPClient client, StreamManager streamManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", streamManager);
        this.client = client;
    }

    @Override
    public void run(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        byte[] data = client.makeRequest(new ShowRequest());
        if (data.length == 0) {
            logger.warn("Сервер вернул пустой ответ");
        }
        ShowRespond respond = SerializationUtils.deserialize(data);

        Deque<Movie> movies = respond.getMovies();
        if (movies.isEmpty()) {
            stream.printSuccess("Коллекция пуста\n");
        }
        else {
            stream.printSuccess("Элементы коллекции:\n");
            for (Movie movie : movies) {
                stream.print(movie + "\n");
            }
        }
        logger.info("Команда выполнена");
    }
}
