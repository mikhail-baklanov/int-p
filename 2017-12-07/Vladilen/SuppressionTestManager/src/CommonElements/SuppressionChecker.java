package CommonElements;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public interface SuppressionChecker {
    List<String> parseSuppression(String fullFileName) throws Exception;

    // Поиск java-файлов по заданному пути
    List<String> dir(String path);

    // Поиск удаленных файлов
    List<String> findDeletedFiles(List<String> suppressionsList, List<String> filesList);

    // Получить имя автора класса (метод находится под вопросом)
    // Было предложено реализовать через аннотации
    //String getDeveloperName();
}
