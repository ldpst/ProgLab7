package general.objects;

import server.utils.Validatable;
import server.utils.ValidationError;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Класс фильма
 *
 * @author ldpst
 */
public class Movie
        implements Comparable<Movie>, Validatable, Serializable {

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long oscarsCount; //Значение поля должно быть больше 0, Поле не может быть null
    private MovieGenre genre; //Поле может быть null
    private MpaaRating mpaaRating; //Поле может быть null
    private Person operator; //Поле может быть null

    public Movie(String name, Coordinates coordinates, Long oscarsCount, MovieGenre genre, MpaaRating mpaaRating, Person operator) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = ZonedDateTime.now();
        this.oscarsCount = oscarsCount;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.operator = operator;
        if (!isValid()) {
            throw new ValidationError("Movie");
        }
    }

    public Movie(Integer id, String name, Coordinates coordinates, String creationDate, Long oscarsCount, MovieGenre genre, MpaaRating mpaaRating, Person operator) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = ZonedDateTime.parse(creationDate);
        this.oscarsCount = oscarsCount;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.operator = operator;
        if (!isValid()) {
            throw new ValidationError("Movie");
        }
    }

    /**
     * Заполняет поля текущего объекта данными из другого объекта
     *
     * @param movie другой объект
     */
    public void update(Movie movie) {
        this.name = movie.getName();
        this.coordinates = movie.getCoordinates();
        this.creationDate = movie.getCreationDate();
        this.oscarsCount = movie.getOscarsCount();
        this.genre = movie.getGenre();
        this.mpaaRating = movie.getMpaaRating();
        this.operator = movie.getOperator();
    }

    /**
     * Метод для сравнения с другим объектом Movie
     *
     * @param other объект типа Movie для сравнения
     * @return Результат меньше нуля, если other больше данного объекта. Результат равен нулю, если элементы равны. Результат больше нуля, если данный объект больше other
     */
    @Override
    public int compareTo(Movie other) {
        return Coordinates.compare(getCoordinates(), other.getCoordinates());
    }

    /**
     * Метод для сравнения двух Movie
     *
     * @param x объект 1
     * @param y объект 2
     * @return Результат меньше нуля, если y больше данного x. Результат равен нулю, если элементы равны. Результат больше нуля, если данный x больше y
     */
    public static int compare(Movie x, Movie y) {
        return Coordinates.compare(x.getCoordinates(), y.getCoordinates());
    }

    /**
     * Метод, возвращающий поле id
     *
     * @return id
     */
    public long getId() {
        return this.id;
    }

    public void setId(int newId) {
        id = newId;
    }

    /**
     * Метод проверки валидности полей класса
     *
     * @return результат проверки
     */
    @Override
    public boolean isValid() {
        if (this.name == null || this.name.isEmpty()) return false;
        if (this.coordinates == null) return false;
        if (this.creationDate == null) return false;
        return this.oscarsCount > 0;
    }

    /**
     * Метод, возвращающий поле name
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Метод, возвращающий поле coordinates
     *
     * @return coordinates
     */
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    /**
     * Метод, возвращающий поле creationDate
     *
     * @return creationDate
     */
    public ZonedDateTime getCreationDate() {
        return this.creationDate;
    }

    /**
     * Метод, возвращающий поле oscarsCount
     *
     * @return oscarsCount
     */
    public Long getOscarsCount() {
        return this.oscarsCount;
    }

    /**
     * Метод, возвращающий поле genre
     *
     * @return genre
     */
    public MovieGenre getGenre() {
        return this.genre;
    }

    /**
     * Метод, возвращающий поле mpaaRating
     *
     * @return mpaaRating
     */
    public MpaaRating getMpaaRating() {
        return this.mpaaRating;
    }

    /**
     * Метод, возвращающий поле operator
     *
     * @return operator
     */
    public Person getOperator() {
        return this.operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return (this.id == movie.getId() &&
                Objects.equals(this.name, movie.getName()) &&
                Objects.equals(this.coordinates, movie.getCoordinates()) &&
                Objects.equals(this.creationDate, movie.getCreationDate()) &&
                Objects.equals(this.oscarsCount, getOscarsCount()) &&
                Objects.equals(this.genre, getGenre()) &&
                Objects.equals(this.mpaaRating, getMpaaRating()) &&
                Objects.equals(this.operator, movie.getOperator()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.coordinates, this.creationDate, this.oscarsCount, this.genre, this.mpaaRating, this.operator);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id + ", " +
                "name='" + name + "', " +
                "Coordinates=" + coordinates + ", " +
                "creationDate='" + creationDate.toString() + "', " +
                "oscarsCount=" + oscarsCount + ", " +
                "genre=" + ((genre != null) ? ("'" + this.genre + "'") : "null") + ", " +
                "mpaaRating=" + ((mpaaRating != null) ? ("'" + this.mpaaRating + "'") : "null") + ", " +
                "operator=" + ((operator != null) ? (operator) : "null") +
                "}";
    }
}