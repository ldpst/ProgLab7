package server.requests;

import java.io.Serializable;
import java.net.SocketAddress;

public class Request implements Serializable {
    private final String message;
    private final SocketAddress clientAddress;

    public Request(String message) {
        this(message, null);
    }

    public Request(String message, SocketAddress clientAddress) {
        this.message = message;
        this.clientAddress = clientAddress;
    }

    public String getMessage() {
        return message;
    }

    public SocketAddress getClientAddress() {
        return clientAddress;
    }

    public String toString() {
        return "Request{message=\"" + message + "\", clientAddress=\"" + clientAddress + "\"}";
    }
}
