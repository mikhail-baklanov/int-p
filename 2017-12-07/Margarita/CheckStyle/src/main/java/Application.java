import common.Controller;
import common.Registrator;
import implementation.Checker;
import implementation.ControllerImpl;

public class Application {

    /**
     * Пути к файлам для решения задач
     * (вместо них будут использоваться аргументы командной строки)
     */
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String DIR = USER_DIR + "/files.txt";
    private static final String SUPPRESSED = USER_DIR + "/checkstyle-suppressions.xml";

    /**
     * Точка входа приложения
     * @param args Аргументы командной строки
     */
    public static void main(String[] args) {
        ControllerImpl myController = new ControllerImpl();
        Checker checker = new Checker();

        for (Controller controller: Registrator.getControllers()) {
            controller.start(SUPPRESSED, DIR, Registrator.getCheckers());
        }
    }
}
