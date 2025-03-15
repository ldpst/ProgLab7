package server.commands;

import server.managers.CollectionManager;
import server.requests.AddIfMaxRequest;
import server.requests.Request;
import server.responds.AddIfMaxRespond;
import server.responds.Respond;

public class AddIfMax extends Command {
    private final CollectionManager collectionManager;

    public AddIfMax(CollectionManager collectionManager) {
        super("add_if_max {element}", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Respond execute(Request request) {
        AddIfMaxRequest req = (AddIfMaxRequest) request;
        String error = collectionManager.addIfMax(req.getMovie());
        return new AddIfMaxRespond(error);
    }
}
