package ru.relex.practice.symbolscounter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Класс по подсчету символов в файле.
 * @author Евгений Воронин
 */
public class SymbolsCounter {
    /**
     * Считает символы в файле, используя указанную кодировку.
     * @param path
     */
    public void symbolsCount(String path) {
        int count = 0;
        try {
            File myFile = new File(path);
            InputStreamReader is = new InputStreamReader(new FileInputStream(myFile), "Cp866");

            while(is.read() != -1){
                    count ++;
            }

            System.out.println("В файле " + path + "  " + count + " символов");

            is.close();

        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
