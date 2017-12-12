package interfaces;

public interface Controller {

    /**
     * Вывод результатов поиска удаленных файлов
     * @param checker Реализация интерфейса, для которой требуется вывести результат работы
     */
    void printResult(SuppressionChecker checker);
}
