package ru.relex.inttrust.suppresion;

import java.util.List;

public interface SuppressionChecker {
    //Получение списка файлов из файла suppression.xml
    List<String> parseSuppression(String fullFileName);

    // Поиск java-файлов по заданному пути
    List<String> dir(String path);

    // Поиск удаленных файлов
    List<String> findDeletedFiles(List<String> suppresions, List<String> files);
}
