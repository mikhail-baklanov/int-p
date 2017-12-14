package ru.relex.intertrust.suppression.sergei;

import ru.relex.intertrust.suppression.Registrator;
import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import java.util.List;

public class ControllerSergey implements Controller {

    final String FILENAME = "CtrlSergey_results.txt";
    final int ITERATIONS = 1;
    File file;
    FileWriter results;

    /**
     * Поочередный запуск реализаций из списка и вывод результатов
     * @param suppressionFilename Полное имя файла suppressions.xml
     * @param dir Каталог, откуда будет начат поиск java-файлов
     * @param listOfChekers Зарегистрированные реализации интерфейсов каждого разработчика
     */
    public void start(String suppressionFilename, String dir, List<SuppressionChecker> listOfChekers) {
        file = new File(FILENAME);
        System.out.println();
        System.out.println("# Control_Sergey is working...");
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            results = new FileWriter(file, false);

            results.write("#------------------------------------- Результаты тестирования -------------------------------------#\r\n");

            //запуск и прочее

            List<String> suppression = null;
            List<String> dirs = null;
            List<String> res = null;
            for (SuppressionChecker checker: listOfChekers) {
                System.out.println("<> Testing " + checker.getDeveloperName());
                long start = System.currentTimeMillis();
                for (int i = 0; i < ITERATIONS; i++) {
                    suppression = checker.parseSuppression(suppressionFilename);
                    if (suppression == null) {
                        continue;
                    }
                    dirs = checker.dir(dir);
                    if (dirs == null) {
                        continue;
                    }
                    res = checker.findDeletedFiles(suppression, dirs);
                }
                long end = System.currentTimeMillis() - start;
                long avgTime = end/ITERATIONS;

                results.write("# Автор реализации: " + checker.getDeveloperName() + " #\r\n");
                results.write("# Количество итераций: " + ITERATIONS + " | Общее время работы реализаци: ");
                results.write(end + " ms | Среднее время работы реализаци: " + avgTime + " ms\r\n");
                results.write("-------------------------------------------- Результаты: --------------------------------------------\r\n");

                for (String line: res) {
                    results.write("<> " + line + "\r\n");
                }
                results.write("-----------------------------------------------------------------------------------------------------\r\n\r\n");
            }
            results.write("#---------------------------------------- Конец тестирования ---------------------------------------#\r\n");
            results.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println("# Some errors was happened");
            System.out.println();
        }
        System.out.println("# Control_Sergey successfully was close");
        System.out.println();
    }
}

