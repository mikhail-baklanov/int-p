package ru.relex.intertrust.suppressions;

import java.io.File;
import java.io.IOException;
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

public class SuppresionSergey implements SuppressionChecker {

    /**
     * Медод получения списка файлов из файла исключений suppresions.xml
     * @param fullFileName Полное имя файла suppresions.xml на диске
     * @return Список файлов
     * @return null если в метод передан неверный путь
     */
    final String regexpSuppressLayout = ".*?(<suppress).*?(files=\").*?(\\.java\").*?";

    public List<String> parseSuppression(String fullFileName) {
        List<String> allLines = null;

        try {
            allLines = Files.readAllLines(Paths.get(fullFileName), StandardCharsets.UTF_8);
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
        List<String> suppresionList = new ArrayList<>();

        Pattern suppresPattern = Pattern.compile(regexpSuppressLayout);
        Matcher suppresFind = null;

        for (String line: allLines){
            suppresFind = suppresPattern.matcher(line);
            if(suppresFind.matches()){
                suppresionList.add(line.substring(line.indexOf("files=\"") + 7, line.indexOf("java\"") + 4).replace("[\\\\/]", File.separator));
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

        try {
            files = new File(path);
            if (files.isFile()){
                dirs = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
                return dirs;
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }

        try {
            if (files.isDirectory()){
                getFilesFromDirectory(files, dirs);
                return dirs;
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
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

       // Pattern packagePattern = Pattern.compile(regexpPackageLayout);
        //Matcher packageFinder = null;

        for (String line: suppresions){
            boolean flag = false;
            for (String eachLine: files){
                flag = eachLine.contains(line);
                if (flag) break;
            }
            if (!flag) {
                result.add(line);}
        }
        return result;
    }

    public String getDeveloperName() {
        return "Sergei Stukalov";
    }
}
