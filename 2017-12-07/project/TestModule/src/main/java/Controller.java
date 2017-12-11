import java.util.List;

public class Controller {

    //private static final String USER_DIR = System.getProperty("user.dir");//???
    //private static final String DIR = USER_DIR + "/files.txt";////c коммандной строки
    //private static final String SUPPRESSED = USER_DIR + "/checkstyle-suppressions.xml";//c коммандной строки

    /*вывод у всех свой
    private static final String MESSAGE = "Время выполнения программы: ";
    private static final String TIME_UNIT = " мс";

    private static final String DIVIDER = "===========================";
    private static final String SIMPLE_LINE = "___________________________";*/

    public static void main(String[] args) {
        for (SuppressionChecker checker: Registrator.getList()) {
            //print(DIVIDER);
            print(checker.getDeveloperName());
            //print(SIMPLE_LINE);

            long startTime = System.currentTimeMillis();
            List<String> deletedFileNames = checker.findDeletedFiles(
                    checker.parseSuppression(SUPPRESSED),
                    checker.dir(DIR));
            long totalTime = System.currentTimeMillis() - startTime;

            for (String fileName: deletedFileNames) {
                print(fileName);
            }
            print(SIMPLE_LINE);
            print(MESSAGE + totalTime + TIME_UNIT);
        }
    }

    private static void print(String message) {
        System.out.println(message);
    }
}
