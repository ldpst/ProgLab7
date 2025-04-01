package server.managers;

import server.object.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.lang.System.exit;

/**
 * Метод для загрузки и сохранения коллекции в csv файл
 *
 * @author ldpst
 */
public class CSVManager {
    private final PrintStream stream;
    private final CollectionManager collectionManager;

    private String[] header;

    public CSVManager(PrintStream stream, CollectionManager collectionManager) {
        this.stream = stream;
        this.collectionManager = collectionManager;
    }

    public void loadFromCSV() {
        String fileName = loadFileNameFromEnvironment();

        if (fileName == null) {
            return;
        }
        long maxId;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            maxId = runWithThisBuffer(br);
        } catch (IOException e) {
            stream.print("Файл с названием " + fileName + " не найден\n");
            return;
        }
        if (maxId == -1) {
            return;
        }
        collectionManager.setNextId((int) maxId + 1);
    }

    private long runWithThisBuffer(BufferedReader br) throws IOException {
        long maxId = 0;
        String line = br.readLine();
        if (line == null) {
            stream.print("В файле отсутствует строка-заголовок\n");
            return -1;
        }
        this.header = line.split(",");
        int id = find("id"),
                name = find("name"),
                coordinatesX = find("coordinates.x"),
                coordinatesY = find("coordinates.y"),
                creationDate = find("creationDate"),
                oscarCount = find("oscarsCount"),
                genre = find("genre"),
                mpaaRating = find("mpaaRating"),
                operatorName = find("operator.name"),
                operatorBirthday = find("operator.birthday"),
                operatorWeight = find("operator.weight"),
                operatorPassportID = find("operator.passportID");

        line = br.readLine();
        while (line != null) {
            String[] movieLine = line.split(",");
            DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date date = null;
            try {
                date = ((movieLine[operatorBirthday].equals("null")) ? null : dateFormat.parse(movieLine[operatorBirthday]));
            } catch (Exception e) {
                stream.print("При чтении данных из файла дата " + movieLine[operatorBirthday] + " не была полноценно распознана.\n");
            }
            Movie movie;
            try {
                movie = new Movie(Integer.parseInt(movieLine[id]),
                        movieLine[name],
                        new Coordinates(Float.parseFloat(movieLine[coordinatesX]),
                                Integer.parseInt(movieLine[coordinatesY])),
                        movieLine[creationDate],
                        Long.parseLong(movieLine[oscarCount]),
                        (movieLine[genre].equals("null")) ? null : MovieGenre.valueOf(movieLine[genre]),
                        (movieLine[mpaaRating].equals("null")) ? null : MpaaRating.valueOf(movieLine[mpaaRating]),
                        ((movieLine[operatorName].equals("null") && date == null && movieLine[operatorWeight].equals("null") && movieLine[operatorPassportID].equals("null")) ? null :
                                new Person(
                                        movieLine[operatorName],
                                        date,
                                        Long.parseLong(movieLine[operatorWeight]),
                                        movieLine[operatorPassportID]
                                )));
            } catch (NumberFormatException e) {
                stream.print("Где-то в файле " + loadFileNameFromEnvironment() + " введена неизвестная строка вместо числа c:\n");
                exit(-1);
                return -1;
            }
            collectionManager.add(movie);
            maxId = Long.max(maxId, movie.getId());
            line = br.readLine();
        }
        return maxId;
    }

    /**
     * Метод для получения имени файла из переменной окружения
     *
     * @return имя файла
     */
    private String loadFileNameFromEnvironment() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String envName = ConfigManager.getEnvName();
        String filePath = processBuilder.environment().get(envName);
        if (filePath == null) {
            stream.print("Переменная окружения с названием файла " + envName + " пуста\n");
            return null;
        }
        return filePath;
    }

    /**
     * Метод для поиска поля в строке с полями
     *
     * @param head искомое поле
     * @return позиция в строке с полями
     */
    private int find(String head) {
        for (int i = 0; i < header.length; i++) {
            if (header[i].equals(head)) {
                return i;
            }
        }
        stream.print("Не обнаружено поле " + head + "в исходном файле\n");
        return -1;
    }
}
