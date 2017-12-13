package implementation;

import common.Controller;
import common.Registrator;
import common.SuppressionChecker;

import java.util.List;

public class ControllerImpl implements Controller {

    /**
     * Единица измерения времени для вывода статистики
     */
    private static final String TIME_UNIT = " мс";

    /**
     * Разделители для вывода статистики
     */
    private static final String DIVIDER =     "==============================================";
    private static final String SIMPLE_LINE = "______________________________________________";

    /**
     * Сообщения, используемые при выводе статистики
     */
    private static final String DEVELOPER_MESSAGE = "\tИмя разработчика: ";
    private static final String RESULT_MESSAGE = "\tСписок удаленных файлов: ";
    private static final String TIME_MESSAGE = "\tВремя выполнения программы: ";

    /*
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String SUPP_FILE = USER_DIR + "/supp.txt";
    private static final String DELETED_FILES = USER_DIR + "/result.txt";
    */

    static {
        Registrator.register(new ControllerImpl());
    }

    @Override
    public void start(String suppressionFilename,
                      String dir,
                      List<SuppressionChecker> checkers) {

        for (SuppressionChecker checker: checkers) {
            long startTime = System.currentTimeMillis();

            List<String> deletedFileNames = checker.findDeletedFiles(
                    checker.parseSuppression(suppressionFilename),
                    checker.dir(dir));
            /*
            try {
                Files.write(Paths.get(DELETED_FILES), deletedFileNames, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */

            long totalTime = System.currentTimeMillis() - startTime;

            // Печать статистики
            print(DIVIDER);
            print(DEVELOPER_MESSAGE);
            print(checker.getDeveloperName());
            print(SIMPLE_LINE);

            print(RESULT_MESSAGE);
            print(SIMPLE_LINE);
            for (String fileName: deletedFileNames) {
                print(fileName);
            }
            print(SIMPLE_LINE);

            print(TIME_MESSAGE);
            print(totalTime + TIME_UNIT);
            print(DIVIDER);
        }
    }

    /**
     * Функция печати сообщения на консоль
     * @param message Сообщение, которое будет напечатано
     */
    private static void print(String message) {
        System.out.println(message);
    }
}
