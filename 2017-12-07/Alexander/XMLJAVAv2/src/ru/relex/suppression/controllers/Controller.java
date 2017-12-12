package ru.relex.suppression.controllers;

import ru.relex.suppression.Registrator;
import ru.relex.suppression.interfaces.SuppressionChecker;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Controller {

    private Controller(){}

    public static void start(int amountIterations, List<SuppressionChecker> listOfClasses){
        List<Long> timeSpended = new ArrayList<>();
        for (SuppressionChecker item : listOfClasses) {
            long fullTime = 0;
            for(int i = 0; i < amountIterations; i++){
                long time = System.currentTimeMillis();
                List<String> parsePaths = item.parseSuppression("checkstyle-suppressions.xml");
                List<String> dirPaths = item.dir("files.txt");
                item.findDeletedFiles(parsePaths, dirPaths);
                fullTime += System.currentTimeMillis() - time;
            }
            timeSpended.add(fullTime);
        }
        try {
            String fileAbsoultePath = printResults(Registrator.getList(), amountIterations, timeSpended);
            Runtime.getRuntime().exec("cmd /c " + "\"" + fileAbsoultePath + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String printResults(List<SuppressionChecker> listOfClasses, int amountIterations, List<Long> timeSpended) throws IOException{
        StringBuilder tableItems = new StringBuilder();
        // Создание блока со всеми участниками
        int count = 1;
        while(listOfClasses.size() != 0){

            // Поиск программы с наименьшим временем выполнения и последующая запись его в блок
            int numberOfClass = 0;
            for(int j = 0; j < timeSpended.size(); j++)
                if(timeSpended.get(j) < timeSpended.get(numberOfClass))
                    numberOfClass = j;

            tableItems.append("<div class=\"main_item\">\n" +
                    "<div>" + count + ". " + listOfClasses.get(numberOfClass).getDeveloperName() + "</div>\n" +
                    "<div>" + amountIterations + "</div>\n" +
                    "<div>" + timeSpended.get(numberOfClass) / amountIterations + " мс</div>\n" +
                    "<div>" + timeSpended.get(numberOfClass) + " мс</div>\n" +
                    "</div>\n");
            listOfClasses.remove(numberOfClass);
            timeSpended.remove(numberOfClass);
            count++;
        }
        // Загрузка шаблона сайта и преобразование всего текста в одну строку
        List<String> lines = Files.readAllLines(Paths.get("template.txt"), StandardCharsets.UTF_8);
        StringBuilder page = new StringBuilder();
        for(String line: lines)
            page.append(line + "\n");
        File file = new File("statistic.html");
        PrintWriter output = new PrintWriter(file);

        // Форматированная запись в файл statistic.html
        output.printf(page.toString(), tableItems, new SimpleDateFormat("yyy.MM.dd - HH:mm").format(new Date()).toString());
        String fileAbsoultePath = file.getAbsolutePath();
        output.close();
        return fileAbsoultePath;
    }
}
