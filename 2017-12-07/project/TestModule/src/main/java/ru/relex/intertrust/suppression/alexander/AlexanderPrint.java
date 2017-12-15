package ru.relex.intertrust.suppression.alexander;

import ru.relex.intertrust.suppression.Result;
import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.interfaces.ListPrinter;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlexanderPrint implements ListPrinter {
    /**
     * В цикле в порядке возрастания создаем блоки html кода для каждой программы, после этого берем шаблон template.txt,
     * В который вставляем эти блоки и сохраняем все в statistic.html
     */
    public void visualize(List<Result> listOfResults){
        StringBuilder tableItems = new StringBuilder();
        List<String> developerNames = new ArrayList<>();
        List<Long> timeSpended = new ArrayList<>();
        List<List<String>> notFoundFiles = new ArrayList<>();
        for(Result item: listOfResults) {
            timeSpended.add(item.getDirTime() + item.getFindTime() + item.getParseTime());
            developerNames.add(item.getDeveloperName());
            notFoundFiles.add(item.getFileList());
        }
        // Создание блока со всеми участниками
        int count = 1;
        while(developerNames.size() != 0){

            // Поиск программы с наименьшим временем выполнения и последующая запись его в блок
            int numberOfClass = 0;
            for(int j = 0; j < timeSpended.size(); j++)
                if(timeSpended.get(j) < timeSpended.get(numberOfClass))
                    numberOfClass = j;

            tableItems.append("<div class=\"main_row\"> \n" +
                    "<div class=\"main_item\">\n" +
                    "<div>" + count + ". " + developerNames.get(numberOfClass) + "</div>\n" +
                    "<div>" + 1 + "</div>\n" +
                    "<div>" + timeSpended.get(numberOfClass) + " мс</div>\n" +
                    "<div>" + timeSpended.get(numberOfClass) + " мс</div>\n" +
                    "</div>\n" +
                    "<div class=\"main_item_info\">\n" +
                    "<ul>\n");
            for(String info: notFoundFiles.get(numberOfClass))
                tableItems.append("<li>" + info + "</li>\n");
            tableItems.append("</ul>\n" + "</div>\n" + "</div>\n");
            developerNames.remove(numberOfClass);
            timeSpended.remove(numberOfClass);
            notFoundFiles.remove(numberOfClass);
            count++;
        }
        // Загрузка шаблона сайта и преобразование всего текста в одну строку
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("template.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder page = new StringBuilder();
        for(String line: lines)
            page.append(line + "\n");
        File file = new File("statistic.html");
        PrintWriter output = null;
        try {
            output = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Форматированная запись в файл statistic.html
        output.printf(page.toString(), tableItems, new SimpleDateFormat("yyy.MM.dd - HH:mm").format(new Date()).toString());
        output.close();
    }
}
