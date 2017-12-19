import java.io.*;

public class SymbolCounter {
    /**
     * Метод, который считает количество символов в файле.
     * @param path путь к файлу
     * @param charset кодировка, с помощью которой нужно прочитать файл
     * @return количество символов
     */
    public static int count(String path, String charset){

        int count = 0;
        try(InputStreamReader input = new InputStreamReader(new FileInputStream(new File(path)), charset)) {

            while (input.read() != -1)
                count++;

            input.close();
        }catch(IOException e){
            return -1;
        }

        return count;
    }
}
