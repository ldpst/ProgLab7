package server.requests;

import java.io.Serializable;
import java.net.SocketAddress;

public class Request implements Serializable {
    private final String message;
    private final SocketAddress clientAddress;
    private final Object data;

    public Request(String message) {
        this(message, null, null);
    }
    public Request(String message, SocketAddress clientAddress) {
        this(message, clientAddress, null);
    }

    public Request(String message, SocketAddress clientAddress, Object data) {
        this.message = message;
        this.clientAddress = clientAddress;
        this.data = data;
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

    public Object getData() {
        return data;
    }
}
