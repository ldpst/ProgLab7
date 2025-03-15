package server.commands;

import server.managers.CollectionManager;
import server.requests.Request;
import server.responds.MaxByOperatorRespond;
import server.responds.Respond;

public class MaxByOperator extends Command {
    private final CollectionManager collectionManager;
    public MaxByOperator(CollectionManager collectionManager) {
        super("max_by_operator","вывести любой объект из коллекции, значение поля operator которого является максимальным");
        this.collectionManager = collectionManager;
    }

    @Override
    public Respond execute(Request request) {
        return new MaxByOperatorRespond(collectionManager.getMaxByOperator());
    }
}
