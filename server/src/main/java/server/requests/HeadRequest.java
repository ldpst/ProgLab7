package server.requests;

import server.commands.Commands;

public class HeadRequest extends Request {
    public HeadRequest() {
        super(Commands.HEAD);
    }
}
