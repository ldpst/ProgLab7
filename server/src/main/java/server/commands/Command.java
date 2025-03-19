package server.commands;

import server.response.Response;

public abstract class Command {
    private final String name;
    private final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract Response execute(String[] args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
