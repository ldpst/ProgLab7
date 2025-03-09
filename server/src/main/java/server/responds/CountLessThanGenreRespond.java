package server.responds;

import server.commands.Commands;

public class CountLessThanGenreRespond extends Respond {
    private final int count;

    public CountLessThanGenreRespond(int count) {
        super(Commands.COUNTLESSTHANGENRE, "");
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
