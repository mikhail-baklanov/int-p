package ru.relex.intertrust.suppression.margarita;

import ru.relex.intertrust.suppression.Result;
import ru.relex.intertrust.suppression.interfaces.ListPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MargaritaPrinter implements ListPrinter {

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
    private static final String DEVELOPER_MESSAGE = "\nИмя разработчика: ";
    private static final String DIR_TIME_MESSAGE = "\nВремя работы функции dir: ";
    private static final String PARSE_TIME_MESSAGE = "\nВремя работы метода parseSuppression: ";
    private static final String SOLVE_TIME_MESSAGE = "\nВремя работы метода findDeletedFiles: ";
    private static final String RESULT_MESSAGE = "\nСписок удаленных файлов: \n";

    /**
     * Имя файла для записи результата работы контроллера
     */
    private static final String RESULT_FILENAME = "MargaritaFileResult.txt";

    @Override
    public void visualize(List<Result> list) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESULT_FILENAME))) {

            for (Result result: list) {
                writer.println(DIVIDER);
                writer.println(DEVELOPER_MESSAGE);
                writer.println(result.getDeveloperName());
                writer.println(SIMPLE_LINE);

                printTime(writer, DIR_TIME_MESSAGE, result.getDirTime());
                printTime(writer, PARSE_TIME_MESSAGE, result.getParseTime());
                printTime(writer, SOLVE_TIME_MESSAGE, result.getFindTime());
                writer.println(SIMPLE_LINE);

                writer.println(RESULT_MESSAGE);
                for (String fileName: result.getFileList()) {
                    writer.println(fileName);
                }
                writer.println(DIVIDER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Функция печати времени в миллисекундах в файл
     * @param writer PrintWriter, с помощью которого открыт файл
     * @param message Сообщение, поясняющее, к какому действию относится величина времени
     * @param time Значение времени в миллисекундах
     */
    private static void printTime(PrintWriter writer, String message, long time)
            throws IOException {
        writer.println(message + time + TIME_UNIT);
    }
}
