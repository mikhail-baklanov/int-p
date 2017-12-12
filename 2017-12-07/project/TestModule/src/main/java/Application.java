import interfaces.Controller;
import interfaces.SuppressionChecker;

/**
 * Класс, демонстрирующий поиск лучших реализаций для решения задачи
 */
public class Application {

    /**
     * Точка входа приложения
     * @param args Аргументы командной строки
     */
    public static void main(String[] args) {
        for (Controller controller: Registrator.getControllers()) {
            for (SuppressionChecker checker: Registrator.getCheckers()) {
                // Вывод результата поиска удаленных файлов с помощью контроллера
                controller.printResult(checker);
                //TODO Добавить вывод статистики
            }
        }
    }
}
