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
     * Считает строки в файле
     * @param path
     */
    public void linesCount(String path) {
        int count=0;
        try
        {
            File myFile = new File(path);
            FileReader fileReader = new FileReader(myFile);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader);

            while (lineNumberReader.readLine() != null){
                count++;
            }

            System.out.println("В файле " + path + "  " + count + " строк");

            if(fileReader!=null)
                fileReader.close();
            lineNumberReader.close();
        } catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
