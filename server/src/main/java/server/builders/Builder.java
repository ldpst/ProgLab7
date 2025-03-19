package client.builders;

import client.managers.ScannerManager;
import client.managers.StreamManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.server.UDPDatagramChannel;

import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

public abstract class Builder {
    protected final UDPDatagramChannel channel;
    protected final SocketAddress clientAddress;

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
    abstract public Object build();
}
