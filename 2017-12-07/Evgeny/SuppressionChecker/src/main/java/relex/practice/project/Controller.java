package relex.practice.project;

import relex.practice.evgeny.MyFactory;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Created by 1 on 11.12.2017.
 */
public class Controller {
    private static final String DIR_PATH = "F:\\Projects\\Relex\\SimpleTest\\IntrTrust";
    private static final String SUPPRESSION_PATH = "F:\\Projects\\Relex\\SimpleTest\\test.xml";

    public static void main(String[] args) {
        IFactory factory = new MyFactory();
        Starter starter = new Starter(factory);
        SuppressionChecker checker = starter.getChecker();
//        List<SuppressionChecker> list =  Registrator.getList();
//        for (SuppressionChecker checker : list) {
            System.out.println("Тестирую реализацию " + checker.getDeveloperName());
            long startTime = System.currentTimeMillis();
            List<String> dir = checker.dir(DIR_PATH);
            List<String> suppressedFileNames = checker.parseSuppression(SUPPRESSION_PATH);
            checker.findDeletedFiles(dir, suppressedFileNames);
            long time = System.currentTimeMillis() - startTime;
            System.out.println("Время работы реализации " + checker.getDeveloperName() + " " + time + " миллисекунд");
//        }
    }
}
