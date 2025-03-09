package server.commands;

import server.managers.CollectionManager;
import server.requests.RemoveGreaterRequest;
import server.requests.Request;
import server.responds.RemoveGreaterRespond;
import server.responds.Respond;

public class RemoveGreater extends Command {
    private final CollectionManager collectionManager;

    public RemoveGreater(CollectionManager collectionManager) {
        super("remove_greater {element}", "удалить из коллекции все элементы, превышающие заданный");
        this.collectionManager = collectionManager;
    }

    @Override
    public Respond execute(Request request) {
        RemoveGreaterRequest req = (RemoveGreaterRequest) request;
        int count = collectionManager.removeGreater(req.getMovie());
        return new RemoveGreaterRespond(count);
    }
}
