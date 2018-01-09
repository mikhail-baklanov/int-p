import java.nio.charset.StandardCharsets;

/**
 * @author Ерофеев Александр
 */
public class Main {

    /**
     * Массив, в котором хранятся нужные кодировки.
     */
    public static final String[] CHARSETS = {"Cp866", StandardCharsets.UTF_8.toString()};

    /**
     * Разделитель для красивого вывода.
     */
    public static final String SEPORATOR = "-----------";

    /**
     * Метод, который в цикле перебирает массив с кодировками и массив с путями к файлам.
     * @param args массив путей к файлам
     */
    public static void main(String[] args) {

        if(args.length != 0)
            for(String charset : CHARSETS) {
                System.out.println(SEPORATOR + "Кодировка " + charset + SEPORATOR);
                for (String item : args) {
                    int count = SymbolCounter.count(item, charset);
                    if (count != -1)
                        System.out.println("В файле " + item + " " + count + " символов.");
                    else
                        System.out.println("Произошла ошибка чтения файла " + item + ".");
                }
            }
        else
            System.out.println("Пути к файлам не были переданы через командную строку.");

    }
}
