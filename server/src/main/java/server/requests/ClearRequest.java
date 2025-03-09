package server.requests;

import server.commands.Commands;

public class ClearRequest extends Request {

    public ClearRequest() {
        super(Commands.CLEAR);
    }
}
