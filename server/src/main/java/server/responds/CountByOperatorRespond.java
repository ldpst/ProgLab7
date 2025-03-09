package server.responds;

import server.commands.Commands;

public class CountByOperatorRespond extends Respond {
    private final int count;

    public CountByOperatorRespond(int count) {
        super(Commands.COUNTBYOPERATOR, "");
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
