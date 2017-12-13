package ru.relex.intertrust.suppression.alexander;

import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

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

public class AlexanderCtrl implements Controller {
    private final static int ITERATIONS = 2;

    public void start(String suppressionFilename, String dir, List<SuppressionChecker> listOfChekers){
        List<Long> timeSpended = new ArrayList<>();
        for (SuppressionChecker item : listOfChekers) {
            long fullTime = 0;
            for(int i = 0; i < ITERATIONS; i++){
                long time = System.currentTimeMillis();
                List<String> parsePaths = item.parseSuppression(suppressionFilename);
                List<String> dirPaths = item.dir(dir);
                item.findDeletedFiles(parsePaths, dirPaths);
                fullTime += System.currentTimeMillis() - time;
            }
            timeSpended.add(fullTime);
        }
        try {
            String fileAbsoultePath = printResults(listOfChekers, timeSpended);
            Runtime.getRuntime().exec("cmd /c " + "\"" + fileAbsoultePath + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String printResults(List<SuppressionChecker> listOfCheckers, List<Long> timeSpended) throws IOException{
        StringBuilder tableItems = new StringBuilder();
        // Создание блока со всеми участниками
        int count = 1;
        for(int i =0; i < listOfCheckers.size(); i++){
            tableItems.append("<div class=\"main_item\">\n" +
                    "<div>" + count + ". " + listOfCheckers.get(i).getDeveloperName() + "</div>\n" +
                    "<div>" + ITERATIONS + "</div>\n" +
                    "<div>" + timeSpended.get(i) / ITERATIONS + " мс</div>\n" +
                    "<div>" + timeSpended.get(i) + " мс</div>\n" +
                    "</div>\n");
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
