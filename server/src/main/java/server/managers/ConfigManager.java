package server.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private final static Logger logger = LogManager.getLogger(ConfigManager.class);

    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /**
     * Возвращает ip адрес сервера
     *
     * @return адрес
     */
    public static String getAddress() {

        String address = properties.getProperty("server.ip");

        if (address == null || address.isBlank()) {
            logger.warn("Не удалось получить IP-адрес сервера. Значение пустое или отсутствует.");
        }

        return address;
    }

    /**
     * Возвращает порт
     *
     * @return порт
     */
    public static Integer getPort() {
        Integer port = null;
        try {
            port = Integer.parseInt(properties.getProperty("server.port"));
        } catch (NumberFormatException e) {
            logger.warn("Не удалось получить порт сервера. Поданное значение не является числом.");
        }
        if (port == null) {
            logger.warn("Не удалось получить порт сервера. Значение пустое или отсутствует.");
        }
        return port;
    }

    /**
     * Возвращает размер пакета
     *
     * @return размер пакета
     */
    public static int getPacketSize() {
        Integer size = null;
        try {
            size = Integer.parseInt(properties.getProperty("packet.size"));
        } catch (NumberFormatException e) {
            logger.warn("Не удалось получить размер пакета. Поданное значение не является числом.");
        }
        if (size == null) {
            logger.warn("Не удалось получить размер пакета. Значение пустое или отсутствует.");
        }
        return size;
    }

    public static int getAttemptMax() {
        Integer attemptMax = null;
        try {
            attemptMax = Integer.parseInt(properties.getProperty("attempt.max"));
        } catch (NumberFormatException e) {
            logger.warn("Не удалось получить максимум попыток. Поданное значение не является числом.");
        }
        if (attemptMax == null) {
            logger.warn("Не удалось получить максимум попыток. Значение пустое или отсутствует.");
        }
        return attemptMax;
    }
}
