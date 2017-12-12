import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Класс для проведения эксперимента: необходимо открыть файл в разных кодировках
 * и посчитать количество символов в нем (убедиться, что оно может отличаться).
 */
public class Experience {

    /**
     * Пользовательская директория, которая содержит текущий проект
     */
    private static final String USER_DIR = System.getProperty("user.dir");

    /**
     * Имена файлов, используемых для тестирования
     */
    private static final String ENG_FILE_NAME = USER_DIR + "/eng.txt";
    private static final String RUS_FILE_NAME = USER_DIR + "/rus.txt";

    /**
     * Точка входа приложения
     * @param args Аргументы командной строки
     */
    public static void main(String[] args) {
        try {
            System.out.println("For English symbols:");
            printSolve(ENG_FILE_NAME, StandardCharsets.UTF_8);
            printSolve(ENG_FILE_NAME, StandardCharsets.UTF_16);
            System.out.println();

            System.out.println("For Russian symbols:");
            printSolve(RUS_FILE_NAME, StandardCharsets.UTF_8);
            printSolve(RUS_FILE_NAME, StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Функция подсчета количества символов для заданного файла в заданной кодировке
     * @param fileName Имя файла
     * @param charset Кодировка, в которой будет прочитан файл
     * @return Количество символов в файле
     */
    private static int getCountOfSymbols(String fileName, Charset charset) {
        int result = 0;
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), charset))) {
            String line;
            while( (line = bufferedReader.readLine()) != null) {
                result += line.length();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Функция печати решения задачи на консоль
     * @param fileName Имя файла
     * @param charset Кодировка, в которой будет прочитан файл
     * @throws IOException Исключение, которое будет сгенерировано, если файл не найден
     */
    private static void printSolve(String fileName, Charset charset) throws IOException {
        System.out.println(charset + ": " + getCountOfSymbols(fileName, charset) + " symbols.");
    }
}
