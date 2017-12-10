package CommonElements;
import java.util.ArrayList;
import java.util.List;

public class Registrator {
    // Пустой приватный конструктор необходим, так как
    // данный класс является утилитным и содержит
    // только статические методы
    private Registrator() {

    }

    // Зарегистрированные для проверки классы
    private static List<SuppressionChecker> list = new ArrayList<>();

    // Метод регистрации нового класса
    public static void register(SuppressionChecker item){
        list.add(item);
    }

    // Метод получения списка зарегистрированных классов
    public static List<SuppressionChecker> getList() {
        return list;
    }
}
