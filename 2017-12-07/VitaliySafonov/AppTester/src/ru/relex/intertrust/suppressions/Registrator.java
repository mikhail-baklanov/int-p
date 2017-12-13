package ru.relex.intertrust.suppressions;

import java.util.ArrayList;
import java.util.List;

public class Registrator
{

    private static List<SuppressionChecker> checkers = new ArrayList<>();
    private static List<Controller> controllers = new ArrayList<>();

    private Registrator() {};
    public static void register(SuppressionChecker obj)
    {
        checkers.add(obj);
    }

    public static List<SuppressionChecker> getCheckers()
    {
        return checkers;
    }
    public static List<Controller> getControllers() { return controllers; }
    public static void register(Controller controller) { controllers.add(controller); }

}
