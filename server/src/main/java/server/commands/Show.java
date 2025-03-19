package server.commands;

import server.managers.CollectionManager;
import server.response.Response;
import server.response.ResponseType;

public class Show extends Command {
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(String[] args) {
        return new Response("", ResponseType.COLLECTION, collectionManager.getMovies());
    }
}
