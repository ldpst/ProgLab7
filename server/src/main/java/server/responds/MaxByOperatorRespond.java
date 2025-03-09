package server.responds;

import general.objects.Movie;
import server.commands.Commands;

public class MaxByOperatorRespond extends Respond {
    private final Movie movie;

    public MaxByOperatorRespond(Movie movie) {
        super(Commands.MAXBYOPERATOR, "");
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }
}
