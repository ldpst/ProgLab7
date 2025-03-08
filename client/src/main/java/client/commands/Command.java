package client.commands;

import client.managers.StreamManager;

import java.io.IOException;

public abstract class Command {
    private final String name;
    private final String description;
    protected final StreamManager stream;

    public Command(String name, String description, StreamManager stream) {
        this.name = name;
        this.description = description;
        this.stream = stream;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract void run(String[] args) throws IOException;
}
