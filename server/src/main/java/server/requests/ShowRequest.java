package server.requests;

import server.commands.Commands;

public class ShowRequest extends Request {
    public ShowRequest() {
        super(Commands.SHOW);
    }
}
