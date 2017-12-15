package ru.relex.intertrust.suppression.evgeny;

import ru.relex.intertrust.suppression.Registrator;
import ru.relex.intertrust.suppression.Result;
import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.interfaces.ListPrinter;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Класс запускает методы из FindDeletedClasses для объектов и отображает статистику.
 * @author Евгений Воронин
 */
public class PrinterInTextFile implements ListPrinter {

    @Override
    public void visualize(List<Result> list) {
        for (Result result : list) {
            try(FileWriter writer = new FileWriter("evgenyOut.txt", true))
            {
                writer.write("Реализация от " + result.getDeveloperName());
                writer.append('\n');
                writer.write("Время работы метода dir " + result.getDirTime());
                writer.append('\n');
                writer.write("Время работы метода parse " + result.getParseTime());
                writer.append('\n');
                writer.write("Время работы метода find " + result.getFindTime());
                writer.append('\n');
                writer.write("Следующие исключения не были найдены:");
                writer.append('\n');
                for (String files : result.getFileList()) {
                    writer.write("<suppress files='" + files + "' />");
                    writer.append('\n');
                }

                writer.flush();
            } catch(IOException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}
