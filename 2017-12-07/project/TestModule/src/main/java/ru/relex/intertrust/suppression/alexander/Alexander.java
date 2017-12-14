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
     * Идём по всем строкам xml файла, если строка удовлетворяет нашему регулярному выражению, то
     * "вырезаем" путь из аттрибута files, преобразуем слеши в те, которые предусмотрены системой и
     * помещаем в массив.
     * @param fullFileName - путь к xml файлу
     * @return paths
     */
    public List<String> parseSuppression(String fullFileName){
        List<String> paths = new ArrayList<>();
        try {
            for(String item: Files.readAllLines(Paths.get(fullFileName), StandardCharsets.UTF_8)) {
                if (PATTERN.matcher(item).find() && item.indexOf("files=") != -1) {
                    String attribute = "files=\"";
                    int first = item.indexOf(attribute) + attribute.length();
                    int last = item.indexOf("\"", first + 1);
                    String path = item.substring(first, last);
                    if (path.endsWith(".java") && !paths.contains(path))
                        paths.add(item.substring(first, last).replace("[\\\\/]", File.separator));
                }
            }
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл " + fullFileName);
        }
        return paths;
    }

    /**
     * Считываем файл и добавляем строки из файла в массив.
     * @param path - путь к txt файлу
     * @return fileNames
     */
    public List<String> dir(String path){
        List<String> fileNames = null;
        try {
            fileNames = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл " + path);
        }
        return fileNames;
    }

    /**
     * Возвращаем имя разработчика.
     * @return DEVELOPER_NAME
     */
    public String getDeveloperName(){
        return DEVELOPER_NAME;
    }

    /**
     * Идем по всему массиву suppressionsPaths и сверяем каждый элемент с каждым элементом массива dirPaths.
     * Если пути совпали, прекращаем внутренний цикл. Если массив dirPaths закончился, а пути не совпали,
     * добавляем путь в массив удаленных файлов.
     * @param suppressionsPaths - массив путей к файлам из xml
     * @param dirPaths - массив путей, взятых из txt файла
     * @return deletedFilePaths
     */
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
