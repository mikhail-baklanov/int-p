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
    public List<String> parseSuppression(String fullFileName) {
        //создаем лист
        List<String> allLines = null;

        //открываем файл
        try {
            allLines = Files.readAllLines(Paths.get(fullFileName), StandardCharsets.UTF_8);
        } catch (Exception e){
            return null;
        }
        List<String> suppresionList = new ArrayList<>();

        //маска исключение вида: <suppress files="
        Pattern suppresPattern = Pattern.compile("<suppress files=\"");
        Matcher suppresFind = null;

        //ищем пути с файлами и кладем их в лист
        for (String line: allLines){
            suppresFind = suppresPattern.matcher(line);
            if(suppresFind.find()){
                suppresionList.add(line.substring(line.indexOf("\"") + 1, line.indexOf("\" ")).replace("[\\\\/]", "\\"));
            }
        }
        //отдаем лист
        return suppresionList;
    }

    /**
     * Возвращает список файлов, лежащих в указанной директории. Так же может принимать на вход готовый текстовый файл
     * @param path путь к целевой директории или целевому файлу
     * @return Список файлов на диске
     * @return null, если переданный путь некорректный
     */
    public List<String> dir(String path) {
        //--с файлом
        //открываем файл
        List<String> dirs = new ArrayList<>();
        File files = null;

        Pattern packagePattern = Pattern.compile("\\\\(ru|com)\\\\");
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

        //--с директорией
        //открываем директорию
        //сканируем и ищем файлы *.java
        //-------------------------------------------ИСПРАВИТЬ!
        try {
            if (files.isDirectory()){
                String[] tmp = files.list();
                for (String i: tmp){
                    System.out.println(i);
                }
                for(String line: tmp){
                    packageFinder = packagePattern.matcher(line);
                    if (packageFinder.find()){
                        dirs.add(line.substring(packageFinder.start()));
                    }
                }
                return dirs;
            }
        } catch (Exception e){
            return null;
        }

        //кладем пути в лист
        //отдаем лист
        return null;
    }

    /**
     * Метод, который возвращает список файлов из файла suppresions.xml, которых уже нет в указанной директории
     * @param suppresions список файлов, полученный из файла suppresions.xml
     * @param files список файлов, лежащих в указанной директории
     * @return Список с отсутствующими в проекте файлами
     */
    public List<String> findDeletedFiles(List<String> suppresions, List<String> files) {
        //создаем результатирующий лист
        List<String> result = new ArrayList<>();

        //бежим по листу с исключениями
        for (String line: suppresions){
            if (!files.contains(line)){
                result.add(line);
            }
        }
        //смотим наличие очередного файла в листе с файлами
        //в случае отсутствия в листе с файлами файла из списка исключений кладем эту строчку в результатирующий лист
        //отдаем лист
        return result;
    }

    public String getDeveloperName() {
        return "Сергей";
        //возвращаем строку с именем автора (пока что)
    }
}
