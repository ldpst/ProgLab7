package server.commands;

import server.managers.CollectionManager;
import server.requests.Request;
import server.requests.UpdateRequest;
import server.responds.Respond;
import server.responds.UpdateRespond;

public class Update extends Command {
    private final CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        super("update id {element}", "обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
    }

    @Override
    public Respond execute(Request request) {
        UpdateRequest req = (UpdateRequest) request;
        String error = collectionManager.updateById(req.getId(), req.getMovie());
        return new UpdateRespond(error);
    }
}
