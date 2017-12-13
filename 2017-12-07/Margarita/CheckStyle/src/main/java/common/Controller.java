package common;

import java.util.List;

public interface Controller {

    /**
     * Метод запуска поиска удаленных файлов и отображения статистики
     * (время работы, результат работы)
     * @param suppressionFilename Полное имя файла suppressions.xml
     * @param dir Каталог, откуда будет начат поиск java-файлов
     * @param checkers Зарегистрированные реализации интерфейсов каждого разработчика
     */
    void start(String suppressionFilename, String dir, List<SuppressionChecker> checkers);
}
