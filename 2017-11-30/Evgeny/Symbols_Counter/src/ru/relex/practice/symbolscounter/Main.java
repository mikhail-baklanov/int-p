package ru.relex.practice.symbolscounter;

import java.io.IOException;

/**
 * Основной класс, в котором происходит вызов метода по посчету символов в файлах.
 * @author Евгений Воронин
 */
public class Main {
    public static void main(String[] args) throws IOException {
        SymbolsCounter symbolsCounter = new SymbolsCounter();
        for (int i = 0; i < args.length; i++) {
            symbolsCounter.symbolsCount(args[i]);
        }
    }
}
