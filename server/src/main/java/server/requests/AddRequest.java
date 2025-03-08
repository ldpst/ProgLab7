package server.requests;

import general.objects.Movie;
import server.commands.Commands;

public class AddRequest extends Request {
    private final Movie movie;

    public AddRequest(Movie movie) {
        super(Commands.ADD);
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
}
