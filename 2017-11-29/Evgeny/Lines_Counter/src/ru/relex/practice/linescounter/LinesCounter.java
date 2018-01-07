package ru.relex.practice.linescounter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Класс по подсчету строк в файле.
 * @author Евгений Воронин
 */
public class LinesCounter {
    /**
     * Метод выводит количество строк в файле.
     * @param path путь к файлу
     */
    public void linesCount(String path) {
        int count=0;
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)))) {

            while (bufferedReader.readLine() != null){
                count++;
            }

            System.out.println("В файле " + path + "  " + count + " строк");

        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
