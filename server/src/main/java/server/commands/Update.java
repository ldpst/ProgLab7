package server.commands;

import general.objects.Movie;
import server.managers.CollectionManager;
import server.requests.Request;
import server.requests.UpdateRequest;
import server.responds.Respond;
import server.responds.UpdateRespond;

import java.util.Deque;

public class Update extends Command {
    private final CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        super("update id {element}", "обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
    }

    @Override
    public Respond execute(Request request) {
        UpdateRequest req = (UpdateRequest) request;
        collectionManager.updateById(req.getId(), req.getMovie());
        return new UpdateRespond();
    }
}
