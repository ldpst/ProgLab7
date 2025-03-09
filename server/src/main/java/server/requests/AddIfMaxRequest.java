package server.requests;

import general.objects.Movie;
import server.commands.Commands;

public class AddIfMaxRequest extends Request {
    private final Movie movie;

    public AddIfMaxRequest(Movie movie) {
        super(Commands.ADDIFMAX);
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
}
