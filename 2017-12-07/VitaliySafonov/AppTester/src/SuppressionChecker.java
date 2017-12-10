import java.util.ArrayList;
import java.util.List;

public interface SuppressionChecker
{
    List<String> parseSuppression(String FullFileName);//добавляет объект класса в arrayList
    List<String> dir(String path);
    List<String> findDeletedFiles(List<String> sup,List<String> dir);
    String getDeveloperName();

    //увеличение скорости???
}

