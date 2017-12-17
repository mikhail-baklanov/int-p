package ru.relex.intertrust.suppression.sergei;

import ru.relex.intertrust.suppression.Result;
import ru.relex.intertrust.suppression.interfaces.ListPrinter;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class PrintAll implements ListPrinter {

    final String FILENAME = "output\\SergeyOut.txt";
    final String FOLDERNAME = "output";
    File file;
    FileWriter results;

    /**
     * Метод, который выводит в файл список результатов. Выводит поочередно имя разработчика,
     * время работы отдельных методов и список резултатов работы реализаций
     * @param list готовый список с результатами работы реализаций
     */
    public void visualize(List<Result> list) {
        File folder = new File( FOLDERNAME);
        file = new File(FILENAME);
        //System.out.println("Начинаю печатать результаты...");
        try {
            if (!folder.exists()){
                folder.mkdirs();
            }
            if (!file.exists()){
                file.createNewFile();
            }
            results = new FileWriter(file, false);

            results.write("#------------------------------------- Результаты тестирования -------------------------------------#\r\n");
            for (Result res : list) {
                results.write("Автор реализации: " + res.getDeveloperName() + "\r\n");
                results.write("Время работы parseSuppression:" + res.getParseTime() + " ms" + "\r\n");
                results.write("Время работы dir:" + res.getDirTime() + " ms" + "\r\n");
                results.write("Время работы findDeletedFiles:" + res.getFindTime() + " ms" + "\r\n");
                results.write("-------------------------------------------- Результаты: --------------------------------------------\r\n");
                for (String line : res.getFileList()) {
                    results.write("<> " + line + "\r\n");
                }
                results.write("-----------------------------------------------------------------------------------------------------\r\n");
            }
            results.write("#---------------------------------------- Конец тестирования ---------------------------------------#");
        } catch (Exception ex){
            System.out.println("(Печатающий модуль Сергея) Произошла ошибка при выводе данных(");
        }
        //System.out.println("Вывод данных успешно завершен");
    }
}

