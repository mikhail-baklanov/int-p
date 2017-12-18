import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Ерофеев Александр
 */
public class Main {
    /**
     * Метод, который считает и выводит кол-во строк в файлах.
     * @param args массив путей к файлам
     */
    public static void main(String[] args) {
        if(args.length != 0)
            for(String item: args) {
                File file = new File(item);
                System.out.println("File: " + item);
                Scanner input = null;
                try {
                    input = new Scanner(file);
                    int count = 0;
                    while(input.hasNextLine()){
                        count++;
                        input.nextLine();
                    }
                    System.out.println("Lines: " + count);
                } catch (IOException ex) {
                    System.out.println("Файл " + item + " не был найден.");
                } finally {
                    input.close();
                }
            }
        else
            System.out.println("Файлы не были переданы через командную строку.");
    }
}
