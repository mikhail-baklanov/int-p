package ru.relex.intertrust.suppression.evgeny;

import ru.relex.intertrust.suppression.Registrator;
import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Класс запускает методы из FindDeletedClasses для объектов и отображает статистику.
 * @author Евгений Воронин
 */
public class EvgenyController implements Controller {

    public void start(String suppressionFilename, String dir, List<SuppressionChecker> listOfChekers) {
        for (SuppressionChecker checkerMethod : listOfChekers) {
            System.out.println("Тестирую реализацию " + checkerMethod.getDeveloperName());
            long startTime = System.currentTimeMillis();
            List<String> directory = checkerMethod.dir(dir);
            List<String> suppressionFileNames = checkerMethod.parseSuppression(suppressionFilename);
            List<String> deletedFiles = checkerMethod.findDeletedFiles(suppressionFileNames,directory);
            long time = System.currentTimeMillis() - startTime;
            try(FileWriter writer = new FileWriter("evgenyOut.txt", false))
            {
                writer.write("Время работы реализации " + checkerMethod.getDeveloperName() + " в секундах " + time/1000);
                writer.append('\n');
                writer.write("Следующие исключения не были найдены:");
                writer.append('\n');
                for (String files : deletedFiles) {
                    writer.write("<suppress files='" + files + "' />");
                    writer.append('\n');
                }
                writer.write("Всего исключений не найдено: " + deletedFiles.size());

                writer.flush();
            } catch(IOException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}
