package server.commands;

import server.managers.CollectionManager;
import server.requests.RemoveByIdRequest;
import server.requests.Request;
import server.responds.RemoveByIdRespond;
import server.responds.Respond;

public class RemoveById extends Command {
    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager) {
        super("remove_by_id id", "удалить элемент из коллекции по его id");
        this.collectionManager = collectionManager;
    }

    @Override
    public Respond execute(Request request) {
        RemoveByIdRequest req = (RemoveByIdRequest) request;
        String error = collectionManager.removeById(req.getId());
        return new RemoveByIdRespond(error);
    }
}
