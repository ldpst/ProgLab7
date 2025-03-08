package client.builders;

import client.managers.ScannerManager;
import client.managers.StreamManager;
import general.objects.Coordinates;

public class CoordinatesBuilder extends Builder {
    public CoordinatesBuilder(StreamManager stream, ScannerManager scanner) {
        super(stream, scanner);
    }

    @Override
    public Coordinates build() {
        return new Coordinates(
                readX(),
                readY());
    }

    /**
     * Метод для чтения координаты X
     *
     * @return Найденное число
     */
    private Float readX() {
        while (true) {
            stream.print("> Введите координату x:\n$ ");
            String res = scanner.nextLine().trim();
            try {
                return Float.parseFloat(res);
            } catch (NumberFormatException e) {
                stream.printErr("Координата x должна быть целым или вещественным числом\n");
                stream.print("* Повторная попытка ввода\n");
            }
        }

    }

    /**
     * Метод для чтения координаты Y
     *
     * @return Найденное число
     */
    private int readY() {
        while (true) {
            stream.print("> Введите координату y:\n$ ");
            String res = scanner.nextLine().trim();
            try {
                return Integer.parseInt(res);
            } catch (NumberFormatException e) {
                stream.printErr("Координата y должна быть целым числом\n");
                stream.print("* Повторная попытка ввода\n");
            }
        }
    }
}
