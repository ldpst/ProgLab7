package server.responds;

import server.commands.Commands;

public class ClearRespond extends Respond {
    public ClearRespond() {
        super(Commands.CLEAR, "");
    }
}
