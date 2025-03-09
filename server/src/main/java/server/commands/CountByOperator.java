package server.commands;

import server.managers.CollectionManager;
import server.requests.CountByOperatorRequest;
import server.requests.Request;
import server.responds.CountByOperatorRespond;
import server.responds.Respond;

public class CountByOperator extends Command {
    private final CollectionManager collectionManager;

    public CountByOperator(CollectionManager collectionManager) {
        super("count_by_operator operator", "вывести количество элементов, значение поля operator которых равно заданному");
        this.collectionManager = collectionManager;
    }

    @Override
    public Respond execute(Request request) {
        CountByOperatorRequest req = (CountByOperatorRequest) request;
        return new CountByOperatorRespond(collectionManager.countByOperator(req.getPerson()));
    }
}
