package server.commands;

import general.objects.Movie;
import server.managers.CollectionManager;
import server.requests.Request;
import server.response.Response;

public class Add extends Command {
    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String[] args) {
        Movie movie =
        movie.setId(collectionManager.getAndIncreaseNextID());
        collectionManager.add(movie);
        return new Response("", R)
    }
}