package server.requests;

import general.objects.Movie;
import server.commands.Commands;

public class HeadRequest extends Request {
    public HeadRequest() {
        super(Commands.HEAD);
    }
}
