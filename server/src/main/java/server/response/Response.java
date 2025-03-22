package server.response;

import general.objects.Movie;

import java.io.Serializable;
import java.util.Deque;

public class Response implements Serializable {
    private final String message;
    private final ResponseType type;
    private final Deque<Movie> collection;

    public Response(String message, ResponseType type) {
        this(message, type, null);
    }

    public Response(String message, ResponseType type, Deque<Movie> collection) {
        this.message = message;
        this.type = type;
        this.collection = collection;
    }

    public String getMessage() {
        return message;
    }

    public ResponseType getType() {
        return type;
    }

    public Deque<Movie> getCollection() {
        return collection;
    }

    public String toString() {
        return "Response{" + "message=\"" + message + '\"' + ", type=" + type + ", collection=" + collection + '}';
    }
}
