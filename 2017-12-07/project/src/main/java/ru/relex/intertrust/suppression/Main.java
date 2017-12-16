package ru.relex.intertrust.suppression;

import ru.relex.intertrust.suppression.alexander.*;
import ru.relex.intertrust.suppression.evgeny.*;
import ru.relex.intertrust.suppression.interfaces.ListPrinter;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;
import ru.relex.intertrust.suppression.margarita.*;
import ru.relex.intertrust.suppression.sergei.*;
import ru.relex.intertrust.suppression.Vitaliy.*;
import ru.relex.intertrust.suppression.oleg.*;
import ru.relex.intertrust.suppression.vladilen.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static {
        Registrator.register(new Alexander());
        Registrator.register(new AlexanderPrint());

        //Registrator.register(new MargaritaChecker());
        //Registrator.register(new MargaritaController());

        Registrator.register(new SuppresionSergey());
        Registrator.register(new PrintAll());

        Registrator.register(new FindDeletedClasses());
        Registrator.register(new PrinterInTextFile());

        Registrator.register(new Vitaliy());
        Registrator.register(new VitaliysListPrinter());

        Registrator.register(new SupOleg());
        Registrator.register(new OlegPrinter());

        //Registrator.register(new DenisovSuppressionCheckerAdapter());
        //Registrator.register(new MainClass());

    }
    public static void main(String[] args) {
        List<Result> listResults = new ArrayList<>();
        for(SuppressionChecker item: Registrator.getCheckers()){
            Result result = new Result();
            result.setDeveloperName(item.getDeveloperName());
            long parseTime = System.currentTimeMillis();
            List<String> parsePaths = item.parseSuppression(args[0]);
            result.setParseTime(System.currentTimeMillis() - parseTime);
            long dirTime = System.currentTimeMillis();
            List<String> dirPaths = item.dir(args[1]);
            result.setDirTime(System.currentTimeMillis() - dirTime);
            long findTime = System.currentTimeMillis();
            List<String> findPaths = item.findDeletedFiles(parsePaths, dirPaths);
            result.setFindTime(System.currentTimeMillis() - findTime);
            result.setFileList(findPaths);
            listResults.add(result);
        }
        for(ListPrinter item: Registrator.getPrinters()){
            item.visualize(listResults);
        }
    }
}
