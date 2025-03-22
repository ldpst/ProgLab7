package server.builders;

import general.objects.Person;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.Logger;
import server.requests.Request;
import server.response.Response;
import server.response.ResponseType;
import server.server.UDPDatagramChannel;
import server.utils.Pair;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class PersonBuilder extends Builder {
    private final String format = "dd:MM:yyyy";

    public PersonBuilder(UDPDatagramChannel channel, SocketAddress clientAddress, Logger logger) {
        super(channel, clientAddress, logger);
    }

    @Override
    public Person build() throws IOException {
        logger.debug("Заполнение человека...");
        Person newPerson = new Person(
                readName(),
                readBirthday(),
                readWeight(),
                readPassportID()
        );
        logger.debug("Человек заполнен");
        return newPerson;
    }

    private String readName() throws IOException {
        logger.debug("Заполнение имени");
        Response response = new Response("> Введите имя человека:\n", ResponseType.NEXT_STEP);
        channel.sendData(SerializationUtils.serialize(response), clientAddress);
        try (Selector selector = Selector.open()) {
            channel.getChannel().register(selector, SelectionKey.OP_READ);
            while (true) {
                if (selector.select(100) > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();

                        if (key.isReadable()) {
                            Pair<byte[], SocketAddress> data = channel.getData();
                            Request request = SerializationUtils.deserialize(data.first);
                            String res = request.getMessage();

                            if (res.isEmpty()) {
                                channel.sendData(SerializationUtils.serialize(new Response("Имя не должно быть пустым\n* Повторная попытка ввода\n> Введите имя человека:\n", ResponseType.ERROR)), clientAddress);
                            } else {
                                logger.debug("Имя заполнено {}", res);
                                return res;
                            }
                        }
                    }
                }
            }
        }
    }

    private Date readBirthday() throws IOException {
        logger.debug("Заполнение дня рождения");
        Response response = new Response("> Введите дату рождения человека (формата " + format + "):\n", ResponseType.NEXT_STEP);
        channel.sendData(SerializationUtils.serialize(response), clientAddress);
        try (Selector selector = Selector.open()) {
            channel.getChannel().register(selector, SelectionKey.OP_READ);
            while (true) {
                if (selector.select(100) > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();

                        if (key.isReadable()) {
                            Pair<byte[], SocketAddress> data = channel.getData();
                            Request request = SerializationUtils.deserialize(data.first);
                            String res = request.getMessage();
                            if (res.isEmpty()) {
                                logger.debug("День рождения заполнен");
                                return null;
                            }
                            Date date = parseDate(res);
                            if (date != null) {
                                return date;
                            }
                        }
                    }
                }
            }
        }

    }

    private Date parseDate(String res) throws IOException {
        String[] split = res.split(":");
        if (split.length != 3) {
            channel.sendData(SerializationUtils.serialize(new Response("Введенные данные неверного формата\n* Повторная попытка ввода\n> Введите дату рождения человека (формата " + format + "):\n", ResponseType.ERROR)), clientAddress);
        } else {
            int day, month, year;
            try {
                day = Integer.parseInt(split[0]);
                month = Integer.parseInt(split[1]);
                year = Integer.parseInt(split[2]);
                if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1) {
                    channel.sendData(SerializationUtils.serialize(new Response("Введена невозможная дата\n* Повторная попытка ввода\n> Введите дату рождения человека (формата " + format + "):\n", ResponseType.ERROR)), clientAddress);
                } else {
                    DateFormat dateFormat = new SimpleDateFormat(format);
                    try {
                        Date date = dateFormat.parse(res);
                        logger.debug("День рождения заполнен {}", date);
                        return date;
                    } catch (ParseException e) {
                        channel.sendData(SerializationUtils.serialize(new Response("Введенные данные неверного формата\n* Повторная попытка ввода\n> Введите дату рождения человека (формата " + format + "):\n", ResponseType.ERROR)), clientAddress);
                    }

                }
            } catch (NumberFormatException e) {
                channel.sendData(SerializationUtils.serialize(new Response("В дате допустимо использование только цифр и символа \":\"\n* Повторная попытка ввода\n> Введите дату рождения человека (формата " + format + "):\n", ResponseType.ERROR)), clientAddress);
            }
        }
        return null;
    }

    private long readWeight() throws IOException {
        logger.debug("Заполнение веса");
        Response response = new Response("> Введите вес человека:\n", ResponseType.NEXT_STEP);
        channel.sendData(SerializationUtils.serialize(response), clientAddress);
        try (Selector selector = Selector.open()) {
            channel.getChannel().register(selector, SelectionKey.OP_READ);
            while (true) {
                if (selector.select(100) > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();

                        if (key.isReadable()) {
                            Pair<byte[], SocketAddress> data = channel.getData();
                            Request request = SerializationUtils.deserialize(data.first);
                            String res = request.getMessage();

                            if (res.isEmpty()) {
                                channel.sendData(SerializationUtils.serialize(new Response("Вес не должен быть пустым\n* Повторная попытка ввода\n> Введите вес человека:\n", ResponseType.ERROR)), clientAddress);
                            } else {
                                long weight;
                                try {
                                    weight = Long.parseLong(res);
                                    if (weight <= 0) {
                                        channel.sendData(SerializationUtils.serialize(new Response("Вес должен быть больше 0\n* Повторная попытка ввода\n> Введите вес человека:\n", ResponseType.ERROR)), clientAddress);
                                    } else {
                                        logger.debug("Вес заполнен {}", weight);
                                        return weight;
                                    }
                                } catch (NumberFormatException e) {
                                    channel.sendData(SerializationUtils.serialize(new Response("Вес должен быть целым числом\n* Повторная попытка ввода\n> Введите вес человека:\n", ResponseType.ERROR)), clientAddress);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private String readPassportID() throws IOException {
        logger.debug("Ввод айди паспорта");
        Response response = new Response("> Введите паспорт айди:\n", ResponseType.NEXT_STEP);
        channel.sendData(SerializationUtils.serialize(response), clientAddress);
        try (Selector selector = Selector.open()) {
            channel.getChannel().register(selector, SelectionKey.OP_READ);
            while (true) {
                if (selector.select(100) > 0) {
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        keyIterator.remove();

                        if (key.isReadable()) {
                            Pair<byte[], SocketAddress> data = channel.getData();
                            Request request = SerializationUtils.deserialize(data.first);
                            String res = request.getMessage();

                            if (res.isEmpty()) {
                                channel.sendData(SerializationUtils.serialize(new Response("Паспорт айди не может быть пустым\n* Повторная попытка ввода\n> Введите паспорт айди:\n", ResponseType.ERROR)), clientAddress);
                            } else if (!res.matches("\\d+")) {
                                channel.sendData(SerializationUtils.serialize(new Response("Паспорт айди должен состоять только из цифр\n* Повторная попытка ввода\n> Введите паспорт айди:\n", ResponseType.ERROR)), clientAddress);
                            } else if (res.length() > 25) {
                                channel.sendData(SerializationUtils.serialize(new Response("Паспорт айди не должен быть больше 25 символов\n* Повторная попытка ввода\n> Введите паспорт айди:\n", ResponseType.ERROR)), clientAddress);
                            } else {
                                logger.debug("Айди паспорта введен {}", res);
                                return res;
                            }
                        }
                    }
                }
            }
        }
    }
}


