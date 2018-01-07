package ru.relex.practice.symbolscounter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Класс по подсчету символов в файле.
 * @author Евгений Воронин
 */
public class SymbolsCounter {
    /**
     * Метод считает символы в файле, используя указанную кодировку, и выводит количество символов.
     * @param path путь к файлу
     */
    public void symbolsCount(String path) {
        int count = 0;
        try(InputStreamReader is = new InputStreamReader(new FileInputStream(new File(path)), "Cp866")) {

            while(is.read() != -1){
                    count ++;
            }

            System.out.println("В файле " + path + "  " + count + " символов");

        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
