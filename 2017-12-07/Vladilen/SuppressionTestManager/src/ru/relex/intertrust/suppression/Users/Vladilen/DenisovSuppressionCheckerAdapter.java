package ru.relex.intertrust.suppression.Users.Vladilen;

import ru.relex.intertrust.suppression.CommonElements.Author;
import ru.relex.intertrust.suppression.CommonElements.Registrator;
import ru.relex.intertrust.suppression.CommonElements.SuppressionChecker;
import ru.relex.intertrust.suppression.MainClass;

import java.io.File;
import java.util.List;

@Author("Denisov Vladilen")
public class DenisovSuppressionCheckerAdapter implements SuppressionChecker {
    static {
        Registrator.register(new DenisovSuppressionCheckerAdapter());
    }
    @Override
    public List<String> parseSuppression(String fullFileName) {
        //return null;
        return DenisovSC.getSuppressionPathesList(DenisovSC.getDocument(fullFileName));
    }

    @Override
    public List<String> dir(String path) {
        return DenisovSC.getAllPathes(new File(path));
    }

    @Override
    public List<String> findDeletedFiles(List<String> suppressionsList, List<String> filesList) {
        //return new ArrayList<>();
        //return null;
        return DenisovSC.getRemovedFilesList(suppressionsList, filesList);
    }
}