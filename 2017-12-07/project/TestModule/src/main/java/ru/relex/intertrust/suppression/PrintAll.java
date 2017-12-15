package ru.relex.intertrust.suppression;

import ru.relex.intertrust.suppression.interfaces.ListPrinter;

import java.util.List;

public class PrintAll implements ListPrinter {

    /**
     * Метод, который выводит в консоль список результатов на экран. Выводит поочередно имя разработчика,
     * время работы отдельных методов и список резултатов работы реализаций
     * @param list список с результатами работы реализаций
     */
    public void visualize(List<Result> list) {
        System.out.println("Результаты:");
        for (Result res: list){
            System.out.println("Автор реализации: " + res.getDeveloperName());
            System.out.println("Время работы parseSuppression:" + res.getParseTime() + " ms");
            System.out.println("Время работы dir:" + res.getDirTime() + " ms");
            System.out.println("Время работы findDeletedFiles:" + res.getFindTime() + " ms");
            System.out.println("*** Результаты работы реализации: ");
            for (String line: res.getFileList()){
                System.out.println("<> " + line);
            }
            System.out.println("**********************************");
        }
        System.out.println("Конец работы");
    }
}
