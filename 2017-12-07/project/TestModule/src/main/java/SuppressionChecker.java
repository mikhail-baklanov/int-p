import java.util.List;

public interface SuppressionChecker {

    List<String> parseSuppression(String fullFileName);

    List<String> dir(String path);

    List<String> findDeletedFiles(List<String> suppressedFileNames, List<String> dir);

    //String getDeveloperName(); перенести в registrator
}
