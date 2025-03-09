package server.responds;

import server.commands.Commands;

public class ClearRespond extends Respond {
    public ClearRespond(String error) {
        super(Commands.CLEAR, error);
    }

    public ClearRespond() {
        super(Commands.CLEAR, "");
    }
}
