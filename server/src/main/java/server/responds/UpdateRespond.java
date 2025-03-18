package server.responds;

import server.commands.Commands;

public class UpdateRespond extends Respond {

    public UpdateRespond(String error) {
        super(Commands.UPDATE, error);
    }
}
