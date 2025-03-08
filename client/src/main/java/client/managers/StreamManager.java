package client.managers;

import client.utils.InputFormat;

import java.io.PrintStream;

/**
 * Класс кастомного потока вывода
 *
 * @author ldpst
 */
public class StreamManager {
    private final PrintStream stream;

    private final InputFormat inputFormat;

    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";

    public StreamManager(PrintStream stream, InputFormat inputFormat) {
        this.stream = stream;
        this.inputFormat = inputFormat;
    }

    /**
     * Метод для вывода сообщения красным цветом
     *
     * @param msg сообщение
     */
    public void printErr(String msg) {
        stream.print(RED + msg + RESET);
    }

    /**
     * Метод для вывода сообщения
     *
     * @param msg сообщение
     */
    public void print(String msg) {
        if (inputFormat == InputFormat.CONSOLE) {
            stream.print(msg);
        }
    }

    /**
     * Метод для вывода сообщения зеленым цветом
     *
     * @param msg сообщение
     */
    public void printSuccess(String msg) {
        print(GREEN + msg + RESET);
    }

    /**
     * Метод для вывода сообщения с аргументами
     *
     * @param format сообщение
     * @param args   аргументы, заменяемые на %s
     */
    public void printf(String format, Object... args) {
        if (inputFormat == InputFormat.CONSOLE) {
            stream.printf(format, args);
        }
    }

    /**
     * Метод для вывода сообщения с переносом строки
     *
     * @param msg сообщение
     */
    public void println(String msg) {
        print(msg + "\n");
    }

    /**
     * Метод для вывода сообщения с аргументами зеленым цветом
     *
     * @param format сообщение
     * @param args   аргументы, заменяемые на %s
     */
    public void printSuccessf(String format, Object... args) {
        printf(GREEN + format + RESET, args);
    }
}
