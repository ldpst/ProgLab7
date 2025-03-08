package server.commands;

import server.requests.Request;
import server.responds.Respond;

public abstract class Command {
    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract Respond execute(Request request);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
