package ru.relex.inttrust.suppression.Oleg;

import ru.relex.inttrust.suppression.Interfaces.SuppressionChecker;

import java.util.List;

public class SuppressionCheckerAdapter implements SuppressionChecker{
    public List<String> parseSuppression(String fullFileName) throws Exception {
        return Main.parseSuppression(fullFileName);
    }

    public List<String> dir(String path) {
        return Main.dir(path);
    }

    public List<String> findDeletedFiles(List<String> suppressionsList, List<String> filesList) {
        return Main.findDeletedFiles(suppressionsList,filesList);
    }

//    public String getDeveloperName() {
//        return ;
//    }
}
