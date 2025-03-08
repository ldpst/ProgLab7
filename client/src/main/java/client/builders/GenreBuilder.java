package client.builders;

import client.managers.ScannerManager;
import client.managers.StreamManager;
import general.objects.MovieGenre;

import java.util.Arrays;

public class GenreBuilder extends Builder {
    public GenreBuilder(StreamManager stream, ScannerManager scanner) {
        super(stream, scanner);
    }

    @Override
    public MovieGenre build() {
        return readGenre();
    }

    /**
     * Метод для чтения жанра
     *
     * @return Найденный жанр
     */
    private MovieGenre readGenre() {
        while (true) {
            stream.print("> Введите жанр " + Arrays.toString(MovieGenre.values()) + ":\n$ ");
            String res = scanner.nextLine().trim();
            if (res.isEmpty() || res.equals("null")) {
                return null;
            }
            try {
                return MovieGenre.checkOf(res);
            } catch (IllegalArgumentException e) {
                stream.printErr("Введенный жанр не является одним из предложенных\n");
                stream.print("* Повторная попытка ввода\n");
            }
        }
    }
}
