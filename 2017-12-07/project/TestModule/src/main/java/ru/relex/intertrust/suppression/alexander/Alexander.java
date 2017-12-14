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
    private final static String DEVELOPER_NAME = "Александр Ерофеев";
    private final static Pattern PATTERN = Pattern.compile("^<suppress checks=*|files=*");

    public List<String> parseSuppression(String fullFileName){
        List<String> paths = new ArrayList<>();
        try {
            for(String item: Files.readAllLines(Paths.get(fullFileName), StandardCharsets.UTF_8))
                if(PATTERN.matcher(item).find()) {
                    String attribute = "files=\"";
                    int first = item.indexOf(attribute) + attribute.length();
                    int last = item.indexOf("\"",first + 1);
                    String path = item.substring(first, last);
                    if(path.endsWith(".java") && !paths.contains(path))
                        paths.add(item.substring(first, last).replace("[\\\\/]", File.separator));
                }
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл " + fullFileName);
        }
        return paths;
    }

    public List<String> dir(String path){
        List<String> fileNames = null;
        try {
            fileNames = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл " + path);
        }
        return fileNames;
    }

    public String getDeveloperName(){
        return DEVELOPER_NAME;
    }

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
