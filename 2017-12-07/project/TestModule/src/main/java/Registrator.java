import java.util.ArrayList;
import java.util.List;

public class Registrator {

    private Registrator() { }

    private static List<SuppressionChecker> list = new ArrayList<>();

    public static List<SuppressionChecker> getList() {
        return list;
    }

    public static void register(SuppressionChecker item) {
        list.add(item);
    }
    //public static String getDeveloperName(SuppressionChecker name ){};
    //return developerName;};

}
