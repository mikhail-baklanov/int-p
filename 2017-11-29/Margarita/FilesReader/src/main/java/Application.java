import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для задачи о подсчте количества строк в файлах,
 * имена которых вводятся как аргументы командной строки
 */
public class Application {

    /**
     * Символ конца строки
     */
    private static final byte END_OF_LINE = '\n';

    /**
     * Символ кавычки, в которых может быть имя файла с пробелами
     */
    private static final char QUOTE_SYMBOL = '"';

    /**
     * Точка входа приложения
     * @param args Аргументы командной строки
     */
    public static void main(String[] args) {
        List<String> fileNames = new ArrayList<>();

        int i = 0;
        while (i < args.length) {
            // Если имя файла без пробелов, то просто добавляем его в список
            if (!isNameWithSpaces(args[i]))
                fileNames.add(args[i++]);
            else {
                // Если имя файла с пробелами, то сначала мы должны удалить кавычки
                String fileName = args[i].substring(1, args[i].length() - 2);
                fileNames.add(fileName);
            }
        }

        try {
            printCountOfStrings(fileNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Функция, проверяющая, содержит ли имя файла пробелы
     * @param fileName Имя файла
     * @return True, если имя файла содержит пробелы, иначе False
     */
    private static boolean isNameWithSpaces(String fileName) {
        // Если имя файла содержит пробелы, то оно должно заключаться в кавычки
        return fileName.charAt(0) == QUOTE_SYMBOL &&
                fileName.charAt(fileName.length() - 1) == QUOTE_SYMBOL;
    }

    /**
     * Функция печати количества строк в файлах
     * @param fileNames Список имен файлов
     * @throws IOException Исключение, которое будет сгенерировано, если файл не найден
     */
    private static void printCountOfStrings(List<String> fileNames) throws IOException {
        // Алгоритм: читаем файл побайтово и считаем количество символов конца строки
        for (String name: fileNames) {
            int currentByte, countOfLines = 0;
            byte[] arr = Files.readAllBytes(Paths.get(name));
            ByteArrayInputStream inputStream = new ByteArrayInputStream(arr);
            while ((currentByte = inputStream.read()) != -1) {
                if (currentByte == END_OF_LINE)
                    countOfLines++;
            }
            System.out.println("Количество строк в файле " + name + " равно " + countOfLines);
        }
    }
}
