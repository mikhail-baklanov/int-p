package ru.relex.intertrust.suppression.alexander;

import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Alexander implements SuppressionChecker {

    /**
     * Имя разработчика
     */
    private final static String DEVELOPER_NAME = "Александр Ерофеев";

    /**
     * Паттерн регулярного выражения
     */
    private final static Pattern PATTERN = Pattern.compile("<suppress\\u0020");

    /**
     * Разделитель, который используется в аттрибуте SUPPRESS_ATTRIBUTE
     */
    private final static String REGEX_SEPORATOR = "[\\\\/]";

    /**
     * Нужный аттрибут
     */
    private final static String SUPPRESS_ATTRIBUTE = "files=\"";

    public List<String> parseSuppression(String fullFileName){
        List<String> paths = new ArrayList<>();
        try {
            for(String item: Files.readAllLines(Paths.get(fullFileName), StandardCharsets.UTF_8)) {
                if (PATTERN.matcher(item).find() && item.indexOf("files=") != -1 && item.indexOf("<!--") == -1) {
                    int first = item.indexOf(SUPPRESS_ATTRIBUTE) + SUPPRESS_ATTRIBUTE.length();
                    int last = item.indexOf("\"", first + 1);
                    String path = item.substring(first, last);
                    if (path.endsWith(".java") && !paths.contains(path))
                        paths.add(item.substring(first, last).replace(REGEX_SEPORATOR, File.separator));
                }
            }
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл " + fullFileName);
        }
        return paths;
    }

    @Override
    public List<String> dir(String path){
        List<String> fileNames = null;
        try {
            fileNames = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл " + path);
        }
        return fileNames;
    }

    @Override
    public String getDeveloperName(){
        return DEVELOPER_NAME;
    }

    @Override
    public List<String> findDeletedFiles(List<String> suppressionsPaths, List<String> dirPaths){
        List<String> deletedFilePaths = new ArrayList<>();
        for(String supPath: suppressionsPaths) {
            boolean isExist = false;
            for (String dirPath: dirPaths)
                if (dirPath.endsWith(supPath)) {
                    isExist = true;
                    break;
                }
            if (!isExist)
                deletedFilePaths.add(supPath);
        }
        return deletedFilePaths;
    }

}
