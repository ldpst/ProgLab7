package server.commands;

import general.objects.Movie;
import server.managers.CollectionManager;
import server.requests.AddRequest;
import server.requests.Request;
import server.responds.AddRespond;
import server.responds.Respond;

public class Add extends Command {
    private CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public Respond execute(Request request) {
        Movie movie = ((AddRequest) request).getMovie();
        movie.setId(collectionManager.getAndIncreaseNextID());
        collectionManager.add(movie);
        return new AddRespond();
    }
}
