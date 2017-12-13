package ru.relex.intertrust.suppression.sergei;

import ru.relex.intertrust.suppression.Registrator;
import ru.relex.intertrust.suppression.interfaces.Controller;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import java.util.List;

public class ControllerSergey implements Controller {

    /**
     * @author Stukalov Sergei 
     * @version 1.0.0
     */
    public void start(String suppressionFilename, String dir, List<SuppressionChecker> listOfChekers) {
        //запуск и прочее
        List<String> suppression = null;
        List<String> dirs = null;
        List<String> res = null;
        for (SuppressionChecker checker: listOfChekers) {
            long start = System.currentTimeMillis();
            suppression = checker.parseSuppression(suppressionFilename);
            if (suppression == null) {
                continue;
            }
            dirs = checker.dir(dir);
            if (dirs == null) {
                continue;
            }
            res = checker.findDeletedFiles(suppression, dirs);
            long end = System.currentTimeMillis() - start;
            printResult(res, end, checker);
        }
    }

    private  void printResult (List<String> result, long totalTime, SuppressionChecker checker){
        //вывод результатов
        System.out.print("Автор реализации: ");
        System.out.print(checker.getDeveloperName());
        System.out.print(" | время работы реализаци: ");
        System.out.print(totalTime);
        System.out.println(" ms -----------------");
        System.out.println("---------------- Результаты: ----------------");
        for (String line: result) {
            System.out.println(line);
        }
        System.out.println("---------------------------------------------");
        System.out.println();
    }
}

