package server.commands;

import server.managers.CollectionManager;
import server.requests.Request;
import server.responds.Respond;
import server.responds.ShowRespond;

public class Show extends Command {
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }

    @Override
    public Respond execute(Request request) {
        return new ShowRespond(collectionManager.getMovies());
    }
}
