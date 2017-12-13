package ru.relex.practice.linescounter;

import java.io.IOException;

/**
 * Основной класс, в котором происходит вызов метода по посчету строк в файлах.
 * @author Евгений Воронин
 */
public class Main {
    public static void main(String[] args) throws IOException {
        LinesCounter linesCounter = new LinesCounter();
        for (int i = 0; i < args.length; i++) {
            linesCounter.linesCount(args[i]);
        }
    }
}
