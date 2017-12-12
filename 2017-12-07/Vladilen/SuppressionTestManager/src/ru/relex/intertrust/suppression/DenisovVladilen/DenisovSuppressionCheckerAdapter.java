package ru.relex.intertrust.suppression.DenisovVladilen;

import ru.relex.intertrust.suppression.CommonElements.Author;
import ru.relex.intertrust.suppression.CommonElements.SuppressionChecker;

import java.io.File;
import java.util.List;

@Author("Denisov Vladilen")
public class DenisovSuppressionCheckerAdapter implements SuppressionChecker {
    @Override
    public List<String> parseSuppression(String fullFileName) throws Exception {
        throw new Exception();
        //return MainClass.getSuppressionPathesList(MainClass.getDocument(fullFileName));
    }

    @Override
    public List<String> dir(String path) {
        return MainClass.getAllPathes(new File(path));
    }

    @Override
    public List<String> findDeletedFiles(List<String> suppressionsList, List<String> filesList) {
        return MainClass.getRemovedFilesList(suppressionsList, filesList);
    }
}