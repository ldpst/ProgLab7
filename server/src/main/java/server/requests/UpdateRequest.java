package server.requests;

import general.objects.Movie;
import server.commands.Commands;

public class UpdateRequest extends Request {
    private final Movie movie;
    private final int id;

    public UpdateRequest(int id, Movie movie) {
        super(Commands.UPDATE);
        this.id = id;
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getId() {
        return id;
    }
}
