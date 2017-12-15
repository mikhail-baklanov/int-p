package ru.relex.intertrust.suppression.alexander;

import org.omg.CORBA.Environment;
import ru.relex.intertrust.suppression.Result;
import ru.relex.intertrust.suppression.interfaces.ListPrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class AlexanderPrint implements ListPrinter {
    /**
     * В цикле в порядке возрастания создаем блоки html кода для каждой программы, после этого берем шаблон template.txt,
     * В который вставляем эти блоки и сохраняем все в statistic.html
     * @param listOfResults - список всех результатов выполнения программ
     */
    public void visualize(List<Result> listOfResults){
        StringBuilder tableItems = new StringBuilder();

        /*
        final Comparator<Result> COMPARE_BY_FULLTIME = new Comparator<Result>() {
            @Override
            public int compare(Result lhs, Result rhs) {
                long firstDigit = lhs.getDirTime() + lhs.getFindTime() + lhs.getParseTime();
                long secondDigit = rhs.getDirTime() + rhs.getFindTime() + rhs.getParseTime();
                return (int)(firstDigit - secondDigit);
            }
        };
        Collections.sort(listOfResults, COMPARE_BY_FULLTIME);
        */

        // Создание блока со всеми участниками
        int count = 1;
        for(Result item : listOfResults){
            // Поиск программы с наименьшим временем выполнения и последующая запись его в блок

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
            count++;
        }

        // Загрузка шаблона сайта и преобразование всего текста в одну строку
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("template.txt"), StandardCharsets.UTF_8);

            StringBuilder page = new StringBuilder();
            for(String line: lines)
                page.append(line + "\n");
            File folder = new File( "output");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            folder = new File(folder + File.separator + "statistic.html");
            PrintWriter output = new PrintWriter(folder);
            // Форматированная запись в файл statistic.html
            output.printf(page.toString(), tableItems, new SimpleDateFormat("yyy.MM.dd - HH:mm").format(new Date()).toString());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
