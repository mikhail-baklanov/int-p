package ru.relex.intertrust.suppression.interfaces;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Интерфейс для поиска удаленных файлов в файле suppressions.xml
 */
public interface SuppressionChecker extends HasDeveloper {

    /**
     * Функция получения имен java-классов, которые указаны в файле suppressions.xml
     * @param fullFileName Полное имя к файлу suppressions.xml
     * @return Список имен java-классов, указанных в файле
     */
    List<String> parseSuppression(String fullFileName);

    /**
     * Функция поиска файлов, которые указаны в файле suppressions.xml,
     * но которые были удалены из проекта
     * @param suppressionsPaths Список имен java-классов из файла suppressions.xml
     * @param dirPaths Список полных имен java-классов проекта, для которого выполняется проверка
     * @return Список имен java-классов, которые были удалены из проекта
     */
    List<String> findDeletedFiles(List<String> suppressionsPaths, List<String> dirPaths);

    /**
     * типо док
     * @param path
     * @return
     */
    List<String> dir(String path);
}
