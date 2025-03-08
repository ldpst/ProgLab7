package client.managers;

import client.commands.Exit;
import client.utils.InputFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Класс для единообразного использования потоков
 *
 * @author ldpst
 */
public class ScannerManager {
    private final BufferedReader reader;

    public ScannerManager(InputStreamReader inputStreamReader) {
        reader = new BufferedReader(inputStreamReader);
    }

    /**
     * Метод для считывания новой строки
     *
     * @return считанная строка
     */
    public String nextLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}
