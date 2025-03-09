package server.requests;

import general.objects.Person;
import server.commands.Commands;

public class CountByOperatorRequest extends Request {
    private final Person person;

    public CountByOperatorRequest(Person person) {
        super(Commands.COUNTBYOPERATOR);
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
}
