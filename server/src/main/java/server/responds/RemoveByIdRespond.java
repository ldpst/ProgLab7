package server.responds;

import server.commands.Commands;

public class RemoveByIdRespond extends Respond {

    public RemoveByIdRespond(String error) {
        super(Commands.REMOVEBYID, error);
    }
}
