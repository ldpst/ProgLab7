package server.requests;

import server.commands.Commands;

public class RemoveByIdRequest extends Request {
    private final int id;

    public RemoveByIdRequest(int id) {
        super(Commands.REMOVEBYID);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
