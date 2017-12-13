package ru.relex.inttrust.suppresion;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для поиска отсутствующих в проекте файлов, добавленных в файл исключений suppresions.xml плагина checkStyle для Maven
 * @author Stukalov Sergei
 * @version 1.0.0
 */

public class Suppresion implements SuppressionChecker {
    /**
     * Медод получения списка файлов из файла исключений suppresions.xml
     * @param fullFileName Полное имя файла suppresions.xml на диске
     * @return Список файлов
     * @return null если в метод передан неверный путь
     */

    final String regexpSuppressLayout = "<suppress files=\"";
    final String regexpPackageLayout = "\\\\(ru|com)\\\\";

    public List<String> parseSuppression(String fullFileName) {
        List<String> allLines = null;

        try {
            allLines = Files.readAllLines(Paths.get(fullFileName), StandardCharsets.UTF_8);
        } catch (Exception e){

            return null;
        }
        List<String> suppresionList = new ArrayList<>();

        //маска исключение вида: <suppress files="
        Pattern suppresPattern = Pattern.compile(regexpSuppressLayout);
        Matcher suppresFind = null;

        for (String line: allLines){
            suppresFind = suppresPattern.matcher(line);
            if(suppresFind.find()){
                suppresionList.add(line.substring(line.indexOf("\"") + 1, line.indexOf("\" ")).replace("[\\\\/]", "\\"));
            }
        }
        return suppresionList;
    }

    //получение списка файлов
    private void getFilesFromDirectory(File dir, List<String> files){
        File[] folderEntries = dir.listFiles();
        for (File entry : folderEntries)
        {
            if (entry.isDirectory())
            {
                getFilesFromDirectory(entry, files);
                continue;
            }
            files.add(entry.getAbsoluteFile().toString());
        }
    }

    /**
     * Возвращает список файлов, лежащих в указанной директории. Так же может принимать на вход готовый текстовый файл
     * @param path путь к целевой директории или целевому файлу
     * @return Список файлов на диске
     * @return null, если переданный путь некорректный
     */
    public List<String> dir(String path) {
        List<String> dirs = new ArrayList<>();
        File files = null;

        Pattern packagePattern = Pattern.compile(regexpPackageLayout);
        Matcher packageFinder = null;

        try {
            files = new File(path);
            if (files.isFile()){
                List<String> tmp = new ArrayList<>();
                tmp = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
                for(String line: tmp){
                    packageFinder = packagePattern.matcher(line);
                    if (packageFinder.find()){
                        dirs.add(line.substring(packageFinder.start() + 1));
                    }
                }
                return dirs;
            }
        } catch (Exception e){
            return null;
        }

        try {
            if (files.isDirectory()){
                List<String> tmp = new ArrayList<>();
                getFilesFromDirectory(files, tmp);

                for(String line: tmp){
                    packageFinder = packagePattern.matcher(line);
                    if (packageFinder.find()){
                        if (line.indexOf(".java") != -1) {
                            dirs.add(line.substring(packageFinder.start() + 1).replace("/", "\\"));
                        }
                    }
                }
                return dirs;
            }
        } catch (Exception e){
            return null;
        }

        return null;
    }

    /**
     * Метод, который возвращает список файлов из файла suppresions.xml, которых уже нет в указанной директории
     * @param suppresions список файлов, полученный из файла suppresions.xml
     * @param files список файлов, лежащих в указанной директории
     * @return Список с отсутствующими в проекте файлами
     */
    public List<String> findDeletedFiles(List<String> suppresions, List<String> files) {
        List<String> result = new ArrayList<>();

        Pattern packagePattern = Pattern.compile(regexpPackageLayout);
        Matcher packageFinder = null;

        for (String line: suppresions){
            if (!files.contains(line)){
                packageFinder = packagePattern.matcher(line);
                if (packageFinder.find()){
                    result.add(line);
                } else {
                    boolean flag = false;
                    for (String eachLine: files){
                        flag = eachLine.contains(line);
                        if (flag) break;
                    }
                    if (!flag) {
                        result.add(line);}
                }
            }
        }
        return result;
    }
}