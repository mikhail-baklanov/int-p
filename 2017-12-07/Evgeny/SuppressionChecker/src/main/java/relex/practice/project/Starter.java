package relex.practice.project;

/**
 * Created by 1 on 11.12.2017.
 */
public class Starter {
    private SuppressionChecker checker;
    public Starter(IFactory factory) {
        checker = factory.createChecker();
    }

    public SuppressionChecker getChecker() {
        return checker;
    }
}
