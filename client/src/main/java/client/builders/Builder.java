package client.builders;

import client.managers.ScannerManager;
import client.managers.StreamManager;

public abstract class Builder {
    protected StreamManager stream;
    protected ScannerManager scanner;

    public Builder(StreamManager stream, ScannerManager scanner) {
        this.stream = stream;
        this.scanner = scanner;
    }

    /**
     * Метод для заполнения объекта данными, используя стандартный ввод
     *
     * @return Заполненный объект
     */
    abstract public Object build();
}
