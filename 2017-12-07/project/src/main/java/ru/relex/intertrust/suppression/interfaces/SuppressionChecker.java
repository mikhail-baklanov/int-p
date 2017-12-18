package ru.relex.intertrust.suppression.interfaces;

import java.util.List;

/**
 * Интерфейс для поиска удаленных файлов в файле suppressions.xml.
 */
public interface SuppressionChecker extends HasDeveloper {

    /**
     * Функция получения имен java-классов, которые указаны в файле suppressions.xml.
     * Вид возвращаемых данных (Пример): ru\relex\intertrust\result.java.
     * @param fullFileName Полное имя к файлу suppressions.xml
     * @return Список имен java-классов, указанных в файле
     */
    List<String> parseSuppression(String fullFileName);

    /**
     * Функция поиска файлов, которые указаны в файле suppressions.xml,
     * но которые были удалены из проекта.
     * Вид возвращаемых данных (Пример): ru\relex\intertrust\result.java.
     * @param suppressionsPaths список имен java-классов из файла suppressions.xml
     * @param dirPaths список полных имен java-классов проекта, для которого выполняется проверка
     * @return Список имен java-классов, которые были удалены из проекта
     */
    List<String> findDeletedFiles(List<String> suppressionsPaths, List<String> dirPaths);

    /**
     * Функция, которая возвращает список путей к файлам.
     * Вид возвращаемых данных (Пример): ru\relex\intertrust\result.java.
     * @param path путь до файла
     * @return cписок путей
     */
    List<String> dir(String path);
}
