package client.commands;

import client.builders.PersonBuilder;
import client.client.UDPClient;
import client.managers.ScannerManager;
import client.managers.StreamManager;
import general.objects.Person;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.requests.CountByOperatorRequest;
import server.responds.CountByOperatorRespond;

import java.io.IOException;

public class CountByOperator extends Command {
    private final UDPClient client;
    private final ScannerManager scanner;
    private final Logger logger = LogManager.getLogger(CountByOperator.class);

    public CountByOperator(UDPClient client, StreamManager stream, ScannerManager scanner) {
        super("count_by_operator operator/null", "вывести количество элементов, значение поля operator которых равно заданному", stream);
        this.client = client;
        this.scanner = scanner;
    }

    @Override
    public void run(String[] args) throws IOException {
        logger.info("Команда выполняется...");
        Person person;
        if (args.length == 2 && args[1].equals("null")) {
            person = null;
        } else {
            person = new PersonBuilder(logger, stream, scanner).build();
        }
        byte[] data = client.makeRequest(new CountByOperatorRequest(person));
        if (data.length == 0) {
            logger.warn("Сервер вернул пустой ответ");
        }
        CountByOperatorRespond respond = SerializationUtils.deserialize(data);
        stream.printSuccessf("Элементов с таким оператором: %s\n", respond.getCount());
        logger.info("Команда выполнена");
    }
}
