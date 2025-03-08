package client;

import client.client.UDPClient;
import client.managers.RunManager;
import client.managers.ScannerManager;
import client.utils.InputFormat;

import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        try {
            UDPClient client = new UDPClient();
            RunManager runManager = new RunManager(client, new ScannerManager(new InputStreamReader(System.in)), InputFormat.CONSOLE);
            runManager.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
