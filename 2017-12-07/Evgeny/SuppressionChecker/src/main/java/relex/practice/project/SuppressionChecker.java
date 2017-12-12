package relex.practice.project;

import java.util.List;

/**
 * Created by 1 on 10.12.2017.
 */
public interface SuppressionChecker {
    List<String> parseSuppression(String fullFileName);

    // Поиск java-файлов по заданному пути
    List<String> dir(String path);

    // Поиск удаленных файлов
    List<String> findDeletedFiles(List<String> dir, List<String> suppressedFileNames);

    // Получить имя автора класса (метод находится под вопросом)
    // Было предложено реализовать через аннотации
    String getDeveloperName();

}
