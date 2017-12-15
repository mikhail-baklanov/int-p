package ru.relex.intertrust.suppression.margarita;

import ru.relex.intertrust.suppression.interfaces.SuppressionChecker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MargaritaController {

    /**
     * Единица измерения времени для вывода статистики
     */
    private static final String TIME_UNIT = " мс";

    /**
     * Разделители для вывода статистики
     */
    private static final String DIVIDER =     "==============================================";
    private static final String SIMPLE_LINE = "______________________________________________";

    /**
     * Сообщения, используемые при выводе статистики
     */
    private static final String DEVELOPER_MESSAGE = "\tИмя разработчика: ";
    private static final String RESULT_MESSAGE = "\tСписок удаленных файлов: ";
    private static final String TIME_MESSAGE = "\tВремя выполнения программы: ";

    /**
     * Имя файла для записи результата работы контроллера
     */
    private static final String RESULT_FILENAME = "MargaritaFileResult.txt";

    public void start(String suppressionFilename,
                      String dir,
                      List<SuppressionChecker> checkers) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(RESULT_FILENAME))) {
            for (SuppressionChecker checker : checkers) {

                long startTime = System.currentTimeMillis();

                List<String> deletedFileNames = checker.findDeletedFiles(
                        checker.parseSuppression(suppressionFilename),
                        checker.dir(dir));

                long totalTime = System.currentTimeMillis() - startTime;

                // Печать статистики
                writer.println(DIVIDER);
                writer.println(DEVELOPER_MESSAGE);
                writer.println(checker.getDeveloperName());
                writeLine(writer);

                writer.println(RESULT_MESSAGE);
                writeLine(writer);
                for (String fileName : deletedFileNames) {
                    writer.println(fileName);
                }
                writeLine(writer);

                writer.println(TIME_MESSAGE);
                writer.println(totalTime + TIME_UNIT);
                writer.println(DIVIDER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Функция записи линии-разделителя в файл
     * @param writer PrintWriter, с помощью которого открыт файл
     * @throws IOException Исключение, которое будет сгенерировано, если файл не найден
     */
    private static void writeLine(PrintWriter writer) throws IOException {
        writer.println(SIMPLE_LINE);
    }
}
