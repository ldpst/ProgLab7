package client.builders;

import client.managers.ScannerManager;
import client.managers.StreamManager;
import general.objects.*;

import java.util.Arrays;

/**
 * Класс для создания объекта Movie
 *
 * @author ldpst
 */
public class MovieBuilder extends Builder {
    public MovieBuilder(StreamManager stream, ScannerManager scanner) {
        super(stream, scanner);
    }

    @Override
    public Movie build() {
        return new Movie(
                readName(),
                readCoordinates(),
                readOscarCount(),
                readGenre(),
                readMpaaRating(),
                readPerson());
    }

    /**
     * Метод для чтения имени
     *
     * @return Найденная строка
     */
    private String readName() {
        while (true) {
            stream.print("> Введите название фильма:\n$ ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                stream.printErr("Название не должно быть пустым\n");
                stream.print("* Повторная попытка ввода\n");
            } else {
                return name;
            }
        }
    }

    /**
     * Метод для чтения координат
     *
     * @return Найденные координаты
     */
    private Coordinates readCoordinates() {
        stream.print("* Ввод координат\n");
        return new CoordinatesBuilder(stream, scanner).build();
    }

    /**
     * Метод для чтения количества оскаров
     *
     * @return Найденное количество
     */
    private Long readOscarCount() {
        while (true) {
            stream.print("> Введите количество оскаров:\n$ ");
            String res = scanner.nextLine().trim();
            long count;
            try {
                count = Long.parseLong(res);
                if (count <= 0) {
                    stream.printErr("Количество оскаров должно быть больше нуля\n");
                    stream.print("* Повторная попытка ввода\n");
                } else {
                    return count;
                }
            } catch (NumberFormatException e) {
                stream.printErr("Количество оскаров должно быть целым числом\n");
                stream.print("* Повторная попытка ввода\n");
            }
        }
    }

    /**
     * Метод для чтения жанра
     *
     * @return Найденный жанр
     */
    private MovieGenre readGenre() {
        return new GenreBuilder(stream, scanner).build();
    }

    /**
     * Метод для чтения Мпаа Рейтинга
     *
     * @return Найденный MpaaRating
     */
    private MpaaRating readMpaaRating() {
        return new MpaaRatingBuilder(stream, scanner).build();
    }

    /**
     * Метод для чтения человека
     *
     * @return Найденный Person
     */
    private Person readPerson() {
        while (true) {
            stream.print("> Оператор == null? y/n ");
            String res = scanner.nextLine().trim().toLowerCase();
            if (res.equals("n") || res.isEmpty() || res.equals("no")) {
                stream.print("* Ввод оператора\n");
                return new PersonBuilder(stream, scanner).build();
            } else if (res.equals("y") || res.equals("yes")) {
                return null;
            } else {
                stream.printErr("Введённая строка не соответствует y или n\n");
                stream.print("* Повторная попытка ввода\n");
            }
        }
    }
}
