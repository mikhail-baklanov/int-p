package ru.relex.intertrust.suppression;

import ru.relex.intertrust.suppression.interfaces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Утилитный класс для хранения всех реализаций интерфейсов SuppressionChecker и Controller
 */
public class Registrator {

    /**
     * Приватный пустой конструктор необходим для утилитного класса
     */
    private Registrator() { }

    /**
     * Список всех реализаций интерфейса SuppressionChecker
     */
    private static List<SuppressionChecker> checkers = new ArrayList<>();

    /**
     * Список всех реализаций интерфейса Controller
     */
    private static List<ListPrinter> printer = new ArrayList<>();

    /**
     * Функция, возвращающая список реализаций интерфейса SuppressionChecker для чтения
     * @return Список реализаций интерфейса
     */
    public static List<SuppressionChecker> getCheckers() {
        return checkers;
    }

    /**
     * Функция, возвращающая список реализаций интерфейса Controller для чтения
     * @return Список реализаций интерфейса
     */
    public static List<ListPrinter> getPrinters() {
        return printer;
    }

    /**
     * Функция регистрации конкретной реализации интерфейса SuppressionChecker
     * @param checker Реализация интерфейса, которую требуется зарегистрировать
     */
    public static void register(SuppressionChecker checker) {
        checkers.add(checker);
    }

    /**
     * Функция регистрации конкретной реализации интерфейса Controller
     * @param list Реализация интерфейса, которую требуется зарегистрировать
     */
    public static void register(ListPrinter list) {
        printer.add(list);
    }
}
