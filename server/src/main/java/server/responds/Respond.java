package server.responds;

import java.io.Serializable;

public abstract class Respond implements Serializable {
    private final String name;

    public Respond(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
