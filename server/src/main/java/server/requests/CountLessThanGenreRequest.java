package server.requests;

import general.objects.MovieGenre;
import server.commands.Commands;

public class CountLessThanGenreRequest extends Request {
    private final MovieGenre genre;

    public CountLessThanGenreRequest(MovieGenre genre) {
        super(Commands.COUNTLESSTHANGENRE);
        this.genre = genre;
    }

    public MovieGenre getGenre() {
        return genre;
    }
}
