package relex.practice.evgeny;

import relex.practice.project.IFactory;
import relex.practice.project.SuppressionChecker;

/**
 * Created by 1 on 11.12.2017.
 */
public class MyFactory implements IFactory {

    public FindDeletedClasses createChecker() {
        return new FindDeletedClasses();
    }
}
