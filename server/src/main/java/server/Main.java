package server;

import server.managers.RunManager;
import server.server.UDPDatagramServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new RunManager().run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
