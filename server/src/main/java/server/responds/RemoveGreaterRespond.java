package server.responds;

import server.commands.Commands;

public class RemoveGreaterRespond extends Respond {
    private final int count;

    public RemoveGreaterRespond(int count) {
        super(Commands.REMOVEGREATER, "");
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
