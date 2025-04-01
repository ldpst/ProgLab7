package server.builders;

import org.apache.logging.log4j.Logger;
import server.managers.ConfigManager;
import server.server.UDPDatagramChannel;
import server.utils.TextColors;

import java.io.IOException;
import java.net.SocketAddress;

public abstract class Builder {
    protected final UDPDatagramChannel channel;
    protected final SocketAddress clientAddress;

    protected static final String RED = ConfigManager.getColor(TextColors.RED);
    protected static final String RESET = ConfigManager.getColor(TextColors.RESET);
    protected static final String GREEN = ConfigManager.getColor(TextColors.GREEN);

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
