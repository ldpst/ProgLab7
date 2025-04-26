package server.managers;

import server.object.Movie;
import server.object.MovieGenre;
import server.object.Person;
import server.utils.TextColors;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * Класс для хранения и управления коллекцией
 *
 * @author ldpst
 */
public class CollectionManager {
    private LinkedBlockingDeque<Movie> movies = new LinkedBlockingDeque<>();
    private int nextId = 1;

    protected static final String RED = ConfigManager.getColor(TextColors.RED);
    protected static final String RESET = ConfigManager.getColor(TextColors.RESET);
    protected static final String GREEN = ConfigManager.getColor(TextColors.GREEN);

    public CollectionManager() {

    }

    /**
     * Метод для добавления movie в файл
     *
     * @param movie элемент для добавления
     */
    public void add(Movie movie) {
        movies.addLast(movie);
        nextId++;
    }

    /**
     * Метод для очищения коллекции
     */
    public void clear() {
        movies = new LinkedBlockingDeque<>();
        nextId = 1;
    }

    /**
     * Обновляет значение по айди
     *
     * @param id       айди
     * @param newMovie новое значение
     */
    public void updateById(int id, Movie newMovie) {
        movies = movies.stream().map(movie -> (movie.getId() == id ? newMovie : movie)).collect(Collectors.toCollection(LinkedBlockingDeque::new));
    }

    public boolean checkIfIdExists(int id) {
        LinkedBlockingDeque<Movie> checker = movies.stream().filter(movie -> movie.getId() == id).collect(Collectors.toCollection(LinkedBlockingDeque::new));
        return !checker.isEmpty();
    }

    /**
     * Удаляет значение по айди
     *
     * @param id айди
     * @return Возможная ошибка
     */
    public String removeById(int id) {
        LinkedBlockingDeque<Movie> checker = movies.stream().filter(movie -> movie.getId() == id).collect(Collectors.toCollection(LinkedBlockingDeque::new));
        if (checker.isEmpty()) {
            return RED + "Элемента с данным id не существует\n" + RESET;
        }
        movies = movies.stream().filter(movie -> movie.getId() != id).collect(Collectors.toCollection(LinkedBlockingDeque::new));
        return GREEN + "Элемент с id " + id + " успешно удален\n" + RESET;
    }

    /**
     * Возвращает первый элемент в коллекции
     *
     * @return первый элемент
     */
    public Movie getHead() {
        if (isEmpty()) {
            return null;
        }
        return movies.getFirst();
    }

    /**
     * Добавляет элемент в коллекцию, если он максимален
     *
     * @param newMovie добавляемый элемент
     * @return Возможная ошибка
     */
    public String addIfMax(Movie newMovie) {
        newMovie.setId(nextId);
        LinkedBlockingDeque<Movie> checker = movies.stream().filter(movie -> movie.compareTo(newMovie) > 0).collect(Collectors.toCollection(LinkedBlockingDeque::new));
        if (checker.isEmpty()) {
            add(newMovie);
            return GREEN + "Элемент успешно добавлен\n" + RESET;
        }
        return "Элемент не был добавлен\n";
    }

    /**
     * Возвращает максимальный элемент по полю оператор
     *
     * @return максимальный элемент по полю оператор
     */
    public Movie getMaxByOperator() {
        if (movies.isEmpty()) return null;
        Movie max = movies.getFirst();
        for (Movie movie : movies) {
            if (max.getOperator() == null || (movie.getOperator() != null && max.getOperator().compareTo(movie.getOperator()) > 0)) {
                max = movie;
            }
        }
        return max;
    }

    /**
     * Удалить все элементы превышающие данный и вернуть их количество
     *
     * @return количество удаленных элементов
     */
    public int removeGreater(Movie greater) {
        if (greater == null) return 0;
        int count = movies.stream().filter(movie -> movie.compareTo(greater) > 0).collect(Collectors.toCollection(LinkedBlockingDeque::new)).size();
        movies = movies.stream().filter(movie -> movie.compareTo(greater) < 0).collect(Collectors.toCollection(LinkedBlockingDeque::new));
        return count;
    }

    /**
     * Возвращает количество элементов с полем operator равным данному
     *
     * @param operator значение для проверки
     * @return количество
     */
    public int countByOperator(Person operator) {
        return (movies.stream().filter(movie -> ((movie.getOperator() == null && operator == null) || (movie.getOperator() != null && movie.getOperator().equals(operator)))).collect(Collectors.toCollection(LinkedBlockingDeque::new))).size();
    }

    /**
     * Возвращает количество элементов с полем genre меньшим данного
     *
     * @param genre значение для проверки
     * @return количество
     */
    public int countLessThanGenre(MovieGenre genre) {
        return (movies.stream().filter(movie -> ((movie.getGenre() == null && genre == null) || (movie.getGenre() != null && genre != null && movie.getGenre().compareTo(genre) < 0))).collect(Collectors.toCollection(LinkedBlockingDeque::new))).size();
    }

    /**
     * Метод, проверяющий пуста ли коллекция
     *
     * @return результат проверки
     */
    public boolean isEmpty() {
        return movies.isEmpty();
    }

    /**
     * Метод, возвращающий содержимое коллекции
     *
     * @return LinkedBlockingDeque[Movie]
     */
    public LinkedBlockingDeque<Movie> getMovies() {
        return movies;
    }

    public int getAndIncreaseNextID() {
        return nextId++;
    }

    public void setNextId(int id) {
        nextId = id;
    }
}
