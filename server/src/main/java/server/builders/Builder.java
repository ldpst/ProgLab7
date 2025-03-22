package server.builders;

import org.apache.logging.log4j.Logger;
import server.server.UDPDatagramChannel;

import java.io.IOException;
import java.net.SocketAddress;

public abstract class Builder {
    protected final UDPDatagramChannel channel;
    protected final SocketAddress clientAddress;

    protected static final String RED = "\u001B[31m";
    protected static final String RESET = "\u001B[0m";
    protected static final String GREEN = "\u001B[32m";

    protected final Logger logger;

    public Builder(UDPDatagramChannel channel, SocketAddress clientAddress, Logger logger) {
        this.channel = channel;
        this.clientAddress = clientAddress;
        this.logger = logger;
    }

    /**
     * Метод для заполнения объекта данными, используя стандартный ввод
     *
     * @return Заполненный объект
     */
    abstract public Object build() throws IOException;
}
