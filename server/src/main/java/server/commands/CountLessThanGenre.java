package server.commands;

import server.managers.CollectionManager;
import server.requests.CountLessThanGenreRequest;
import server.requests.Request;
import server.responds.CountLessThanGenreRespond;
import server.responds.Respond;

public class CountLessThanGenre extends Command {
    private final CollectionManager collectionManager;

    public CountLessThanGenre(CollectionManager collectionManager) {
        super("count_less_than_genre genre", "вывести количество элементов, значение поля genre которых меньше заданного");
        this.collectionManager = collectionManager;
    }

    @Override
    public Respond execute(Request request) {
        CountLessThanGenreRequest req = (CountLessThanGenreRequest) request;
        return new CountLessThanGenreRespond(collectionManager.countLessThanGenre(req.getGenre()));
    }
}
