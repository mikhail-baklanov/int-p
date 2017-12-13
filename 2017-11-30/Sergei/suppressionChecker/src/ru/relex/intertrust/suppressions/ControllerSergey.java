package ru.relex.intertrust.suppressions;

import java.util.List;

public class ControllerSergey implements Controller {

//    static {
//        Registrator.register(new ControllerSergey());
//    }

    public void start(String suppressionFilename, String dir, List<SuppressionChecker> listOfChekers) {
        //запуск и прочее
        System.out.println();
        System.out.println("------------- Управляющий модуль (Сергей) -------------");

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
        System.out.println("------------------ Конец тестирования -----------------");
    }

    private  void printResult (List<String> result, long totalTime, SuppressionChecker checker){
        //вывод результатов
        System.out.print("Автор реализации: ");
        System.out.print(checker.getDeveloperName());
        System.out.print(" | Время работы реализаци: ");
        System.out.print(totalTime);
        System.out.println(" ms");
        System.out.println("--------------------- Результаты: ---------------------");
        for (String line: result) {
            System.out.print("<> ");
            System.out.println(line);
        }
        System.out.println("-------------------------------------------------------");
        System.out.println();
    }
}

