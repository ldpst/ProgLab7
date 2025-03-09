package server.responds;

import general.objects.Movie;
import server.commands.Commands;

public class HeadRespond extends Respond {
    private final Movie head;

    public HeadRespond(Movie head, String error) {
        super(Commands.HEAD, error);
        this.head = head;
    }

    public HeadRespond(Movie head) {
        this(head, "");
    }

    public Movie getHead() {
        return head;
    }
}
