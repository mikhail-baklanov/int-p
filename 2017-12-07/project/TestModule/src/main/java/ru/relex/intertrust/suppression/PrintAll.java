package ru.relex.intertrust.suppression;

import ru.relex.intertrust.suppression.Result;
import ru.relex.intertrust.suppression.interfaces.ListPrinter;

import java.util.List;

public class PrintAll implements ListPrinter {

    @Override
    public void visualize(List<Result> list) {
        System.out.println("Результаты:");
        for (Result res: list){
            System.out.println("Автор реализации: " + );
        }
    }
}
