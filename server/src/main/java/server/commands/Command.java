package server.commands;

import server.requests.Request;
import server.response.Response;

import java.io.IOException;

public abstract class Command {
    private final String name;
    private final String description;

    protected static final String RED = "\u001B[31m";
    protected static final String RESET = "\u001B[0m";
    protected static final String GREEN = "\u001B[32m";

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract Response execute(Request request) throws IOException;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
