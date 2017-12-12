package relex.practice.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 11.12.2017.
 */
public class Registrator {
    private Registrator() { }

    private static List<SuppressionChecker> list = new ArrayList<SuppressionChecker>();

    public static void register(SuppressionChecker item) {
        list.add(item);
    }

    public static List<SuppressionChecker> getList() {
        return list;
    }

}
