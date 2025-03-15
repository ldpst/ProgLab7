package client;

import client.client.UDPClient;
import client.managers.RunManager;
import client.managers.ScannerManager;
import client.utils.InputFormat;
import org.jline.reader.LineReaderBuilder;


public class Main {
    public static void main(String[] args) {
        try {
            UDPClient client = new UDPClient();
            RunManager runManager = new RunManager(client, new ScannerManager(LineReaderBuilder.builder().build()), InputFormat.CONSOLE);
            runManager.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
