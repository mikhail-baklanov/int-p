package ru.relex.intertrust.suppression.margarita;

import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MargaritaChecker implements SuppressionChecker {

    /**
     * Имя разработчика текущего класса
     */
    private static final String DEVELOPER_NAME = "Margarita";

    /**
     * Константы, используемые для поиска suppressed-файлов
     */
    private static final String SUPPRESS_STRING = "^<suppress checks=*|files=*";
    private static final String SUPPRESS_DIVIDER = "[\\\\/]", SLASH = "\\";
    private static final Pattern SUPPRESS_PATTERN = Pattern.compile(SUPPRESS_STRING);
    private static final char QUOTE = '"';

    /**
     * Строка, после которой идет имя suppressed-файла в файле suppressions.xml
     */
    private static final String FILES_ATTRIBUTE = "files=";

    /**
     * Расширение искомых файлов
     */
    private static final String JAVA_EXTENSION = ".java";

    /**
     * Смещение для выделения из строки имени suppressed-файла
     */
    private static final int OFFSET = FILES_ATTRIBUTE.length() + 1;

    @Override
    public List<String> parseSuppression(String fullFileName) {
        List<String> result = new ArrayList<>();
        try {
            for (String line: Files.readAllLines(Paths.get(fullFileName))) {
                if (SUPPRESS_PATTERN.matcher(line).find()) {

                    int begin = line.indexOf(FILES_ATTRIBUTE) + OFFSET;
                    int end = line.indexOf(QUOTE, begin + 1);

                    String fileName = line.substring(begin, end)
                            .replace(SUPPRESS_DIVIDER, SLASH);

                    if (fileName.endsWith(JAVA_EXTENSION) && !result.contains(fileName))
                        result.add(fileName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<String> dir(String path) {
        try {
            return Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> findDeletedFiles(List<String> suppressedFileNames, List<String> dir) {
        List<String> result = new ArrayList<>(suppressedFileNames);
        for (String fileName: dir) {
            for (String suppressedFileName: result) {
                if (fileName.endsWith(suppressedFileName)) {
                    result.remove(suppressedFileName);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public String getDeveloperName() {
        return DEVELOPER_NAME;
    }
}
