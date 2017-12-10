package DenisovVladilen;

import CommonElements.Author;
import CommonElements.SuppressionChecker;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@Author("Denisov Vladilen")
public class DenisovSuppressionCheckerAdapter implements SuppressionChecker {
    @Override
    public List<String> parseSuppression(String fullFileName) throws Exception {
        return MainClass.getSuppressionPathesList(MainClass.getDocument(fullFileName));
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
