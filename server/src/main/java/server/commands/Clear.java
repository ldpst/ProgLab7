package server.commands;

import server.managers.CollectionManager;
import server.requests.Request;
import server.responds.ClearRespond;
import server.responds.Respond;

public class Clear extends Command {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public Respond execute(Request request) {
        collectionManager.clear();
        return new ClearRespond();
    }
}
