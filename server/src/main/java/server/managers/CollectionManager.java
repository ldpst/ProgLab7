package server.managers;

import general.objects.Movie;
import general.objects.MovieGenre;
import general.objects.Person;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Класс для хранения и управления коллекцией
 *
 * @author ldpst
 */
public class CollectionManager {
    private Deque<Movie> movies = new ArrayDeque<>();
    private final java.time.ZonedDateTime creationTime;
    private int nextId = 1;

    public CollectionManager() {
        creationTime = java.time.ZonedDateTime.now();
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
     * Метод для получения типа коллекции
     *
     * @return тип коллекции
     */
    public Class<?> getType() {
        return movies.getClass();
    }

    /**
     * Метод для очищения коллекции
     */
    public void clear() {
        movies = new ArrayDeque<>();
        nextId = 1;
    }

    /**
     * Метод для поиска элемента по id
     *
     * @param id айди
     * @return Movie
     */
    public Movie findElemById(long id) {
        for (Movie movie : movies) {
            if (movie.getId() == id) {
                return movie;
            }
        }
        return null;
    }

    /**
     * Обновляет значение по айди
     *
     * @param id       айди
     * @param newMovie новое значение
     * @return Возможная ошибка
     */
    public String updateById(int id, Movie newMovie) {
        Deque<Movie> checker = movies.stream().filter(movie -> movie.getId() == id).collect(Collectors.toCollection(ArrayDeque::new));
        if (checker.isEmpty()) {
            return "Элемента с данным id не существует";
        }
        movies = movies.stream().map(movie -> (movie.getId() == id ? newMovie : movie)).collect(Collectors.toCollection(ArrayDeque::new));
        return "";
    }

    /**
     * Удаляет значение по айди
     *
     * @param id айди
     * @return Возможная ошибка
     */
    public String removeById(int id) {
        Deque<Movie> checker = movies.stream().filter(movie -> movie.getId() == id).collect(Collectors.toCollection(ArrayDeque::new));
        if (checker.isEmpty()) {
            return "Элемента с данным id не существует";
        }
        movies = movies.stream().filter(movie -> movie.getId() != id).collect(Collectors.toCollection(ArrayDeque::new));
        return "";
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
        Deque<Movie> checker = movies.stream().filter(movie -> movie.compareTo(newMovie) > 0).collect(Collectors.toCollection(ArrayDeque::new));
        if (checker.isEmpty()) {
            add(newMovie);
            return "";
        }
        return "Элемент не был добавлен";
    }

    /**
     * Возвращает последний элемент в коллекции
     *
     * @return первый элемент
     */
    public Movie getTail() {
        if (isEmpty()) {
            return null;
        }
        return movies.getLast();
    }

    /**
     * Возвращает максимальный элемент коллекции
     *
     * @return максимальный элемент
     */
    public Movie getMax() {
        Movie max = movies.getFirst();
        for (Movie movie : movies) {
            if (max.compareTo(movie) < 0) {
                max = movie;
            }
        }
        return max;
    }

    /**
     * Возвращает минимальный элемент коллекции
     *
     * @return максимальный элемент
     */
    public Movie getMin() {
        Movie min = movies.getFirst();
        for (Movie movie : movies) {
            if (min.compareTo(movie) > 0) {
                min = movie;
            }
        }
        return min;
    }

    /**
     * Возвращает максимальный элемент по полю оператор
     *
     * @return максимальный элемент по полю оператор
     */
    public Movie getMaxByOperator() {
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
        int count = movies.stream().filter(movie -> movie.compareTo(greater) > 0).collect(Collectors.toCollection(ArrayDeque::new)).size();
        movies = movies.stream().filter(movie -> movie.compareTo(greater) < 0).collect(Collectors.toCollection(ArrayDeque::new));
        return count;
    }

    /**
     * Возвращает количество элементов с полем operator равным данному
     *
     * @param operator значение для проверки
     * @return количество
     */
    public int countByOperator(Person operator) {
        int count = 0;
        for (Movie movie : movies) {
            if (movie.getOperator() != null && movie.getOperator().equals(operator) || operator == null && movie.getOperator() == null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Возвращает количество элементов с полем genre меньшим данного
     *
     * @param genre значение для проверки
     * @return количество
     */
    public int countLessTanGenre(MovieGenre genre) {
        int count = 0;
        for (Movie movie : movies) {
            if (movie.getGenre() != null && genre != null && movie.getGenre().compareTo(genre) > 0 || movie.getGenre() == null && genre == null) {
                count++;
            }
        }
        return count;
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
     * Метод для удаления элемента
     *
     * @param movie элемент для удаления
     */
    public void remove(Movie movie) {
        movies.remove(movie);
    }

    /**
     * Метод, возвращающий размер коллекции
     *
     * @return int
     */
    public int getSize() {
        return movies.size();
    }

    /**
     * Метод, возвращающий значение поля creationTime
     *
     * @return ZonedDateTime
     */
    public java.time.ZonedDateTime getCreationTime() {
        return creationTime;
    }

    /**
     * Метод, возвращающий содержимое коллекции
     *
     * @return Deque[Movie]
     */
    public Deque<Movie> getMovies() {
        return movies;
    }

    public int getAndIncreaseNextID() {
        return nextId++;
    }

    public void setNextId(int id) {
        nextId = id;
    }
}
