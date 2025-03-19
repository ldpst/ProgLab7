package client.managers;

import client.client.UDPClient;
import general.objects.Movie;

import java.util.Deque;

public class ResponseManager {
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";

    private final UDPClient client;

    public ResponseManager(UDPClient client) {
        this.client = client;
    }

    public static String collectionToString(Deque<Movie> movies) {
        StringBuilder message;
        if (movies.isEmpty()) {
            message = new StringBuilder(GREEN + "Коллекция пуста\n" + RESET);
        } else {
            message = new StringBuilder(GREEN + "Коллекция:\n" + RESET);
            for (Movie movie : movies) {
                message.append(movie).append("\n");
            }
        }
        return message.toString();
    }
}
