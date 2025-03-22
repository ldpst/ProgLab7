package server.commands;

import general.objects.Coordinates;
import general.objects.Movie;
import general.objects.MovieGenre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.builders.CoordinatesBuilder;
import server.builders.GenreBuilder;
import server.builders.MovieBuilder;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;

import java.io.IOException;

public class Add extends Command {
    private final Logger logger = LogManager.getLogger(Add.class);

    private final CollectionManager collectionManager;
    private final UDPDatagramChannel channel;


    public Add(CollectionManager collectionManager, UDPDatagramChannel channel) {
        super("add", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
        this.channel = channel;
    }

    @Override
    public Response execute(Request request) throws IOException {
        Movie movie = new MovieBuilder(channel, request.getClientAddress(), logger).build();
        movie.setId(collectionManager.getAndIncreaseNextID());
        collectionManager.add(movie);
        return new Response("Элемент успешно добавлен\n", ResponseType.PRINT_MESSAGE);
    }
}