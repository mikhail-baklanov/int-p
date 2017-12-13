package ru.relex.intertrust.suppression.vladilen;

import ru.relex.intertrust.suppression.Registrator;
import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import java.io.File;
import java.util.List;

public class DenisovSuppressionCheckerAdapter implements SuppressionChecker {
    @Override
    public List<String> parseSuppression(String fullFileName) {
        //return null;
        return DenisovSC.getSuppressionPathesList(DenisovSC.getDocument(fullFileName));
    }

    @Override
    public List<String> dir(String path) {
        return DenisovSC.getAllPathes(path);
    }

    @Override
    public List<String> findDeletedFiles(List<String> suppressionsList, List<String> filesList) {
        //return new ArrayList<>();
        //return null;
        return DenisovSC.getRemovedFilesList(suppressionsList, filesList);
    }


    public String getDeveloperName(){
        return "Denisov Vladilen";
    }
}