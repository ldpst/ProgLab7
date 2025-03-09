package server.responds;

import server.commands.Commands;

public class RemoveGreaterRespond extends Respond {
    public RemoveGreaterRespond(String error) {
        super(Commands.REMOVEGREATER, error);
    }
}
