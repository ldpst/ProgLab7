package server.builders;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.Logger;
import server.requests.Request;
import server.server.UDPDatagramChannel;
import server.utils.Pair;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.function.Function;

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
    abstract public Object build() throws IOException;
}
