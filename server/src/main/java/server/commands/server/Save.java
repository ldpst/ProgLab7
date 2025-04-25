package server.commands.server;

import server.object.Movie;
import server.managers.CollectionManager;
import server.managers.ConfigManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;

/**
 * Класс команды save
 *
 * @author ldpst
 */
public class Save extends Command {
    private final CollectionManager collectionManager;
    private final PrintStream stream;

    public Save(CollectionManager collectionManager, PrintStream stream) {
        super("save", "сохранить коллекцию в файл");
        this.collectionManager = collectionManager;
        this.stream = stream;
    }

    @Override
    public void run(String[] args) {
        String header = countFields(Movie.class, "");
        String[] fieldNames = header.split(",");
        ProcessBuilder processBuilder = new ProcessBuilder();
        String filePath = processBuilder.environment().get(ConfigManager.getEnvName());
        if (filePath == null) {
            stream.print("Переменная окружения с названием файла " + ConfigManager.getEnvName() + " пуста\n");
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write((header + "\n").getBytes());
            for (Movie movie : collectionManager.getMovies()) {
                String csvLine = makeCsvLineFromObject(movie, fieldNames);
                fos.write((csvLine + "\n").getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stream.print("Коллекция успешно сохранена в файл " + filePath + "\n");
    }

    /**
     * Метод для создания строки
     *
     * @param object     объект, значения полей которого будут расставлены по формату
     * @param fieldNames названия полей
     * @return строка в формате csv
     */
    private String makeCsvLineFromObject(Object object, String[] fieldNames) {
        LinkedHashMap<String, Object> fieldValues = loadFieldsFromObject(object, new LinkedHashMap<>(), "");
        StringBuilder res = new StringBuilder();
        for (String fieldName : fieldNames) {
            Object value = fieldValues.get(fieldName);
            if (value == null) {
                res.append("null,");
            } else {
                res.append(value).append(",");
            }
        }
        if (!res.isEmpty()) {
            return res.substring(0, res.length() - 1);
        }
        return res.toString();
    }

    /**
     * Метод, рекурсивно достающий из объекта значения всех его полей и подклассов
     *
     * @param object Обрабатываемый объект
     * @param res    Словарь, в котором будет результат обработки
     * @param prefix Строка для создания полного имени поля
     * @return Словарь формата [полное имя поля, содержимое]
     */
    public LinkedHashMap<String, Object> loadFieldsFromObject(Object object, LinkedHashMap<String, Object> res, String prefix) {
        if (object == null) {
            return res;
        }

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if (value == null) {
                    continue;
                }
                String fieldName = prefix + field.getName();
                if (!field.getType().isPrimitive() && !field.getType().getName().startsWith("java.") && !field.getType().isEnum()) {
                    loadFieldsFromObject(value, res, fieldName + ".");
                } else {
                    res.put(fieldName, value);
                }
            } catch (IllegalAccessException e) {
                //
            }
        }
        return res;
    }

    /**
     * Метод, рекурсивно достающий названия полей
     *
     * @param clazz  Обрабатываемый класс
     * @param prefix Строка для создания полного имени поля
     * @return Строка csv формата
     */
    private String countFields(Class<?> clazz, String prefix) {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder res = new StringBuilder();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Class<?> type = field.getType();
                if (!field.getType().isPrimitive() && !field.getType().getName().startsWith("java.") && !field.getType().isEnum()) {
                    res.append(countFields(type, prefix + field.getName() + "."));
                } else {
                    res.append(prefix).append(field.getName()).append(",");
                }
            } catch (IllegalAccessError e) {
                //
            }
        }
        if (prefix.isEmpty() && !res.isEmpty()) {
            return res.substring(0, res.length() - 1);
        }
        return res.toString();
    }
}
