package ru.relex.intertrust.suppression.alexander;

import ru.relex.intertrust.suppression.Result;
import ru.relex.intertrust.suppression.interfaces.ListPrinter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class AlexanderPrint implements ListPrinter {
    /**
     * Метод, который в цикле в порядке возрастания создает блоки html кода для каждой программы, после этого берет шаблон template.txt,
     * В который вставляет эти блоки и сохраняет все в statistic.html.
     * @param listOfResults - список всех результатов выполнения программ
     */
    public void visualize(List<Result> listOfResults){
        StringBuilder tableItems = new StringBuilder();

        for(Result item : listOfResults){                   // Создание блока со всеми участниками
            tableItems.append("<div class=\"main_row\"> \n" +
                    "<div class=\"main_item\">\n" +
                    "<div>" + item.getDeveloperName() + "</div>\n" +
                    "<div>" + item.getParseTime() + " мс</div>\n" +
                    "<div>" + item.getDirTime() + " мс</div>\n" +
                    "<div>" + item.getFindTime() + " мс</div>\n" +
                    "<div>" + (item.getFindTime() + item.getParseTime() + item.getDirTime()) + " мс</div>\n" +
                    "</div>\n" +
                    "<div class=\"main_item_info\">\n" +
                    "<ul>\n");
            for(String info: item.getFileList())
                tableItems.append("<li>" + info + "</li>\n");
            tableItems.append("</ul>\n" + "</div>\n" + "</div>\n");
        }

        try {                                               // Загрузка шаблона сайта и преобразование всего текста в одну строку
            List<String> lines = Files.readAllLines(Paths.get("template.txt"), StandardCharsets.UTF_8);

            StringBuilder page = new StringBuilder();
            for(String line: lines)
                page.append(line + "\n");
            File folder = new File( "output");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            folder = new File(folder + File.separator + "statistic.html");
            PrintWriter output = new PrintWriter(folder, StandardCharsets.UTF_8.toString());
            output.printf(page.toString(), tableItems, new SimpleDateFormat("yyy.MM.dd - HH:mm").format(new Date()).toString());       // Форматированная запись в файл statistic.html
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
