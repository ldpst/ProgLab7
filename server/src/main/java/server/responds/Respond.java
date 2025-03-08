package server.responds;

import java.io.Serializable;

public abstract class Respond implements Serializable {
    private final String name;
    private final String error;

    public Respond(String name, String error) {
        this.name = name;
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public String getError() {
        return error;
    }
}
