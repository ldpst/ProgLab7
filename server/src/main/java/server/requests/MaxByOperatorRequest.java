package server.requests;

import server.commands.Commands;

public class MaxByOperatorRequest extends Request {
    public MaxByOperatorRequest() {
        super(Commands.MAXBYOPERATOR);
    }
}
