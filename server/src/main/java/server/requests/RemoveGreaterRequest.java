package server.requests;

import general.objects.Movie;
import server.commands.Commands;

public class RemoveGreaterRequest extends Request {
    private final Movie movie;

    public RemoveGreaterRequest(Movie movie) {
        super(Commands.REMOVEGREATER);
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
}
