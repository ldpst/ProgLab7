package server.responds;

import general.objects.Movie;
import server.commands.Commands;

import java.util.ArrayDeque;
import java.util.Deque;

public class ShowRespond extends Respond {
    private final Deque<Movie> movies;

    public ShowRespond(Deque<Movie> movies) {
        super(Commands.SHOW, "");
        this.movies = movies;
    }

    public Deque<Movie> getMovies() {
        return new ArrayDeque<>(movies.stream().sorted().toList());
    }
}
