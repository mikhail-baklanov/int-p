package ru.relex.intertrust.suppression;


import java.util.ArrayList;
import java.util.List;

public class Main {
    static
}
        Registrator.register(new Vitaliy());
        Registrator.register(new VitaliysListPrinter());
    }

    public static void main(String[] args)
        {
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

        for(ListPrinter item: Registrator.getPrinters())
            item.visualize(listResults);
    }
}
