package ru.relex.suppression;

import ru.relex.suppression.interfaces.SuppressionChecker;

import java.util.ArrayList;
import java.util.List;

public class Registrator {
    private static List<SuppressionChecker> list = new ArrayList<>();

    private Registrator(){}

    // Регистрация нового класса
    public static void register(SuppressionChecker item){
        list.add(item);
    }

    // Получение всех зарегистрированных классов
    public static List<SuppressionChecker> getList(){
        return list;
    }

}
