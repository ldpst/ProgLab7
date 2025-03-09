package server.commands;

import server.managers.CollectionManager;
import server.requests.Request;
import server.responds.HeadRespond;
import server.responds.Respond;

public class Head extends Command {
    private final CollectionManager collectionManager;

    public Head(CollectionManager collectionManager) {
        super("head", "вывести первый элемент коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Respond execute(Request request) {
        return new HeadRespond(collectionManager.getHead());
    }
}
