package ru.relex.intertrust.suppressions;


import java.util.List;

public interface SuppressionChecker
{
    List<String> parseSuppression(String FullFileName);
    List<String> dir(String path);
    List<String> findDeletedFiles(List<String> sup,List<String> dir);
    String getDeveloperName();

    //увеличение скорости???
}

