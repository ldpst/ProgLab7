package server.responds;

import server.commands.Commands;

public class AddIfMaxRespond extends Respond {

    public AddIfMaxRespond(String error) {
        super(Commands.ADDIFMAX, error);
    }
}
